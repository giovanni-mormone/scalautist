package view.fxview.component.HumanResources.subcomponent.util

import caseclass.CaseClassDB.Zona
import javafx.beans.property.SimpleStringProperty

/**
 * @author Francesco Cassano
 *
 * Class to draw zones in table
 *
 * @param idz
 *            id Zona
 * @param namez
 *              name Zona
 */
class ZonaTable (idz: String, namez: String) extends TableArgument {

  var id = new SimpleStringProperty(idz)
  var name = new SimpleStringProperty(namez)

  def getId: String = id.get
  def getName: String = name.get

  def setId(v: String): Unit = id.set(v)
  def setName(v: String): Unit = name.set(v)

}

/**
 * @author Francesco Cassano
 *
 * Object contains Implicit conversion from zona to zonaTable
 */
object ZonaTable{

  implicit def ZonaToZonaTable(zone: Zona) =
    new ZonaTable(zone.idZone.head.toString, zone.zones)

  implicit def ZonaListToZonaTableList(zones: List[Zona]) =
    zones.map(zone => new ZonaTable(zone.idZone.head.toString, zone.zones))
}

