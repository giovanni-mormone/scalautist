package view.driverviewoperations

import view.baseconfiguration.BaseTest

trait StipendioOperation{
  def clickDateStipendio(): Unit
  def clickButton(): Unit
}
object StipendioOperation {
  def apply(toTest: BaseTest): StipendioOperation = new StipendioOperationImpl(toTest)

  private class StipendioOperationImpl(toTest: BaseTest) extends StipendioOperation{
    override def clickDateStipendio(): Unit = ???

    override def clickButton(): Unit = ???
  }
}
