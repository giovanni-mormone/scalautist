package controller

import caseclass.CaseClassHttpMessage.Response
import messagecodes.StatusCodes
import model.entity.DriverModel
import view.mainview.DriverView

import scala.util.{Failure, Success, Try}

trait DriverController  extends AbstractController[DriverView] {

  /**
   * method that verify if exist queue for notification
   */
  def startListenNotification(): Unit

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

  /**
   * Called when a driver logs in the system; used to check if the driver has a disponibilita
   * for the week.
   */
  def startupDriverCheck(): Unit

  /**
   * Sets the disponibilita for the logged driver in the current week
   * @param day1
   *             The first day of disponibilita
   * @param day2
   *             The second day of disponibilita
   */
  def sendDisponibility(day1: String, day2: String): Unit
}
object DriverController{
  private val instance = new DriverControllerImpl()
  private val model = DriverModel()

  def apply(): DriverController = instance

  private class DriverControllerImpl() extends DriverController {

    private def notificationReceived(message:String,tag:Long):Unit={
      myView.drawNotification(message,tag)
    }

    override def startListenNotification(): Unit ={
      model.verifyExistedQueue(Utils.userId,notificationReceived)
    }

    override def drawHomePanel(): Unit =
      Utils.userId.foreach(id=>
        model.getTurniInDay(id).onComplete {
          case Success(value) => value.payload.foreach(result=> myView.drawHomeView(result))
          case t => generalSuccessAndError(t)
        }
      )


    override def drawShiftPanel(): Unit =
      Utils.userId.foreach(id=>
        model.getTurniSettimanali(id).onComplete {
          case Success(value) =>value.payload.foreach(result=> myView.drawShiftView(result))
          case t => generalSuccessAndError(t)
        }
      )

    private def generalSuccessAndError[A](response:Try[A]): Unit = response match {
      case Failure(_)  => myView.showMessageError("general-error")
      case Success(Response(StatusCodes.BAD_REQUEST,_))=>myView.showMessageError("bad-request-error")
    }
    override def drawSalaryPanel(): Unit =
      Utils.userId.foreach(id=>
        model.getSalary(id) onComplete {
          case Success(Response(StatusCodes.SUCCES_CODE, payload)) =>payload.foreach(result=>myView.drawSalaryView(result))
          case Success(Response(StatusCodes.NOT_FOUND,_))=>myView.showMessageError("not-found-error")
          case t => generalSuccessAndError(t)
        }
      )

    override def drawInfoSalary(idSalary: Int): Unit =
      model.getInfoForSalary(idSalary).onComplete {
        case Success(Response(StatusCodes.NOT_FOUND,_))=>myView.showMessageError("not-found-error")
        case Success(Response(_, payload)) =>payload.foreach(result=>myView.informationSalary(result))
        case t => generalSuccessAndError(t)
      }

    override def startupDriverCheck(): Unit = {
      Utils.userId.foreach(e => model.getDisponibilita(e).onComplete{
        case Failure(_)  => myView.showMessageFromKey("general-error")
        case Success(Response(StatusCodes.BAD_REQUEST,_))=> myView.showMessageFromKey("bad-request-error")
        case Success(Response(StatusCodes.NOT_FOUND,_))=> drawHomePanel()
        case Success(Response(_, payload)) =>payload.foreach(result=>myView.drawDisponibilitaPanel(result))
      })
    }

    override def sendDisponibility(day1: String, day2: String): Unit =
      Utils.userId.foreach(userId =>model.setDisponibilita(day1,day2,userId).onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE,_))=>  myView.disponibilityInserted()
        case _ => myView.showMessageFromKey("update-disponibility-failed")
      })

  }

}
