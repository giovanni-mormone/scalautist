package view.fxview.component.manager.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.{Button, DatePicker, TextArea}
import view.fxview.component.AbstractComponent
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker
import view.fxview.component.manager.subcomponent.parent.ModalGruopParent
import view.fxview.util.ResourceBundleUtil._

trait GroupModal extends AbstractComponent[ModalGruopParent] {

}

object GroupModal {

  def apply(dataI: LocalDate, dataF: LocalDate): GroupModal = new GroupModalFX(dataI, dataF)

  private class GroupModalFX(dataI: LocalDate, dataF: LocalDate)
  extends AbstractComponent[ModalGruopParent]("manager/subcomponent/GroupModal")
  with GroupModal{

    @FXML
    var days: DatePicker = _
    @FXML
    var chosen: TextArea = _
    @FXML
    var add: Button = _
    @FXML
    var done: Button = _

    private var chosenDate: List[LocalDate] = List.empty

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initDatePicker()
      initTextArea()
      initButton()
    }

    private def initButton(): Unit = {
      add.setText(resources.getResource("addtxt"))
      add.setOnAction(_ => {
        val date = Option(days.getValue)
        if(date.isDefined) {
          writeOnTextArea(date.get.toString)
          chosenDate = chosenDate :+ date.get
          days.getEditor.clear()
        }
      })
    }

    private def initDatePicker(): Unit =
      days = CreateDatePicker.createDatePicker(dataI, dataF, chosenDate)

    private def initTextArea(): Unit = {
      writeOnTextArea(resources.getResource("datetit"))
      chosen.setEditable(false)
    }

    private def writeOnTextArea(str: String): Unit =
      chosen.setText(chosen.getText + str + "\n")
  }
}
