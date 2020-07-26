package testdboperation.assenza

import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Ferie
import dbfactory.operation.AssenzaOperation
import messagecodes.StatusCodes
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import testdboperation.persona.AssenzaOperationTestValues
import utils.StartServer

import scala.concurrent.Future

class TestAssenza extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer{

  import AssenzaOperationTestValues._

  behavior of "GetAllFerie"
  it should "return the given the list when tries to get all ferie for that year" in {
    val ferie: Future[Option[List[Ferie]]] = AssenzaOperation.getAllFerie(2020)
    ferie map {list =>println(list); assert(list.head.equals(remainingFerieList))}
  }
  it should "return the given the list when tries to get all ferie for next year" in {
    val ferie: Future[Option[List[Ferie]]] = AssenzaOperation.getAllFerie(2021)
    ferie map {list => assert(list.head.equals(remainingFerieListNext))}
  }
  behavior of "Insert"
  it should "return ERROR_CODE1 when adding an assenza when the period has already an assenza" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(alreadyAssenzaInPeriod)
    assenza map { ass => assert(ass.head == StatusCodes.ERROR_CODE1) }
  }
  it should "return ERROR_CODE2 when adding an assenza that is greater than the max possible" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(tooLongFerie)
    assenza map { ass => assert(ass.head == StatusCodes.ERROR_CODE2) }
  }
  it should "return an int > 0 when adding an assenza that is greater than the max possible but is malattia" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(malattia)
    assenza map { ass => assert(ass.head >0) }
  }
  it should "return an ERROR_CODE3 when adding an ferie between 2 years" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(betweenYears)
    assenza map { ass => assert(ass.head == StatusCodes.ERROR_CODE3)}
  }
  it should "return an int >0 when adding good malattie between 2 years" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(malattiaBetweenYears)
    assenza map { ass => assert(ass.head > 0)}
  }
  it should "return an ERROR_CODE4 when adding an assenza that starts after the end" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(startAfterEnd)
    assenza map { ass => assert(ass.head == StatusCodes.ERROR_CODE4) }
  }
  it should "return an int >0 when adding an assenza that starts the same day of the end" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(startSameAsEnd)
    assenza map { ass => assert(ass.head > 0) }
  }
  it should "return an ERROR_CODE5 when adding ferie days greater than remaining ferie for the year" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(notSoManyFerie)
    assenza map { ass => assert(ass.head == StatusCodes.ERROR_CODE5)}
  }
  it should "return an int > 0 when adding an assenza good ferie" in {
    val assenza: Future[Option[Int]] = AssenzaOperation.insert(goodFerie)
    assenza map { ass => assert(ass.head >0) }
  }
  behavior of "getAssenzeInYearForPerson"
  it should "return the provided list of Assenze when getting it for a person in a year " in {
    val assenza: Future[Option[List[Assenza]]] = AssenzaOperation.getAssenzeInYearForPerson(2020,2)
    assenza map { ass => assert(ass.head.equals(assenzaListId2)) }
  }
  it should "return the provided None if there are no assenze for the person or year " in {
    val assenza: Future[Option[List[Assenza]]] = AssenzaOperation.getAssenzeInYearForPerson(2020,12)
    assenza map { ass => assert(ass.isEmpty) }
  }
}
