package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Assenza
import view.fxview.component.{AbstractComponent, Component}

//metodi view -> controller
trait IllBoxParent {

  def saveAbsence(absence: Assenza)
}
//metodi controller -> view
trait IllBox extends Component[IllBoxParent]{

}

object IllBox{

  private val instance = new IllBoxFX()

  def apply(): IllBox = instance

  private class IllBoxFX extends AbstractComponent[IllBoxParent]("humanresources/subcomponent/qualcosa :)") with IllBox {
    override def initialize(location: URL, resources: ResourceBundle): Unit = ???
  }
}
