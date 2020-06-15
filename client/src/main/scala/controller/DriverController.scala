package controller

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
      Future.successful( myView.drawHomeView()).onComplete {
      case Failure(_) => myView.showMessage("Error")
      case Success(value) => ???
    }

    override def drawShiftPanel(): Unit =
      Future.successful(myView.drawShiftView()).onComplete {
        case Failure(_) => myView.showMessage("Error")
        case Success(value) => ???
      }

    override def drawSalaryPanel(): Unit =
      model.getSalary(5) onComplete {
        case Success(value) =>value.payload.foreach(result=>myView.drawSalaryView(result))
        case Failure(_) =>myView.showMessage("Error")
      }
    override def drawInfoSalary(idSalary: Int): Unit =
      model.getInfoForSalary(idSalary).onComplete {
        case Failure(_) => myView.showMessage("Error")
        case Success(value) =>value.payload.foreach(result=>myView.informationSalary(result))
      }
  }

}
