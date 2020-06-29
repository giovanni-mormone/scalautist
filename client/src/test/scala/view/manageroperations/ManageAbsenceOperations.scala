package view.manageroperations

import view.baseconfiguration.BaseTest

trait ManageAbsenceOperations {
  def clickAbsence(): Unit
}

object ManageAbsenceOperations{

  def apply(toTest: BaseTest): ManageAbsenceOperations = new ManageAbsenceOperationsImpl(toTest)

  private class ManageAbsenceOperationsImpl(toTest: BaseTest) extends ManageAbsenceOperations{
    override def clickAbsence(): Unit = ???
}