package controller

import caseclass.CaseClassHttpMessage.Response
import messagecodes.StatusCodes
import model.entity.{HumanResourceModel, ManagerModel}
import utils.TransferObject.InfoRichiesta
import view.fxview.mainview.ManagerView

import scala.util.{Failure, Success}

trait ManagerController extends AbstractController[ManagerView]{

  /**
   * method that send to server a theorical request with all info for a time frame
   * @param richiesta case class that represent all info for create a theorical request
   */
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  /**
   * method that select all shift that exist in database
   */
  def selectShift(idTerminal: Int): Unit

  /**
   * method that return all terminal for view theorical request.
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

  def replacementSelected(idRisultato: Int, idPersona: Int):Unit

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
        case Failure(_) => myView.showMessageFromKey("general-error")
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.drawRichiesta(value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
      }


    override def selectShift(idTerminal: Int): Unit =
      HumanResourceModel().getAllShift.onComplete {
        case Failure(_) => myView.showMessageFromKey("general-error")
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) =>myView.drawShiftRequest(value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-shift")
      }

    override def sendRichiesta(richiesta: InfoRichiesta): Unit = {
      model.defineTheoreticalRequest(richiesta).onComplete {
        case Failure(_) =>  myView.showMessageFromKey("general-error")
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => myView.showMessageFromKey("bad-request-error")
        case Success(Response(StatusCodes.NOT_FOUND,_)) => myView.showMessageFromKey("bad-request-error")
        case Success(Response(StatusCodes.SUCCES_CODE,_)) => myView.showMessageFromKey("ok-save-request")
      }
    }
  }
}