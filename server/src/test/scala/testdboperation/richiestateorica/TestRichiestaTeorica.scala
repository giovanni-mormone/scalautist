package testdboperation.richiestateorica

import dbfactory.operation.RichiestaTeoricaOperation
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.StartServerRichiesta
import RichiestaTeoricaTestValues._
import messagecodes.StatusCodes

import scala.concurrent.Future
class TestRichiestaTeorica extends AsyncFlatSpec with BeforeAndAfterEach with StartServerRichiesta{

  behavior of "SaveRichiesta"
  it should "Return a ERROR_CODE1 when the start dates are not a start of a month" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(startNotInitMonth.request,startNotInitMonth.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.ERROR_CODE1)}
  }
  it should "Return a ERROR_CODE1 when the end dates are not an end of a month" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(endNotEndMonth.request,endNotEndMonth.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.ERROR_CODE1)}
  }
  it should "Return a ERROR_CODE1 when the end dates are before the start dates" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(startAfterEnd.request,startAfterEnd.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.ERROR_CODE1)}
  }
  it should "Return a ERROR_CODE1 when the period dates of the requests are not the same" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(notSamePeriod.request,notSamePeriod.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.ERROR_CODE1)}
  }
  it should "Return a ERROR_CODE2 when the request to insert are Empty" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(noneRequest.request,noneRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.ERROR_CODE2)}
  }
  it should "Return a ERROR_CODE9 when there is some terminale duplicated in the insert request" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(duplicatedTerminal.request,duplicatedTerminal.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.ERROR_CODE9)}
  }
  it should "Return a SuccessCode when inserting a good richiesta" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(baseGoodRequest.request,baseGoodRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that must split the requests already present" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(splitStartRequest.request,splitStartRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that must split the request already present " in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(splitEndRequest.request,splitEndRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that overrides the request included in it" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(overrideRequest.request,overrideRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that updates requests with the same date" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(updateRequest.request,updateRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that splits the request already present and inserts one new request" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(splitAndInsert.request,splitAndInsert.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that splits a part of requests already present in the db" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(splitSomeRequest.request,splitSomeRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that splits one request and updates the others" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(updateAndSplitRequest.request,updateAndSplitRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }
  it should "Return a SuccessCode when inserting a good richiesta that splits requests, add a new request and add new days in the db" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(insertSplitAndAddDays.request,insertSplitAndAddDays.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }

}
