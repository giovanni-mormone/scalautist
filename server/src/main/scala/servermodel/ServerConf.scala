package servermodel

import java.sql.Date
import java.time.LocalDate

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.{ActorSystem, Behavior}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import dbfactory.operation.{DisponibilitaOperation, StipendioOperation}
import messagecodes.StatusCodes
import servermodel.routes.RouteServer._

import scala.concurrent.duration._
import scala.util.{Failure, Success}
/**
 * @author Francesco Cassano
 * Object to configure and start Http server
 */
private object ServerConf {
  final case class StartServer()
  private val interface = "0.0.0.0"
  private val port = 8080
  private val INITIAL_DELAY = 3
  private val DELAY = 1
  def apply():Behavior[StartServer]=
    Behaviors.setup{
      context =>
        startHttpServer(route, context.system)
        Behaviors.empty
    }

  def startHttpServer(routes: Route, system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext
    system.scheduler.scheduleWithFixedDelay(INITIAL_DELAY seconds, DELAY day)(()=>verifyDayInWeek())
    Http().bindAndHandle(routes, interface, port).onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  def verifyDayInWeek(): Unit ={
    import scala.concurrent.ExecutionContext.Implicits.global
    def _verifyDayInWeek(count:Int=0):Unit={
      import utils.DateConverter._
      val date = Date.valueOf(LocalDate.now())
      if(date.compareTo(getFirstDayWeek(date))==0 && count<2){
        DisponibilitaOperation.updateAvailabilityWeekFissi(date).filter(_.isEmpty).map(_=>_verifyDayInWeek(count+1))
      }
      if(date.compareTo(startMonthDate(date))==0 && count<2){
            StipendioOperation.calculateStipendi(date).filter(x => !x.contains(StatusCodes.SUCCES_CODE)).map(_=>_verifyDayInWeek(count+1))
      }
    }
  _verifyDayInWeek()
  }
}
