package controller

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{GiornoInSettimana, Parametro, Regola, Terminale, Zona, ZonaTerminale}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, CheckResultRequest, GruppoA, InfoAlgorithm, Response, SettimanaN, SettimanaS}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, CheckResultRequest, GruppoA, Response, SettimanaN, SettimanaS}
import com.typesafe.sslconfig.ssl.FakeChainedKeyStore.User
import messagecodes.StatusCodes
import model.entity.{HumanResourceModel, ManagerModel}
import utils.TransferObject.InfoRichiesta
import view.fxview.component.manager.subcomponent.ParamsModal.DataForParamasModel
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm
import view.fxview.mainview.ManagerView

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait ManagerController extends AbstractController[ManagerView]{

  /**
   *
   * @param param
   */
  def saveParam(param: InfoAlgorithm): Unit


  /**
   *
   * @param idp
   */
  def getInfoParamToShow(idp: Int, data: DataForParamasModel): Unit

  /**
   *
   * @param info
   * @param name
   */
  def showParamAlgorithm(info: AlgorithmExecute, name: Option[String]): Unit

  def consumeNotification(tag: Long): Unit

  def resultForTerminal(value: Option[Int], date: Date, date1: Date): Unit

  def dataToResultPanel(): Unit

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
  def modalOldParams(terminals: List[Terminale]): Unit

  /**
   *
   */
  def weekParam(params: ParamsForAlgoritm): Unit

  /**
   *
   */
  def groupParam(params: ParamsForAlgoritm): Unit

  def startListenNotification():Unit
}

object ManagerController {
  private val instance = new ManagerControllerImpl()
  private val model = ManagerModel()

  def apply(): ManagerController = instance

  private class ManagerControllerImpl extends ManagerController {

    private def notificationReceived(message:String,tag:Long):Unit={
        myView.drawNotification(message,tag)
    }

    override def startListenNotification(): Unit ={
       model.verifyExistedQueue(Utils.userId,notificationReceived)
    }
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

    override def runAlgorithm(algorithmExecute: AlgorithmExecute): Future[Response[Int]] =
      model.runAlgorithm(algorithmExecute,statusAlgorithm)

    override def verifyOldResult(dataToCheck: CheckResultRequest): Future[Response[List[Option[Int]]]] =
      model.verifyOldResult(dataToCheck)

    override def chooseParams(): Unit = {
      HumanResourceModel().getAllTerminale.onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.drawRunAlgorithm(value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }
    }

    override def modalOldParams(terminals: List[Terminale]): Unit = {
    case class LoadParams(params: Response[List[Parametro]], weekRule: Response[List[Regola]])

      val future: Future[LoadParams] = for{
        params <- model.getOldParameter
        weekRule <- model.weekRule()
      } yield LoadParams(params, weekRule)
      future.onComplete{
        case Success(data) if data.weekRule.statusCode == StatusCodes.SUCCES_CODE =>
          myView.modalOldParamDraw(data.params.payload.getOrElse(List.empty), terminals,
            data.weekRule.payload.getOrElse(List.empty))
        case Success(_) => myView.showMessageFromKey("result-not-found")
        case _ => myView.showMessageFromKey("general-error")
      }

    }

    override def weekParam(params: ParamsForAlgoritm): Unit = {
      model.weekRule().onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.drawWeekParam(params, value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }
    }

    override def groupParam(params: ParamsForAlgoritm): Unit = {
      model.groupRule().onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.drawGroupParam(params, value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }
    }

    override def dataToResultPanel(): Unit =
      HumanResourceModel().getAllTerminale.onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.drawResultTerminal(value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }

    override def resultForTerminal(idTerminal: Option[Int], date: Date, date1: Date): Unit = {
      model.getResultAlgorithm(idTerminal.head,date,date1).onComplete {
        case Failure(exception) => println(exception)
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) =>myView.drawResult(value._1,value._2)
        case Success(Response(StatusCodes.NOT_FOUND, None)) => myView.showMessageFromKey("result-not-found")
      }
    }

    override def consumeNotification(tag: Long): Unit = model.consumeNotification(tag,Utils.userId)

    override def showParamAlgorithm(info: AlgorithmExecute, name: Option[String]): Unit = {

      case class DataToShow(terminals: Response[List[Terminale]], weekRule: Response[List[Regola]],
                            groupRule: Response[List[Regola]])

      val future: Future[DataToShow] = for{
        terminals <- HumanResourceModel().getAllTerminale
        weekRule <- model.weekRule()
        gruopRule <- model.groupRule()
      } yield DataToShow(terminals, weekRule, gruopRule)
      future.onComplete{
        case Success(data) if data.terminals.statusCode == StatusCodes.SUCCES_CODE &&
          data.groupRule.statusCode == StatusCodes.SUCCES_CODE && data.weekRule.statusCode == StatusCodes.SUCCES_CODE =>
          myView.drawShowParams(info, name, data.terminals.payload.getOrElse(List.empty),
            data.groupRule.payload.getOrElse(List.empty) ::: data.weekRule.payload.getOrElse(List.empty))
        case Success(_) => myView.showMessageFromKey("result-not-found")
        case _ => myView.showMessageFromKey("general-error")
      }

    }

    override def getInfoParamToShow(idp: Int, data: DataForParamasModel): Unit =
      model.getParameterById(idp).onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.showInfoParam(data.copy(info = Some(value)))
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }

    override def saveParam(param: InfoAlgorithm): Unit =
      model.saveParameters(param).onComplete {
        case Success(value) => myView.showMessageFromKey("GeneralError-Success")
        case _ => myView.showMessageFromKey("general-error")
      }
  }
}
/*
object t extends App{
  import scala.concurrent.ExecutionContext.Implicits.global
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,7,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,7,31))
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
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,None,None,None,threeSaturday)
  ManagerController().runAlgorithm(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>println(value)
  }
  while (true){}
}


object t2 extends App{
  import scala.concurrent.ExecutionContext.Implicits.global
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,1,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,1,31))
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
  ManagerController().runAlgorithm(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>
      println("FINE???" + value)
  }
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
*/
