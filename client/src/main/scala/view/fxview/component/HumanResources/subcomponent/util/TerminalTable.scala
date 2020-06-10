package view.fxview.component.HumanResources.subcomponent.util

import caseclass.CaseClassDB.Terminale
import javafx.beans.property.SimpleStringProperty

/**
 * @author Francesco Cassano
 *
 * Class to draw Terminal in table
 *
 * @param idt
 *            id Terminal
 * @param namet
 *              name Terminal
 */
class TerminalTable(idt: String, namet: String) extends TableArgument {

  var id = new SimpleStringProperty(idt)
  var name = new SimpleStringProperty(namet)

  def getId: String = id.get
  def getName: String = name.get

  def setId(v: String): Unit = id.set(v)
  def setName(v: String): Unit = name.set(v)
}

/**
 * @author Francesco Cassano
 *
 * Object contains Implicit conversion from Terminale to TerminalTable
 */
object TerminalTable {

  implicit def ZonaToZonaTable(terminal: Terminale): TerminalTable =
    new TerminalTable(terminal.idTerminale.head.toString, terminal.nomeTerminale)

  implicit def ZonaListToZonaTableList(terminals: List[Terminale]): List[TerminalTable] =
    terminals.map(terminal => new TerminalTable(terminal.idTerminale.head.toString, terminal.nomeTerminale))
}