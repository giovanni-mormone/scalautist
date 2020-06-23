package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.VBox
import view.fxview.component.driver.subcomponent.parent.ModalDisponibilitaParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait ModalDisponibilita extends Component[ModalDisponibilitaParent]{
  def daySelected(day: String):Unit
}

object ModalDisponibilita {

  def apply(disponibilita: List[String]): ModalDisponibilita = {
    val modal = new ModalDisponibilitaFX()
    modal.drawList(disponibilita)
    modal
  }

  private class ModalDisponibilitaFX()
    extends AbstractComponent[ModalDisponibilitaParent]("driver/subcomponent/ModalDisponibilita")
      with ModalDisponibilita{

    @FXML
    var baseBox: VBox = _
    @FXML
    var confirm: Button = _
    @FXML
    var title: Label = _

    private var selected: List[String] = List.empty
    private var selectors: List[CheckBoxSelector] = List.empty
    private val DAYS_TO_WORK: Int = 2
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      confirm.setText(resources.getResource("confirm-button"))
      confirm.setDisable(true)
      confirm.setOnAction(_ => sendToParent())
      title.setText(resources.getResource("title-label"))
    }

    def drawList(disponibilita: List[String]): Unit = {
      disponibilita.foreach(day => {
        val selector = CheckBoxSelector(day)
        selector.setParent(this)
        selectors = selector::selectors
        pane.getChildren.add(1,selector.pane)
      })
    }

    override def daySelected(day: String): Unit = {
      if (selected.contains(day)) {
        selected = selected filter (_ != day)
        selectors.foreach(_.enable())
        confirm.setDisable(true)
      } else {
        selected = day :: selected
        checkSelection()
      }
    }

    private def checkSelection(): Unit = {
      if (selected.size == DAYS_TO_WORK) {
        selectors.foreach(_.disable())
        confirm.setDisable(false)
      }
    }

    private def sendToParent(): Unit = {
      if (selected.size == DAYS_TO_WORK)
        parent.selectedDays(selected.head,selected.tail.head)
    }
  }
}
