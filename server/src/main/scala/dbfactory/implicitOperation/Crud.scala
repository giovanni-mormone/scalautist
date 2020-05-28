package dbfactory.implicitOperation

import caseclass.CaseClassDB._
import dbfactory.setting.GenericOperation.Operation
import dbfactory.table.ContrattoTable.ContrattoTableRep
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

import scala.concurrent.Future
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * [[caseclass.CaseClassDB]]
 * @tparam A
 */
trait Crud[A]{
  private[implicitOperation] def select(element:Int):Future[Option[A]]
  private[implicitOperation] def selectAll: Future[List[A]]
  private[implicitOperation] def insert(element:A):Future[Int]
  private[implicitOperation] def insertAll(element:List[A]):Future[List[Int]]
  private[implicitOperation] def delete(element:A):Future[Int]
  private[implicitOperation] def deleteAll(element:List[A]): Future[Int]
  private[implicitOperation] def update(element:A):Future[Int]
}

object Crud {
  implicit object CrudContratto extends OperationImplicit[Contratto,ContrattoTableRep] with Crud[Contratto]{
    override private[implicitOperation] def insert(element: Contratto):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Contratto]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Contratto): Future[Int]                = typeDB().delete(element.idContratto.get)
    override private[implicitOperation] def update(element: Contratto): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Contratto]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Contratto]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Contratto]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idContratto.get))

  }
  implicit object CrudGiornoInSettimana extends OperationImplicit[GiornoInSettimana,GiornoInSettimanaTableRep] with Crud[GiornoInSettimana] {
    override private[implicitOperation] def insert(element: GiornoInSettimana):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[GiornoInSettimana]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: GiornoInSettimana): Future[Int]                = typeDB().delete(element.idSettimana.get)
    override private[implicitOperation] def update(element: GiornoInSettimana): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[GiornoInSettimana]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[GiornoInSettimana]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[GiornoInSettimana]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idSettimana.get))

  }
  implicit object CrudGiorno extends OperationImplicit[Giorno,GiornoTableRep] with Crud[Giorno] {
    override private[implicitOperation] def insert(element: Giorno):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Giorno]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Giorno): Future[Int]                = typeDB().delete(element.idGiorno.get)
    override private[implicitOperation] def update(element: Giorno): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Giorno]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Giorno]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Giorno]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idGiorno.get))

  }
  implicit object CrudGruppoTerminale extends OperationImplicit[GruppoTerminale,GruppoTerminaleTableRep] with Crud[GruppoTerminale] {
    override private[implicitOperation] def insert(element: GruppoTerminale):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[GruppoTerminale]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: GruppoTerminale): Future[Int]                = typeDB().delete(element.idGruppoTerminale.get)
    override private[implicitOperation] def update(element: GruppoTerminale): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[GruppoTerminale]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[GruppoTerminale]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[GruppoTerminale]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idGruppoTerminale.get))

  }
  implicit object CrudParametro extends OperationImplicit[Parametro,ParametroTableRep] with Crud[Parametro] {
    override private[implicitOperation] def insert(element: Parametro):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Parametro]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Parametro): Future[Int]                = typeDB().delete(element.idParametri.get)
    override private[implicitOperation] def update(element: Parametro): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Parametro]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Parametro]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Parametro]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idParametri.get))

  }
  implicit object CrudPersona extends OperationImplicit[Persona,PersonaTableRep] with Crud[Persona] {
    private val operation: Operation[Persona, PersonaTableRep] = Operation[Persona,PersonaTableRep]()
    override private[implicitOperation] def insert(element: Persona):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Persona]]        = operation.execQuery(f=>(f.nome, f.cognome,f.numTelefono,Option[String](""),f.ruolo,f.terminaleId,Option[Int](element)),element)
                                                                                                  .map(convertTupleToPerson)
    override private[implicitOperation] def delete(element: Persona): Future[Int]                = typeDB().delete(element.matricola.get)
    override private[implicitOperation] def update(element: Persona): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Persona]]                     = operation.execQueryAll(t=>(t.nome, t.cognome,t.numTelefono,Option[String](""),t.ruolo,t.terminaleId,t.id.?))
                                                                                                   .map(_.map(r=>convertTupleToPerson(Some(r)).get))
    override private[implicitOperation] def insertAll(element: List[Persona]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Persona]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.matricola.get))
    private def convertTupleToPerson(persona:Option[(String, String, String,Option[String], Int,Option[Int], Option[Int])]):Option[Persona] = persona.map(value =>Persona.apply _ tupled value)
  }
  implicit object CrudPresenza extends OperationImplicit[Presenza,PresenzaTableRep] with Crud[Presenza] {
    override private[implicitOperation] def insert(element: Presenza):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Presenza]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Presenza): Future[Int]                = typeDB().delete(element.idPresenza.get)
    override private[implicitOperation] def update(element: Presenza): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Presenza]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Presenza]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Presenza]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idPresenza.get))

  }
  implicit object CrudRichiesta extends OperationImplicit[Richiesta,RichiestaTableRep] with Crud[Richiesta] {
    override private[implicitOperation] def insert(element: Richiesta):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Richiesta]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Richiesta): Future[Int]                = typeDB().delete(element.idRichiesta.get)
    override private[implicitOperation] def update(element: Richiesta): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Richiesta]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Richiesta]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Richiesta]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idRichiesta.get))

  }
  implicit object CrudRichiestaTeorica extends OperationImplicit[RichiestaTeorica,RichiestaTeoricaTableRep] with Crud[RichiestaTeorica] {
    override private[implicitOperation] def insert(element: RichiestaTeorica):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[RichiestaTeorica]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: RichiestaTeorica): Future[Int]                = typeDB().delete(element.idRichiestaTeorica.get)
    override private[implicitOperation] def update(element: RichiestaTeorica): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[RichiestaTeorica]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[RichiestaTeorica]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[RichiestaTeorica]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idRichiestaTeorica.get))

  }
  implicit object CrudRisultato extends OperationImplicit[Risultato,RisultatoTableRep] with Crud[Risultato] {
    override private[implicitOperation] def insert(element: Risultato):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Risultato]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Risultato): Future[Int]                = typeDB().delete(element.idRisultato.get)
    override private[implicitOperation] def update(element: Risultato): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Risultato]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Risultato]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Risultato]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idRisultato.get))

  }
  implicit object CrudSettimana extends OperationImplicit[Settimana,SettimanaTableRep] with Crud[Settimana] {
    override private[implicitOperation] def insert(element: Settimana):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Settimana]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Settimana): Future[Int]                = typeDB().delete(element.idZone.get)
    override private[implicitOperation] def update(element: Settimana): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Settimana]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Settimana]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Settimana]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idZone.get))

  }
  implicit object CrudStoricoContratto extends OperationImplicit[StoricoContratto,StoricoContrattoTableRep] with Crud[StoricoContratto] {
    override private[implicitOperation] def insert(element: StoricoContratto):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[StoricoContratto]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: StoricoContratto): Future[Int]                = typeDB().delete(element.idStoricoContratto.get)
    override private[implicitOperation] def update(element: StoricoContratto): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[StoricoContratto]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[StoricoContratto]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[StoricoContratto]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idStoricoContratto.get))

  }
  implicit object CrudStraordinario extends OperationImplicit[Straordinario,StraordinarioTableRep] with Crud[Straordinario] {
    override private[implicitOperation] def insert(element: Straordinario):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Straordinario]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Straordinario): Future[Int]                = typeDB().delete(element.idStraordinario.get)
    override private[implicitOperation] def update(element: Straordinario): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Straordinario]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Straordinario]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Straordinario]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idStraordinario.get))

  }
  implicit object CrudTerminale extends OperationImplicit[Terminale,TerminaleTableRep] with Crud[Terminale] {
    override private[implicitOperation] def insert(element: Terminale):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Terminale]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Terminale): Future[Int]                = typeDB().delete(element.idTerminale.get)
    override private[implicitOperation] def update(element: Terminale): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Terminale]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Terminale]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Terminale]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idTerminale.get))

  }
  implicit object CrudTurno extends OperationImplicit[Turno,TurnoTableRep] with Crud[Turno] {
    override private[implicitOperation] def insert(element: Turno):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Turno]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Turno): Future[Int]                = typeDB().delete(element.id.get)
    override private[implicitOperation] def update(element: Turno): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Turno]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Turno]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Turno]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.id.get))

  }
  implicit object CrudZona extends OperationImplicit[Zona,ZonaTableRep] with Crud[Zona] {
    override private[implicitOperation] def insert(element: Zona):Future[Int]                 = typeDB().insert(element)
    override private[implicitOperation] def select(element: Int): Future[Option[Zona]]        = typeDB().select(element)
    override private[implicitOperation] def delete(element: Zona): Future[Int]                = typeDB().delete(element.idZone.get)
    override private[implicitOperation] def update(element: Zona): Future[Int]                = typeDB().update(element)
    override private[implicitOperation] def selectAll: Future[List[Zona]]                     = typeDB().selectAll
    override private[implicitOperation] def insertAll(element: List[Zona]): Future[List[Int]] = typeDB().insertAll(element)
    override private[implicitOperation] def deleteAll(element: List[Zona]): Future[Int]       = typeDB().deleteAll(element.map(t=>t.idZone.get))

  }
}
