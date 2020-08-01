package view.baseconfiguration.driver

import caseclass.CaseClassDB.Stipendio
import com.sun.javafx.scene.control.skin.DatePickerContent
import javafx.scene.control.{Label, ListView}
import javafx.scene.layout.{AnchorPane, BorderPane, HBox, VBox}
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import utilstest.StartServer3
import view.baseconfiguration.BaseTest
import view.driverviewoperations.StipendioDriver
import view.launchview.DriverLaunch

@RunWith(classOf[JUnitParamsRunner])
class StipendioTest extends BaseTest with StartServer3{
  var driverStipendio:StipendioDriver=_
  val TOTAL_STIPENDI = 4
  val NORMAL_DAY = "11"
  val EXTRA_DAY = "0"
  val MAY = "May"
  val APRIL = "April"

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[DriverLaunch])
    login()
    driverStipendio = StipendioDriver(this)

  }
  private def login()={
    val user: String = "tutuMaestro"
    val password: String = "rootrootN2"
    clickOn("#usernameField")
    write(user)
    clickOn("#passwordField")
    write(password)
    clickOn("#loginButton")
    sleep(6000)
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
    driverStipendio.clickElementListView("2020-05-01")
    sleep(3000)
    val internalVBox:VBox = find("#internalVBox")
    val normalDay = internalVBox.getChildren.get(2).asInstanceOf[HBox].getChildren.get(1).asInstanceOf[Label]
    assert(normalDay.getText.equals(NORMAL_DAY))
  }
  @Test
  def quantityExtraDayWork():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    driverStipendio.clickElementListView("2020-05-01")
    sleep(3000)
    val internalVBox:VBox = find("#internalVBox")
    val extraDay = internalVBox.getChildren.get(1).asInstanceOf[HBox].getChildren.get(1).asInstanceOf[Label]
    assert(extraDay.getText.equals(EXTRA_DAY))
  }
  @Test
  def monthInCalendar():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    driverStipendio.clickElementListView("2020-05-01")
    sleep(3000)
    val salaryInfo:AnchorPane = find("#datePickerA")
    val datepicker:Label = salaryInfo.getChildren.get(0).asInstanceOf[DatePickerContent]
      .getChildren.get(0).asInstanceOf[BorderPane].getChildren.get(0).asInstanceOf[HBox]
      .getChildren.get(1).asInstanceOf[Label]
    assert(datepicker.getText.equals(MAY))

  }
  @Test
  def monthInCalendar2():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    driverStipendio.clickElementListView("2020-04-01")
    sleep(3000)
    val salaryInfo:AnchorPane = find("#datePickerA")
    val datepicker:Label = salaryInfo.getChildren.get(0).asInstanceOf[DatePickerContent]
      .getChildren.get(0).asInstanceOf[BorderPane].getChildren.get(0).asInstanceOf[HBox]
      .getChildren.get(1).asInstanceOf[Label]
    assert(datepicker.getText.equals(APRIL))
  }
}
