package controller

import caseclass.CaseClassHttpMessage.Response
import messagecodes.StatusCodes
import model.entity.{HumanResourceModel, ManagerModel}
import utils.TransferObject.InfoRichiesta
import view.fxview.mainview.ManagerView

import scala.util.{Failure, Success}

trait ManagerController extends AbstractController[ManagerView]{

  /**
   *
   * @param richiesta
   */
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  /**
   *
   * @param idTerminal
   */
  def selectShift(idTerminal: Int): Unit

  /**
   *
   */
  def datatoRichiestaPanel(): Unit


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
        case Success(Response(StatusCodes.NOT_FOUND, _)) => myView.showMessageFromKey("no-absences-day")
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => myView.showMessageFromKey("bad-request-error")
        case Failure(_) => myView.showMessageFromKey("general-error")
      }
    }

    override def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit = {
      model.extraAvailability(idTerminale,idTurno,idRisultato).onComplete{
        case Success(Response(StatusCodes.ERROR_CODE1,_)) => myView.showMessageFromKey("result-error")
        case Success(Response(StatusCodes.ERROR_CODE2,_)) => myView.showMessageFromKey("terminal-error")
        case Success(Response(StatusCodes.ERROR_CODE3,_)) => myView.showMessageFromKey("turn-error")
        case Success(Response(StatusCodes.NOT_FOUND,_)) => myView.showMessageFromKey("no-replacement-error")
        case Success(Response(StatusCodes.SUCCES_CODE,payload)) => payload.foreach(result => myView.drawReplacement(result))
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => myView.showMessageFromKey("bad-request-error")
        case Failure(_) => myView.showMessageFromKey("general-error")
      }
    }

    override def replacementSelected(idRisultato: Int, idPersona: Int): Unit = {
      model.replaceShift(idRisultato,idPersona).onComplete{
        case Success(Response(StatusCodes.ERROR_CODE1,_)) => myView.showMessageFromKey("result-error")
        case Success(Response(StatusCodes.ERROR_CODE2,_)) => myView.showMessageFromKey("driver-error")
        case Success(Response(StatusCodes.SUCCES_CODE,_)) =>
          myView.showMessageFromKey("replaced-driver")
          dataToAbsencePanel()
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => myView.showMessageFromKey("bad-request-error")
        case Failure(_) => myView.showMessageFromKey("general-error")
      }
    }

    override def datatoRichiestaPanel(): Unit =
      HumanResourceModel().getAllTerminale.onComplete {
        case Failure(exception) =>
        case Success(value) =>value.payload.foreach(value => myView.drawRichiesta(value))
      }


    override def selectShift(idTerminal: Int): Unit =
      HumanResourceModel().getAllShift.onComplete {
        case Failure(exception) =>
        case Success(value) =>value.payload.foreach(value=>myView.drawShiftRequest(value))
      }

    override def sendRichiesta(richiesta: InfoRichiesta): Unit = {
      model.defineTheoreticalRequest(richiesta).onComplete {
        case Failure(e) =>  myView.showMessageFromKey("general-error")
          println(e)
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => myView.showMessageFromKey("bad-request-error")
        case Success(Response(StatusCodes.NOT_FOUND,_)) => myView.showMessageFromKey("bad-request-error")
        case Success(value) =>println(value)
      }
    }
  }
}