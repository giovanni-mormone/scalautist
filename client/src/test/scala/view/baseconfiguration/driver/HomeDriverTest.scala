package view.baseconfiguration.driver

import java.util.Locale

import com.sun.javafx.scene.control.skin.{DatePickerContent, TableColumnHeader}
import javafx.scene.control.Label
import javafx.scene.layout.{AnchorPane, BorderPane, HBox}
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import utilstest.StartServer3
import view.baseconfiguration.BaseTest
import view.driverviewoperations.HomeDriver
import view.launchview.DriverLaunch

@RunWith(classOf[JUnitParamsRunner])
class HomeDriverTest extends BaseTest with StartServer3{
  var driverHome:HomeDriver=_
  val ORARIO_PRIMA_MATTINATA = "8:00-12:00"
  val PRIMA_MATTINATA = "Seconda Mattinata"
  val ORARIO_PRIMA_POMERIGGIO = "12:00-16:00"
  val PRIMA_POMERIGGIO = "Primo Pomeriggio"
  val DISPONIBILITA = "true"
  val GIORNO = "Lunedi"
  val GIORNO2 = "Martedi"
  val MONTH: String = month()

  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[DriverLaunch])
    login()
    driverHome = HomeDriver(this)
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
  @Test
  def verifyInfo():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickTable()
    val table:TableColumnHeader = find("#orario")
    assert(table.getTableColumn.getCellData(0).equals(ORARIO_PRIMA_MATTINATA))
  }
  @Test
  def verifyInfoOrarioTurno():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickTable()
    val columnOrario:TableColumnHeader = find("#orario")
    val columnTurno:TableColumnHeader = find("#turno")
    assert(columnOrario.getTableColumn.getCellData(0).equals(ORARIO_PRIMA_MATTINATA) && columnTurno.getTableColumn.getCellData(0).equals(PRIMA_MATTINATA))
  }
  @Test
  def verifyOrderedColumn():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickHeader()
    val table:TableColumnHeader = find("#orario")
    assert(table.getTableColumn.getCellData(0).equals(ORARIO_PRIMA_POMERIGGIO))
  }
  @Test
  def verifyOrderedColumn2():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickHeader()
    val table:TableColumnHeader = find("#turno")
    assert(table.getTableColumn.getCellData(0).equals(PRIMA_POMERIGGIO))
  }
  @Test
  def verifyAvailability():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickTableAvailability()
    val table:TableColumnHeader = find("#disponibilita")
    assert(table.getTableColumn.getCellData(0).equals(DISPONIBILITA))
  }
  @Test
  def verifyAvailability2():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickTableAvailability()
    val table:TableColumnHeader = find("#giorno")
    assert(table.getTableColumn.getCellData(0).equals(GIORNO))
  }
  @Test
  def verifyOrderedAvailability():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickHeaderAvailability()
    val table:TableColumnHeader = find("#disponibilita")
    assert(table.getTableColumn.getCellData(0).equals(DISPONIBILITA))
  }
  @Test
  def verifyOrderedAvailability2():Unit={
    driverHome.clickHomeMenu()
    ensureEventQueueComplete()
    sleep(4000)
    driverHome.clickHeaderAvailability()
    val table:TableColumnHeader = find("#giorno")
    assert(table.getTableColumn.getCellData(0).equals(GIORNO2))
  }
  @Test
  def verifyDatepicker():Unit={
    driverHome.clickHomeMenu()
    val table:AnchorPane = find("#datepicker")
    val datepicker:Label = table.getChildren.get(0).asInstanceOf[DatePickerContent]
      .getChildren.get(0).asInstanceOf[BorderPane].getChildren.get(0).asInstanceOf[HBox]
      .getChildren.get(1).asInstanceOf[Label]
    assert(datepicker.getText.equals(MONTH))
  }

  private def month(): String ={
    import java.util.Calendar
    val mCalendar = Calendar.getInstance
    mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
  }
}
