package view.fxview.component.manager.subcomponent.parent

import utils.TransferObject.InfoRichiesta

trait ManagerRichiestaParent {
  /**
   * method that call view send all information for create a new info request
   * @param richiesta case class that contains all information for info request
   */
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  /**
   * method that show message when user tries return to view [[view.fxview.component.manager.subcomponent.DateAndTerminalBox]]
   * @param str message when return to principal view
   */
  def showBackMessage(str: String): Unit

  /**
   * method that call principal view for return all shift in terminal
   * @param idTerminal id of the terminal that exist in database
   */
  def selectShift(idTerminal: Int): Unit

}
