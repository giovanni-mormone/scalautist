package view.mainview

import java.sql.Date

import caseclass.CaseClassDB.{Regola, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoAlgorithm, InfoReplacement, ResultAlgorithm}
import utils.TransferObject.InfoRichiesta
import view.DialogView
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm


trait ManagerView extends DialogView {
  def drawNotification(str: String, tag: Long): Unit
  /**
   * method that draw result for all driver in the time frame
   * @param resultList all result of the driver with yours information for selected time frame
   * @param dateList list of all date that the manager wants to see
   */
  def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit

  /**
   * method that draw all shift that existing in database this is for theorical request
   * @param value List with all shift
   */
  def drawShiftRequest(value: List[Turno]): Unit

  /**
   * method that draw all terminal that existing in database this is for theorical request
   * @param terminal list with all terminal that existing in database
   */
  def drawRichiesta(terminal: List[Terminale]): Unit

  /**
   * method that send InfoRichiesta, this case class contains all information
   * for the request and the terminal that this infoRichiesta is associated
   * @param richiesta case class that all information relationship with theorical request
   */
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  /**
   * Show the zone view
   *
   * @param zones
   *              List of [[caseclass.CaseClassDB.Zona]]
   */
  def drawZonaView(zones: List[Zona]): Unit

  /**
   * show terminal view
   *
   * @param zones
   *              Listo of [[caseclass.CaseClassDB.Zona]]
   * @param terminals
   *                   Listo of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawTerminaleView(zones: List[Zona], terminals: List[Terminale]): Unit

  /**
   * show terminal modal
   *
   * @param zoneList
   *                 List of [[caseclass.CaseClassDB.Zona]]
   * @param terminal
   *                 instance of [[caseclass.CaseClassDB.Terminale]]
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
   * The method draws the list of [[caseclass.CaseClassDB.Parametro]] and it allows to choose params
   *
   * @param olds list of [[caseclass.CaseClassDB.Parametro]]
   */
  def modalOldParamDraw(olds: List[InfoAlgorithm], terminals: List[Terminale], rules: List[Regola]): Unit

  /**
   *
   * @param params
   */
  def drawWeekParam(params: ParamsForAlgoritm, rules: List[Regola]):Unit

  /**
   *
   * @param params
   */
  def drawGroupParam(params: ParamsForAlgoritm,  rule: List[Regola]):Unit

  def drawResultTerminal(terminal: List[Terminale]): Unit

  def refreshTerminalPanel(messageKey: String): Unit
  def refreshZonaPanel(messageKey: String): Unit
  def consumeNotification(tag: Long): Unit
  def result(message: String): Unit
}
