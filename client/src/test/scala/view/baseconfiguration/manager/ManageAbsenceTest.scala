package view.baseconfiguration.manager

import junitparams.JUnitParamsRunner
import org.junit.{After, Before, Test}
import org.junit.runner.RunWith
import view.baseconfiguration.BaseTest
import view.launchview.ManagerLaunch
import view.mainviewoperations.ManagerOperations
import view.manageroperations.ManageAbsenceOperations

@RunWith(classOf[JUnitParamsRunner])
class ManageAbsenceTest extends BaseTest{

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
    Thread.sleep(15000)
    absence.clickAbsence()
    Thread.sleep(10000)
  }
}
