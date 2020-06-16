package view.baseconfiguration

import junitparams.JUnitParamsRunner
import org.junit.{After, Before, Test}
import org.junit.runner.RunWith
import view.driverviewoperations.TurnoDriver
import view.launchview.DriverLaunch

@RunWith(classOf[JUnitParamsRunner])
class TurnoTest extends BaseTest {
  var driverTurno:TurnoDriver=_
  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[DriverLaunch])
    driverTurno = TurnoDriver(this)
  }

  @After
  def closeScene():Unit={
    closeCurrentWindow()
  }
}