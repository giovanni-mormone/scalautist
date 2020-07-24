package dbfactory.operation

import caseclass.CaseClassDB.Regola
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceRegola
import dbfactory.implicitOperation.OperationCrud
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.Future
trait RegolaOperation extends OperationCrud[Regola]{
  /**
   * Method that return all rule that contains a group for the algorithm
   * @return Future of Option of List of Regola that contains all Rule for group for the algorithm
   */
 def regolaGruppo():Future[Option[List[Regola]]]
  /**
   * Method that return all rule that contains a normal week and special week for the algorithm
   * @return Future of Option of List of Regola that contains all Rule for a normal
   *         week and special week for the algorithm
   */
  def regolaSettimana():Future[Option[List[Regola]]]
}
object RegolaOperation extends RegolaOperation {
  private val groupRule=List(1,2,3)
  private val weekRule=List(4,5,6)
  override def regolaGruppo():Future[Option[List[Regola]]] =
    InstanceRegola.operation().selectFilter(_.id.inSet(groupRule))


  override def regolaSettimana():Future[Option[List[Regola]]]=
    InstanceRegola.operation().selectFilter(_.id.inSet(weekRule))
}

