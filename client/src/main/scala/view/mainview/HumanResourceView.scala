package view.mainview

import caseclass.CaseClassDB.{Assenza, Contratto, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Ferie
import view.DialogView


/**
 * @author Francesco Cassano
 *
 * A view to manage human resource Views functionalities.
 * It extends [[view.BaseView]]
 *
 */
trait HumanResourceView extends DialogView {
  /**
   * method that show the result of the operation saveAbsence
   * @param keyMessage key that represent a message save in BundlePropierties
   * @param isMalattia represent if this method is called to illness or holiday for refresh
   */
  def resultAbsence(keyMessage: String, isMalattia: Boolean): Unit

  /**
   * Shows recruit view
   *
   * @param zones
   *              list of [[caseclass.CaseClassDB.Zona]] to show
   * @param contracts
   *                  list of [[caseclass.CaseClassDB.Contratto]] to show
   * @param shifts
   *               list of [[caseclass.CaseClassDB.Turno]] to show
   */
  def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit

  /**
   * Show terminals into recruit view
   *
   * @param terminals
   *                  list of [[caseclass.CaseClassDB.Terminale]] to show
   */
  def drawTerminal(terminals: List[Terminale]): Unit

  /**
   * Show the view that requested the list of employees
   *
   * @param employeesList
   *                      List of [[caseclass.CaseClassDB.Persona]] represent employees
   * @param viewToDraw
   *                   The string represent the view code requesting the data
   */
  def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit

  /**
   * show a list of employee to assign holiday
   *
   * @param employeesList
   *                      list of [[caseclass.CaseClassHttpMessage.Ferie]] to show
   */
  def drawHolidayView(employeesList: List[Ferie]):Unit

  /**
   * show a message in the modal like a pop-up
   *
   * @param message
   *                String of message to show
   */
  def result(message:String):Unit

  /**
   * method that send list of assenza for a person within a year
   * @param item represent ferie of a person, this case class contains day that remain to the person and your name and surname and idPerson
   * @param isMalattia boolean that represent if modal is open for illness or holiday
   * @param assenza list of assenza for one person
   */
  def drawModalAbsenceHoliday(item:Ferie,isMalattia:Boolean,assenza: List[Assenza]): Unit
  /**
   * show a message in the main view
   *
   * @param message
   *                String of message to show
   */
  def dialog(message: String): Unit

  /**
   * show a specific message in the main view
   *
   * @param className
   *                  String of the entity name
   * @param message
   *                String of type of message to print
   */
  def dialog(className: String, message: String): Unit
}
