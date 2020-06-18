package dbfactory.setting

import caseclass.CaseClassDB._
import dbfactory.setting.GenericCRUD.GenericOperationCRUD
import dbfactory.table.AssenzaTable.AssenzaTableRep
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
import dbfactory.table.TerminaleTable.TerminaleTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import dbfactory.table.ZonaTable.ZonaTableRep
import slick.jdbc.SQLServerProfile.api._

/**
 * @author Fabian Aspée Encina
 * Trait which allow obtain the instance of table of the database this allow create join and another operation
 * @tparam C class that represent the table in database, allow make query in database [[dbfactory.table]]
 * @tparam A case class that represent instance in database [[caseclass.CaseClassDB]]
 */
trait Table[C,A<:GenericTable[C]] extends GenericOperationCRUD[C,A]{
  /**
   * method which call [[dbfactory.setting.GenericTableQuery.tableDB]] for generate instance of a table
   * @return instance of a table of the database
   */
  def tableQuery():TableQuery[A]
}
object Table{

  object AssenzaTableQuery extends Table[Assenza,AssenzaTableRep]{
    override def tableQuery(): TableQuery[AssenzaTableRep] = tableDB()
  }

  object ContrattoTableQuery extends Table[Contratto,ContrattoTableRep]{
    override def tableQuery(): TableQuery[ContrattoTableRep] = tableDB()
  }
  object GiornoInSettimanaTableQuery extends Table[GiornoInSettimana,GiornoInSettimanaTableRep]{
    override def tableQuery(): TableQuery[GiornoInSettimanaTableRep] = tableDB()
  }
  object GiornoTableQuery extends Table[Giorno,GiornoTableRep]{
    override def tableQuery():TableQuery[GiornoTableRep] = tableDB()
  }
  object GruppoTerminaleTableQuery extends Table[GruppoTerminale,GruppoTerminaleTableRep]{
    override def tableQuery():TableQuery[GruppoTerminaleTableRep] = tableDB()
  }
  object ParametroTableQuery extends Table[Parametro,ParametroTableRep]{
    override def tableQuery():TableQuery[ParametroTableRep] = tableDB()
  }
  object PersonaTableQuery extends  Table[Persona,PersonaTableRep]{
    override def tableQuery(): TableQuery[PersonaTableRep] = tableDB()
  }
  object PresenzaTableQuery extends Table[Presenza,PresenzaTableRep]{
    override def tableQuery():TableQuery[PresenzaTableRep] = tableDB()
  }
  object RichiestaTableQuery extends Table[Richiesta,RichiestaTableRep]{
    override def tableQuery():TableQuery[RichiestaTableRep] = tableDB()
  }
  object RichiestaTeoricaTableQuery extends Table[RichiestaTeorica,RichiestaTeoricaTableRep]{
    override def tableQuery():TableQuery[RichiestaTeoricaTableRep] = tableDB()
  }
  object RisultatoTableQuery extends Table[Risultato,RisultatoTableRep]{
    override def tableQuery():TableQuery[RisultatoTableRep] = tableDB()
  }
  object SettimanaTableQuery extends Table[Settimana,SettimanaTableRep]{
    override def tableQuery():TableQuery[SettimanaTableRep] = tableDB()
  }
  object StoricoContrattoTableQuery extends Table[StoricoContratto,StoricoContrattoTableRep]{
    override def tableQuery():TableQuery[StoricoContrattoTableRep] = tableDB()
  }
  object TerminaleTableQuery extends Table[Terminale,TerminaleTableRep]{
    override def tableQuery(): TableQuery[TerminaleTableRep] = tableDB()
  }
  object TurnoTableQuery extends Table[Turno,TurnoTableRep]{
    override def tableQuery():TableQuery[TurnoTableRep] = tableDB()
  }
  object ZonaTableQuery extends Table[Zona,ZonaTableRep]{
    override def tableQuery(): TableQuery[ZonaTableRep] = tableDB()
  }
  object DisponibilitaTableQuery extends Table[Disponibilita,DisponibilitaTableRep]{
    override def tableQuery(): TableQuery[DisponibilitaTableRep] = tableDB()
  }
}

