package dbfactory.operation

import caseclass.CaseClassDB.StoricoContratto
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceStoricoContratto
import dbfactory.implicitOperation.OperationCrud
import dbfactory.table.StoricoContrattoTable.StoricoContrattoTableRep
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Giovanni Mormone
 * Trait which allows to perform operations on the StoricoContratto table.
 *
 */
trait StoricoContrattoOperation extends OperationCrud[StoricoContratto]{
  /**
   * Deletes all StoricoContratto associate to a specific Persona.
   *
   * @param idPerson
   *                 The id of the Persona's StoricoContratto group to delete.
   * @return
   *         A Future Option containing the number of deleted StoriciContratti or None
   *         if no storico was deleted.
   */
  def deleteAllStoricoForPerson(idPerson: Int): Future[Option[Int]]

  /**
   * Deletes all StoricoContratto associate to a List of Persone.
   *
   * @param idList
   *               The ids of the StoriciContratti's Personas to delete
   * @return
   *         A Future Option containing the number of deleted StoriciContratti or None
   *          if no storico was deleted.
   */
  def deleteAllStoricoForPersonList(idList: List[Int]): Future[Option[Int]]

}
object StoricoContrattoOperation extends StoricoContrattoOperation {

  override def deleteAllStoricoForPerson(idPerson: Int): Future[Option[Int]] ={
   selectStorico(f => f.personaId === idPerson).flatMap(checkStorici)
  }

  override def deleteAllStoricoForPersonList(idList: List[Int]): Future[Option[Int]] = {
    selectStorico(f => f.personaId.inSet(idList)).flatMap(checkStorici)
  }

  private val selectStorico: (StoricoContrattoTableRep => Rep[Boolean]) => Future[Option[List[StoricoContratto]]] =
    filter => InstanceStoricoContratto.operation().selectFilter(filter)


  private def checkStorici(storici: Option[List[StoricoContratto]]) : Future[Option[Int]] =
        deleteAll(storici.map(_.flatMap(_.idStoricoContratto)).getOrElse(List()))
}