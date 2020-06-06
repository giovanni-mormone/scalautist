package controller

import view.fxview.mainview.DriverView

trait DriverController  extends AbstractController[DriverView] {

}
object DriverController{
  def apply(): DriverController = new DriverControllerImpl()
  private class DriverControllerImpl() extends DriverController

}
