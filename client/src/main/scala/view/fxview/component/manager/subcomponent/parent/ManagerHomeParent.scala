package view.fxview.component.manager.subcomponent.parent

trait ManagerHomeParent extends FillHolesParent with ManagerRichiestaParent{
  def drawRichiestaPanel(): Unit


  /**
   * Method used when is needed to draw the absence panel
   */
  def drawAbsencePanel(): Unit
  
}
