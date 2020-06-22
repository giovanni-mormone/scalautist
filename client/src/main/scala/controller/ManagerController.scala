package controller

import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement}
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

  def replacementSelected(idRisultato: Int, idPersona: Int)
}

object ManagerController {
  private val instance = new ManagerControllerImpl()
//  private val model = ManagerModel()

  def apply(): ManagerController = instance

  private class ManagerControllerImpl extends ManagerController {

    override def dataToAbsencePanel(): Unit = {
      val a = List(InfoAbsenceOnDay("Cesena","Notte",1,2,3),InfoAbsenceOnDay("Cesena","Notte",1,2,3),InfoAbsenceOnDay("Cesena","Notte",1,2,3),InfoAbsenceOnDay("Cesena","Notte",1,2,3),InfoAbsenceOnDay("Cesena","Notte",1,2,3))

      myView.drawAbsence(a)
    }

    override def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit = {
      //model!
      val a = List(InfoReplacement(1,2,"Francesco","Cassano"),InfoReplacement(1,3,"Giorgo","Cassano"),InfoReplacement(1,3,"Francesco","Valenti"))
      myView.drawReplacement(a)
    }

    override def replacementSelected(idRisultato: Int, idPersona: Int): Unit = {
      //model
      val a = List(InfoAbsenceOnDay("Cesena","Notte",1,2,3),InfoAbsenceOnDay("Cesena","Notte",1,2,3),InfoAbsenceOnDay("Cesena","Notte",1,2,3),InfoAbsenceOnDay("Cesena","Notte",1,2,3))
      myView.drawAbsence(a)
    }
  }
}