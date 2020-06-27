package view.fxview.component.manager.subcomponent.parent

import view.fxview.component.manager.subcomponent.ManagerRichiestaBox.InfoRichiesta

trait ManagerRichiestaParent {
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  def showBackMessage(str: String): Unit

  def getShift(idTerminal: Int): Unit

}
