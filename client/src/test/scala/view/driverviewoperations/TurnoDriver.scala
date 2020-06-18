package view.driverviewoperations

import view.baseconfiguration.BaseTest

trait TurnoDriver {
  def clickTurnoMenu():Unit
  def clickLunedi():Unit
  def clickVenerdi():Unit
}
object TurnoDriver {
  def apply(toTest: BaseTest): TurnoDriver = new TurnoDriverImpl(toTest)

  private class TurnoDriverImpl(toTest: BaseTest) extends TurnoDriver{
    override def clickTurnoMenu(): Unit = toTest.clickOn("#labelTurni")

    override def clickLunedi(): Unit = toTest.clickOn("Lunedi")

    override def clickVenerdi(): Unit = ???
  }
}
