package dbfactory.operation

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{Disponibilita, Persona}
import caseclass.CaseClassHttpMessage.InfoReplacement
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstanceDisponibilita, InstancePersona, InstanceRisultato, InstanceStoricoContratto}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table._
import dbfactory.util.Helper._
import messagecodes.StatusCodes
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._

import scala.annotation.nowarn
import scala.concurrent.Future
/**
 * @author Giovanni Mormone,Fabian Aspee Encina, Francesco Cassano
 *
 * Allows to perform operations on the table Disponibilita in the DB
 */
trait DisponibilitaOperation extends OperationCrud[Disponibilita]{

  /**
   * returns diponibilita of employee in a week
   *
   * @param idUser
   *               employee id
   * @param week
   *             number of week in year
   * @return
   *         disponibilita
   */
  def getDisponibilita(idUser: Int, week: Int): Future[Option[Disponibilita]]

  /**
   *  Method that return all driver that have a availability for replace an another driver, return all driver
   *  that working one or 2 shift, if work 3 shift this is not considered
   * @param idRisultato id that works for search the day where exist a absence
   * @param idTemrinale represent terminal where exist a absence
   * @param idTurno represent turno where existe absence
   * @return Option of List of Int,String,String where Int-> idDriver, String->name, String->surName
   */
  def allDriverWithAvailabilityForADate(idRisultato:Int, idTemrinale:Int, idTurno:Int): Future[Option[List[InfoReplacement]]]

  /**
   * Method that starting from a result, a terminal and a turn controls if they exist
   *
   * @param idRisultato represent a row in result table, this contains information per one driver in one determinate shift
   * @param idTerminal represent terminal where the idRisultato was recorded
   * @param idTurno shift where idRisultato was recorded
   * @return Future of Option of Int that represent status operation, the result code that can return is :
   *         [[messagecodes.StatusCodes.ERROR_CODE1]] if idResult not exist in database
   *         [[messagecodes.StatusCodes.ERROR_CODE2]]  if idTerminal not exist in database
   *        [[messagecodes.StatusCodes.ERROR_CODE3]] if idTurno not exist in database
   *        [[messagecodes.StatusCodes.SUCCES_CODE]] if idResult, idTerminal and idTurno exist in database
   *
   */
  def verifyIdRisultatoAndTerminalAndShift(idRisultato:Int, idTerminal:Int, idTurno:Int): Future[Option[Int]]

  /**
   *  Method that having idUser and data return day where driver can have availability in the week
   *  if driver have assenza in the same day when he open app and assenza finish next week, so this driver
   *  can't have availability, if driver have absence in various day within week, then this driver can have
   *  availability only in days without absence, absence is considered illness and holidays
   * @param idUser represent a driver in database
   * @param date represent any date
   * @return Future of Option of List of String with all day where drive can have availability
   */
  def getGiorniDisponibilita(idUser: Int, date: Date):Future[Option[List[String]]]

  /**
   *  Method which enable update availability for a driver on database, receive element that is a
   *  case class [[caseclass.CaseClassDB.Disponibilita]] that represent the struct for a disponibilita on
   *  system
   *
   * @param element case class [[caseclass.CaseClassDB.Disponibilita]] that represent Disponibilita Table
   * @param idUser represent a driver in database
   * @return Future of Option of Int
   *         [[messagecodes.StatusCodes.NOT_FOUND]] if idUser not exist in database
   *         [[messagecodes.StatusCodes.ERROR_CODE1]]  if update not have success event
   */
  def updateDisponibilita(element: Disponibilita,idUser:Int): Future[Option[Int]]

  /**
   * method that update every monday, the availability for all driver with contract fixed
   * @param date date that represent day in week, this day is ever monday
   * @return Future of List of Option of Int with result of the operation
   */
  def updateAvailabilityWeekFissi(date:Date): Future[List[Option[Int]]]
}

object DisponibilitaOperation extends DisponibilitaOperation{

  private case class QueryPersonStoricAvail(idPerson:Option[List[Int]],week:Int,giorno:String,idTurno:Int,idTerminale:Int,date:Date)

  private val IS_FISSO = true
  private val GIORNI_SETTIMANA=7
  private val UNION=2
  private val SHIFT_IN_DAY=2
  private val SUNDAY = 6
  private val SATURDAY=0
  private val SUCCESS_UPDATE = 1
  private val DEFAULT_RESPONSE = Future.successful(None)
  private object convertToQueryPersonStoricAvail{

    private val DEFAULT_YEAR = 0
    private val DEFAULT_MONTH = 0
    private val DEFAULT_DAY = 0
    private val DEFAULT_WEEK = 0
    private val DEFAULT_NAME_DAY = ""
    implicit def datToQueryPersonStoricAvail(idPerson:(Option[List[Int]],Option[(Date, Int, String)],Int,Int)):QueryPersonStoricAvail = {
      val (date,week,day) = idPerson._2 match {
        case Some(value) => value
        case None => (Date.valueOf(LocalDate.of(DEFAULT_YEAR,DEFAULT_MONTH,DEFAULT_DAY)),DEFAULT_WEEK,DEFAULT_NAME_DAY)
      }
      QueryPersonStoricAvail(idPerson._1,week,day,idPerson._3,idPerson._4,date)
    }
  }
  import convertToQueryPersonStoricAvail.datToQueryPersonStoricAvail
  //identifies driver by id
  private val RUOLO_DRIVER=3
  override def insert(element:Disponibilita): Future[Option[Int]] = {
    for{
      disponibilita <-  InstanceDisponibilita.operation().execQueryFilter(f => f.id, x => x.giorno1 === element.giorno1 && x.giorno2 === element.giorno2)
      result<-disponibilita.map(_=>super.insert(element)).convert()
    } yield result
  }

  override def verifyIdRisultatoAndTerminalAndShift(idRisultato:Int, idTerminal:Int, idTurno:Int): Future[Option[Int]] =
    for {
      existRisultato<-RisultatoOperation.verifyResult(idRisultato)
      existTerminal<- existRisultato.filter(_ != StatusCodes.ERROR_CODE1).map(_=>
        TerminaleOperation.verifyTerminal(idTerminal)).convert()
      existShift<- existTerminal.filter(_ != StatusCodes.ERROR_CODE2).map(_=>
        TurnoOperation.verifyShift(idTurno)).convert()
      result <- verifyResult(existRisultato,existTerminal,existShift)
    }yield result

  private def verifyResult(existRisultato:Option[Int],existTerminal:Option[Int],existShift:Option[Int]): Future[Option[Int]] =Future{
    (existRisultato,existTerminal,existShift) match{
      case (None, None, None) => None
      case (_, None, None) => existRisultato
      case (_, _, None) => existTerminal
      case (_, _, Some(StatusCodes.ERROR_CODE3)) => existShift
      case (_, _, _) => Option(StatusCodes.SUCCES_CODE)
    }
  }
  override def allDriverWithAvailabilityForADate(idRisultato:Int, idTemrinale:Int, idTurno:Int): Future[Option[List[InfoReplacement]]] = {
    callOperation(idRisultato, idTemrinale, idTurno).collect{
      case (personWithoutShift, allWithAbsence) =>
        personWithoutShift.toList.flatten.filter(absence=> !allWithAbsence.toList.flatten
        .contains(absence)
       )
    }.flatMap(result=>selectAllDriverNameAndSurname(result,idRisultato))
  }

  private def callOperation(idRisultato:Int, idTemrinale:Int, idTurno:Int): Future[(Option[List[Int]], Option[List[Int]])] =
    for {
      risultato <- selectDateAndCreateDayAndWeek(idRisultato)
      resultJoinPersonaDis<- risultato.map(value => getPersonWithoutAbsence(idTemrinale,value._1)).convert()
      allWithAbsence<- risultato.map(value => selectAllWithAbsence(value._1)).convert()
      personWithoutShift<- getPersonaWithoutShiftAndAvailability(resultJoinPersonaDis,risultato,idTurno,idTemrinale)
    }yield(personWithoutShift,allWithAbsence)

  private def selectDateAndCreateDayAndWeek(idRisultato:Int): Future[Option[(Date, Int, String)]] =
    InstanceRisultato.operation()
      .execQueryFilter(risultato => risultato.data, risultato => risultato.id === idRisultato)
      .collect {
        case Some(value) =>Some(value.map(date =>  (date, getWeekNumber(date), nameOfDay(date))) match {
          case List(a) => a })
        case None =>None
      }

  private def selectAllWithAbsence(date:Date): Future[Option[List[Int]]] =
    InstanceAssenza.operation().execQueryFilter(assenza=>assenza.personaId,
      assenza=>assenza.dataInizio<=date && assenza.dataFine>=date).map(_.map(_.distinct))

  private def getPersonWithoutAbsence(idTerminale:Int,date:Date): Future[Option[List[Int]]] = {
    val joinQuery = for {
      (persona,_) <- PersonaTableQuery.tableQuery() joinLeft AssenzaTableQuery.tableQuery()
        .filter(az=>az.dataInizio>date || az.dataFine<date || Some(az.dataInizio).isEmpty || Some(az.dataFine).isEmpty) on (_.id===_.personaId)
      if persona.ruolo===RUOLO_DRIVER && persona.terminaleId === idTerminale
    } yield persona.id
    InstancePersona.operation().execJoin(joinQuery).map(result=>result.map(_.distinct))
  }


  private def getPersonaWithoutShiftAndAvailability(dat:QueryPersonStoricAvail): Future[Option[List[Int]]] ={
    val allDriver = for{
      persona<- PersonaTableQuery.tableQuery()
      risultato<-RisultatoTableQuery.tableQuery()
      if persona.terminaleId===dat.idTerminale && persona.id===risultato.personeId && risultato.turnoId===dat.idTurno
    }yield risultato.personeId

    InstancePersona.operation().execJoin(allDriver).flatMap {
      case Some(value) => finalGetPersonaWithoutShiftAndAvailability(dat,Some(value))
      case None =>finalGetPersonaWithoutShiftAndAvailability(dat,None)
    }
  }

  private def finalGetPersonaWithoutShiftAndAvailability(dat:QueryPersonStoricAvail,idDriver:Option[List[Int]])={
    val joinQuery = for {
      persona<- PersonaTableQuery.tableQuery()
      storico<-StoricoContrattoTableQuery.tableQuery()
      disp<-DisponibilitaTableQuery.tableQuery()
      risultato<-RisultatoTableQuery.tableQuery()
      if(persona.id===storico.personaId && persona.disponibilitaId===disp.id && risultato.personeId===persona.id && persona.terminaleId===dat.idTerminale
        && risultato.data===dat.date && disp.settimana===dat.week &&
        ((disp.giorno1===dat.giorno || disp.giorno2===dat.giorno) && ((storico.dataFine >= dat.date || !storico.dataFine.isDefined) && storico.dataInizio <= dat.date))
        && persona.id.inSet(dat.idPerson.toList.flatten) && !persona.id.inSet(idDriver.toList.flatten) && persona.disponibilitaId.isDefined)
    } yield storico.personaId
    InstancePersona.operation().execJoin(joinQuery).map(result=>result.map(persons=>persons
      .filter(id=>persons.count(_ == id)==SHIFT_IN_DAY)).map(_.distinct))
  }

  private def selectAllDriverNameAndSurname(idPerson:List[Int],idRisultato:Int): Future[Option[List[InfoReplacement]]] ={
    InstancePersona.operation().execQueryFilter(person=>(person.id,person.nome,person.cognome),person=>person.id.inSet(idPerson)).collect {
      case Some(value) => Some(value.map(person=>InfoReplacement(idRisultato,person._1,person._2,person._3)))
      case None =>None
    }
  }

  override def getDisponibilita(idUser: Int, week: Int): Future[Option[Disponibilita]] = {
    val filter = for {
      disponibilita <- PersonaTableQuery.tableQuery() join DisponibilitaTableQuery.tableQuery() on (_.disponibilitaId === _.id)
                      if disponibilita._2.settimana === week && disponibilita._1.id === idUser
    } yield (disponibilita._2.id, disponibilita._2.giorno1, disponibilita._2.giorno2, disponibilita._2.settimana)

    InstanceDisponibilita.operation().execJoin(filter).collect{
      case Some(List(disp)) => Some(Disponibilita(disp._4, disp._2, disp._3, Some(disp._1)))
      case _ => None
    }
  }

  private def deleteDayAbsence(listDate:List[(Date,Date)], dates:Date): List[Date] ={
    val days = createListDay(dates)
    listDate.map {
      case (date, date1) if date1.compareTo(getEndDayWeek(date)) > 0 => date -> getEndDayWeek(dates)
      case (date, date1) if date1.compareTo(getEndDayWeek(date)) <= 0 => date -> date1
    }.flatMap(dates => {
      val dayBetween = createListDayBetween(dates._1, dates._2)
      days.filter(day => !dayBetween.contains(day))
    }).groupBy(date => date) match {
      case map if map.exists(_._2.length>=UNION)=> deleteDateBefore(map.filter(_._2.length==2),dates)
      case map =>deleteDateBefore(map,dates)
    }
  }

  private def deleteDateBefore(map: Map[Date, List[Date]],dateDay:Date): List[Date] = {
    map.keySet.toList.sortBy(date=>date).dropWhile(date=>{
      date.compareTo(dateDay)<0
    })
  }

  private def operationWhenExistAbsence(listDate:List[Date],idUser:Int,date:Date): Future[Option[List[String]]] ={
    listDate match {
      case element if element.length==GIORNI_SETTIMANA=> DEFAULT_RESPONSE
      case element if element.length<=GIORNI_SETTIMANA=>
        getDisponibilitaName(idUser,date).map(_.map(days => {
          listDate.map(nameOfDay).filter(day => days.contains(day))
        })).map(finalResponse)
      case Nil =>getDisponibilitaName(idUser,date)
    }
  }

  private def getDisponibilitaName(idUser: Int, date: Date): Future[Option[List[String]]] ={
    val join = for{
      contratto<- ContrattoTableQuery.tableQuery()
      storico <- StoricoContrattoTableQuery.tableQuery()
      if(contratto.id===storico.contrattoId && (contratto.turnoFisso===IS_FISSO || (storico.dataInizio>date || storico.dataFine<date))
      && storico.personaId===idUser)
    }yield contratto.turnoFisso
    InstanceStoricoContratto.operation().execJoin(join).flatMap {
      case Some(_) => DEFAULT_RESPONSE
      case None =>getDisponibilita(idUser,getWeekNumber(date)).flatMap{
        case Some(_) => DEFAULT_RESPONSE
        case None =>getDayWithoutWorking(date,getEndDayWeek(date),idUser)
      }
    }
  }

  override def getGiorniDisponibilita(idUser: Int, date: Date):Future[Option[List[String]]]={
    getDayNumber(date) match {
      case SUNDAY | SATURDAY => DEFAULT_RESPONSE
      case _ => InstanceAssenza.operation().execQueryFilter(disp=>(disp.dataInizio,disp.dataFine),disp=>
        (disp.dataInizio>=getFirstDayWeek(date) && disp.dataFine>=getFirstDayWeek(date) ||
          disp.dataInizio<=getFirstDayWeek(date) && disp.dataFine>=getFirstDayWeek(date)) && disp.personaId===idUser)
        .flatMap {
          case Some(value) => operationWhenExistAbsence(deleteDayAbsence(value,date),idUser,date).map(finalResponse)
          case None =>  getDisponibilitaName(idUser,date).map(finalResponse)
        }
    }
  }

  private def finalResponse(result:Option[List[String]]): Option[List[String]] ={
    result match {
      case Some(List() | List(_))=>None
      case None => None
      case _ => result
    }
  }

  private def getDayWithoutWorking(initDate:Date,finishDate:Date,idUser:Int): Future[Option[List[String]]] ={
    InstanceRisultato.operation()
      .execQueryFilter(result=>result.data,result=>result.data>=initDate && result.data<=finishDate && result.personeId===idUser)
      .collect(result=>  Option(result.toList.flatten.distinct.map(nameOfDay)))
  }

  override def updateDisponibilita(element: Disponibilita,idUser:Int): Future[Option[Int]] = {
        InstancePersona.operation().execQueryFilter(persona=>persona.id,persona=>persona.id===idUser).flatMap{
          case Some(_) =>InstanceDisponibilita.operation().execQueryFilter(disp=>disp.id,disp=>disp.settimana===element.settimana &&
            ((disp.giorno1===element.giorno1 && disp.giorno2===element.giorno2) ||
            (disp.giorno1===element.giorno2 && disp.giorno2===element.giorno1))).flatMap {
            case Some(value) if value.length==SUCCESS_UPDATE => InstancePersona.operation()
                .execQueryUpdate(persona=>persona.disponibilitaId,persona=>persona.id===idUser,value.headOption)
            case None =>Future.successful(Some(StatusCodes.ERROR_CODE1))
          }
          case None =>  Future.successful(Some(StatusCodes.NOT_FOUND))
        }
  }
  override def updateAvailabilityWeekFissi(date:Date): Future[List[Option[Int]]] = {
    val week = getWeekNumber(date)
    val join = for{
      persona<- PersonaTableQuery.tableQuery()
      storico<- StoricoContrattoTableQuery.tableQuery()
      contratto <- ContrattoTableQuery.tableQuery()
      if(persona.ruolo===3 && persona.id === storico.personaId &&
        storico.contrattoId===contratto.id) && (!storico.dataFine.isDefined || storico.dataFine>date) && contratto.turnoFisso===true
    }yield persona
    InstancePersona.operation().execJoin(join).flatMap{
      case Some(persone) =>
        val previousWeek = persone.flatMap(_.disponibilita.toList)
      InstanceDisponibilita.operation().selectFilter(x=>x.settimana===week || x.id.inSet(previousWeek)).flatMap {
        case Some(availability) => updateAvailability(availability,week,persone)
      }: @nowarn
      case None => Future.successful(List.empty)
    }
  }

  private def updateAvailability(availability:List[Disponibilita],week:Int,persone:List[Persona]): Future[List[Option[Int]]] ={
    val (actualAvailability, previousAvailabiliti) = availability.partition(_.settimana==week)
    @scala.annotation.tailrec
    def _updateAvailability(persona: List[Persona], previousAvailability:List[Disponibilita]): Future[List[Option[Int]]] = previousAvailability match {
      case ::(availability, next) if persona.exists(_.disponibilita.equals(availability.idDisponibilita))=>
      val newPerson  = persona.map{
        case person if person.disponibilita.equals(availability.idDisponibilita)=>
          actualAvailability.find(actual => ((actual.giorno1 == availability.giorno1 && actual.giorno2 == availability.giorno2)
            ||(actual.giorno2 == availability.giorno1 && actual.giorno1 == availability.giorno2))) match {
            case Some(actualAvailabili) => person.copy(disponibilita = actualAvailabili.idDisponibilita)
            case None =>person
          }
        case person => person
      }
        _updateAvailability(newPerson,next)
      case ::(_,next)=> _updateAvailability(persona,next)
      case Nil =>updateAvailabilityDriver(persona)
    }
    _updateAvailability(persone,previousAvailabiliti)
  }
  private def updateAvailabilityDriver(persona: List[Persona]): Future[List[Option[Int]]] ={
    Future.sequence(persona.map(person=>PersonaOperation.update(person).collect {
      case Some(StatusCodes.SUCCES_CODE) =>Some(StatusCodes.SUCCES_CODE)
      case _ =>None
    }))
  }
}
