package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Turno
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextField}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

/** @author Fabian Aspee Encina
 *  Trait which allows to perform operations on the shift and quantity for teoric request.
 */
trait LabelTextFieldBox extends Component[RichiestaForDayBox]{
  /**
   * method that return quantity and shift
   *
   * @return return terminal and shift that are related
   */
  def getQuantity:(Int,Int)

  /**
   * method that verify if all textfield are completed
   * @return true if textfield is complete false another case
   */
  def verifyData:Boolean

  /**
   * method that allow set text error when textfield contains bad data
   */
  def setTextError():Unit

  /**
   * method that allow clear text error
   */
  def clearText():Unit

  /**
   * method which allow set quantity when re draw component, this methosd is call only if shift in current day
   * contains info
   * @param quantity represent quantity for a shift
   */
  def setInfo(quantity:Int):Unit
}
object LabelTextFieldBox{

  def apply(shift: Turno): LabelTextFieldBox = new LabelTextFieldBoxFX(shift)

  private class LabelTextFieldBoxFX(shift: Turno) extends AbstractComponent[RichiestaForDayBox]("manager/subcomponent/LabelTextFieldBox")
    with LabelTextFieldBox{

    @FXML
    var shiftLabel: Label = _
    @FXML
    var quantity: TextField = _
    @FXML
    var errorLabel: Label = _

    val DEFAULT_VALUE: Int = -1
    val DEFAULT_VALUE_LABEL = ""

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      shiftLabel.setText(shift.nomeTurno)
      quantity.textProperty().addListener(verifyInput())
    }

    private def verifyInput()=new ChangeListener[String]() {
      override def changed(observable:ObservableValue[_<:String] , oldValue:String,
                           newValue:String):Unit= {
        if (!newValue.matches("\\d*")) {
          quantity.setText(newValue.replaceAll("[^\\d]", ""));
          errorLabel.setText(resources.getResource("only-numeric-input"))
        }else errorLabel.setText(DEFAULT_VALUE_LABEL)
      }
    }

    override def setInfo(quantity:Int):Unit=quantity match {
      case DEFAULT_VALUE => this.quantity.clear()
      case _=> this.quantity.setText(quantity.toString)
    }

    override def getQuantity: (Int, Int) = (shift.id.head,if(quantity.getText.isEmpty) DEFAULT_VALUE else quantity.getText.toInt)

    override def verifyData:Boolean =quantity.getText().isEmpty

    override def setTextError():Unit =errorLabel.setText(resources.getResource("error-message"))

    override def clearText():Unit =errorLabel.setText("")
  }
}