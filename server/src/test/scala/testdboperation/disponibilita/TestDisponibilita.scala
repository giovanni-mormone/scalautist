package testdboperation.disponibilita

import dbfactory.operation.DisponibilitaOperation
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach}
import utils.StartServer3

import scala.concurrent.Future

class TestDisponibilita  extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer3 {
  import DisponibilitaOperationTestValues._
  behavior of "GetDisponibilita"
  it should "return None if driver is fisso" in {
    val getAvailability: Future[Option[List[String]]] = DisponibilitaOperation
      .getGiorniDisponibilita(idUserFisso,dateFisso)
    getAvailability map {allAvailability =>  assert(allAvailability.isEmpty)}
  }
  it should "return None if user have availability" in {
    val getAvailability: Future[Option[List[String]]] = DisponibilitaOperation.getGiorniDisponibilita(idUser,date)
    getAvailability map {allAvailability =>  assert(allAvailability.isEmpty)}
  }
  it should "return list length 0 if driver dont have availability in week" in {
    val getListAvailability: Future[Option[List[String]]] = DisponibilitaOperation
      .getGiorniDisponibilita(idUserWithoutAvailability,dateWithoutAvailability)
    getListAvailability map {allAvailability =>  assert(allAvailability.head.isEmpty)}
  }
  it should "return list equal a DisponibilitaOperationTestValues.days if driver dont have availability in week" in {
    val getListAvailability: Future[Option[List[String]]] = DisponibilitaOperation
      .getGiorniDisponibilita(idUserInitWeek,dateInitWeek)
    getListAvailability map {allAvailability =>assert(allAvailability.head.equals(days.toList))}
  }

  it should "return isEmpty because this driver have assenza from 18/6/2020 to 29/6/2020 and " +
    "the initial date is 18/6/2020" in {
    val getListAvailability: Future[Option[List[String]]] = DisponibilitaOperation
      .getGiorniDisponibilita(idUserWithoutAvailability,dateWithoutAvailability)
    getListAvailability map {allAvailability =>println(allAvailability.head);assert(allAvailability.isEmpty)}
  }
  it should "return list equal a DisponibilitaOperationTestValues.daysForAvailability with day that " +
    "driver can make in week" in {
    val getListAvailability: Future[Option[List[String]]] = DisponibilitaOperation
      .getGiorniDisponibilita(idUserWithAbsence,dateWithoutAvailability)
    getListAvailability map {allAvailability =>assert(allAvailability.head.equals(daysForAvailability.toList))}
  }
}
