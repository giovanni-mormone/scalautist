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
  it should "Return a SuccessCode when inserting a good richiesta" in {
    val richiesta:Future[Option[Int]] = RichiestaTeoricaOperation.saveRichiestaTeorica(baseGoodRequest.request,baseGoodRequest.days)
    richiesta map {richiestaResult => assert(richiestaResult.head == StatusCodes.SUCCES_CODE)}
  }

}
