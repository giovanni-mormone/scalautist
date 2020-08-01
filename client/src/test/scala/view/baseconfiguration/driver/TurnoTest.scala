package view.baseconfiguration.driver

import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before}
import utilstest.StartServer3
import view.baseconfiguration.BaseTest
import view.driverviewoperations.TurnoDriver
import view.launchview.DriverLaunch

@RunWith(classOf[JUnitParamsRunner])
class TurnoTest extends BaseTest with StartServer3{
  var driverTurno:TurnoDriver=_
  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[DriverLaunch])
    login()
    driverTurno = TurnoDriver(this)
  }
  private def login()={
    val user: String = "root2"
    val password: String = "rootrootN2"

    clickOn("#usernameField")
    write(user)
    clickOn("#passwordField")
    write(password)
    clickOn("#loginButton")
    sleep(6000)
  }
  @After
  def closeScene():Unit={
    closeCurrentWindow()
  }
}