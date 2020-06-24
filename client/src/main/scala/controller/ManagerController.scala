package controller

import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement, Response}
import messagecodes.StatusCodes
import model.entity.ManagerModel
import view.fxview.mainview.ManagerView

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

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
  private val model = ManagerModel()

  def apply(): ManagerController = instance

  private class ManagerControllerImpl extends ManagerController {

    override def dataToAbsencePanel(): Unit = {

      model.allAbsences().onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE, payload)) => payload.foreach(result => myView.drawAbsence(result))
        case Success(Response(StatusCodes.NOT_FOUND, _)) => print("ok")
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => print("bad requ")
        case Failure(_) => print("no")
      }
    }

    override def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit = {
      model.extraAvailability(idTerminale,idTurno,idRisultato).onComplete{
        case Success(Response(StatusCodes.ERROR_CODE1,_)) => print("res no in db")
        case Success(Response(StatusCodes.ERROR_CODE2,_)) => print("ter no in db")
        case Success(Response(StatusCodes.ERROR_CODE3,_)) => print("tur no in db")
        case Success(Response(StatusCodes.NOT_FOUND,_)) => print("no disp")
        case Success(Response(StatusCodes.SUCCES_CODE,payload)) => payload.foreach(result => myView.drawReplacement(result))
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => print("bad requ")
        case Failure(e) => print(e)
      }
    }

    override def replacementSelected(idRisultato: Int, idPersona: Int): Unit = {
      model.replaceShift(idRisultato,idPersona).onComplete{
        case Success(Response(StatusCodes.ERROR_CODE1,_)) => print("res no in db")
        case Success(Response(StatusCodes.ERROR_CODE2,_)) => print("pers no in db")
        case Success(Response(StatusCodes.SUCCES_CODE,payload)) =>
          print("SOSTITUITO")
          dataToAbsencePanel()
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => print("bad requ")
        case Failure(e) => print(e)
      }
    }
  }
}