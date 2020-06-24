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

  def startupDriverCheck(): Unit

  def sendDisponibility(day1: String, day2: String): Unit
}
object DriverController{
  def apply(): DriverController = new DriverControllerImpl()
  private class DriverControllerImpl() extends DriverController {
    private val model = DriverModel()

    override def drawHomePanel(): Unit =
      model.getTurniInDay(Utils.userId.head).onComplete {
        case Failure(_) => myView.showMessage("Error")
        case Success(value) => value.payload.foreach(result=> myView.drawHomeView(result))
      }

    override def drawShiftPanel(): Unit =
      model.getTurniSettimanali(Utils.userId.head).onComplete {
        case Failure(_) => myView.showMessage("Error")
        case Success(value) =>value.payload.foreach(result=> myView.drawShiftView(result))
      }

    override def drawSalaryPanel(): Unit =
      model.getSalary(Utils.userId.head) onComplete {
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

    override def startupDriverCheck(): Unit = {
      //model?
      val a = List("Lunedi","Martedi","Mercoledi","Giovedi","Venerdi","Sabato","Domenica")
      myView.drawDisponibilitaPanel(a)
      //myView.drawHomePage()
    }

    override def sendDisponibility(day1: String, day2: String): Unit =
    //model
      myView.disponibilityInserted()
  }

}
