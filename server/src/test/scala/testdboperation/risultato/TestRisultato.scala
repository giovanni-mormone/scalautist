package testdboperation.risultato

import caseclass.CaseClassHttpMessage.{InfoHome, InfoShift, ShiftDay}
import dbfactory.operation.RisultatoOperation
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.StartServer
import RisultatoOperationTestValue._

import scala.concurrent.Future

class TestRisultato extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer {

  behavior of "getTurniInDate"

  it should "return the daily work shift if employee is a driver" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idAutist, date)
    req map { one => assert(one.isDefined) }
  }

  it should "return empty the daily work shift if employee isn't a driver" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idManager, date)
    req map { one => assert(one.isDefined) }
  }

  it should "return empty work shift if employee doesn't exist" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idNobody, date)
    req map { one => assert(one.isDefined) }
  }

  behavior of "getTurniSettimanali"

  it should "return the daily work shift if employee is a driver" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idAutist, date)
    req map { one => assert(one.isDefined) }
  }

  it should "return empty the daily work shift if employee isn't a driver" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idManager, date)
    req map { one => assert(one.isDefined) }
  }

  it should "return empty work shift if employee doesn't exist" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idNobody, date)
    req map { one => assert(one.isDefined) }
  }
}
