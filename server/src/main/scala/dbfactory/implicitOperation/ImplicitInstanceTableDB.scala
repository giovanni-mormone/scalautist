package dbfactory.implicitOperation

import caseclass.CaseClassDB._
import dbfactory.setting.GenericCRUD.GenericOperationCRUD
import dbfactory.setting.GenericOperation.Operation
import dbfactory.setting.GenericTable
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
  private[implicitOperation] def typeDB():GenericOperationCRUD[A,B]

  /**
   * method which enable obtain the instance of Operation [[dbfactory.setting.GenericOperation]],
   * this method is used for [[dbfactory.operation]] for make the another operation in the system
   * e.g ExecQuery,ExecQueryFilter, etc.
   * @return A instance of Operation which enables operation in database
   */
  def operation():Operation[A,B]
}
object ImplicitInstanceTableDB {
  implicit object InstanceContratto extends ImplicitInstanceTableDB[Contratto,ContrattoTableRep] {
    override private[implicitOperation]  def typeDB(): GenericOperationCRUD[Contratto, ContrattoTableRep] = GenericOperationCRUD[Contratto,ContrattoTableRep]
    override def operation(): Operation[Contratto, ContrattoTableRep] = Operation[Contratto,ContrattoTableRep]
  }
  implicit object InstanceGiornoInSettimana extends ImplicitInstanceTableDB[GiornoInSettimana,GiornoInSettimanaTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[GiornoInSettimana, GiornoInSettimanaTableRep] = GenericOperationCRUD[GiornoInSettimana, GiornoInSettimanaTableRep]

    override def operation(): Operation[GiornoInSettimana, GiornoInSettimanaTableRep] = Operation[GiornoInSettimana, GiornoInSettimanaTableRep]
  }
  implicit object InstanceGiorno extends ImplicitInstanceTableDB[Giorno,GiornoTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Giorno, GiornoTableRep] = GenericOperationCRUD[Giorno, GiornoTableRep]

    override def operation(): Operation[Giorno, GiornoTableRep] =  Operation[Giorno, GiornoTableRep]
  }
  implicit object InstanceGruppoTerminale extends ImplicitInstanceTableDB[GruppoTerminale,GruppoTerminaleTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[GruppoTerminale, GruppoTerminaleTableRep] = GenericOperationCRUD[GruppoTerminale, GruppoTerminaleTableRep]

    override def operation(): Operation[GruppoTerminale, GruppoTerminaleTableRep] = Operation[GruppoTerminale, GruppoTerminaleTableRep]
  }
  implicit object InstanceParametro extends ImplicitInstanceTableDB[Parametro,ParametroTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Parametro, ParametroTableRep] = GenericOperationCRUD[Parametro, ParametroTableRep]

    override def operation(): Operation[Parametro, ParametroTableRep] = Operation[Parametro, ParametroTableRep]
  }
  implicit object InstancePersona extends ImplicitInstanceTableDB[Persona,PersonaTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Persona, PersonaTableRep] = GenericOperationCRUD[Persona,PersonaTableRep]

    override def operation(): Operation[Persona, PersonaTableRep] = Operation[Persona, PersonaTableRep]
  }
  implicit object InstancePresenza extends ImplicitInstanceTableDB[Presenza,PresenzaTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Presenza, PresenzaTableRep] = GenericOperationCRUD[Presenza, PresenzaTableRep]

    override def operation(): Operation[Presenza, PresenzaTableRep] = Operation[Presenza, PresenzaTableRep]
  }
  implicit object InstanceRichiesta extends ImplicitInstanceTableDB[Richiesta,RichiestaTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Richiesta, RichiestaTableRep] = GenericOperationCRUD[Richiesta, RichiestaTableRep]

    override  def operation(): Operation[Richiesta, RichiestaTableRep] = Operation[Richiesta, RichiestaTableRep]
  }
  implicit object InstanceRichiestaTeorica extends ImplicitInstanceTableDB[RichiestaTeorica,RichiestaTeoricaTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[RichiestaTeorica, RichiestaTeoricaTableRep] = GenericOperationCRUD[RichiestaTeorica, RichiestaTeoricaTableRep]

    override def operation(): Operation[RichiestaTeorica, RichiestaTeoricaTableRep] = Operation[RichiestaTeorica, RichiestaTeoricaTableRep]
  }
  implicit object InstanceRisultato extends ImplicitInstanceTableDB[Risultato,RisultatoTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Risultato, RisultatoTableRep] = GenericOperationCRUD[Risultato, RisultatoTableRep]

    override def operation(): Operation[Risultato, RisultatoTableRep] = Operation[Risultato, RisultatoTableRep]
  }
  implicit object InstanceSettimana extends ImplicitInstanceTableDB[Settimana,SettimanaTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Settimana, SettimanaTableRep] = GenericOperationCRUD[Settimana, SettimanaTableRep]

    override def operation(): Operation[Settimana, SettimanaTableRep] = Operation[Settimana, SettimanaTableRep]
  }
  implicit object InstanceStoricoContratto extends ImplicitInstanceTableDB[StoricoContratto,StoricoContrattoTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[StoricoContratto, StoricoContrattoTableRep] = GenericOperationCRUD[StoricoContratto, StoricoContrattoTableRep]

    override def operation(): Operation[StoricoContratto, StoricoContrattoTableRep] = Operation[StoricoContratto, StoricoContrattoTableRep]
  }
  implicit object InstanceStraordinario extends ImplicitInstanceTableDB[Straordinario,StraordinarioTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Straordinario, StraordinarioTableRep] = GenericOperationCRUD[Straordinario, StraordinarioTableRep]

    override def operation(): Operation[Straordinario, StraordinarioTableRep] = Operation[Straordinario, StraordinarioTableRep]
  }
  implicit object InstanceTerminale extends ImplicitInstanceTableDB[Terminale,TerminaleTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Terminale, TerminaleTableRep] = GenericOperationCRUD[Terminale, TerminaleTableRep]

    override def operation(): Operation[Terminale, TerminaleTableRep] =  Operation[Terminale, TerminaleTableRep]
  }
  implicit object InstanceTurno extends ImplicitInstanceTableDB[Turno,TurnoTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Turno, TurnoTableRep] = GenericOperationCRUD[Turno, TurnoTableRep]

    override def operation(): Operation[Turno, TurnoTableRep] = Operation[Turno, TurnoTableRep]
  }
  implicit object InstanceZona extends ImplicitInstanceTableDB[Zona,ZonaTableRep] {
    override private[implicitOperation] def typeDB(): GenericOperationCRUD[Zona, ZonaTableRep] = GenericOperationCRUD[Zona,ZonaTableRep]

    override def operation(): Operation[Zona, ZonaTableRep] = Operation[Zona, ZonaTableRep]
  }
}