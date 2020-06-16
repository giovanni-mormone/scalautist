package view.baseconfiguration

import caseclass.CaseClassDB.Stipendio
import javafx.scene.control.ListView
import junitparams.JUnitParamsRunner
import org.junit.{After, Before, Test}
import org.junit.runner.RunWith
import view.driverviewoperations.StipendioDriver
import view.launchview.DriverLaunch

@RunWith(classOf[JUnitParamsRunner])
class StipendioTest extends BaseTest {
  var driverStipendio:StipendioDriver=_
  val TOTAL_STIPENDI = 4
  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[DriverLaunch])
    driverStipendio = StipendioDriver(this)
  }
  @After
  def closeScene():Unit={
    closeCurrentWindow()
  }

  @Test
  def lengthListView():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    val list:ListView[Stipendio] = find("#salaryList")
    assert(list.getItems.parallelStream().count()==TOTAL_STIPENDI)
  }
  @Test
  def quantityNormalDayWork():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    val list:ListView[Stipendio] = find("#salaryList")
    assert(list.getItems.parallelStream().count()==TOTAL_STIPENDI)
  }
  @Test
  def quantityExtraDayWork():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    val list:ListView[Stipendio] = find("#salaryList")
    assert(list.getItems.parallelStream().count()==TOTAL_STIPENDI)
  }
}
