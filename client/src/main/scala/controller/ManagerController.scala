package controller

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{Parametro, Regola, Terminale, Zona}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, CheckResultRequest, GruppoA, InfoAlgorithm, Response, SettimanaN, SettimanaS}
import javafx.application.Platform
import messagecodes.StatusCodes
import model.entity.{HumanResourceModel, ManagerModel}
import utils.TransferObject.{DataForParamasModel, InfoRichiesta}
import view.fxview.component.manager.subcomponent.util.ParamsForAlgorithm
import view.mainview.ManagerView

import scala.concurrent.Future
import scala.util.Success

trait ManagerController extends AbstractController[ManagerView]{

  /**
   * Allows to save params on the DB
   *
   * @param param instance of [[InfoAlgorithm]] to save
   */
  def saveParam(param: InfoAlgorithm): Unit


  /**
   * Get the information about the selected param from the DB and show them
   *
   * @param idp id of param to show
   * @param data instance of [[DataForParamasModel]] that contains the information to draw modal
   */
  def infoParamToShow(idp: Int, data: DataForParamasModel): Unit

  /**
   * The method allows to show all selected params before run
   *
   * @param info instance of [[AlgorithmExecute]] that contains all chosen params
   * @param name optional string. It allows to save params if is defined
   */
  def showParamAlgorithm(info: AlgorithmExecute, name: Option[String]): Unit

  /**
   * method that allow delete notification when user make click
   * @param tag tag that allow delete notification
   */
  def consumeNotification(tag: Long): Unit

  /**
   * Method that return result of the algorithm for a terminal and time frame
   * @param value id of terminal for we want select result
   * @param date init date for select result
   * @param date1 end date for select result
   */
  def resultForTerminal(value: Option[Int], date: Date, date1: Date): Unit

  /**
   * method called when is requested to draw the first result panel.
   */
  def dataToResultPanel(): Unit

  /**
   * runAlgorithm checks if you can run algorithm without any problem before running it. If there are problems, it asks
   * the user whether to proceed
   * @param algorithmExecute information that allows the algorithm to work
   */
  def runAlgorithm(algorithmExecute: AlgorithmExecute): Unit

  /**
   * runAlgorithm checks if you can run algorithm without any problem before running it.
   * @param algorithmExecute information that allows the algorithm to work
   */
  def runAlgorithm2(algorithmExecute: AlgorithmExecute):Future[Response[Int]]
  /**
   * It makes algorithm run without check if it overwrites something
   * @param algorithmExecute information that allows the algorithm to work
   */
  def executeAlgorithm(algorithmExecute: AlgorithmExecute):Unit

  /**
   * save a new zone into db
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]] to save
   */
  def saveZona(zone: Zona): Unit

  /**
   * update selected zone
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]] to update
   */
  def updateZona(zone: Zona): Unit

  /**
   * delete selected zone
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]] to delete
   */
  def deleteZona(zone: Zona): Unit

  /**
   * insert terminal into db
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]] to save
   */
  def saveTerminal(terminal: Terminale): Unit

  /**
   * update selected terminal
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]] to update
   */
  def updateTerminal(terminal: Terminale): Unit

  /**
   * delete selected terminal
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]] to delete
   */
  def deleteTerminal(terminal: Terminale): Unit

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
   * method that call model and await result with all driver that can be replacement
   * @param idRisultato id for search driver in database table risutlatoSets
   * @param idTerminale represent a terminal where driver is absence
   * @param idTurno represent shift that we want replace
   */
  def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit

  /**
   * Method called when the user has selected a replacement for an absence.
   * @param idRisultato
   *                    The result that is being replaced
   * @param idPersona
   *                  The replacement selected for the result
   */
  def replacementSelected(idRisultato: Int, idPersona: Int):Unit

  /**
   * Method that asks model to find data about the terminals before draw the panel
   */
  def chooseParams(): Unit

  /**
   * Method asks the old params list to draw modal
   */
  def modalOldParams(terminals: List[Terminale]): Unit

  /**
   * Method asks to draw the the panel to define weekly variations
   * @param params instance of [[ParamsForAlgorithm]] that contains information to draw
   */
  def weekParam(params: ParamsForAlgorithm): Unit

  /**
   * Method asks to draw the the panel to define groups variations
   * @param params instance of [[ParamsForAlgorithm]] that contains information to draw
   */
  def groupParam(params: ParamsForAlgorithm): Unit

  /**
   * getZonaData method retrieves all data needed to draw zona view
   *
   */
  def dataToZone(): Unit

  /**
   * getTerminalData method retrieves all data needed to draw zona view
   *
   */
  def dataToTerminal(): Unit

  /**
   * draw the terminal modal to manage it
   *
   * @param terminalId
   *                   terminal id to manage
   */
  def terminalModalData(terminalId: Int): Unit

  /**
   * method that verify if exist queue for notification
   */
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
    override def runAlgorithm2(algorithmExecute: AlgorithmExecute):Future[Response[Int]]=
      model.runAlgorithm(algorithmExecute,statusAlgorithm)

    private def statusAlgorithm(message:String):Unit=
        myView.showInfoAlgorithm(message)

    override def executeAlgorithm(algorithmExecute: AlgorithmExecute): Unit =
      model.runAlgorithm(algorithmExecute,statusAlgorithm).onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE,_)) =>
          Platform.runLater(() => myView.showMessageFromKey(key= "start-algorithm"))
        case Success(Response(StatusCodes.ERROR_CODE10,_)) =>
          Platform.runLater(() => myView.showMessageFromKey("no-algorithm"))
        case Success(Response(StatusCodes.ERROR_CODE1,_)) => Platform.runLater(() => myView.showMessageFromKey("no-driver-ter"))
        case Success(Response(StatusCodes.ERROR_CODE2,_)) => Platform.runLater(() => myView.showMessageFromKey("error-date"))
        case Success(Response(StatusCodes.ERROR_CODE3,_)) => Platform.runLater(() => myView.showMessageFromKey("error-terminal"))
        case Success(Response(StatusCodes.ERROR_CODE4,_)) => Platform.runLater(() => myView.showMessageFromKey("error-group"))
        case Success(Response(StatusCodes.ERROR_CODE5,_)) => Platform.runLater(() => myView.showMessageFromKey("error-special-week"))
        case Success(Response(StatusCodes.ERROR_CODE6,_)) => Platform.runLater(() => myView.showMessageFromKey("no-request"))
        case Success(Response(StatusCodes.ERROR_CODE7,_)) => Platform.runLater(() => myView.showMessageFromKey("no-driver"))
        case Success(Response(StatusCodes.ERROR_CODE8,_)) => Platform.runLater(() => myView.showMessageFromKey("no-shift"))
        case Success(Response(StatusCodes.ERROR_CODE9,_)) => Platform.runLater(() => myView.showMessageFromKey("no-driver-contract"))
        case _ => Platform.runLater(() => myView.showMessageFromKey("general-error"))
      }

    override def runAlgorithm(algorithmExecute: AlgorithmExecute): Unit =
      model.verifyOldResult(CheckResultRequest(algorithmExecute.idTerminal, algorithmExecute.dateI, algorithmExecute.dateF))
        .onComplete {
          case Success(Response(StatusCodes.SUCCES_CODE, Some(list))) =>
            Option(list.collect{
              case Some(StatusCodes.INFO_CODE2) =>
                "info-run-other"
              case Some(StatusCodes.INFO_CODE4) =>
                "info-run-overwrite"
            }).filter(_.nonEmpty)
              .fold(executeAlgorithm(algorithmExecute))(list => myView.confirmRun(list, algorithmExecute))
          case _ => myView.showMessageFromKey("general-error")
        }

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

    override def weekParam(params: ParamsForAlgorithm): Unit = {
      model.weekRule().onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.drawWeekParam(params, value)
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }
    }

    override def groupParam(params: ParamsForAlgorithm): Unit = {
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

    override def resultForTerminal(idTerminal: Option[Int], date: Date, date1: Date): Unit =
      idTerminal.foreach(idTerminal=>
        model.getResultAlgorithm(idTerminal,date,date1).onComplete {
          case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) =>myView.drawResult(value._1,value._2)
          case Success(Response(StatusCodes.NOT_FOUND, None)) => myView.showMessageFromKey("result-not-found")
          case _ => myView.showMessageFromKey("general-error")

        }
      )


    override def terminalModalData(terminalId: Int): Unit = {
      case class terminalMData(zoneL: Response[List[Zona]], terminalS: Response[Terminale])
      val future: Future[terminalMData] = for{
        zones <- getZone
        terminal <- model.getTerminale(terminalId)
      } yield terminalMData(zones, terminal)
      future.onComplete {
        case Success(terminalMData(responseZ,responseT)) if responseZ.statusCode == StatusCodes.SUCCES_CODE
        && responseT.statusCode == StatusCodes.SUCCES_CODE=>
          responseZ.payload.zip(responseT.payload).foreach{
            case (zone,terminal) => myView.openTerminalModal(zone, terminal)
          }
        case Success(terminalMData(_,responseT)) if responseT.statusCode == StatusCodes.NOT_FOUND=> myView.showMessageFromKey("terminal-not-found")
        case _ => myView.showMessageFromKey("GeneralError-Unknown")
      }
    }

    override def dataToTerminal(): Unit = {
      case class TerminalData(zoneL: Response[List[Zona]], terminalL: Response[List[Terminale]])
      val future: Future[TerminalData] = for {
        terminals <- model.getAllTerminale
        zones <- getZone
      } yield TerminalData(zones, terminals)
      future.onComplete {
        case Success(TerminalData(zoneL,terminalL)) if terminalL.statusCode == StatusCodes.SUCCES_CODE &&
          zoneL.statusCode == StatusCodes.SUCCES_CODE =>
          zoneL.payload.zip(terminalL.payload).foreach{
            case (zone,terminal) => myView.drawTerminaleView(zone, terminal)
          }
        case Success(TerminalData(responseZ,responseT)) if responseT.statusCode == StatusCodes.NOT_FOUND ||
          responseZ.statusCode == StatusCodes.NOT_FOUND => myView.showMessageFromKey("not-found-error")
        case _ => myView.showMessageFromKey("GeneralError-Unknown")
      }
    }

    override def dataToZone(): Unit =
      getZone.onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE,Some(zones))) => myView.drawZonaView(zones)
        case Success(Response(StatusCodes.NOT_FOUND,_)) => myView.showMessageFromKey("not-found-error")
        case _ => myView.showMessageFromKey("general-error")
      }

    override def saveZona(zone: Zona): Unit =
      model.setZona(zone).onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE, _)) => myView.refreshZonaPanel("ok-zone-request")
        case Success(Response(_,None)) => myView.showMessageFromKey("bad-request")
        case _ => myView.showMessageFromKey("general-error")
      }

    override def updateZona(zone: Zona): Unit =
      model.updateZona(zone).onComplete {
          case Success(Response(StatusCodes.SUCCES_CODE, _)) => myView.refreshZonaPanel("ok-zone-update")
          case Success(Response(_,None)) => myView.showMessageFromKey("bad-request")
          case _ => myView.showMessageFromKey("general-error")
        }
    override def deleteZona(zone: Zona): Unit =
      zone.idZone.foreach(id =>model.deleteZona(id).onComplete{
          case Success(Response(StatusCodes.SUCCES_CODE, _)) => myView.refreshZonaPanel("ok-zone-delete")
          case Success(Response(_,None)) => myView.showMessageFromKey("bad-request")
          case _ => myView.showMessageFromKey("general-error")
        })
    override def saveTerminal(terminal: Terminale): Unit =
      model.createTerminale(terminal).onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE, _)) => myView.refreshTerminalPanel("ok-terminal-request")
        case Success(Response(_,None)) => myView.showMessageFromKey("bad-request")
        case _ => myView.showMessageFromKey("general-error")
      }
    override def updateTerminal(terminal: Terminale): Unit =
      model.updateTerminale(terminal).onComplete{
          case Success(Response(StatusCodes.SUCCES_CODE, _)) => myView.refreshTerminalPanel("ok-terminal-update")
          case Success(Response(_,None)) => myView.showMessageFromKey("bad-request")
          case _ => myView.showMessageFromKey("general-error")
        }
    override def deleteTerminal(terminal: Terminale): Unit =
      terminal.idTerminale.foreach(id=>model.deleteTerminale(id).onComplete{
          case Success(Response(StatusCodes.SUCCES_CODE, _)) => myView.refreshTerminalPanel("ok-terminal-delete")
          case Success(Response(_,None)) => myView.showMessageFromKey("bad-request")
          case _ => myView.showMessageFromKey("general-error")
        })

    private def getZone: Future[Response[List[Zona]]] =
      model.getAllZone


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

    override def infoParamToShow(idp: Int, data: DataForParamasModel): Unit =
      model.getParameterById(idp).onComplete{
        case Success(Response(StatusCodes.SUCCES_CODE, Some(value))) => myView.showInfoParam(data.copy(info = Some(value)))
        case Success(Response(StatusCodes.NOT_FOUND, None)) =>  myView.showMessageFromKey("not-found-terminal")
        case _ => myView.showMessageFromKey("general-error")
      }

    override def saveParam(param: InfoAlgorithm): Unit =
      model.saveParameters(param).onComplete {
        case Success(Response(StatusCodes.SUCCES_CODE, _)) => Platform.runLater(() => myView.showMessageFromKey("GeneralError-Success"))
        case Success(Response(StatusCodes.ERROR_CODE1, _)) => Platform.runLater(() => myView.showMessageFromKey("no-save-par"))
        case Success(Response(StatusCodes.ERROR_CODE2, _)) => Platform.runLater(() => myView.showMessageFromKey("no-save-day"))
        case Success(Response(StatusCodes.ERROR_CODE3, _)) => Platform.runLater(() => myView.showMessageFromKey("no-zona-terminal-valid"))
        case Success(Response(StatusCodes.ERROR_CODE4, _)) => Platform.runLater(() => myView.showMessageFromKey("no-rules"))
        case Success(Response(StatusCodes.ERROR_CODE5, _)) => Platform.runLater(() => myView.showMessageFromKey("no-val-day"))
        case _ => Platform.runLater(() => myView.showMessageFromKey("general-error"))
      }
  }
}
