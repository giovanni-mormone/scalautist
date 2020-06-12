package view.fxview.component.driver.utils

import caseclass.CaseClassDB.Stipendio

class StipendioFX(valorep: Double, datap: String,idStipendiop:Int){
  var valore:Double = valorep
  var data:String = datap
  var idStipendio:Int = idStipendiop

  def getIdStipendio: Int = idStipendio
  def getData: String = data
  def getValore: Double = valore

  def setIdStipendio(v: Int): Unit = idStipendio=v
  def setData(v: String): Unit = data=v
  def setValore(v: Double): Unit = valore=v
}

/**
 * @author Fabian Aspee
 *
 * Object contains Implicit conversion from stipendio to stipendio list view
 */
object StipendioFX {

  implicit def StipendioToStipendio(salary: Stipendio): StipendioFX =
    getStipendio(salary)

  implicit def ListFerieToListFerieTable(stipendioList: List[Stipendio]): List[StipendioFX] =
    stipendioList.map(salary =>getStipendio(salary))

  private def getStipendio(salary:Stipendio)=salary.idStipendio.fold(new StipendioFX(salary.valore, salary.data.toString, 0))(st=>new StipendioFX(salary.valore, salary.data.toString, st))

}
