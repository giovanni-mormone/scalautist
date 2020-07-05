package model

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{GiornoInSettimana, Parametro, ZonaTerminale}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA, InfoAlgorithm, SettimanaN, SettimanaS}
import model.entity.ManagerModel

object ManagerOperationTest {
  val model:ManagerModel=ManagerModel()
  val date: Date = Date.valueOf("2020-06-15")
  val idTerminale: Int = 3
  val idTurno: Int = 5
  val idRisultato: Int = 291
  val idNewDriver: Int = 6
  //===============================RESULT ALGORITHM==========================================================
  val idTerminaleNotContainsResult: Int=1
  val dataINotContainsResult: Date= Date.valueOf(LocalDate.of(2020,1,1))
  val dataFNotContainsResult: Date= Date.valueOf(LocalDate.of(2020,2,28))
  val idTerminaleContainsResult: Int=1
  val dataIContainsResult: Date= Date.valueOf(LocalDate.of(2020,6,1))
  val dataFContainsResult: Date= Date.valueOf(LocalDate.of(2020,8,28))
  val idTerminaleContainsResult2: Int=3
  //===============================RESULT ALGORITHM==========================================================
  //=================================PARAMETERS ==============================================================
  val parametro:Parametro = Parametro(treSabato = true,"First Run")
  val parametroWithoutName:Parametro = Parametro(treSabato = true,"")
  val zonaTerminale:List[ZonaTerminale] = List(ZonaTerminale(1,1),ZonaTerminale(1,2))
  val giornoInSettimana:Option[List[GiornoInSettimana]] = Option(List(GiornoInSettimana(1,1,1),GiornoInSettimana(1,2,2),GiornoInSettimana(1,3,3)))
  val giornoInSettimanaWithBadIdRegola:Option[List[GiornoInSettimana]] = Option(List(GiornoInSettimana(1,1,4),GiornoInSettimana(1,2,2),GiornoInSettimana(1,3,3)))
  val infoAlgorithm: InfoAlgorithm = InfoAlgorithm(parametro,zonaTerminale,giornoInSettimana)
  val infoAlgorithmWithBadIdRegola: InfoAlgorithm = InfoAlgorithm(parametro,zonaTerminale,giornoInSettimanaWithBadIdRegola)
  val infoAlgorithmWithoutGiornoInSettimana: InfoAlgorithm = InfoAlgorithm(parametro,zonaTerminale,None)
  val infoAlgorithmWithoutZonaTerminale: InfoAlgorithm = InfoAlgorithm(parametro,List.empty,None)
  val infoAlgorithmWithoutNameParameters: InfoAlgorithm = InfoAlgorithm(parametroWithoutName,zonaTerminale,None)
  val parameterNotExist = 90
  val parametersExist=1
  val parametersWithoutGiornoInSettimana=3
  //=================================PARAMETERS ==============================================================
  //===============================EXECUTE ALGORITHM ========================================
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,6,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,7,31))
  val timeFrameInitError: Date =Date.valueOf(LocalDate.of(2020,6,1))
  val timeFrameFinishError: Date =Date.valueOf(LocalDate.of(2020,6,15))
  val terminals=List(1,2,3)
  val terminalWithoutTheoricRequest=List(4)
  val terminalsWithoutTheoricRequest=List(1,2,4)
  val terminalsNotExist=List(1,2,3,50)
  val firstDateGroup: Date =Date.valueOf(LocalDate.of(2020,6,10))
  val dateOutsideTimeFrameAlgorithm: Date =Date.valueOf(LocalDate.of(2020,3,10))
  val secondDateGroup: Date =Date.valueOf(LocalDate.of(2020,6,11))
  val regolaGroup = 2
  val gruppi = List(GruppoA(1,List(firstDateGroup,secondDateGroup),2))
  val gruppiWithOneDate = List(GruppoA(1,List(firstDateGroup,secondDateGroup),2),GruppoA(1,List(firstDateGroup),2))
  val gruppiWithRulerNotExist = List(GruppoA(1,List(firstDateGroup,secondDateGroup),20),GruppoA(1,List(firstDateGroup),2))
  val gruppiWithDateOutsideTimeFrame = List(GruppoA(1,List(dateOutsideTimeFrameAlgorithm,secondDateGroup),2),GruppoA(1,List(firstDateGroup,secondDateGroup),2))
  val normalWeek = List(SettimanaN(1,2,15,3),SettimanaN(2,2,15,2))
  val normalWeekWithIdDayGreater7 = List(SettimanaN(1,2,15,3),SettimanaN(9,2,15,2))
  val normalWeekWithRulerNotExist = List(SettimanaN(1,2,15,30),SettimanaN(2,2,15,2))
  val normalWeekWithShiftNotExist = List(SettimanaN(1,2,15,3),SettimanaN(2,50,15,2))
  val specialWeek = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,6,8))),SettimanaS(1,3,15,3,Date.valueOf(LocalDate.of(2020,6,8))))
  val specialWeekWithIdDayGreater7 = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,6,8))),SettimanaS(30,3,15,3,Date.valueOf(LocalDate.of(2020,6,8))))
  val specialWeekWithRulerNotExist = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,6,8))),SettimanaS(1,3,15,30,Date.valueOf(LocalDate.of(2020,6,8))))
  val specialWeekWithShiftNotExist = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,6,8))),SettimanaS(1,30,15,3,Date.valueOf(LocalDate.of(2020,6,8))))
  val specialWeekWithDateOutside  = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,6,8))),SettimanaS(1,3,15,3,Date.valueOf(LocalDate.of(2020,12,8))))
  val threeSaturday=false

  val algorithmExecute: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteWithGroupNone: AlgorithmExecute =algorithmExecute.copy(gruppo = None)
  val algorithmExecuteWithNormalWeekNone: AlgorithmExecute =algorithmExecute.copy(settimanaNormale = None)
  val algorithmExecuteWithSpecialWeekNone: AlgorithmExecute =algorithmExecute.copy(settimanaSpeciale = None)

  val algorithmExecuteDateError: AlgorithmExecute =
    AlgorithmExecute(timeFrameInitError,timeFrameFinishError,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteDateContrary: AlgorithmExecute =
    AlgorithmExecute(timeFrameFinishError,timeFrameInitError,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)

  val algorithmExecuteTerminalNotExist: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminalsNotExist,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteTerminalEmpty: AlgorithmExecute =algorithmExecuteTerminalNotExist.copy(idTerminal = List.empty)

  val algorithmExecuteGroupWithOneDate: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppiWithOneDate),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteGroupWithRulerNotExist: AlgorithmExecute =algorithmExecuteGroupWithOneDate.copy(gruppo = Some(gruppiWithRulerNotExist))
   val algorithmExecuteGroupContainDateOutsideTimeFrame: AlgorithmExecute =algorithmExecuteGroupWithOneDate.copy(gruppo = Some(gruppiWithDateOutsideTimeFrame))

  val algorithmExecuteNormalWeekWithIdDayGreater7: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeekWithIdDayGreater7),Some(specialWeek),threeSaturday)
  val algorithmExecuteNormalWeekWithRulerNotExist: AlgorithmExecute =algorithmExecuteNormalWeekWithIdDayGreater7.copy(settimanaNormale =Some(normalWeekWithRulerNotExist) )
   val algorithmExecuteNormalWeekWithShiftNotExist: AlgorithmExecute =algorithmExecuteNormalWeekWithIdDayGreater7.copy(settimanaNormale =Some(normalWeekWithShiftNotExist) )


  val algorithmExecuteSpecialWeekWithIdDayGreater7: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeek),Some(specialWeekWithIdDayGreater7),threeSaturday)
  val algorithmExecuteSpecialWeekWithRulerNotExist: AlgorithmExecute =algorithmExecuteSpecialWeekWithIdDayGreater7.copy(settimanaSpeciale = Some(specialWeekWithRulerNotExist))
   val algorithmExecuteSpecialWeekWithShiftNotExist: AlgorithmExecute =algorithmExecuteSpecialWeekWithIdDayGreater7.copy(settimanaSpeciale = Some(specialWeekWithShiftNotExist))
   val algorithmExecuteSpecialWeekWithDateOutside: AlgorithmExecute =algorithmExecuteSpecialWeekWithIdDayGreater7.copy(settimanaSpeciale = Some(specialWeekWithDateOutside))

  val algorithmExecuteTerminalWithoutTheoricRequest: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminalWithoutTheoricRequest,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteTerminalsWithoutTheoricRequest: AlgorithmExecute =algorithmExecuteTerminalWithoutTheoricRequest.copy(idTerminal = terminalsWithoutTheoricRequest)


}
