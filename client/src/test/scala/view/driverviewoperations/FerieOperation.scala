package view.driverviewoperations

import view.baseconfiguration.BaseTest

trait FerieOperation{
  def enterFirstDate(day: String): Unit
  def enterSecondDate(day: String): Unit
  def clickButton(): Unit
  def clickModalButton(): Unit
  def clickTable():Unit
}
object FerieOperation {
  def apply(toTest: BaseTest): FerieOperation = new FerieOperationImpl(toTest)

  private class FerieOperationImpl(toTest: BaseTest) extends FerieOperation{

    override def enterFirstDate(day: String): Unit = ???

    override def enterSecondDate(day: String): Unit = ???

    override def clickButton(): Unit = ???

    override def clickModalButton(): Unit = ???

    override def clickTable(): Unit = ???
  }
}
