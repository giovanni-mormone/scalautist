package view.baseconfiguration.manager

import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import view.baseconfiguration.BaseTest
import view.launchview.ManagerLaunch
import view.mainviewoperations.ManagerOperations
import view.manageroperations.ManagerParamsOperation

@RunWith(classOf[JUnitParamsRunner])
class ManagerParamsTest extends BaseTest {

  var params: ManagerParamsOperation = _
  var managerOperations: ManagerOperations = _
  var modalOn: Boolean = false

  @Before
  def beforeEachManageAbsenceTest(): Unit = {
    setUp(classOf[ManagerLaunch])
    params = ManagerParamsOperation(this)
    managerOperations = ManagerOperations(this)
    managerOperations.openGenerateTurns()
    params.sleep(2000)
  }
  @After
  def closeStage():Unit={
    closeCurrentWindow()
  }

  @Test
  def initialNoNextTest(): Unit =
    assert(!params.isButtonEnable("#run"))

  @Test
  def nextTest(): Unit = {
    params.setTime("#initDate",1)
    params.setTime("#endDate", 2)
    params.clickOnComponent("#old")
    params.sleep(2000)
    params.chooseOldParam()
    params.sleep(2000)
    params.clickOnComponent("#open")
    params.sleep(2000)
    params.clickNext()
    params.sleep(2000)
    assert(params.isThere("#dayShiftN"))
  }

  @Test
  def openModalOldParam(): Unit = {
    params.setTime("#initDate",1)
    params.setTime("#endDate", 2)
    params.clickOnComponent("#old")
    params.sleep(2000)
    params.chooseOldParam()
    params.sleep(2000)
    params.clickOnComponent("#open")
    params.sleep(2000)
    assert(params.isButtonEnable("#run"))
  }

  @Test
  def next2Test(): Unit = {
    params.setTime("#initDate",1)
    params.setTime("#endDate", 2)
    params.clickOnComponent("#old")
    params.sleep(2000)
    params.chooseOldParam()
    params.sleep(2000)
    params.clickOnComponent("#open")
    params.sleep(2000)
    params.clickNext()
    params.sleep(2000)
    params.clickNext()
    params.sleep(2000)
    assert(params.isThere("#add"))
  }

  @Test
  def next3Test(): Unit = {
    params.setTime("#initDate",1)
    params.setTime("#endDate", 2)
    params.clickOnComponent("#old")
    params.sleep(2000)
    params.chooseOldParam()
    params.sleep(2000)
    params.clickOnComponent("#open")
    params.sleep(2000)
    params.clickNext()
    params.sleep(2000)
    params.clickNext()
    params.sleep(2000)
    params.clickNext()
    params.sleep(2000)
    assert(params.isThere("#titleInitial"))
  }

}
