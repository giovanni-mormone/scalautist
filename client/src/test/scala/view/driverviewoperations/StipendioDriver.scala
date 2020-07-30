package view.driverviewoperations

import view.baseconfiguration.BaseTest

import scala.annotation.nowarn

trait StipendioDriver {
  def clickStipendioMenu():Unit
  def clickElementListView(date:String):Unit
}
object StipendioDriver {
  def apply(toTest: BaseTest): StipendioDriver = new StipendioDriverImpl(toTest)

  private class StipendioDriverImpl(toTest: BaseTest) extends StipendioDriver{
    override def clickStipendioMenu(): Unit = toTest.clickOn("#labelStipendio")

    override def clickElementListView(date:String): Unit = toTest.clickOn(date)
  }
}
