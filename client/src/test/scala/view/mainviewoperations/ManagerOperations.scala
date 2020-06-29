package view.mainviewoperations

import view.baseconfiguration.BaseTest

trait ManagerOperations {
  def openGenerateTurns(): Unit
  def openManageAbsence(): Unit
  def openRedoTurns(): Unit
  def openPrintResult(): Unit
  def openManageZone(): Unit
  def openManageTerminal(): Unit
}

object ManagerOperations{

  def apply(toTest: BaseTest): ManagerOperations = new ManagerOperationsImpl(toTest)

  private class ManagerOperationsImpl(toTest: BaseTest) extends ManagerOperations{
    override def openGenerateTurns(): Unit =
      toTest.clickOn("#generateTurnsButton")

    override def openManageAbsence(): Unit =
      toTest.clickOn("#manageAbsenceButton")

    override def openRedoTurns(): Unit =
      toTest.clickOn("#redoTurnsButton")

    override def openPrintResult(): Unit =
      toTest.clickOn("#printResultButton")

    override def openManageZone(): Unit =
      toTest.clickOn("#manageZoneButton")

    override def openManageTerminal(): Unit =
      toTest.clickOn("#manageTerminalButton")
  }
}
