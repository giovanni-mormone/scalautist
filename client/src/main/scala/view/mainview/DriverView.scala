package view.mainview

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{InfoHome, InfoShift, StipendioInformations}
import view.DialogView


trait DriverView extends DialogView{
  def drawNotification(str: String, tag: Long): Unit

  /**
   * this method draw information of the driver, this is a shift in the day and
   * extra shift that this can have.
   * @param infoHome case class with information of driver for day
   */
  def drawHomeView(infoHome: InfoHome):Unit

  /**
   * method which draw view that contains all shift of a driver into 7 days
   * @param shift all shift for the next seven days
   */
  def drawShiftView(shift: InfoShift):Unit

  /**
   * method that draw view for salary for a driver
   * @param list list of all salary for a driver
   */
  def drawSalaryView(list:List[Stipendio]):Unit

  /**
   * method which enable view information for a salary in the specific month
   * @param information case class with all presenze, absence and salary for a month
   */
  def informationSalary(information:StipendioInformations):Unit

  /**
   * Called when a rotatory driver needs to insert the disponibilità for a new week
   * @param days list with day and availability for driver
   */
  def drawDisponibilitaPanel(days: List[String]): Unit

  /**
   * Called when a disponibilità was correctly inserted at the start of the week
   */
  def disponibilityInserted(): Unit

  def showMessageError(message: String): Unit

}