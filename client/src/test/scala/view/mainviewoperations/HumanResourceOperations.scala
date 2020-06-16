package view.mainviewoperations

import view.baseconfiguration.BaseTest
import view.fxview.mainview.HumanResourceView

trait HumanResourceOperations {
  //home operations
  def openRescruit(): Unit
  def openFire(): Unit
  def openSick(): Unit
  def openHoliday(): Unit
  def openChangePassword(): Unit
  def openZona(): Unit
  def openTerminal(): Unit
}

object HumanResourceOperations {

  def apply(toTest: BaseTest): HumanResourceOperations = new HumanResourceOperationsImpl(toTest)

  private class HumanResourceOperationsImpl(toTest: BaseTest) extends HumanResourceOperations {

    override def openRescruit(): Unit =
      toTest.clickOn("#recruitButton")

    override def openFire(): Unit =
      toTest.clickOn("#firesButton")

    override def openSick(): Unit =
      toTest.clickOn("#illness")

    override def openHoliday(): Unit =
      toTest.clickOn("#holidays")

    override def openChangePassword(): Unit =
      toTest.clickOn("#changePassword")

    override def openZona(): Unit =
      toTest.clickOn("#zonaManage")

    override def openTerminal(): Unit =
      toTest.clickOn("#terminalManger")

  }
}