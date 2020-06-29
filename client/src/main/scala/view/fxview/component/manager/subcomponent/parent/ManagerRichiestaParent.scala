package view.fxview.component.manager.subcomponent.parent

import utils.TransferObject.InfoRichiesta

trait ManagerRichiestaParent {
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  def showBackMessage(str: String): Unit

  def selectShift(idTerminal: Int): Unit

}
