package view.baseconfiguration.humanresource

import java.time.LocalDate

import javafx.application.Platform
import javafx.scene.control.{Button, ComboBox, Label, TextField}
import junitparams.JUnitParamsRunner
import org.junit.{After, Before, Test}
import org.junit.runner.RunWith
import utilstest.StartServer
import view.baseconfiguration.BaseTest
import view.humanresourceoperation.RecruitOperation
import view.launchview.HumanResourceLaunch

@RunWith(classOf[JUnitParamsRunner])
class AssumiTest extends BaseTest with StartServer{

  var assumi: RecruitOperation = _

  val date: LocalDate = LocalDate.now()
  val name: String = "Mar1io."
  val trueName: String = "Mario"
  val tel: String = "1.23s4"
  val trueTel: String = "1234"
  val TryRecruitTel: String = "1234567890"

  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[HumanResourceLaunch])
    login()
    assumi = RecruitOperation(this)
    assumi.openRecruit()
    ensureEventQueueComplete()
    sleep(3500)
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
  def closeStage(): Unit = {
    Platform.runLater(()=>myStage.close())
  }

  @Test
  def enterName(): Unit = {
    assumi.enterName(name)
    sleep(2000)
    val text: TextField = assumi.getNameField
    assert(text.getText().equals(trueName))
  }

  @Test
  def enterSurname(): Unit = {
    assumi.enterSurname(name)
    sleep(2000)
    val text: TextField = assumi.getSurnameField
    assert(text.getText().equals(trueName))
  }

  @Test
  def enterTel(): Unit = {
    assumi.enterTel(tel)
    sleep(2000)
    val text: TextField = assumi.getTelField
    assert(text.getText().equals(trueTel))
  }

  @Test
  def choseRoleManager(): Unit = {
    assumi.chooseRole("Manager di sistema")
    sleep(100)
    val selected: ComboBox[String] = assumi.getRole
    assert(selected.getSelectionModel.getSelectedItem.equals("Manager di sistema"))
  }

  @Test
  def choseContractManager(): Unit = {
    assumi.chooseRole("Manager di sistema")
    sleep(100)
    val contract: ComboBox[String] = assumi.getContract
    assert(contract.getItems.size() == 1)
  }

  @Test
  def choseRoleHR(): Unit = {
    assumi.chooseRole("Risorse umane")
    sleep(100)
    val selected: ComboBox[String] = assumi.getRole
    assert(selected.getSelectionModel.getSelectedItem.equals("Risorse umane"))
  }

  @Test
  def choseContractHR(): Unit = {
    assumi.chooseRole("Risorse umane")
    sleep(100)
    val contract: ComboBox[String] = assumi.getContract
    assert(contract.getItems.size() == 1)
  }

  @Test
  def recruitHROrManager(): Unit = {
    assumi.enterName(trueName)
    assumi.enterSurname(trueName)
    assumi.enterTel(TryRecruitTel)
    assumi.enterData(LocalDate.now())
    assumi.chooseRole("Risorse umane")
    sleep(100)
    assumi.pressRecruit()
    sleep(3000)
    val recruit: Label = assumi.getMessage
    assert(!recruit.getText.equals(""))
  }

  @Test
  def enableFieldForDriverFixFull5x2(): Unit = {
    assumi.chooseRole("Autista")
    assumi.chooseContract("Full-Time-5x2-Fisso")
    assert(!assumi.getZone.isDisable && !assumi.getExtraday1.isDisable && assumi.getTerminal.isDisable &&
      !assumi.getExtraday2.isDisable && !assumi.getShift1.isDisable && assumi.getShift2.isDisable &&
      assumi.getExtraday1.getItems.size() == 5 && assumi.getExtraday2.getItems.size() == 5)
  }

  @Test
  def enableFieldForDriverFixFull6x1(): Unit = {
    assumi.chooseRole("Autista")
    assumi.chooseContract("Full-Time-6x1-Fisso")
    assert(!assumi.getZone.isDisable && !assumi.getExtraday1.isDisable && assumi.getTerminal.isDisable &&
      !assumi.getExtraday2.isDisable && !assumi.getShift1.isDisable && assumi.getShift2.isDisable &&
      assumi.getExtraday1.getItems.size() == 6 && assumi.getExtraday2.getItems.size() == 6)
  }

  @Test
  def controlShiftForDriverFixFull6x1(): Unit = {
    assumi.chooseRole("Autista")
    assumi.chooseContract("Full-Time-6x1-Fisso")
    assumi.chooseShift("10:00 alle 14:00")
    assert(!assumi.getZone.isDisable && assumi.getTerminal.isDisable &&
      assumi.getShift2.isDisable && assumi.getShift2.getSelectionModel.getSelectedItem.equals("14:00 alle 18:00"))
  }

  @Test
  def controlShiftForDriverFixPart6x1(): Unit = {
    assumi.chooseRole("Autista")
    assumi.chooseContract("Part-Time-6x1-Fisso")
    assumi.chooseShift("10:00 alle 14:00")
    assert( assumi.getShift2.isDisable && assumi.getShift2.getSelectionModel.isEmpty)
  }

  @Test
  def controlExtraForDriverFixPart6x1(): Unit = {
    assumi.chooseRole("Autista")
    assumi.chooseContract("Part-Time-6x1-Fisso")
    assumi.chooseExtra1("Lunedi")
    assumi.chooseExtra2("Lunedi")
    assert( assumi.getShift2.getSelectionModel.isEmpty)
  }

  @Test
  def disableFieldForDriverRotFull5x2(): Unit = {
    assumi.chooseRole("Autista")
    assumi.chooseContract("Full-Time-5x2-Rotazione")
    assert(!assumi.getZone.isDisable && assumi.getTerminal.isDisable && assumi.getExtraday1.isDisable &&
      assumi.getExtraday2.isDisable && assumi.getShift1.isDisable && assumi.getShift2.isDisable )
  }

  @Test
  def chooseTerminal(): Unit = {
    assumi.chooseRole("Autista")
    assumi.chooseZona("Cesena")
    sleep(1000)
    assumi.chooseTerminal("Cansas")
    assert(assumi.getZone.getSelectionModel.getSelectedItem.equals("Cesena") &&
      assumi.getTerminal.getSelectionModel.getSelectedItem.equals("Cansas") &&
      assumi.getExtraday1.isDisable && assumi.getExtraday2.isDisable &&
      assumi.getShift1.isDisable && assumi.getShift2.isDisable )
  }

  @Test
  def recruitDriverFix(): Unit = {
    assumi.enterName(trueName)
    assumi.enterSurname(trueName)
    assumi.enterTel(TryRecruitTel)
    assumi.enterData(LocalDate.now())
    assumi.chooseRole("Autista")
    assumi.chooseContract("Part-Time-6x1-Fisso")
    assumi.chooseShift("10:00 alle 14:00")
    assumi.chooseExtra1("Lunedi")
    assumi.chooseExtra2("Martedi")
    assumi.chooseZona("Cesena")
    sleep(1000)
    assumi.chooseTerminal("Cansas")
    sleep(100)
    assumi.pressRecruit()
    sleep(3000)
    val recruit: Label = assumi.getMessage
    assert(!recruit.getText.equals(""))
  }

  @Test
  def recruitDriverRot(): Unit = {
    assumi.enterName(trueName)
    assumi.enterSurname(trueName)
    assumi.enterTel(TryRecruitTel)
    assumi.enterData(LocalDate.now())
    assumi.chooseRole("Autista")
    assumi.chooseContract("Part-Time-6x1-Rotazione")
    assumi.chooseZona("Cesena")
    sleep(1000)
    assumi.chooseTerminal("Cansas")
    sleep(100)
    assumi.pressRecruit()
    sleep(3000)
    val recruit: Label = assumi.getMessage
    assert(!recruit.getText.equals(""))
  }

}
