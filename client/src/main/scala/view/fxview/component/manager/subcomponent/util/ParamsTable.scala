package view.fxview.component.manager.subcomponent.util

import caseclass.CaseClassDB.Parametro
import javafx.beans.property.SimpleStringProperty
import view.fxview.component.HumanResources.subcomponent.util.TableArgument

/**
 * @author Francesco Cassano
 *
 * Class to draw params in table
 *
 */
class ParamsTable(idp: String, namep: String) extends TableArgument {

  var id = new SimpleStringProperty(idp)
  var name = new SimpleStringProperty(namep)

  def getId: String = id.get
  def getName: String = name.get

  def setId(v: String): Unit = id.set(v)
  def setName(v: String): Unit = name.set(v)
}

object ParamsTable {

  implicit def paramsToParamsTable(param: Parametro): ParamsTable =
    new ParamsTable(param.idParametri.getOrElse("none").toString, param.nome)

  implicit def listParamsToListParamsTable(params: List[Parametro]): List[ParamsTable] =
    params.map(param => new ParamsTable(param.idParametri.getOrElse("none").toString, param.nome))
}