package view.driverviewoperations

import view.baseconfiguration.BaseTest

trait MalattieOperation{
  def enterFirstDate(day: String): Unit
  def enterSecondDate(day: String): Unit
  def clickButton(): Unit
  def clickModalButton(): Unit
  def clickTable():Unit
}
object MalattieOperation {
  def apply(toTest: BaseTest): MalattieOperation = new MalattieOperationImpl(toTest)

  private class MalattieOperationImpl(toTest: BaseTest) extends MalattieOperation{
    override def enterFirstDate(day: String): Unit = ???

    override def enterSecondDate(day: String): Unit = ???

    override def clickButton(): Unit = ???

    override def clickModalButton(): Unit = ???

    override def clickTable(): Unit = ???
  }
}
