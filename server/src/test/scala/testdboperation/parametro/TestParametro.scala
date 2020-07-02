package testdboperation.parametro

import caseclass.CaseClassDB.Parametro
import caseclass.CaseClassHttpMessage.InfoAlgorithm
import dbfactory.operation.ParametroOperation
import messagecodes.StatusCodes
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.StartServer4

import scala.concurrent.Future

class TestParametro extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer4{
  import ParametroOperationTest._
  behavior of "Insert Parametro"

  it should "return Success_Code if save parameters algorithm have success" in {
    val parameters: Future[Option[Int]] = ParametroOperation.saveInfoAlgorithm(infoAlgorithm)
    parameters map { one =>
      assert(one.contains(StatusCodes.SUCCES_CODE))
    }
  }
  it should "return Success_Code if save parameters algorithm have success  although if you don't have day in week" in {
    val parameters: Future[Option[Int]] = ParametroOperation.saveInfoAlgorithm(infoAlgorithmWithoutGiornoInSettimana)
    parameters map { one =>
      assert(one.contains(StatusCodes.SUCCES_CODE))
    }
  }
  it should "return ErrorCode3 if infoAlgorithm not contains name for parameters" in {
    val parameters: Future[Option[Int]] = ParametroOperation.saveInfoAlgorithm(infoAlgorithmWithoutNameParameters)
    parameters map { one =>
      assert(one.contains(StatusCodes.ERROR_CODE3))
    }
  }
  it should "return ErrorCode3 if infoAlgorithm not contains zonaTerminale" in {
    val parameters: Future[Option[Int]] = ParametroOperation.saveInfoAlgorithm(infoAlgorithmWithoutZonaTerminale)
    parameters map { one =>
      assert(one.contains(StatusCodes.ERROR_CODE3))
    }
  }
  it should "return None if idParameter not exist" in {
    val parameters: Future[Option[InfoAlgorithm]] = ParametroOperation.getParameter(idParametersNotExist)
    parameters map { one =>
      assert(one.isEmpty)
    }
  }
  it should "return a Option[InfoAlgorithm] if idParameters Exist in database" in {
    val parameters: Future[Option[InfoAlgorithm]] = ParametroOperation.getParameter(idParameter)
    parameters map { one =>
      assert(one.isDefined)
    }
  }
  it should "return a Option[InfoAlgorithm] With giornoInSettimana length 3" in {
    val parameters: Future[Option[InfoAlgorithm]] = ParametroOperation.getParameter(idParameter)
    parameters map { one =>
      assert(one.head.giornoInSettimana.toList.flatten.length==3)
    }
  }
  it should "return a Option[InfoAlgorithm] With ZonaTerminale length 2" in {
    val parameters: Future[Option[InfoAlgorithm]] = ParametroOperation.getParameter(idParameter)
    parameters map { one =>
      assert(one.head.zonaTerminale.length==2)
    }
  }
  it should "return a Option[List[Parameter]] with length 2" in {
    val parameters: Future[Option[List[Parametro]]] = ParametroOperation.selectAll
    parameters map { one =>
      assert(one.head.length==2)
    }
  }
}
