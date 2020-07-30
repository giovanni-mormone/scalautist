package testdboperation.risultato

import caseclass.CaseClassHttpMessage.{InfoHome, InfoShift}
import dbfactory.operation.RisultatoOperation
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import testdboperation.risultato.RisultatoOperationTestValue._
import utils.StartServerTurni

import scala.concurrent.Future

class TestRisultato extends  AsyncFlatSpec with BeforeAndAfterEach with StartServerTurni {

  behavior of "getTurniInDate"

  it should "return the daily work shift if employee is a driver with disponibilità" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idAutist, date)
    req map { one => assert(one.fold(false)(info => info.disponibilita.isDefined && info.turno.nonEmpty)) }
  }

  it should "return empty disponibilita if disponibilita is not update to the week of the date" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idAutist, noAvailable)
    req map { one => assert(one.isDefined && one.head.turno.nonEmpty && one.head.disponibilita.isEmpty)}
  }

  it should "return no information if data are not update to the week of the date" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idAutist, noShift)
    req map { one => assert(one.isDefined && one.head.turno.isEmpty && one.head.disponibilita.isEmpty)}
  }

  it should "return nothing if is absent" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idAutist2, date2)
    req map { one => assert(one.isEmpty )}
  }

  it should "return empty the daily work shift if employee isn't a driver" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idManager, date)
    req map { one => assert(one.isEmpty)}
  }

  it should "return empty work shift if employee doesn't exist" in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idNobody, date)
    req map { one => assert(one.isEmpty) }
  }

  behavior of "getTurniSettimanali"

  it should "return the daily work shift if employee is a driver" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idAutist, date)
    req map { one => assert(if (one.isDefined) one.head.shiftDay.nonEmpty else false) }
  }

  it should "return no information if data is wrong" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idAutist, noShift)
    req map { one => assert(one.isDefined && one.head.shiftDay.isEmpty && one.head.disponibilita.isEmpty)}
  }

  it should "return empty disponibilita if disponibilita is not update to the week of the date" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idAutist, noAvailable)
    req map { one => assert(one.isDefined && one.head.shiftDay.nonEmpty && one.head.disponibilita.isEmpty)}
  }

  it should "return only assigned shift without absence days" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idAutist2, date2)
    req map { one => assert(one.isDefined && one.head.shiftDay.length == 2)}
  }

  it should "return empty daily work shift if employee isn't a driver" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idManager, date)
    req map { one => assert(one.isEmpty) }
  }

  it should "return empty work shift if employee doesn't exist" in {
    val req: Future[Option[InfoShift]] = RisultatoOperation.getTurniSettimanali(idNobody, date)
    req map { one => assert(one.isEmpty) }
  }
}
