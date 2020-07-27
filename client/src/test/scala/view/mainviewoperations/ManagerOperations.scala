package view.mainviewoperations

import view.baseconfiguration.BaseTest

trait ManagerOperations {
  def openGenerateTurns(): Unit
  def openManageAbsence(): Unit
  def openRedoTurns(): Unit
  def openPrintResult(): Unit
  def openManageZone(): Unit
  def openManageTerminal(): Unit
  def openManageTeoricRequest(): Unit
  def openRunAlgorithm():Unit
}

object ManagerOperations{

  def apply(toTest: BaseTest): ManagerOperations = new ManagerOperationsImpl(toTest)

  private class ManagerOperationsImpl(toTest: BaseTest) extends ManagerOperations{
    val user: String = "admin"
    val password: String = "rootrootN2"

    toTest.clickOn("#usernameField")
    toTest.write(user)
    toTest.clickOn("#passwordField")
    toTest.write(password)
    toTest.clickOn("#loginButton")
    toTest.sleep(5000)

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

    override def openManageTeoricRequest(): Unit =
      toTest.clickOn("#richiestaButton")

    override def openRunAlgorithm(): Unit =
      toTest.clickOn("#generateTurnsButton")
  }
}
