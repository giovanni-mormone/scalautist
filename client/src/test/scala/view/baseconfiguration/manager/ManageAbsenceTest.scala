package view.baseconfiguration.manager

import junitparams.JUnitParamsRunner
import org.junit.{After, Before, Test}
import org.junit.runner.RunWith
import utilstest.StartServer3
import view.baseconfiguration.BaseTest
import view.launchview.ManagerLaunch
import view.mainviewoperations.ManagerOperations
import view.manageroperations.ManageAbsenceOperations

@RunWith(classOf[JUnitParamsRunner])
class ManageAbsenceTest extends BaseTest with StartServer3{

  var absence: ManageAbsenceOperations = _
  var managerOperations: ManagerOperations = _
  @Before
  def beforeEachManageAbsenceTest(): Unit = {
    setUp(classOf[ManagerLaunch])
    absence = ManageAbsenceOperations(this)
    managerOperations = ManagerOperations(this)
  }

  @After
  def closeStage():Unit={
    closeCurrentWindow()
  }

  @Test
  def test(): Unit = {
    managerOperations.openManageAbsence()

  }
}
