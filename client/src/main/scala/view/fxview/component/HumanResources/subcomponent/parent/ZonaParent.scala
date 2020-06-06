package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Zona

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by zona views to make requests to controller
 */
trait ZonaParent {

  /**
   * Add a new Zona in the db
   *
   * @param zona
   */
  def newZona(zona: Zona): Unit

}
