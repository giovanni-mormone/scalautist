package dbfactory.implicitOperation

import caseclass.CaseClassDB._
import dbfactory.setting.GenericOperation.Operation
import dbfactory.table.ContrattoTable.ContrattoTableRep
import dbfactory.table.DisponibilitaTable.DisponibilitaTableRep
import dbfactory.table.GiornoInSettimanaTable.GiornoInSettimanaTableRep
import dbfactory.table.GiornoTable.GiornoTableRep
import dbfactory.table.GruppoTerminaleTable.GruppoTerminaleTableRep
import dbfactory.table.ParametroTable.ParametroTableRep
import dbfactory.table.PersonaTable.PersonaTableRep
import dbfactory.table.PresenzaTable.PresenzaTableRep
import dbfactory.table.RichiestaTable.RichiestaTableRep
import dbfactory.table.RichiestaTeoricaTable.RichiestaTeoricaTableRep
import dbfactory.table.RisultatoTable.RisultatoTableRep
import dbfactory.table.SettimanaTable.SettimanaTableRep
import dbfactory.table.StoricoContrattoTable.StoricoContrattoTableRep
import dbfactory.table.StraordinarioTable.StraordinarioTableRep
import dbfactory.table.TerminaleTable.TerminaleTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import dbfactory.table.ZonaTable.ZonaTableRep
import dbfactory.util.Helper._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/** @author Fabian Aspee Encina
 * Trait which enables operation generic in all tables [[caseclass.CaseClassDB]]
 * @tparam A Is a case class that represent instance of the table in database
 */
trait Crud[A]{
  /**
   *
   * @param element Id for select one element in a table in the database
   * @return Option of case class which represent a instance of object in database
   */
  private[implicitOperation] def select(element:Int):Future[Option[A]]

  /**
   * Select all element in a table in the database
   * @return List of all element in the table of the database
   */
  private[implicitOperation] def selectAll: Future[List[A]]

  /**
   * Generic operation which enable insert any element in any table in database
   * @param element case class that represent instance of the table in database
   * @return Future of Int that represent status of operation
   */
  private[implicitOperation] def insert(element:A):Future[Int]

  /**
   *  Generic operation which enable insert a List of any element in any table in database
   * @param element List of case class that represent instance of the table in database
   * @return Future of List of Int that represent status of operation
   */
  private[implicitOperation] def insertAll(element:List[A]):Future[List[Int]]

  /**
   *  Generic operation which enable delete one element in any table in database
   * @param element case class that represent one element in one table in database
   * @return Future of Int that represent status of operation
   */
  private[implicitOperation] def delete(element:Int):Future[Int]

  /**
   *  Generic Operation which enable delete a List of element of any table in database
   * @param element List of case class that represent one instance of a table in database
   * @return Future of Int that represent status of operation
   */
  private[implicitOperation] def deleteAll(element:List[Int]): Future[Int]

  /**
   *  Operation which enable update one element in any table in database
   * @param element case class that represent element in table of the database we want update
   * @return Future of Int that represent status of operation
   */
  private[implicitOperation] def update(element:A):Future[Int]
}

object Crud {
  implicit object CrudContratto extends OperationImplicit[Contratto,ContrattoTableRep] with Crud[Contratto]{
    override private[implicitOperation] def insert(element: Contratto):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Contratto]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Contratto): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Contratto]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Contratto]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudGiornoInSettimana extends OperationImplicit[GiornoInSettimana,GiornoInSettimanaTableRep] with Crud[GiornoInSettimana] {
    override private[implicitOperation] def insert(element: GiornoInSettimana):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[GiornoInSettimana]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: GiornoInSettimana): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[GiornoInSettimana]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[GiornoInSettimana]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudGiorno extends OperationImplicit[Giorno,GiornoTableRep] with Crud[Giorno] {
    override private[implicitOperation] def insert(element: Giorno):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Giorno]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Giorno): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Giorno]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Giorno]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudGruppoTerminale extends OperationImplicit[GruppoTerminale,GruppoTerminaleTableRep] with Crud[GruppoTerminale] {
    override private[implicitOperation] def insert(element: GruppoTerminale):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[GruppoTerminale]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: GruppoTerminale): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[GruppoTerminale]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[GruppoTerminale]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudParametro extends OperationImplicit[Parametro,ParametroTableRep] with Crud[Parametro] {
    override private[implicitOperation] def insert(element: Parametro):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Parametro]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Parametro): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Parametro]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Parametro]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudPersona extends OperationImplicit[Persona,PersonaTableRep] with Crud[Persona] {
    import slick.jdbc.SQLServerProfile.api._

    private val operation: Operation[Persona, PersonaTableRep] = Operation[Persona,PersonaTableRep]()
    override private[implicitOperation] def insert(element: Persona):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Persona]]        = operation.execQuery(personaSelect,element)
                                                                                                  .map(convertTupleToPerson)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Persona): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Persona]]                     = operation.execQueryAll(personaSelect)
                                                                                                   .map(_.map(r=>convertTupleToPerson(Some(r)).get))
    override private[implicitOperation] def insertAll(element: List[Persona]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudPresenza extends OperationImplicit[Presenza,PresenzaTableRep] with Crud[Presenza] {
    override private[implicitOperation] def insert(element: Presenza):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Presenza]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Presenza): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Presenza]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Presenza]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudRichiesta extends OperationImplicit[Richiesta,RichiestaTableRep] with Crud[Richiesta] {
    override private[implicitOperation] def insert(element: Richiesta):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Richiesta]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Richiesta): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Richiesta]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Richiesta]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudRichiestaTeorica extends OperationImplicit[RichiestaTeorica,RichiestaTeoricaTableRep] with Crud[RichiestaTeorica] {
    override private[implicitOperation] def insert(element: RichiestaTeorica):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[RichiestaTeorica]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: RichiestaTeorica): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[RichiestaTeorica]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[RichiestaTeorica]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudRisultato extends OperationImplicit[Risultato,RisultatoTableRep] with Crud[Risultato] {
    override private[implicitOperation] def insert(element: Risultato):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Risultato]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Risultato): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Risultato]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Risultato]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudSettimana extends OperationImplicit[Settimana,SettimanaTableRep] with Crud[Settimana] {
    override private[implicitOperation] def insert(element: Settimana):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Settimana]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                      = typeDB().delete(element)
    override private[implicitOperation] def update(element: Settimana): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Settimana]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Settimana]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]             = typeDB().deleteAll(element)

  }
  implicit object CrudStoricoContratto extends OperationImplicit[StoricoContratto,StoricoContrattoTableRep] with Crud[StoricoContratto] {
    override private[implicitOperation] def insert(element: StoricoContratto):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[StoricoContratto]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: StoricoContratto): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[StoricoContratto]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[StoricoContratto]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudStraordinario extends OperationImplicit[Straordinario,StraordinarioTableRep] with Crud[Straordinario] {
    override private[implicitOperation] def insert(element: Straordinario):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Straordinario]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Straordinario): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Straordinario]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Straordinario]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudTerminale extends OperationImplicit[Terminale,TerminaleTableRep] with Crud[Terminale] {
    override private[implicitOperation] def insert(element: Terminale):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Terminale]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Terminale): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Terminale]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Terminale]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudTurno extends OperationImplicit[Turno,TurnoTableRep] with Crud[Turno] {
    override private[implicitOperation] def insert(element: Turno):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Turno]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Turno): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Turno]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Turno]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }
  implicit object CrudZona extends OperationImplicit[Zona,ZonaTableRep] with Crud[Zona] {
    override private[implicitOperation] def insert(element: Zona):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Zona]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Zona): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Zona]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Zona]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }

  implicit object CrudDisponibilita extends OperationImplicit[Disponibilita,DisponibilitaTableRep] with Crud[Disponibilita] {
    override private[implicitOperation] def insert(element: Disponibilita):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Disponibilita]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Int): Future[Int]                = typeDB().delete(element)
    override private[implicitOperation] def update(element: Disponibilita): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Disponibilita]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Disponibilita]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Int]): Future[Int]       = typeDB().deleteAll(element)

  }}
