package dbfactory.operation
import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.{Ferie, InfoAbsenceOnDay}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePersona, InstanceRichiestaTeorica, InstanceRisultato, InstanceTerminale, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table._
import messagecodes.StatusCodes
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/** @author Giovanni Mormone, Fabian Aspee
 *  Trait which allows to perform operations on the assenze table.
 */
trait AssenzaOperation extends OperationCrud[Assenza]{
  /**
   * Returns a list of [[caseclass.CaseClassHttpMessage.Ferie]] for the year provided as input for all the conducenti
   * in the DB.
   * @param data
   *             The year of the remainig feries to get
   */
  def getAllFerie(data: Int): Future[Option[List[Ferie]]]

  /**
   * Returns the list of assenze for the person in the year provided
   * @param year
   *            The year that the assenze must have
   * @param idPersona
   *                  the persona which assenze are asked
   * @return
   *         A list of [[Assenza]] or None if no Assenze are in the DB.
   */
  def getAssenzeInYearForPerson(year: Int, idPersona: Int): Future[Option[List[Assenza]]]

  /**
   *  Method that search all driver that contains absence and return his terminal and shift
   *  and if the request for this shift in this terminal is > of driver available, that is, those who are working
   *  (which appear in the risultato table with one shift and do not exist in assenza table for the date sought),
   *  then this terminal and shift will be within the return message, in the case that request< for driver
   *  available, then this terminal and shift not will be within the return message.
   *  a driver may be have a shift with first condition and another shift with second condition,
   *  so one shift of the driver will be sent within return message and the another shift not
   *
   * @param date data to which the number of the week in the year must be extracted
   * @return Future of Option of list of infoAbsenceOnDay [[caseclass.CaseClassHttpMessage.InfoAbsenceOnDay]]
   */
  def getAllAbsence(date:Date):Future[Option[List[InfoAbsenceOnDay]]]

  /**
   * Method which allow update absence of the driver with another driver that contains availability.
   * may be verified if driver that are received, really contains availability in this idRisultato
   *
   * @param idRisultato represent point where absence exist
   * @param idPersona person that replace the driver absenced
   * @return Future of Option with status code of operation
   *         [[messagecodes.StatusCodes.SUCCES_CODE]] if operation of update finish with success
   *         [[messagecodes.StatusCodes.ERROR_CODE1]] if idRisultato not exist
   *         [[messagecodes.StatusCodes.ERROR_CODE2]] if idPersona not exist
   */
  def updateAbsence(idRisultato:Int,idPersona:Int):Future[Option[Int]]
}

object AssenzaOperation extends AssenzaOperation{

  import utils.DateConverter._
  private case class JoinResult(dataInizio:Date,dataFine:Date,idPersona:Int,nomePersona:String,cognomePersona:String)

  private object JoinResult{
    implicit def tuple5ToJoinResult(tuple:(Date,Date,Int,String,String)):JoinResult =
      JoinResult.apply _ tupled tuple
  }
  //caricare da qualche parte?
  private val GIORNI_FERIE_ANNUI: Int = 35
  //anche questo si può caricare da qualche parte
  private val CODICE_CONDUCENTE: Int = 3
  //Terminal not exist
  private val NOT_TERMINAL:Int=0
  //countAvailableForShiftOnDay not contain idRisultato
  private val NOT_RISULTATO_ID:Int=0
  //countAvailableForShiftOnDay not contain idTurno
  private val NOT_RISULTATO_TURNO_ID:Int=0
  //countAvailableForShiftOnDay not contain idPersone
  private val NOT_RISULTATO_PERSONE_ID:Int=0

  override def getAllFerie(data: Int):Future[Option[List[Ferie]]] = {
    val nextYear = dateFromYear(data+1)
    val currentYear = dateFromYear(data)
    constructFerie(currentYear,nextYear)
  }

    /**
     *
     * @param element case class that represent instance of the table in database
     * @return
     *         Future of Int that represent status of operation, returning the id of the assenza inserted or an error code:
     *            [[messagecodes.StatusCodes.ERROR_CODE1]] if the persona alredy has an assenza in the period provided.
     *            [[messagecodes.StatusCodes.ERROR_CODE2]]  if the days between the given day are > of [[GIORNI_FERIE_ANNUI]]
     *            [[messagecodes.StatusCodes.ERROR_CODE3]] if the dates given in input are not of the same year.
     *            [[messagecodes.StatusCodes.ERROR_CODE4]] if the start date is after the end date.
     *            [[messagecodes.StatusCodes.ERROR_CODE5]] if the days of the assenza to insert are greater than the remaninig day of assenza for the persona.
     *
     */
  override def insert(element: Assenza): Future[Option[Int]] ={
    for{
      absence <-InstanceAssenza.operation().selectFilter(f => f.dataInizio <= element.dataFine && f.dataFine >= element.dataInizio && f.personaId === element.personaId)
      result <- if (absence.isDefined) Future.successful(Some(StatusCodes.ERROR_CODE1)) else for(x <- insertPriv(element)) yield x
    }yield result
  }

  override def getAssenzeInYearForPerson(year: Int, idPersona: Int): Future[Option[List[Assenza]]] = {
    InstanceAssenza.operation().selectFilter(f => f.personaId === idPersona && f.dataInizio >= dateFromYear(year) && f.dataFine <= dateFromYear(year+1))
  }

  private def insertPriv(element: Assenza): Future[Option[Int]] = element match {
    case Assenza(_,start,end,false,_) if computeDaysBetweenDates(start,end) > GIORNI_FERIE_ANNUI => Future.successful(Some(StatusCodes.ERROR_CODE2))
    case Assenza(_,start,end,false,_) if notSameYear(start,end) => Future.successful(Some(StatusCodes.ERROR_CODE3))
    case Assenza(_,start,end,false,_) if start.compareTo(end) > 0 => Future.successful(Some(StatusCodes.ERROR_CODE4))
    case Assenza(id,start,end,false,_) => tryInsert(Assenza(id,start,end,malattia = false))
    case _ => super.insert(element)
  }

  private def tryInsert(assenza:Assenza): Future[Option[Int]] = {
    val addDays:(Int,Assenza) => Int = (x,assenz) => x + computeDaysBetweenDates(assenz.dataInizio,assenz.dataFine)

    InstanceAssenza.operation()
      .selectFilter(filter => filter.personaId === assenza.personaId && filter.malattia === false)
      .map(_.map(_.foldLeft(0)(addDays))).map(days => days.exists(_ + computeDaysBetweenDates(assenza.dataInizio,assenza.dataFine) > GIORNI_FERIE_ANNUI))
      .flatMap(outOfDays =>if(outOfDays) Future.successful(Option(StatusCodes.ERROR_CODE5)) else super.insert(assenza))
  }

  /**
   * Constructs the list of ferie in one year. First construct the list of ferie for all the conducenti as if they have
   * full days of ferie remaining; after that select all the assenze in the given year and reduces the output to the Ferie
   * for each conducente; lastly substitutes each Ferie in the starting List of ferie with the computed one, if the conducente
   * did days of assenza, else it remains the basic Ferie.
   */
  private def constructFerie(currentYear: Date, nextYear: Date): Future[Option[List[Ferie]]] ={
    val filterJoin = for{
      (persona,assenza) <- PersonaTableQuery.tableQuery() join AssenzaTableQuery.tableQuery() on (_.id === _.personaId)
      if assenza.dataFine < nextYear && assenza.dataInizio >= currentYear && !assenza.malattia
    }yield (assenza.dataInizio,assenza.dataFine,persona.id,persona.nome,persona.cognome)
    val ferieReduction: (Ferie,Ferie) => Ferie = (x,y) => Ferie(x.idPersona,x.nomeCognome,x.giorniVacanza + y.giorniVacanza)
    val joinResultToFerie: JoinResult => Ferie = join =>
      Ferie(join.idPersona,join.nomePersona.concat(join.cognomePersona).concat(join.idPersona.toString),computeDaysBetweenDates(join.dataInizio,join.dataFine))
    val setFerieDays: (List[Ferie],Ferie) => Int = (value, startingFerie)=> GIORNI_FERIE_ANNUI - value.find(_.idPersona == startingFerie.idPersona).getOrElse(Ferie(0,"",0)).giorniVacanza

    startingPersoneFerie()
      .flatMap(ferie => {
        InstanceAssenza.operation()
          .execJoin(filterJoin)
          .map(_.map(ass => ass.map(x => joinResultToFerie(x)).groupBy(_.idPersona).values.map(_.reduce(ferieReduction)).toList))
          .collect{
            case None => ferie
            case Some(value) => ferie.map(_.map(startingFerie => Ferie(startingFerie.idPersona,startingFerie.nomeCognome, setFerieDays(value,startingFerie))))
          }
      })
  }

  /**
   * It constructs the list of ferie with default remaining days for all the conducenti.
   * None is returned if there is no conducente in the db.
   */
  private def startingPersoneFerie(): Future[Option[List[Ferie]]] = {
    //prende id-nome-cognome e li concatena
    val tupToNameSurname: ((Int,String,String)) => String = x => x._2.concat(x._3).concat(x._1.toString)
    InstancePersona.operation()
      .execQueryFilter(field => (field.id,field.nome,field.cognome),_.ruolo === CODICE_CONDUCENTE)
      .map(_.map(_.map(x => Ferie(x._1,tupToNameSurname(x),GIORNI_FERIE_ANNUI))))
  }

  private def getTerminalAndTurno(persona: Option[List[(Int, Option[Int])]], risultato: Option[List[(Int, Int, Int)]]): Map[Int, List[Int]] = {
    mergeResultWithPersona(persona,risultato).map(values=> values._1 match {
      case Some(value) => value-> values._2
      case None =>NOT_TERMINAL-> values._2
    }).map(values=>values._1->values._2.flatMap(_.toList)).filter(_._1!=NOT_TERMINAL)
  }

  override def getAllAbsence(date: Date): Future[Option[List[InfoAbsenceOnDay]]] =
    for{
      resultJoin <- joinAbsencePerson(date)
      risultato<-queryFilterForRisultatoTable(date,resultJoin)
      resultJoinWithRisult<-availableGreaterThanRequested(date,getTerminalAndTurno(resultJoin,risultato))
      turnoResult<-queryToTurnoWithRisultatoTable(resultJoinWithRisult)
      personTerminale <-queryRisultatoPerson(risultato)
      terminaleResult<- queryToTerminale(personTerminale.map(_.map(_._2)))
      mergeResultTurno<-mergeResultWithTurno(risultato.map(_.map(value=>(value._1,value._2))),turnoResult)
      mergeResultTerminal<-mergeResultTerminal(risultato.map(_
        .map(value=>(value._1,value._3))),terminaleResult,personTerminale)
    }yield createListInfoAbsence(mergeResultTurno,mergeResultTerminal)

  private def joinAbsencePerson(date:Date): Future[Option[List[(Int, Option[Int])]]] ={
    val queryJoin = for {
      assenza<- AssenzaTableQuery.tableQuery()
      persona<- PersonaTableQuery.tableQuery()
      if assenza.personaId===persona.id && assenza.dataInizio<=date && assenza.dataFine>=date
    } yield (persona.id,persona.terminaleId)
    executor(queryJoin)
  }

  private def queryFilterForRisultatoTable(date: Date,join:Option[List[(Int, Option[Int])]]): Future[Option[List[(Int, Int, Int)]]] ={
    InstanceRisultato.operation()
      .execQueryFilter(risultato=>(risultato.id,risultato.turnoId,risultato.personeId), risultato=>risultato.data ===date && risultato.personeId
        .inSet(join match {
          case Some(value) => value.map(_._1).distinct
          case None => None
        }))
  }

  private def availableGreaterThanRequested(date:Date,terminalAndTurno:Map[Int, List[Int]]): Future[Option[List[(Int, Int, Int)]]] = {
    terminalAndTurno.flatMap(values=>
      values._2.map(idTurno=>countAvailableForShiftOnDay(values._1,date,idTurno).flatMap {
          case Some(avaiForDay) => functionJoin(date,idTurno,values._1,avaiForDay)
          case None => Future.successful(None)
        })
    ).toList.foldLeft(Future.successful(Option(List((NOT_RISULTATO_ID,NOT_RISULTATO_TURNO_ID,NOT_RISULTATO_PERSONE_ID))))) {
      case (defaulFuture,future)=>defaulFuture.zip(future).map {
        case (option, option1) => Some((option1.toList.flatten::option.toList.flatten::List.empty).flatten)
      }
    }.collect {
      case Some(List((NOT_RISULTATO_ID,NOT_RISULTATO_TURNO_ID,NOT_RISULTATO_PERSONE_ID))) => None
      case Some(value)=>Some(value)
      case None =>None
    }
  }

  private def functionJoin(date:Date,idTurno:Int,values:Int,avaiForDay:List[(Int, Int, Int)])=
    joinTeoricRequestedWithRequestedWithGiorno(date,idTurno,values).collect {
      case Some(resultJoin) if resultJoin.exists(quantita => quantita > Some(avaiForDay).toList.flatten.length) => Some(avaiForDay)
      case Some(resultJoin) if resultJoin.exists(quantita => quantita < Some(avaiForDay).toList.flatten.length) =>
        resultJoin.map(result => result - avaiForDay.map(_._2).length) match {
          case List(a) => Some(avaiForDay.dropRight(a))
          case Nil => None
        }
      case None =>Some(avaiForDay)
    }

  private def countAvailableForShiftOnDay(idTerminal:Int,date:Date,idTurno:Int): Future[Option[List[(Int, Int, Int)]]] ={
    val queryJoin = for{
      persona<-PersonaTableQuery.tableQuery()
      risultato<-RisultatoTableQuery.tableQuery()
      if(persona.terminaleId===idTerminal && risultato.personeId===persona.id && risultato.turnoId===idTurno
        && risultato.data===date)
    }yield (risultato.id,risultato.turnoId,risultato.personeId)
    executor(queryJoin)
  }

  private def joinTeoricRequestedWithRequestedWithGiorno(date:Date,idTurno:Int,idTerminal:Int): Future[Option[List[Int]]] ={
    val queryJoin = for {
      richTeorica<- RichiestaTeoricaTableQuery.tableQuery()
      richiesta<- RichiestaTableQuery.tableQuery()
      giorno <- GiornoTableQuery.tableQuery()
      if(richTeorica.dataInizio<=date && richTeorica.dataFine>=date && richTeorica.terminaleId===idTerminal
        && richiesta.turnoId===idTurno && richiesta.giornoId===giorno.id && giorno.idGiornoSettimana===getDayNumber(date))
    } yield giorno.quantita
    executor(queryJoin)
  }

  private def executor[A,B](queryJoin:Query[A,B,Seq]):Future[Option[List[B]]]={
    InstanceRichiestaTeorica.operation().execJoin(queryJoin).collect {
      case Some(value) => Some(value)
      case None =>None
    }
  }

  private def queryToTurnoWithRisultatoTable(risultato:Option[List[(Int, Int, Int)]])={
    risultato match{
      case Some(value) => InstanceTurno.operation()
        .execQueryFilter(turno=>(turno.id,turno.nomeTurno),turno=>turno.id.inSet(value.map(_._2)))
      case None =>Future.successful(None)
    }
  }

  private def queryRisultatoPerson(risultato:Option[List[(Int, Int, Int)]]): Future[Option[List[(Int, Option[Int])]]] ={
    risultato match{
      case Some(value) => InstancePersona.operation()
        .execQueryFilter(persona=>(persona.id,persona.terminaleId),persona=>persona.id.inSet(value.map(_._3)))
      case None =>Future.successful(None)
    }
  }

  private def queryToTerminale(personTerminale:Option[List[Option[Int]]]): Future[Option[List[(Int, String)]]] ={
    InstanceTerminale.operation()
      .execQueryFilter(terminale=>(terminale.id,terminale.nomeTerminale),terminale=>terminale.id
        .inSet(returnListIdTerminal(personTerminale)))
  }

  private def returnListIdTerminal(result: Option[List[Option[Int]]]):List[Int]=
   result.map(_.filter(_.isDefined).collect { case Some(value) => value}).getOrElse(List.empty)

  private def mergeResultWithTurno(result:Option[List[(Int,Int)]],turnoResult: Option[List[(Int, String)]]): Future[Option[Map[Int, Option[(Int, String)]]]] ={
     val merge=result.zip(turnoResult).map(values=>values._1.map(result=>{
        result._1->values._2.find(turno=>turno._1==result._2)
     }).toMap)
    Future.successful(merge)
  }

  private def mergeResultWithPersona(persona: Option[List[(Int, Option[Int])]], risultato: Option[List[(Int, Int, Int)]]): Map[Option[Int], List[Option[Int]]] ={
    persona.toList.flatten.map(values=>values._2->risultato.toList.flatten.find(_._3==values._1)).filter(_._2.isDefined)
      .map(values=>values._1->values._2.map(_._2)).groupMap(_._1)(_._2).filter(_._1.isDefined)
  }

  private def mergeResultTerminal(risultato: Option[List[(Int,Int)]],terminaleResult: Option[List[(Int,String)]],persona:Option[List[(Int,Option[Int])]]): Future[Option[Map[Int, Option[(Int, String)]]]] ={
    val merge = risultato.zip(mergeTerminalWithPersona(terminaleResult,persona)).map(result=>result._1
      .map(value=>value._1->result._2.getOrElse(value._2,None)).toMap)
    Future.successful(merge)
  }

  private def mergeTerminalWithPersona(terminaleResult: Option[List[(Int,String)]],persona:Option[List[(Int,Option[Int])]]): Option[Map[Int, Option[(Int, String)]]] =
    persona.zip(terminaleResult).map(values=>values._1.map(result=>{
      result._1->values._2.find(terminal=>result._2.contains(terminal._1))
    }).toMap)

  private def createListInfoAbsence(mergeResultTurno: Option[Map[Int, Option[(Int, String)]]], mergeResultTerminal: Option[Map[Int, Option[(Int, String)]]]): Option[List[InfoAbsenceOnDay]] = {
     val merged = mergeResultTerminal.zip(mergeResultTurno).map(values=>(values._1.toList++values._2).groupMap(_._1)(_._2))
     merged.map(_.map(values=>convertListToTuple(values._1,values._2).toList).toList.flatten)
  }

  private def convertListToTuple(id:Int,values:List[Option[(Int,String)]]): Option[InfoAbsenceOnDay] = (id,values) match {
    case (id:Int,List(a,b)) => a.zip(b).map(result=>InfoAbsenceOnDay(result._1._2,result._2._2,result._1._1,result._2._1,id))
    case (_,Nil) => None
  }

  override def updateAbsence(idRisultato:Int,idPersona:Int):Future[Option[Int]]={
      val result = InstanceRisultato.operation().execQueryFilter(risultato=>risultato.id,
        risultato=>risultato.id===idRisultato)
      val persona = InstancePersona.operation().execQueryFilter(persona=>persona.id,persona=>persona.id===idPersona)
      result.zip(persona).flatMap{
        case (_, None) => Future.successful(Some(StatusCodes.ERROR_CODE2))
        case (None, _) => Future.successful(Some(StatusCodes.ERROR_CODE1))
        case (_, _) => update(idRisultato,idPersona)
      }
  }

  private def update(idRisultato:Int,idPersona:Int)={
    InstanceRisultato.operation().execQueryUpdate(risultato=>risultato.personeId,risultato=>risultato.id===idRisultato,
      idPersona).collect {
      case Some(value) => Some(value)
      case None =>None
    }
  }

}
object tryAbsence extends App{
  //idRisultato:Int, idTemrinale:Int, idTurno:Int
  val local = LocalDate.of(2020,6,18)

  def driveravai():Unit={
    AssenzaOperation.getAllAbsence(Date.valueOf(local)).onComplete {
      case Failure(exception) => println("PRIMO FAIL :C",exception)
      case Success(value) =>println("STO QUI ssas",value); value.toList.flatten.map(result=>DisponibilitaOperation
        .allDriverWithAvailabilityForADate(result.idRisultato,result.idTerminale,result.idTurno).onComplete {
        case Failure(exception) => println("SECONDO FAIL :(",exception)
        case Success(values) =>println("STO QUI",result); println("STOQUI 2"); println(values);
          values match {
            case Some(value) =>value.filter(_.idPersona==12).map(persona=>AssenzaOperation.updateAbsence(result.idRisultato,persona.idPersona).onComplete {
              case Failure(exception) => println(exception, "NOT AGGIORNATO")
              case Success(value) =>println(value, "AGGIORNATO");AssenzaOperation.getAllAbsence(Date.valueOf(local)).onComplete {
                case Failure(exception) => println("PRIMO FAIL :C",exception)
                case Success(value) =>println("NEW ABSENCE",value);}
            })
            case None => println("SOY NONE")
          }
      })
    }
  }
  /*
  (STO QUI ssas,Some(List(InfoAbsenceOnDay(Florida,Tarda Serata,3,5,140),
  InfoAbsenceOnDay(Cansas,Tarda Mattinata,1,2,79),
  InfoAbsenceOnDay(Florida,Sera,3,4,1957), InfoAbsenceOnDay(Casablanca,Sera,2,4,818))))

  (STO QUI ssas,Some(List(InfoAbsenceOnDay(Florida,Notte,3,6,574), InfoAbsenceOnDay(Cansas,Tarda Mattinata,1,2,79),
  InfoAbsenceOnDay(Florida,Sera,3,4,1957), InfoAbsenceOnDay(Casablanca,Sera,2,4,818))))
   */
  AssenzaOperation.getAllAbsence(Date.valueOf(local)).onComplete {
    case Failure(exception) => println("PRIMO FAIL :C", exception)
    case Success(value) => println("STO QUI ssas", value);
  }
  while(true){}
}