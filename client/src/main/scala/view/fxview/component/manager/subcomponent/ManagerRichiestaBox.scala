package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Terminale, Turno}
import view.fxview.component.manager.subcomponent.parent.ManagerRichiestaParent
import view.fxview.component.{AbstractComponent, Component}

trait ManagerRichiestaBox extends Component[ManagerRichiestaParent]{
  def nextAction(valueForDay: (Int, List[(Int, Int)])): Unit


  def drawShiftRequest(listShift: List[Turno],position:Int=0): Unit

  def terminalSelected(idTerminal:Int,date:Date,date1:Date)
}

object ManagerRichiestaBox{

  def apply(terminal: List[Terminale]): ManagerRichiestaBox = {
   val manager= new ManagerRichiestaFX()
    manager.addChildren(terminal)
    manager
  }

  private class ManagerRichiestaFX extends AbstractComponent[ManagerRichiestaParent]("manager/subcomponent/ManagerRichiestaBox")
    with ManagerRichiestaBox{

    var idTerminal:Int=_
    var date:Date=_
    var date1:Date=_
    var listShiftRequest:List[Turno]=_
    var listShiftWeek:List[(Int, List[(Int, Int)])]=List.empty
    private val days = List((1,"Lunedi"),(2,"Martedi"),(3,"wednesday"),(4,"thursday"),(5,"friday"),(6,"saturday"),(7,"sunday"))
    override def initialize(location: URL, resources: ResourceBundle): Unit = {

    }
    def addChildren(terminal: List[Terminale]): Unit ={
      pane.getChildren.add(DateAndTerminalBox(terminal).setParent(this).pane)
    }

    override def terminalSelected(idTerminal: Int, date: Date, date1: Date): Unit = {
      this.idTerminal=idTerminal
      parent.getShift(idTerminal)
    }

    override def drawShiftRequest(listShift: List[Turno],position:Int=0): Unit = {
      pane.getChildren.clear()
      listShiftRequest=listShift
      pane.getChildren.add(RichiestaForDayBox(listShift, days(position)._1,days(position)._2).setParent(this).pane)
    }

    override def nextAction(valueForDay: (Int, List[(Int, Int)])): Unit = {
      listShiftWeek= listShiftWeek:+valueForDay
      println(listShiftWeek)
      valueForDay._1 match {
        case v if v<7 => drawShiftRequest(listShiftRequest,v)
        case _ => println("repilogo")
      }

    }

  }
}
