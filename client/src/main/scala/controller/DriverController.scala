package controller

import java.sql.Date

import caseclass.CaseClassDB.Stipendio
import view.fxview.mainview.DriverView

trait DriverController  extends AbstractController[DriverView] {
  def drawHomePanel(): Unit

  def drawShiftPanel(): Unit

  def drawSalaryPanel(): Unit
}
object DriverController{
  def apply(): DriverController = new DriverControllerImpl()
  private class DriverControllerImpl() extends DriverController {
    override def drawHomePanel(): Unit = myView.drawHomeView()

    override def drawShiftPanel(): Unit = myView.drawShiftView()

    override def drawSalaryPanel(): Unit = myView.drawSalaryView(List(Stipendio(1,20,new Date(System.currentTimeMillis()))))
  }

}
