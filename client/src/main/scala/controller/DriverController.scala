package controller

import caseclass.CaseClassDB.{Disponibilita, Turno}
import caseclass.CaseClassHttpMessage.{InfoHome, InfoShift, Response, ShiftDay}
import messagecodes.StatusCodes
import model.entity.DriverModel
import view.fxview.mainview.DriverView

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait DriverController  extends AbstractController[DriverView] {
  /**
   * draw information for one day into home page of the driver, the information is
   * shift that having in the day and extra day that can make in the week
   */
  def drawHomePanel(): Unit

  /**
   *  draw information of all shift in a week, and the possible extra days I can do
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

    /* case Success(Response(StatusCodes.SUCCES_CODE, payload)) =>payload.foreach(result=>myView.drawSalaryView(result))
        case Success(Response(StatusCodes.BAD_REQUEST,_))=>myView.showMessage("bad-request-error")
        case Success(Response(StatusCodes.NOT_FOUND,_))=>myView.showMessage("not-found-error")
        case Failure(_)  => myView.showMessage("general-error")*/
    override def drawHomePanel(): Unit =
      Future.successful().onComplete {

      case Failure(_) => myView.showMessage("Error")
      case Success(value) => myView.drawHomeView(InfoHome(
        List(
          Turno("Seconda Mattinata","8:00-12:00",40.0,Some(1)),
          Turno("Primo Pomeriggio","12:00-16:00",40.0,Some(1))),
        Disponibilita("Lunedi","Martedi",Some(1))))
    }
    /* case Success(Response(StatusCodes.SUCCES_CODE, payload)) =>payload.foreach(result=>myView.drawSalaryView(result))
           case Success(Response(StatusCodes.BAD_REQUEST,_))=>myView.showMessage("bad-request-error")
           case Success(Response(StatusCodes.NOT_FOUND,_))=>myView.showMessage("not-found-error")
           case Failure(_)  => myView.showMessage("general-error")*/
    override def drawShiftPanel(): Unit =
      Future.successful().onComplete {
        case Failure(_) => myView.showMessage("Error")
        case Success(value) => myView.drawShiftView(InfoShift(List(
          ShiftDay(1,"8:00-12:00"),
          ShiftDay(1,"12:00-16:00"),
          ShiftDay(2,"8:00-12:00"),
          ShiftDay(2,"12:00-16:00"),
          ShiftDay(3,"8:00-12:00"),
          ShiftDay(3,"12:00-16:00"),
          ShiftDay(4,"8:00-12:00"),
          ShiftDay(4,"12:00-16:00"),
          ShiftDay(5,"8:00-12:00"),
          ShiftDay(5,"12:00-16:00"),
          ShiftDay(6,"8:00-12:00"),
          ShiftDay(6,"12:00-16:00")),
          Disponibilita("Lunedi","Martedi",Some(1))))
      }

    override def drawSalaryPanel(): Unit =
      model.getSalary(5) onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE, payload)) =>payload.foreach(result=>myView.drawSalaryView(result))
        case Success(Response(StatusCodes.BAD_REQUEST,_))=>myView.messageErrorSalary("bad-request-error")
        case Success(Response(StatusCodes.NOT_FOUND,_))=>myView.messageErrorSalary("not-found-error")
        case Failure(_)  => myView.messageErrorSalary("general-error")
      }
    override def drawInfoSalary(idSalary: Int): Unit =
      model.getInfoForSalary(idSalary).onComplete {
        case Failure(_)  => myView.messageErrorSalary("general-error")
        case Success(Response(StatusCodes.BAD_REQUEST,_))=>myView.messageErrorSalary("bad-request-error")
        case Success(Response(StatusCodes.NOT_FOUND,_))=>myView.messageErrorSalary("not-found-error")
        case Success(Response(_, payload)) =>payload.foreach(result=>myView.informationSalary(result))
      }
  }

}
