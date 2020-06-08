package caseclass

import java.sql.Date

/**
 * @author Fabian Aspee Encina, Franceso Cassano, Giovanni Mormone
 * Object that encapsulates all case class that required both the server and client,
 * this is util because [[spray.json]] required this for make serialization of the objects
 */
object CaseClassDB{
   /**
   * Login is a case class that represent parameters for login
    *
   * @param user user name in system
   * @param password user password in system
   */
  final case class Login(user:String,password:String)

  /**
   * Contratto is a case class that represent a instance of table into database that contains
   * all contract of the system
   *
   * @param tipoContratto represent diverse type contract in the system, e.g: FULL-TIME 5x2, PART-TIME 5x2, etc.
   * @param turnoFisso represent the type shift that persona can have
   * @param idContratto identifies type shift into database (for insert operation this is not mandatory)
   */
  final case class Contratto(tipoContratto:String,turnoFisso:Boolean,idContratto:Option[Int]=None)

  /**
   * Giorno is a case class that represent a instance of table into database that contains
   * the quantity of driver required in a determinate day
   *
   * @param quantita quantity of driver required in a determinate day
   * @param nomeGiorno day into week
   * @param idGiorno identifies day into database (for insert operation this is not mandatory)
   */
  final case class Giorno(quantita:Int,nomeGiorno:String,idGiorno:Option[Int]=None)

  /**
   * GruppoTerminale is a case class that represent a instance of table into database that contains
   * all group create in the parametrization process
   *
   * @param parametriId represent the set of parameters to which it belongs
   * @param idGruppoTerminale represent id of group, this because a group can have more one day (for insert operation this is not mandatory)
   */
  final case class GruppoTerminale(parametriId:Int,idGruppoTerminale:Option[Int]=None)

  /**
   * Parametro is a case class that represent a instance of table into database that contains
   * identification of a set of parameters
   *
   * @param treSabato represent if ruler threeSaturday for algorithm is active
   * @param Regola PENDING
   * @param idParametri identifies set of parameters that serves the algorithm (for insert operation this is not mandatory)
   */
  final case class Parametro(treSabato:Byte,Regola:String,idParametri:Option[Int]=None)

  /**
   * Persona is a case class that represent a instance of table into database that contains
   * all information necessary for a persona, a persona can be a driver, human resources or
   * manager operation
   *
   * @param nome name of a persona
   * @param cognome surname of a persona
   * @param numTelefono telephone number of a persona
   * @param password password of a persona for login in the system
   * @param ruolo    identifies type of worker in the system, we identified 3 type,
   *                 driver, human resources or manager operation
   * @param isNew represent if a user is new in the system, this is because in the first access
   *              in the system, the user must change the password
   * @param userName identifies user in the system, serve for login in the system
   * @param idTerminale represent terminal with which a driver is associated
   * @param matricola identification univocal for every persona (for insert operation this is not mandatory)
   */
  final case class Persona(nome:String,cognome:String,numTelefono:String,password:Option[String],ruolo:Int,isNew:Boolean,userName:String,idTerminale:Option[Int]=None,disponibilita:Option[Int]=None,matricola:Option[Int]=None)

  /**
   * Presenza is a case class that represent a instance of table into database that contains all presence
   * of all drivers in database
   *
   * @param data date that represent day which a driver work
   * @param personaId identification of a persona
   * @param turnoId identification of a turn in the day
   * @param idPresenza represent univocally for every presence (for insert operation this is not mandatory)
   */
  final case class Presenza(data:Date,personaId:Int,turnoId:Int,idPresenza:Option[Int]=None)

  /**
   * Richiesta is a case class that represent a instance of table into database that contains all request
   * of drivers for a determinate day for turn
   *
   * @param turnoId identification of a turn in the day
   * @param giornoId identification of a day in week
   * @param richiestaTeoricaId identification that represent within itself initdate and finishdate
   *                           for one request
   * @param idRichiesta represent unambiguous for every request (for insert operation this is not mandatory)
   */
  final case class Richiesta(turnoId:Int,giornoId:Int,richiestaTeoricaId:Int,idRichiesta:Option[Int]=None)

  /**
   * RichiestaTeorica is a case class that represent a instance of table into database that contains
   * init date and finish date that represent period of time that contains request of driver
   * [[caseclass.CaseClassDB.Richiesta]]
   *
   * @param dataInizio init date of a request
   * @param dataFine finish date of a request
   * @param idRichiestaTeorica represent unambiguous for every request (for insert operation this is not mandatory)
   */
  final case class RichiestaTeorica(dataInizio:Date,dataFine:Option[Date],idRichiestaTeorica:Option[Int]=None)

  /**
   * Risultato is a case class that represent a instance of table into database that contains all result
   * of free and shift allocate algorithm
   *
   * @param data day that the algorithm has calculated
   * @param personaId represent one drivers in system that the algorithm has calculate your free and shift
   * @param turnoId shift that the allocate algorithm
   * @param idRisultato represent unambiguous for every result (for insert operation this is not mandatory)
   */
  final case class Risultato(data:Date,personaId:Int,turnoId:Int,idRisultato:Option[Int]=None)

  /**
   * GiornoInSettimana is a case class that represent a instance of table into database that contains
   * all day into week, this is to know what week a day belongs to
   *
   * @param giornoId    identifies day, this can be repeat more times
   * @param turnoId     identifies shift into day
   * @param parametriId identifies the parameter to which it belongs
   * @param settimanaId identifies week for all day associated to this
   * @param idSettimana represent unambiguous for every week (for insert operation this is not mandatory)
   */
  final case class GiornoInSettimana(giornoId:Int,turnoId:Int,parametriId:Int,settimanaId:Int,idSettimana:Option[Int]=None)

  /**
   * StoricoContratto is a case class that represent a instance of table into database that contains all
   * work contract
   *
   * @param dataInizio init date of the contract
   * @param dataFine finish date of the contract, a contract may not end
   * @param personaId identifies a driver in system that lets you know the contract
   * @param contrattoId identifies contract for a driver
   * @param turnoId identifies shift set into contract
   * @param turnoId1 identifies another shift if exist
   * @param idStoricoContratto represent unambiguous for every contract story (for insert operation this is not mandatory)
   */
  final case class StoricoContratto(dataInizio:Date,dataFine:Option[Date],personaId:Option[Int],contrattoId:Int,turnoId:Option[Int],turnoId1:Option[Int],idStoricoContratto:Option[Int]=None)

  /**
   * Straordinario is a case class that represent a instance of table into database that contains
   * all over time that driver make in the week, where week can be also saturday
   *
   * @param data day where driver make over time
   * @param personaId driver that make over time
   * @param turnoId shift where driver make over time
   * @param idStraordinario represent unambiguous for every over time (for insert operation this is not mandatory)
   */
  final case class Straordinario(data:Date,personaId:Int,turnoId:Int,idStraordinario:Option[Int]=None)

  /**
   * Terminale is a case class that represent a instance of table into database that contains
   * all terminal in the system, this contains name and associated zone.
   *
   * @param nomeTerminale name for one terminal
   * @param idZona identifies zone that terminal is associated
   * @param idTerminale represent unambiguous for every terminal (for insert operation this is not mandatory)
   */
  final case class Terminale(nomeTerminale:String,idZona:Int,idTerminale:Option[Int]=None)

  /**
   * Turno is a case class that represent a instance of table into database that contains
   * name of shift and your time slot
   *
   * @param nomeTurno name of shift in the system
   * @param fasciaOraria time slot that represent init and finish time
   * @param id represent unambiguous for every turno (for insert operation this is not mandatory)
   */
  final case class Turno(nomeTurno: String, fasciaOraria: String,notturno: Boolean,id: Option[Int] = None)

  /**
   * Zona is a case class that represent a instance of table into database that contains
   * name of zone and your id
   *
   * @param zones name of zona
   * @param idZone represent unambiguous for every zone (for insert operation this is not mandatory)
   */
  final case class Zona(zones:String,idZone:Option[Int]=None)

  /**
   * Settimana is a case class that represent a instance of table into database that contains one instance
   * of week, this is associated a one zone
   *
   * @param parametriSetId represent unambiguous for every settimana allocate in parameters
   * @param idZone represent unambiguous for every zone allocate in parameters (for insert operation this is not mandatory)
   */
  final case class Settimana(parametriSetId:Int,idZone:Option[Int]=None)

  /**
   * Stipendio is a case class that represent a instance of table into database that contains
   * salary information for all drivers
   *
   * @param personaId identifies a drivers in the system
   * @param valore   this represent the amount of money that driver receives
   * @param data      day on which payment is made
   * @param idStipendio represent unambiguous for every salary (for insert operation this is not mandatory)
   */
  final case class Stipendio(personaId: Int, valore: Double, data: Date, idStipendio: Option[Int] = None)

  /**
   * Assenza is a case class that represent a instance of table into database that contains
   *
   * @param personaId identifies a persona in the system
   * @param dataInizio init date of the absence
   * @param dataFine finish date that represent when absence finished
   * @param malattia identifies if absence is for illness or vacation
   * @param idAssenza represent unambiguous for every absence (for insert operation this is not mandatory)
   */
  final case class Assenza(personaId: Int, dataInizio: Date, dataFine: Date, malattia: Boolean, idAssenza: Option[Int] = None)

  /**
   * Representation of an instance of table Disponibilità in the DB
   * @param giorno1
   *                first day of disponibilita
   * @param giorno2
   *                second day of disponibilita
   * @param idDisponibilita
   *                        represent unambiguous for every disponibilita (for insert operation this is not mandatory)
   */
  final case class Disponibilita(giorno1: String, giorno2: String, idDisponibilita: Option[Int] = None)

}

