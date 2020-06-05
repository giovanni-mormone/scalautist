package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Assenza, Persona}
import javafx.fxml.FXML
import javafx.scene.control.Button
import view.fxview.component.{AbstractComponent, Component}

//metodi view -> controller
trait IllBoxParent{
  def getInfo():Unit
  def openModal(id:Int,name:String,surname:String):Unit
}
//metodi controller -> view
trait IllBox extends Component[IllBoxParent]{

}

object IllBox{

  private val instance = new IllBoxFX()
  //button che chiama openModal setOnAction
  def apply(persona:List[Persona]): IllBox = instance

  private class IllBoxFX extends AbstractComponent[IllBoxParent]("humanresources/subcomponent/IllBox") with IllBox {
    @FXML
    var idButton:Button = _
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      idButton.setOnAction(_=>parent.openModal(2,"nome","cognome"))
    }
  }
}
