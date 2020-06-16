package controller

import caseclass.CaseClassDB.{Disponibilita, Turno}
import caseclass.CaseClassHttpMessage.{InfoHome, Response}
import javafx.application.Platform
import model.entity.DriverModel
import view.fxview.mainview.DriverView

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait DriverController  extends AbstractController[DriverView] {
  /**
   *
   */
  def drawHomePanel(): Unit

  /**
   *
   */
  def drawShiftPanel(): Unit

  /**
   * call method into model that return all salary for a person
   */
  def drawSalaryPanel(): Unit

  /**
   * method which enable obtain info for a determinate salary
   * @param idSalary identifies a salary into database, this salary must be exist
   */
  def drawInfoSalary(idSalary:Int):Unit
}
object DriverController{
  def apply(): DriverController = new DriverControllerImpl()
  private class DriverControllerImpl() extends DriverController {
    private val model = DriverModel()
    override def drawHomePanel(): Unit =
      Future.successful().onComplete {
      case Failure(_) => myView.showMessage("Error")
      case Success(value) => myView.drawHomeView(InfoHome(
        List(
          Turno("Seconda Mattinata","8:00-12:00",40.0,Some(1)),
          Turno("Primo Pomeriggio","12:00-16:00",40.0,Some(1))),
        Disponibilita("Lunedi","Martedi",Some(1))))
    }

    override def drawShiftPanel(): Unit =
      Future.successful().onComplete {
        case Failure(_) => myView.showMessage("Error")
        case Success(value) => myView.drawShiftView(List(
          Turno("Manana","8:00-12:00",40,Some(1)),
          Turno("Manana","12:00-16:00",40,Some(1)),
          Turno("Manana","8:00-12:00",40,Some(1)),
          Turno("Manana","12:00-16:00",40,Some(1)),
          Turno("Manana","8:00-12:00",40,Some(1)),
          Turno("Manana","12:00-16:00",40,Some(1)),
          Turno("Manana","8:00-12:00",40,Some(1)),
          Turno("Manana","12:00-16:00",40,Some(1)),
          Turno("Manana","8:00-12:00",40,Some(1)),
          Turno("Manana","12:00-16:00",40,Some(1))))
      }

    override def drawSalaryPanel(): Unit =
      model.getSalary(5) onComplete {
        case Success(value) =>value.payload.foreach(result=>myView.drawSalaryView(result))
        case Failure(_) =>myView.showMessage("Error")
      }
    override def drawInfoSalary(idSalary: Int): Unit =
      model.getInfoForSalary(idSalary).onComplete {
        case Failure(_) => myView.showMessage("Error")
        case Success(Response(_, None))=> myView.showMessage("Not Found")
        case Success(Response(_, payload)) =>payload.foreach(result=>myView.informationSalary(result))
      }
  }

}
