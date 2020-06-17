package controller

import view.fxview.mainview.ManagerView

trait ManagerController extends AbstractController[ManagerView]{

}

object ManagerController {
  private val instance = new ManagerControllerImpl()
//  private val model = ManagerModel()

  def apply(): ManagerController = instance

  private class ManagerControllerImpl extends ManagerController {

  }
}