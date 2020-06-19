package view.fxview.component.manager.subcomponent.parent

trait ManagerHomeParent extends TerminalAndTurnsParent with ReplacementParent{

  /**
   * Method use when is needed to draw the absence panel
   */
  def drawAbsencePanel(): Unit
  
}
