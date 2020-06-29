package view.manageroperations

import view.baseconfiguration.BaseTest

trait ManageAbsenceOperations {
}

object ManageAbsenceOperations{

  def apply(toTest: BaseTest): ManageAbsenceOperations = new ManageAbsenceOperationsImpl(toTest)

  private class ManageAbsenceOperationsImpl(toTest: BaseTest) extends ManageAbsenceOperations {
  }
}