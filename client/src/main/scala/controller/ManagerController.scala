package controller

import caseclass.CaseClassHttpMessage.InfoAbsence
import view.fxview.mainview.ManagerView

import scala.concurrent.ExecutionContextExecutor

trait ManagerController extends AbstractController[ManagerView]{

  /**
   * Method that asks the model to retrieve the data about the absent people
   */
  def dataToAbsencePanel(): Unit

  /**
   *
   * @param idRisultato
   * @param idTerminale
   * @param idTurno
   */
  def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit
}

object ManagerController {
  private val instance = new ManagerControllerImpl()
//  private val model = ManagerModel()

  def apply(): ManagerController = instance

  private class ManagerControllerImpl extends ManagerController {

    override def dataToAbsencePanel(): Unit = {
      val a = List(InfoAbsence("Cesena","Notte",1,2,3),InfoAbsence("Cesena","Notte",1,2,3),InfoAbsence("Cesena","Notte",1,2,3),InfoAbsence("Cesena","Notte",1,2,3),InfoAbsence("Cesena","Notte",1,2,3))

      myView.drawAbsence(a)
    }

    override def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit = {
      //model!
    }
  }
}