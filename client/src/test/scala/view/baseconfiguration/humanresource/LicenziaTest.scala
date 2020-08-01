package view.baseconfiguration.humanresource

import javafx.application.Platform
import javafx.scene.control.{Label, TableView}
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import utilstest.StartServer
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.PersonaTableWithSelection
import view.humanresourceoperation.FireOperation
import view.launchview.HumanResourceLaunch

@RunWith(classOf[JUnitParamsRunner])
class LicenziaTest extends BaseTest with StartServer{

  var licenzia: FireOperation = _

  val nameToSearch: String = "f"
  val resultOk: String = "Operazione riuscita con successo"
  val resultFail: String = "Errore! Seleziona un utente"

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[HumanResourceLaunch])
    login()
    licenzia = FireOperation(this)
    licenzia.openFireBox()
    ensureEventQueueComplete()
    sleep(7000)
  }

  @After
  def closeStage(): Unit = {
    Platform.runLater(()=>myStage.close())
  }

  private def login()={
    val user: String = "risuma"
    val password: String = "rootrootN2"

    clickOn("#usernameField")
    write(user)
    clickOn("#passwordField")
    write(password)
    clickOn("#loginButton")
    sleep(10000)
  }

  @Test
  def checkOne(): Unit = {
    licenzia.fireOne()
    ensureEventQueueComplete()
    licenzia.pressFireButton()
    ensureEventQueueComplete()
    sleep(2000)
    val text: Label = licenzia.getLabel
    assert(text.getText.equals(resultOk))
  }

  @Test
  def checkMore(): Unit = {
    licenzia.fireMore()
    ensureEventQueueComplete()
    licenzia.pressFireButton()
    ensureEventQueueComplete()
    sleep(2000)
    val text: Label = licenzia.getLabel
    assert(!text.getText.equals(""))
  }

  @Test
  def checkNobody(): Unit = {
    licenzia.pressFireButton()
    ensureEventQueueComplete()
    sleep(2000)
    val text: Label = licenzia.getLabel
    assert(text.getText.equals(resultFail))
  }

  @Test
  def searchSomeone(): Unit = {
    licenzia.searchSomeone(nameToSearch)
    ensureEventQueueComplete()
    sleep(2000)
    val table: TableView[PersonaTableWithSelection] = licenzia.getTable
    assert(table.getItems.size() == 3)
  }
}
