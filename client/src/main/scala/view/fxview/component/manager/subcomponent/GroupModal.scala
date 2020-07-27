package view.fxview.component.manager.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Regola
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, DatePicker, Label, TextArea}
import javafx.scene.layout.VBox
import view.fxview.component.AbstractComponent
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker
import view.fxview.component.manager.subcomponent.GroupParamsBox.Group
import view.fxview.component.manager.subcomponent.parent.ModalGruopParent
import view.fxview.util.ResourceBundleUtil._

trait GroupModal extends AbstractComponent[ModalGruopParent] {

}

object GroupModal {

  def apply(dataI: LocalDate, dataF: LocalDate, dateNo: List[LocalDate], rules: List[Regola]): GroupModal =
    new GroupModalFX(dataI, dataF, dateNo, rules)

  private class GroupModalFX(dataI: LocalDate, dataF: LocalDate, dateNo: List[LocalDate], ruleL: List[Regola])
  extends AbstractComponent[ModalGruopParent]("manager/subcomponent/GroupModal")
  with GroupModal{

    @FXML
    var days: DatePicker = _
    @FXML
    var chosen: VBox = _
    @FXML
    var dateSelected: Label = _
    @FXML
    var add: Button = _
    @FXML
    var done: Button = _
    @FXML
    var rules: ComboBox[String] = _

    private val MIN_DATE: Int = 2
    private var chosenDate: List[LocalDate] = dateNo

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      chosenDate = dateNo
      initDatePicker()
      initTextArea()
      initButton()
      initCombo()
    }

    private def initButton(): Unit = {
      add.setText(resources.getResource("addtxt"))
      done.setText(resources.getResource("save"))

      done.setDisable(true)

      add.setOnAction(_ => {
        val date = Option(days.getValue)
        if(date.isDefined) {
          writeOnTextArea(date.get.toString)
          chosenDate = chosenDate :+ date.get
          days.getEditor.clear()
          days.setValue(null)
          initDatePicker()
          controlGroup()
        }
      })

      done.setOnAction(_ => parent.updateGroups(Group(chosenDate.filter(date => !dateNo.contains(date)),
        ruleL.find(_.nomeRegola.equals(getRule)).map(_.idRegola.head).getOrElse(-1), getRule)))
    }

    private def initCombo(): Unit = {
      ruleL.foreach(rule => rules.getItems.add(rule.nomeRegola))
      rules.setOnAction(_ => controlGroup())
    }

    private def initDatePicker(): Unit = {
      CreateDatePicker.createDatePicker(days, dataI, dataF, chosenDate)
      days.setEditable(false)
    }

    private def initTextArea(): Unit = {
      dateSelected.setText(resources.getResource("datetit"))
    }

    private def writeOnTextArea(str: String): Unit = {
      val label = LabelDateBox(str)
      chosen.getChildren.add(label.setParent(parent).pane)
    }

    private def getRule: String =
      rules.selectionModelProperty().getValue.getSelectedItem

    private def controlGroup(): Unit =
      done.setDisable(chosenDate.length < MIN_DATE || rules.selectionModelProperty().getValue.isEmpty)
  }

}
