package view.fxview.component.HumanResources

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Assenza
import javafx.stage.Stage
import view.DialogView
import view.fxview.AbstractFXModalView
import view.fxview.component.HumanResources.subcomponent.ModalAbsence
import view.fxview.component.HumanResources.subcomponent.parent.{ModalAbsenceParent, ModalTrait}

trait MainModalResource extends DialogView{
    def saveAbsence(assenza: Assenza):Unit
}
object MainModalResource{

  def apply(id: Int,name:String,surname:String,stage:Stage,parent:ModalTrait): MainModalResource = new Modal(id,name,surname,stage,parent)

  private class Modal(id: Int,name:String,surname:String,stage:Stage,parent:ModalTrait) extends AbstractFXModalView(stage) with MainModalResource
  with ModalAbsenceParent{
    /**
     * Closes the view.
     */
    private var sonResource: ModalAbsence = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      sonResource = ModalAbsence(id,name,surname)
      sonResource.setParent(this)
      pane.getChildren.add(sonResource.pane)
    }
    override def close(): Unit =myStage.close()

    override def showMessage(message: String): Unit = {
      super.showMessage(message)
      this.close()
    }
    override def saveAbsence(assenza: Assenza): Unit = parent.saveAbscense(assenza)
  }
}
