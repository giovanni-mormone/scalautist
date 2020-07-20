package controller

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{GiornoInSettimana, Parametro, Terminale, Zona, ZonaTerminale}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, CheckResultRequest, GruppoA, InfoAlgorithm, Response, SettimanaN, SettimanaS}
import messagecodes.StatusCodes
import model.entity.{HumanResourceModel, ManagerModel}
import utils.TransferObject.InfoRichiesta
import view.fxview.mainview.ManagerView

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait ManagerController extends AbstractController[ManagerView]{

  def runAlgorithm(algorithmExecute: AlgorithmExecute):Future[Response[Int]]


  /**
   * method that send to server a theorical request with all info for a time frame
   *
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

  def verifyOldResult(dataToCheck:CheckResultRequest): Future[Response[List[Option[Int]]]]

  /**
   * Method that asks model to find data about the terminals before draw the panel
   */
  def chooseParams(): Unit

  /**
   * Method asks the old params list to draw modal
   */
  def modalOldParams(): Unit
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
        case _ => myView.showMessageFromKey("general-error")
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
        case _ => myView.showMessageFromKey("general-error")
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
        case _ => myView.showMessageFromKey("general-error")
      }
    }

    override def datatoRichiestaPanel(): Unit =
      HumanResourceModel().getAllTerminale.onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.drawRichiesta(value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }


    override def selectShift(idTerminal: Int): Unit =
      HumanResourceModel().getAllShift.onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) =>myView.drawShiftRequest(value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-shift")
        case _ => myView.showMessageFromKey("general-error")
      }

    override def sendRichiesta(richiesta: InfoRichiesta): Unit = {
      model.defineTheoreticalRequest(richiesta).onComplete {
        case Success(Response(StatusCodes.BAD_REQUEST,_)) => myView.showMessageFromKey("bad-request-error")
        case Success(Response(StatusCodes.NOT_FOUND,_)) => myView.showMessageFromKey("bad-request-error")
        case Success(Response(StatusCodes.SUCCES_CODE,_)) => myView.showMessageFromKey("ok-save-request")
        case _ => myView.showMessageFromKey("general-error")
      }
    }

    def statusAlgorithm(message:String):Unit=
      println(s"$message")

    //TODO MODIFICARE I TIPI DI RITORNO
    override def runAlgorithm(algorithmExecute: AlgorithmExecute): Future[Response[Int]] =
      model.runAlgorithm(algorithmExecute,statusAlgorithm)

    override def verifyOldResult(dataToCheck: CheckResultRequest): Future[Response[List[Option[Int]]]] =
      model.verifyOldResult(dataToCheck)

    override def chooseParams(): Unit = {
      val terminals = List(Terminale("massimino", 2, Some(3)), Terminale("mingo", 2, Some(2)),
        Terminale("sing", 2, Some(4)), Terminale("osso", 2, Some(5)),
        Terminale("berta", 3, Some(7)), Terminale("fosso", 3, Some(8)) )
      myView.drawRunAlgorithm(terminals)
    }

    override def modalOldParams(): Unit = {
      val params = List(InfoAlgorithm(
        Parametro(true, "mai", Some(1)),
        List(ZonaTerminale(2, 3, Some(1), Some(1))),
        Some(List(GiornoInSettimana(1, 1, 2, Some(1), Some(30)), GiornoInSettimana(2, 2, 2, Some(1), Some(30)),
          GiornoInSettimana(3, 3, 2, Some(1), Some(30)), GiornoInSettimana(4, 4, 2, Some(1), Some(30)),
          GiornoInSettimana(5, 5, 2, Some(1), Some(30)), GiornoInSettimana(3, 5, 2, Some(1), Some(20))
        ))))
      myView.modalOldParamDraw(params)
    }

  }
}

object t extends App{
  import scala.concurrent.ExecutionContext.Implicits.global
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,1,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,12,31))
  val terminals=List(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25)
  val firstDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val secondDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val thirdDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,24))
  val secondDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,15))
  val firstDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,10))
  val secondDateGroup3: Date =Date.valueOf(LocalDate.of(2020,9,16))
  val firstDateGroup3: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val thirdDateGroup2: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val gruppi = List(GruppoA(1,List(firstDateGroup,secondDateGroup,thirdDateGroup),1),GruppoA(2,List(firstDateGroup2,secondDateGroup2,secondDateGroup3),2))
  val normalWeek = List(SettimanaN(1,2,15,3),SettimanaN(2,2,15,2),SettimanaN(3,2,15,2),SettimanaN(4,2,15,2),SettimanaN(5,2,15,2),SettimanaN(6,2,15,2))
  val specialWeek = List(SettimanaS(3,2,15,3,Date.valueOf(LocalDate.of(2020,7,8))),SettimanaS(3,3,15,3,Date.valueOf(LocalDate.of(2020,7,8))))
  val threeSaturday=true
  val algorithmExecute: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  ManagerController().runAlgorithm(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>println(value)
  }
  while (true){}
}


object t2 extends App{
  import scala.concurrent.ExecutionContext.Implicits.global
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,5,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,6,30))
  val terminals=List(15)
  val firstDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val secondDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val thirdDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,24))
  val secondDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,15))
  val firstDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,10))
  val secondDateGroup3: Date =Date.valueOf(LocalDate.of(2020,9,16))
  val firstDateGroup3: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val thirdDateGroup2: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val gruppi = List(GruppoA(1,List(firstDateGroup,secondDateGroup,thirdDateGroup),1),GruppoA(2,List(firstDateGroup2,secondDateGroup2,secondDateGroup3),2))
  val normalWeek = List(SettimanaN(1,2,15,3),SettimanaN(2,2,15,2))
  val specialWeek = List(SettimanaS(3,2,15,3,Date.valueOf(LocalDate.of(2020,7,8))),SettimanaS(3,3,15,3,Date.valueOf(LocalDate.of(2020,7,8))))
  val threeSaturday=false
  val algorithmExecute: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,None,None,None,false)

  val checkData: CheckResultRequest =
    CheckResultRequest(terminals, timeFrameInit,timeFrameFinish)


  ManagerController().verifyOldResult(checkData).onComplete{
    case Failure(exception) => println(exception)
    case Success(value) =>
      println("FINE???" + value)
  }
  /*ManagerController().runAlgorithm(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>
      println("FINE???" + value)
  }*/
  /*ManagerController().runAlgorithm(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>
      println("FINE2???" + value)
  }
  ManagerController().runAlgorithm(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>
      println("FINE3???" + value)
  }*/
  while (true){}
}

