package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoAbsenceOnDay
import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, Label}
import javafx.scene.layout.HBox
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.{FillHolesParent, GroupParamsParent}

trait TwoFieldCheckBox extends Component[GroupParamsParent] {
  def selectedItem(): Boolean

  def getRule: String

  def getDateList: List[Date]
}

object TwoFieldCheckBox{

  def apply(rule:String,date:List[String]): TwoFieldCheckBox = new TwoFieldCheckBoxFX(rule,date)

  private class TwoFieldCheckBoxFX(rule:String,date:List[String]) extends AbstractComponent[GroupParamsParent]("manager/subcomponent/TwoFieldOneCheckBox")
    with TwoFieldCheckBox{

    @FXML
    var field1: Label = _
    @FXML
    var field2: Label = _
    @FXML
    var field3: CheckBox = _
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      field1.setText(rule)
      field2.setText(setString())
    }

    def setString(): String ={
      val DEFAULT_VALUE:String =String.valueOf("")
      val VALUE:String = String.valueOf(" ")
      date.foldLeft(DEFAULT_VALUE)((default,actual)=>default+actual+VALUE)
    }

    override def selectedItem():Boolean = field3.isSelected
    override def getRule:String =rule
    override def getDateList:List[Date]=date.map(x=>Date.valueOf(x))
  }
}