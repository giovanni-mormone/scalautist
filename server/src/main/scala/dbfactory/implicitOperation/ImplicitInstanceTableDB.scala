package dbfactory.implicitOperation

import caseclass.CaseClassDB._
import dbfactory.setting.{GenericCRUD, GenericOperation, GenericTable}
import dbfactory.table.AssenzaTable.AssenzaTableRep
import dbfactory.table.ContrattoTable.ContrattoTableRep
import dbfactory.table.DisponibilitaTable.DisponibilitaTableRep
import dbfactory.table.GiornoInSettimanaTable.GiornoInSettimanaTableRep
import dbfactory.table.GiornoTable.GiornoTableRep
import dbfactory.table.GruppoTerminaleTable.GruppoTerminaleTableRep
import dbfactory.table.ParametroTable.ParametroTableRep
import dbfactory.table.PersonaTable.PersonaTableRep
import dbfactory.table.PresenzaTable.PresenzaTableRep
import dbfactory.table.RegolaTable.RegolaTableRep
import dbfactory.table.RichiestaTable.RichiestaTableRep
import dbfactory.table.RichiestaTeoricaTable.RichiestaTeoricaTableRep
import dbfactory.table.RisultatoTable.RisultatoTableRep
import dbfactory.table.StipendioTable.StipendioTableRep
import dbfactory.table.StoricoContrattoTable.StoricoContrattoTableRep
import dbfactory.table.TerminaleTable.TerminaleTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import dbfactory.table.ZonaTable.ZonaTableRep

/**
 * @author Fabian Aspee Encina
 *  Trait which enable obtain a instance of the case class [[dbfactory.setting.GenericCRUD.GenericOperationCRUD]] initializate with the
 *  table we want use for make operation
 * @tparam A Is a case class that represent instance of the table in database [[caseclass.CaseClassDB]]
 * @tparam B class that represent the table in database, allow make query in database [[dbfactory.table]]
 */
trait ImplicitInstanceTableDB[A,B<:GenericTable[A]] {
  /**
   * operation private enable only enable in the package implicitOperation, this create a instance of
   * Brands [[dbfactory.setting.GenericCRUD.GenericOperationCRUD]], which enable make generic operation in the database
   * @return A instance of GenericOperationCRUD which enables operation in database
   */
  private[implicitOperation] def typeDB():GenericCRUD[A,B]

  /**
   * method which enable obtain the instance of Operation [[dbfactory.setting.GenericOperation]],
   * this method is used for [[dbfactory.operation]] for make the another operation in the system
   * e.g ExecQuery,ExecQueryFilter, etc.
   * @return A instance of Operation which enables operation in database
   */
  def operation():GenericOperation[A,B]
}
object ImplicitInstanceTableDB {
  implicit object InstanceContratto extends ImplicitInstanceTableDB[Contratto,ContrattoTableRep] {
    override private[implicitOperation]  def typeDB(): GenericCRUD[Contratto, ContrattoTableRep] = GenericCRUD[Contratto,ContrattoTableRep]()
    override def operation(): GenericOperation[Contratto, ContrattoTableRep] = GenericOperation[Contratto, ContrattoTableRep]()
  }
  implicit object InstanceGiornoInSettimana extends ImplicitInstanceTableDB[GiornoInSettimana,GiornoInSettimanaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[GiornoInSettimana, GiornoInSettimanaTableRep] = GenericCRUD[GiornoInSettimana, GiornoInSettimanaTableRep]()

    override def operation(): GenericOperation[GiornoInSettimana, GiornoInSettimanaTableRep] = GenericOperation[GiornoInSettimana, GiornoInSettimanaTableRep]()
  }
  implicit object InstanceGiorno extends ImplicitInstanceTableDB[Giorno,GiornoTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Giorno, GiornoTableRep] = GenericCRUD[Giorno, GiornoTableRep]()

    override def operation(): GenericOperation[Giorno, GiornoTableRep] =  GenericOperation[Giorno, GiornoTableRep]()
  }
  implicit object InstanceGruppoTerminale extends ImplicitInstanceTableDB[GruppoTerminale,GruppoTerminaleTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[GruppoTerminale, GruppoTerminaleTableRep] = GenericCRUD[GruppoTerminale, GruppoTerminaleTableRep]()

    override def operation(): GenericOperation[GruppoTerminale, GruppoTerminaleTableRep] = GenericOperation[GruppoTerminale, GruppoTerminaleTableRep]()
  }
  implicit object InstanceParametro extends ImplicitInstanceTableDB[Parametro,ParametroTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Parametro, ParametroTableRep] = GenericCRUD[Parametro, ParametroTableRep]()

    override def operation(): GenericOperation[Parametro, ParametroTableRep] = GenericOperation[Parametro, ParametroTableRep]()
  }
  implicit object InstancePersona extends ImplicitInstanceTableDB[Persona,PersonaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Persona, PersonaTableRep] = GenericCRUD[Persona,PersonaTableRep]()

    override def operation(): GenericOperation[Persona, PersonaTableRep] = GenericOperation[Persona, PersonaTableRep]()
  }
  implicit object InstancePresenza extends ImplicitInstanceTableDB[Presenza,PresenzaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Presenza, PresenzaTableRep] = GenericCRUD[Presenza, PresenzaTableRep]()

    override def operation(): GenericOperation[Presenza, PresenzaTableRep] = GenericOperation[Presenza, PresenzaTableRep]()
  }
  implicit object InstanceRichiesta extends ImplicitInstanceTableDB[Richiesta,RichiestaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Richiesta, RichiestaTableRep] = GenericCRUD[Richiesta, RichiestaTableRep]()

    override  def operation(): GenericOperation[Richiesta, RichiestaTableRep] = GenericOperation[Richiesta, RichiestaTableRep]()
  }
  implicit object InstanceRichiestaTeorica extends ImplicitInstanceTableDB[RichiestaTeorica,RichiestaTeoricaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[RichiestaTeorica, RichiestaTeoricaTableRep] = GenericCRUD[RichiestaTeorica, RichiestaTeoricaTableRep]()

    override def operation(): GenericOperation[RichiestaTeorica, RichiestaTeoricaTableRep] = GenericOperation[RichiestaTeorica, RichiestaTeoricaTableRep]()
  }
  implicit object InstanceRisultato extends ImplicitInstanceTableDB[Risultato,RisultatoTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Risultato, RisultatoTableRep] = GenericCRUD[Risultato, RisultatoTableRep]()

    override def operation(): GenericOperation[Risultato, RisultatoTableRep] = GenericOperation[Risultato, RisultatoTableRep]()
  }
  implicit object InstanceRegola extends ImplicitInstanceTableDB[Regola,RegolaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Regola, RegolaTableRep] = GenericCRUD[Regola, RegolaTableRep]()

    override def operation(): GenericOperation[Regola, RegolaTableRep] = GenericOperation[Regola, RegolaTableRep]()
  }
  implicit object InstanceStoricoContratto extends ImplicitInstanceTableDB[StoricoContratto,StoricoContrattoTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[StoricoContratto, StoricoContrattoTableRep] = GenericCRUD[StoricoContratto, StoricoContrattoTableRep]()

    override def operation(): GenericOperation[StoricoContratto, StoricoContrattoTableRep] = GenericOperation[StoricoContratto, StoricoContrattoTableRep]()
  }

  implicit object InstanceTerminale extends ImplicitInstanceTableDB[Terminale,TerminaleTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Terminale, TerminaleTableRep] = GenericCRUD[Terminale, TerminaleTableRep]()

    override def operation(): GenericOperation[Terminale, TerminaleTableRep] =  GenericOperation[Terminale, TerminaleTableRep]()
  }
  implicit object InstanceTurno extends ImplicitInstanceTableDB[Turno,TurnoTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Turno, TurnoTableRep] = GenericCRUD[Turno, TurnoTableRep]()

    override def operation(): GenericOperation[Turno, TurnoTableRep] = GenericOperation[Turno, TurnoTableRep]()
  }
  implicit object InstanceZona extends ImplicitInstanceTableDB[Zona,ZonaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Zona, ZonaTableRep] = GenericCRUD[Zona, ZonaTableRep]()

    override def operation(): GenericOperation[Zona, ZonaTableRep] = GenericOperation[Zona, ZonaTableRep]()
  }

  implicit object InstanceDisponibilita extends ImplicitInstanceTableDB[Disponibilita,DisponibilitaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Disponibilita, DisponibilitaTableRep] = GenericCRUD[Disponibilita, DisponibilitaTableRep]()

    override def operation(): GenericOperation[Disponibilita, DisponibilitaTableRep] = GenericOperation[Disponibilita, DisponibilitaTableRep]()
  }

  implicit object InstanceAssenza extends ImplicitInstanceTableDB[Assenza,AssenzaTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Assenza, AssenzaTableRep] = GenericCRUD[Assenza, AssenzaTableRep]()

    override def operation(): GenericOperation[Assenza, AssenzaTableRep] = GenericOperation[Assenza, AssenzaTableRep]()
  }

  implicit object InstanceStipendio extends ImplicitInstanceTableDB[Stipendio,StipendioTableRep] {
    override private[implicitOperation] def typeDB(): GenericCRUD[Stipendio, StipendioTableRep] = GenericCRUD[Stipendio, StipendioTableRep]()

    override def operation(): GenericOperation[Stipendio, StipendioTableRep] = GenericOperation[Stipendio, StipendioTableRep]()
  }
}