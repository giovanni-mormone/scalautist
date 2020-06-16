package view.humanresourceoperation

import java.time.LocalDate

import javafx.scene.control._
import view.baseconfiguration.BaseTest

trait RecruitOperation {
  def openRecruit(): Unit
  def enterName(name: String): Unit
  def enterSurname(surname: String): Unit
  def enterTel(tel: String): Unit
  def enterData(day: LocalDate): Unit
  def chooseRole(role: String): Unit
  def chooseContract(contractType: String): Unit
  def chooseShift(shift: String): Unit
  def chooseExtra1(day: String): Unit
  def chooseExtra2(day: String): Unit
  def chooseZona(zone: String): Unit
  def chooseTerminal(terminal: String): Unit
  def pressRecruit(): Unit
  def getNameField: TextField
  def getSurnameField: TextField
  def getTelField: TextField
  def getRole: ComboBox[String]
  def getContract: ComboBox[String]
  def getShift1: ComboBox[String]
  def getShift2: ComboBox[String]
  def getExtraday1: ComboBox[String]
  def getExtraday2: ComboBox[String]
  def getZone: ComboBox[String]
  def getTerminal: ComboBox[String]
  def getButton: Button
  def getMessage: Label
}

object RecruitOperation {

  def apply(toTest: BaseTest): RecruitOperation = new RecruitOperationImpl(toTest)

  private class RecruitOperationImpl(test: BaseTest) extends RecruitOperation {

    val nameId: String = "#name"
    val surnameId: String = "#surname"
    val telId: String = "#tel"
    val dateId: String = "#recruitDate"
    val roleId: String = "#role"
    val contractId: String = "#contractTypes"
    val shift1Id: String = "#shift1"
    val shift2Id: String = "#shift2"
    val extra1Id: String = "#day1"
    val extra2Id: String = "#day2"
    val zoneId: String = "#zones"
    val terminalId: String = "#terminals"
    val button: String = "#save"

    override def enterName(name: String): Unit = {
      test.clickOn(nameId).write(name)
      test.ensureEventQueueComplete()
    }

    override def enterSurname(surname: String): Unit = {
      test.clickOn(surnameId).write(surname)
      test.ensureEventQueueComplete()
    }

    override def enterTel(tel: String): Unit = {
      test.clickOn(telId).write(tel)
      test.ensureEventQueueComplete()
    }

    override def enterData(day: LocalDate): Unit =
      test.find(dateId).asInstanceOf[DatePicker].setValue(day)

    override def chooseRole(role: String): Unit = {
      test.clickOn(roleId)
      test.ensureEventQueueComplete()
      test.clickOn(role)
      test.ensureEventQueueComplete()
    }

    override def chooseContract(contractType: String): Unit = {
      test.clickOn(contractId)
      test.ensureEventQueueComplete()
      test.clickOn(contractType)
      test.ensureEventQueueComplete()
    }

    override def chooseShift(shift: String): Unit = {
      test.clickOn(shift1Id)
      test.ensureEventQueueComplete()
      test.clickOn(shift)
      test.ensureEventQueueComplete()
    }

    override def chooseExtra1(day: String): Unit = {
      test.clickOn(extra1Id)
      test.ensureEventQueueComplete()
      test.clickOn(day)
      test.ensureEventQueueComplete()
    }

    override def chooseExtra2(day: String): Unit = {
      test.clickOn(extra2Id)
      test.ensureEventQueueComplete()
      test.clickOn(day)
      test.ensureEventQueueComplete()
    }

    override def chooseZona(zone: String): Unit = {
      test.clickOn(zoneId)
      test.ensureEventQueueComplete()
      test.clickOn(zone)
      test.ensureEventQueueComplete()
    }

    override def chooseTerminal(terminal: String): Unit = {
      test.clickOn(terminalId)
      test.ensureEventQueueComplete()
      test.clickOn(terminal)
      test.ensureEventQueueComplete()
    }

    override def pressRecruit(): Unit = {
      test.clickOn(button)
      test.ensureEventQueueComplete()
    }

    override def getNameField: TextField =
      test.find(nameId)

    override def getSurnameField: TextField =
      test.find(surnameId)

    override def getTelField: TextField =
      test.find(telId)

    override def getRole: ComboBox[String] =
      test.find(roleId)

    override def getContract: ComboBox[String] =
      test.find(contractId)

    override def getShift1: ComboBox[String] =
      test.find(shift1Id)

    override def getShift2: ComboBox[String] =
      test.find(shift2Id)

    override def getExtraday1: ComboBox[String] =
      test.find(extra1Id)

    override def getExtraday2: ComboBox[String] =
      test.find(extra2Id)

    override def getZone: ComboBox[String] =
      test.find(zoneId)

    override def getTerminal: ComboBox[String] =
      test.find(terminalId)

    override def getButton: Button =
      test.find(button)

    override def openRecruit(): Unit = {
      test.clickOn("#recruitButton")
    }

    override def getMessage: Label = {
      test.find("#messageLabel")
    }
  }
}
