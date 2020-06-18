package testhttp

import java.sql.Date

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Dates, Request, Response}
import jsonmessages.JsonFormats._
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRoutePersona.routePersona
import utils.StartServer
import messagecodes.{StatusCodes=>statusCodes}
import scala.concurrent.duration.DurationInt

object TestHttpPerson{
  private def startServer():Unit=MainServer
  private val getPersona: (String,Request[Int]) = ("/getpersona",Request(Some(1)))
  private val getAllPersona: String = "/getallpersona"
  private val recoveryPassword: (String,Request[Int]) = ("/recoverypassword",Request(Some(1)))
  private val loginPersona: (String,Request[Login]) = ("/loginpersona",Request(Some(Login("admin","admin"))))
  private val deletePersona: (String,Request[Int]) = ("/deletepersona",Request(Some(1)))
  private val calcoloStipendio: (String,Request[Dates]) = ("/calcolostipendio",Request(Some(Dates(new Date(System.currentTimeMillis())))))
  private val getStipendio: (String,Request[Int]) = ("/getstipendio",Request(Some(1)))
  private val assenza:Assenza = Assenza(2,new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()+System.currentTimeMillis()),malattia = true)
  private val persona:Persona = Persona("Fabian","Aspee","569918598",Some(""),1,isNew = true,"admin",None,None,Some(1))
  private val addAbsence: (String,Request[Assenza]) = ("/addabsence",Request(Some(assenza)))
  private val loginNotFound: (String,Request[Login]) = ("/loginpersona",Request(Some(Login("","admin"))))
  private val badLogin: (String,Request[Login]) = ("/loginpersona",Request(Some(Login("admin2","admin"))))
  private val badRequestLogin: (String,Request[Login]) = ("/loginpersona",Request(None))

  private val daAssumere:Persona = Persona("JuanitoS","PerezS","569918598",Some(""),3,isNew = true,"")
  private val contratto:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,1,Some(1),Some(2))
  private val disp:Disponibilita = Disponibilita(1,"Lunes","Sabato")
  private val insertPersona = Assumi(daAssumere,contratto,Some(disp))
  private val hirePerson: (String,Request[Assumi]) = ("/hireperson",Request(Some(insertPersona)))
}

class TestHttpPerson extends AnyWordSpec with ScalatestRouteTest with StartServer{
  import TestHttpPerson._
  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(5).second)
  startServer()
  "The service" should {
    "return login with credential of a person" in {
      Post(hirePerson._1,hirePerson._2) ~> routePersona ~> check {
        responseAs[Response[Login]].payload.isDefined
      }
    }
    "return a person for Post requests to the root path" in {
      Post(getPersona._1,getPersona._2) ~> routePersona ~> check {
        responseAs[Response[Persona]].statusCode==StatusCodes.OK.intValue
      }
    }
    "return a list of person for Post requests to the root path" in {
      Post(getAllPersona) ~> routePersona ~> check {
        responseAs[Response[List[Persona]]].payload.isDefined
      }
    }
    "return a login case class when request to recovery password by id" in {
      Post(recoveryPassword._1,recoveryPassword._2) ~> routePersona ~> check {
        responseAs[Response[Login]].statusCode==StatusCodes.OK.intValue
      }
    }
    "return a person for Post requests when login" in {
      Post(loginPersona._1,loginPersona._2) ~> routePersona ~> check {
        responseAs[Response[Persona]].payload.contains(persona)
      }
    }
    "return a Int with value 1 when delete a person in database" in {
      Post(deletePersona._1,deletePersona._2) ~> routePersona ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.SUCCES_CODE)
      }
    }
    "return a StatusCodes.Created for Post requests" in {
      Post(calcoloStipendio._1,calcoloStipendio._2) ~> routePersona ~> check {
        responseAs[Response[Int]].statusCode==StatusCodes.Created.intValue
      }
    }
    "return a StatusCodes.Created for Post requests  when get stipendio" in {
      Post(getStipendio._1,getStipendio._2) ~> routePersona ~> check {
        responseAs[Response[Stipendio]].statusCode==StatusCodes.Created.intValue
      }
    }
    "return a id for Post requests to the create absence" in {
      Post(addAbsence._1,addAbsence._2) ~> routePersona ~> check {
        responseAs[Response[Int]].payload.isDefined
      }
    }
    "return StatusCodes.NotFound for Post requests login with not exist" in {
      Post(loginNotFound._1,loginNotFound._2) ~> routePersona ~> check {
        responseAs[Response[Int]].statusCode==StatusCodes.NotFound.intValue
      }
    }
    "return StatusCodes.NotFound for Post requests to the bad login" in {
      Post(badLogin._1,badLogin._2) ~> routePersona ~> check {
        responseAs[Response[Int]].statusCode==StatusCodes.NotFound.intValue
      }
    }
    "return a StatusCode BAD_REQUEST for Post requests to the badRequest" in {
      Post(badRequestLogin._1,badRequestLogin._2) ~> routePersona ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }
  }

}
