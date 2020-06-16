package view.baseconfiguration

import javafx.scene.control.Label
import junitparams.JUnitParamsRunner
<<<<<<< Updated upstream
import org.junit.Before
=======
import org.junit.{After, Before, Test}
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

  @After
  def closeScene():Unit={
    closeCurrentWindow()
  }

>>>>>>> Stashed changes
}