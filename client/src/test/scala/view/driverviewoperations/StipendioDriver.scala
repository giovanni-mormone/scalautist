package view.driverviewoperations

import view.baseconfiguration.BaseTest

trait StipendioDriver {
  def clickStipendioMenu():Unit
  def clickElementListView():Unit
}
object StipendioDriver {
  def apply(toTest: BaseTest): StipendioDriver = new StipendioDriverImpl(toTest)

  private class StipendioDriverImpl(toTest: BaseTest) extends StipendioDriver{
    override def clickStipendioMenu(): Unit = toTest.clickOn("#labelStipendio")

    override def clickElementListView(): Unit = ???
  }
}
