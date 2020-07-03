package view.baseconfiguration.manager

import javafx.scene.control.{Label, TextField}
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import view.baseconfiguration.BaseTest
import view.launchview.ManagerLaunch
import view.mainviewoperations.ManagerOperations
import view.manageroperations.ManagerTeoricRequestOperation

@RunWith(classOf[JUnitParamsRunner])
class ManagerTeoricRequest extends BaseTest{

  var teoricRequest: ManagerTeoricRequestOperation = _
  var managerOperations: ManagerOperations = _
  val MESSAGE_ERROR_1 = "Devi selezionare entrambi le date"
  val MESSAGE_ERROR_2 = "Le date devono avere una differenza di 28 giorni"
  val MESSAGE_ERROR_3 = "Deve selezionare al meno un terminale"
  val DAY = "Lunedi"
  val MESSAGE_ERROR_4 = "puoi solo inserire numeri"
  val MESSAGE_ERROR_5 = "riempire"
  val DAY2 = "Martedi"
  val DAY7 = "Domenica"
  val RIEPILOGO = "Riepilogo"
  val TEORIC_REQUEST = "Gestione Richiesta Teorica"

  @Before
  def beforeEachManageAbsenceTest(): Unit = {
    setUp(classOf[ManagerLaunch])
    teoricRequest = ManagerTeoricRequestOperation(this)
    managerOperations = ManagerOperations(this)
  }
  @After
  def closeStage():Unit={
    closeCurrentWindow()
  }

  @Test
  def errorDatepickerTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    teoricRequest.sleep(2000)
    teoricRequest.clickNext()
    val labelError:Label=find("#errorLabel")
    assert(labelError.getText.equals(MESSAGE_ERROR_1))
  }

  @Test
  def errorGreaterDateTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    teoricRequest.sleep(2000)
    teoricRequest.setTime()
    teoricRequest.setTerminal()
    teoricRequest.clickNext()
    ensureEventQueueComplete()
    val labelError:Label=find("#errorLabel")
    assert(labelError.getText.equals(MESSAGE_ERROR_2))
  }

  @Test
  def errorTerminalTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    teoricRequest.sleep(5000)
    teoricRequest.setTimeCorrect()
    teoricRequest.clickNext()
    val labelError:Label=find("#errorLabelTerminal")
    assert(labelError.getText.equals(MESSAGE_ERROR_3))
  }

  @Test
  def setFirstViewOkTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    val titleDay:Label=find("#titleDay")
    assert(titleDay.getText.equals(DAY))
  }
  private def setFirstView(): Unit ={
    teoricRequest.sleep(4000)
    teoricRequest.setTimeCorrect()
    teoricRequest.setTerminal()
    teoricRequest.clickNext()
    ensureEventQueueComplete()
    teoricRequest.sleep(1000)
  }
  @Test
  def onlyIntTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.writeInFirstShift()
    val errorLabel:Label=find("#errorLabel")
    assert(errorLabel.getText.equals(MESSAGE_ERROR_4))
  }

  @Test
  def fieldNotCompleteTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.writeInt()
    teoricRequest.clickNext()
    val errorLabel:Label=teoricRequest.search()
    assert(errorLabel.getText.equals(MESSAGE_ERROR_5))
  }

  @Test
  def setSecondViewOkTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.setAllShiftWithQuantity()
    teoricRequest.clickNext()
    val titleDay:Label=find("#titleDay")
    assert(titleDay.getText.equals(DAY2))
  }

  @Test
  def setAllViewOkTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.setAllDay()
    val titleDay:Label=find("#titleDay")
    assert(titleDay.getText.equals(DAY7))
  }

  @Test
  def finalViewTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.setAllDay()
    teoricRequest.setAllShiftWithQuantity()
    teoricRequest.clickNext()
    teoricRequest.sleep(1000)
    val titleDay:Label=find("#titleDay")
    assert(titleDay.getText.equals(RIEPILOGO))
  }

  @Test
  def RiepilogoViewTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.setAllDay()
    teoricRequest.setAllShiftWithQuantity()
    teoricRequest.clickNext()
    teoricRequest.sleep(1000)
    val dataI:Label=find("#dataI")
    assert(dataI.getText.equals(teoricRequest.getDateI))
  }

  @Test
  def Riepilogo2ViewTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.setAllDay()
    teoricRequest.setAllShiftWithQuantity()
    teoricRequest.clickNext()
    teoricRequest.sleep(1000)
    val dataI:Label=find("#dataF")
    assert(dataI.getText.equals(teoricRequest.getDateF))
  }

  @Test
  def nextActionWithoutInfoTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.setAllShiftWithQuantity()
    teoricRequest.clickNext()
    val quantity:TextField=find("#quantity")
    assert(quantity.getText.isEmpty)
  }

  @Test
  def nextActionWithInfoTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    setTwoView()
    teoricRequest.clickBack()
    teoricRequest.clickBack()
    teoricRequest.clickNext()
    val quantity:TextField=find("#quantity")
    assert(quantity.getText.equals(teoricRequest.getQuantity))
  }
  private def setTwoView(): Unit ={
    (1 to 2).foreach(_=>{
      teoricRequest.setAllShiftWithQuantity()
      teoricRequest.clickNext()
    })
  }
  @Test
  def backActionWithInfoTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    setTwoView()
    teoricRequest.clickBack()
    val quantity:TextField=find("#quantity")
    assert(quantity.getText.equals(teoricRequest.getQuantity))
  }

  @Test
  def allBackWithConfirmationTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.clickBack()
    teoricRequest.clickOk()
    val titleDay:Label=find("#title")
    assert(titleDay.getText.equals(TEORIC_REQUEST))
  }

  @Test
  def allBackWithoutConfirmationTest(): Unit = {
    managerOperations.openManageTeoricRequest()
    setFirstView()
    teoricRequest.clickBack()
    teoricRequest.clickCancel()
    val titleDay:Label=find("#titleDay")
    assert(titleDay.getText.equals(DAY))
  }

}
