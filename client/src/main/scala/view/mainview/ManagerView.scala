package view.mainview

import java.sql.Date

import caseclass.CaseClassDB.{Regola, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, InfoAbsenceOnDay, InfoReplacement, ResultAlgorithm}
import caseclass.{CaseClassDB, CaseClassHttpMessage}
import utils.TransferObject.InfoRichiesta
import view.DialogView
import view.fxview.component.manager.subcomponent.ParamsModal
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm


trait ManagerView extends DialogView {
  /**
   * method that draw a modal to choose whether to continue with the algorithm and overwrite old data or not
   *
   * @param message Type of error that occur
   * @param algorithmExecute information that allows the algorithm to work
   */
  def confirmRun(message: List[String], algorithmExecute: AlgorithmExecute): Unit

  def drawNotification(str: String, tag: Long): Unit

  /**
   * method that draw result for all driver in the time frame
   *
   * @param resultList all result of the driver with yours information for selected time frame
   * @param dateList   list of all date that the manager wants to see
   */
  def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit

  /**
   * method that draw all shift that existing in database this is for theorical request
   *
   * @param value List with all shift
   */
  def drawShiftRequest(value: List[Turno]): Unit

  /**
   * method that draw all terminal that existing in database this is for theorical request
   *
   * @param terminal list with all terminal that existing in database
   */
  def drawRichiesta(terminal: List[Terminale]): Unit

  /**
   * method that send InfoRichiesta, this case class contains all information
   * for the request and the terminal that this infoRichiesta is associated
   *
   * @param richiesta case class that all information relationship with theorical request
   */
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  /**
   * Show the zone view
   *
   * @param zones
   * List of [[caseclass.CaseClassDB.Zona]]
   */
  def drawZonaView(zones: List[Zona]): Unit

  /**
   * show terminal view
   *
   * @param zones
   * Listo of [[caseclass.CaseClassDB.Zona]]
   * @param terminals
   * Listo of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawTerminaleView(zones: List[Zona], terminals: List[Terminale]): Unit

  /**
   * show terminal modal
   *
   * @param zoneList
   * List of [[caseclass.CaseClassDB.Zona]]
   * @param terminal
   * instance of [[caseclass.CaseClassDB.Terminale]]
   */
  def openTerminalModal(zoneList: List[Zona], terminal: Terminale): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view to draw the list of turns that needs
   * a replacement
   *
   * @param absences case class that contains info with all absence for this day
   */
  def drawAbsence(absences: List[InfoAbsenceOnDay]): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view to draw the list of people that needs
   * a replacement
   *
   * @param replacement list of InfoReplacement
   */
  def drawReplacement(replacement: List[InfoReplacement]): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view to draw the panel to choice parameters to run
   * shift assignment algorithm
   *
   * @param terminals the list of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawRunAlgorithm(terminals: List[Terminale]): Unit

  /**
   * Allows to modify daily request and define special week with special rules and requests
   *
   * @param params parameter chosen in precedent panel
   * @param rules  rules that the user can choose
   */
  def drawWeekParam(params: ParamsForAlgoritm, rules: List[Regola]): Unit

  /**
   *
   * @param params
   */
  def drawGroupParam(params: ParamsForAlgoritm, rule: List[Regola]): Unit

  def drawResultTerminal(terminal: List[Terminale]): Unit

  def refreshTerminalPanel(messageKey: String): Unit

  def refreshZonaPanel(messageKey: String): Unit

  def consumeNotification(tag: Long): Unit

  def result(message: String): Unit

  /**
   *
   * @param olds
   * @param terminals
   * @param rules
   */
  def modalOldParamDraw(olds: List[CaseClassDB.Parametro], terminals: List[Terminale], rules: List[Regola]): Unit

  /**
   *
   * @param info
   * @param name
   * @param terminals
   * @param rules
   */
  def drawShowParams(info: CaseClassHttpMessage.AlgorithmExecute, name: Option[String], terminals: List[Terminale], rules: List[Regola]): Unit

  /**
   *
   * @param data
   */
  def showInfoParam(data: ParamsModal.DataForParamasModel): Unit
}
