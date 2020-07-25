package view.fxview.component.manager.subcomponent

import java.sql.Date

import caseclass.CaseClassDB.{Terminale, Turno}
import utils.TransferObject.InfoRichiesta
import view.fxview.component.manager.subcomponent.parent.ManagerRichiestaParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

/** @author Fabian Aspee Encina
 *  Trait which allows to perform operations on richiesta view.
 */
trait ManagerRichiestaBox extends Component[ManagerRichiestaParent]{
  def cancelAction(): Unit


  /**
   * method which allow return action and save information which has been completed in actual view
   *
   * @param valueForDay value that user insert in view, represent idDay that contains a list with
   *                    id shift and yours respective quantity
   */
  def backAction(valueForDay: (Int, List[(Int, Int)])): Unit

  /**
   * method which allow next action and save information which has been completed in actual view
   * @param valueForDay value that user insert in view, represent idDay that contains a list with
   *                   id shift and yours respective quantity
   */
  def nextAction(valueForDay: (Int, List[(Int, Int)])): Unit

  /**
   * method which allow next action but in this point the user is in end view
   * @param listShiftRequest list of value that user insert in all view, view [[utils.TransferObject.InfoRichiesta]]
   */
  def nextAction(listShiftRequest: InfoRichiesta): Unit

  /**
   * method that re paint all element that belong to Richiesta
   */
  def reDrawRichiesta():Unit

  /**
   * method that allow draw all shift in every day in week
   * @param listShift list that contains all shift in the system
   * @param position allows to return the day for which it is being designed
   */
  def drawShiftRequest(listShift: List[Turno],position:Int=0): Unit

  /**
   * method that contains info for terminal or terminals selected and init date and finish date for teoric request
   * @param idTerminal represent list with all terminal selected
   * @param date init date for teoric request
   * @param date1 finish date for teoric request
   */
  def terminalSelected(idTerminal: List[Int],date:Date,date1:Date):Unit
}

object ManagerRichiestaBox{
  final case class ExtraInfo(listShift:List[Turno],listTerminal:List[Terminale],idSummary:Int,nameDay:String,days:List[(Int,String)])
  def apply(terminal: List[Terminale]): ManagerRichiestaBox = {
   val manager= new ManagerRichiestaFX(terminal)
    manager.addChildren()
    manager
  }

  private class ManagerRichiestaFX(terminal: List[Terminale]) extends AbstractComponent[ManagerRichiestaParent]("manager/subcomponent/ManagerRichiestaBox")
    with ManagerRichiestaBox{

    var idTerminal: List[Int]=_
    var date:Date=_
    var date1:Date=_
    var listShiftRequest:List[Turno]=_
    var listShiftWeek:List[(Int, List[(Int, Int)])]=List.empty
    val BACK_POSITION = 2
    val POSITION=1
    val FIRST_DAY = 1
    val DAY_IN_WEEK=7
    val SUMMARY=8
    val days:List[(Int,String)] = List((1,resources.getResource("monday")),
      (2,resources.getResource("tuesday")),
      (3,resources.getResource("wednesday")),
      (4,resources.getResource("thursday")),
      (5,resources.getResource("friday")),
      (6,resources.getResource("saturday")),
      (7,resources.getResource("sunday")),
      (8,resources.getResource("summary")))

    def addChildren(): Unit ={
      pane.getChildren.add(DateAndTerminalBox(terminal).setParent(this).pane)
    }

    override def terminalSelected(idTerminal: List[Int], date: Date, date1: Date): Unit = {
      this.idTerminal=idTerminal
      this.date=date
      this.date1=date1
      idTerminal.headOption.foreach(id=>parent.selectShift(id))

    }

    override def drawShiftRequest(listShift: List[Turno],position:Int=0): Unit = {
      pane.getChildren.clear()
      listShiftRequest=listShift
      pane.getChildren.add(RichiestaForDayBox(listShift, days(position)._1,days(position)._2).setParent(this).pane)
    }

    private def drawShiftWithInfo(listShift: List[Turno],position:Int,info:List[(Int, List[(Int, Int)])]): Unit ={
      pane.getChildren.clear()
      pane.getChildren
        .add(RichiestaForDayBox(listShift, days(position)._1,days(position)._2,info(position)).setParent(this).pane)
    }

    private def drawSummary(position:Int=0): Unit = {
      pane.getChildren.clear()
      pane.getChildren.add(RichiestaForDayBox(InfoRichiesta(date,date1,listShiftWeek,idTerminal)
        ,ExtraInfo(listShiftRequest,terminal,days(position)._1,days(position)._2,days)).setParent(this).pane)
    }

    override def nextAction(valueForDay: (Int, List[(Int, Int)])): Unit =
      valueForDay._1 match {
        case idDay if idDay<DAY_IN_WEEK && !listShiftWeek.exists(day=>day._1==valueForDay._1) => addElement(valueForDay)
          drawShiftRequest(listShiftRequest,idDay)
        case idDay if idDay<DAY_IN_WEEK && !listShiftWeek.exists(day=>day._1==valueForDay._1) => addElement(valueForDay)
          drawShiftRequest(listShiftRequest,idDay)
        case idDay if idDay<DAY_IN_WEEK && listShiftWeek.length>idDay  =>updateElement(valueForDay)
          drawShiftWithInfo(listShiftRequest,idDay,listShiftWeek)
        case idDay if idDay<DAY_IN_WEEK  =>updateElement(valueForDay); drawShiftRequest(listShiftRequest,idDay)
        case idDay if idDay==DAY_IN_WEEK =>addElement(valueForDay); drawSummary(idDay)
      }

    override def backAction(valueForDay: (Int, List[(Int, Int)])): Unit =
      valueForDay._1 match {
        case SUMMARY => drawShiftWithInfo(listShiftRequest,SUMMARY-BACK_POSITION,listShiftWeek)
        case idDay if idDay>FIRST_DAY && !listShiftWeek.exists(day=>day._1==valueForDay._1) =>addElement(valueForDay)
          drawShiftWithInfo(listShiftRequest,idDay-BACK_POSITION,listShiftWeek)
        case idDay if idDay>FIRST_DAY  =>updateElement(valueForDay)
          drawShiftWithInfo(listShiftRequest,idDay-BACK_POSITION,listShiftWeek)
        case _ => parent.showBackMessage(resources.getResource("advertencia"))
      }

    private def addElement(valueForDay: (Int, List[(Int, Int)])): Unit ={
      if(!listShiftWeek.exists(day=>day._1==valueForDay._1))
        listShiftWeek= listShiftWeek:+valueForDay
    }

    private def updateElement(valueForDay: (Int, List[(Int, Int)])):Unit =
      listShiftWeek=listShiftWeek.updated(valueForDay._1-POSITION,valueForDay)

    override def reDrawRichiesta(): Unit = {
      pane.getChildren.clear()
      listShiftWeek=List.empty
      addChildren()
    }

    override def nextAction(listShiftRequest: InfoRichiesta): Unit =
      parent.sendRichiesta(listShiftRequest)

    override def cancelAction(): Unit =
      parent.showBackMessage(resources.getResource("advertencia"))
  }
}
