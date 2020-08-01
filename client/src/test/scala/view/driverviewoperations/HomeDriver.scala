package view.driverviewoperations

import view.baseconfiguration.BaseTest

trait HomeDriver {
  def clickHeaderAvailability():Unit
  def clickTableAvailability():Unit
  def clickHomeMenu():Unit
  def clickTable():Unit
  def clickHeader(): Unit
}
object HomeDriver {
  def apply(toTest: BaseTest): HomeDriver = new HomeDriverImpl(toTest)

  private class HomeDriverImpl(toTest: BaseTest) extends HomeDriver{
    val user: String = "driver"
    val password: String = "rootrootN2"

    toTest.clickOn("#usernameField")
    toTest.write(user)
    toTest.clickOn("#passwordField")
    toTest.write(password)
    toTest.clickOn("#loginButton")
    toTest.sleep(6000)

    override def clickHomeMenu(): Unit = toTest.clickOn("#labelHome")

    override def clickTable(): Unit = {
      toTest.moveTo("#tableTurno").clickOn( "6:00 alle 10:00")
    }
    override def clickHeader(): Unit = {
      toTest.moveTo("#tableTurno")
      toTest.clickOn("#orario")
    }

    override def clickHeaderAvailability(): Unit = {
      toTest.moveTo("#tableDisponibilita")
      toTest.doubleClickOn("#giorno")
    }

    override def clickTableAvailability(): Unit = {
      toTest.moveTo("#tableDisponibilita").clickOn( "Lunedi")
    }
  }
}
