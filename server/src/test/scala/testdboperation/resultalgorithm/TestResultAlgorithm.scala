package testdboperation.resultalgorithm

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassHttpMessage.ResultAlgorithm
import dbfactory.operation.RisultatoOperation
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.StartServer5

import scala.concurrent.Future

class TestResultAlgorithm extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer5{

  import ResultAlgorithmOperationTest._
  behavior of "Get Result Algorithm"
  it should "return None if terminal not exist" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalNotExist,dateI,dateF)
    parameters map { one =>
      assert(one._1.isEmpty)
    }
  }
  it should "return None if time frame not contains info" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminal,dateIWithoutInfo,dateFWithoutInfo)
    parameters map { one =>
      assert(one._1.isEmpty)
    }
  }
  it should "return List ResultAlgorithm length 14 for terminal 3 and time frame is 1/6/2020-31/7/2020" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalThree,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(one._1.head.length==14)
    }
  }
  it should "return List ResultAlgorithm length 3 for terminal 1 and time frame is 1/6/2020-31/7/2020" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminal,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(one._1.head.length==3)
    }
  }
  it should "return List ResultAlgorithm only with date exist in result init date is 1/6/2020 1/4/2020-31/7/2020" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalThree,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(one._1.head.head.dateIDateF.head.date.compareTo(Date.valueOf(LocalDate.of(2020,6,1)))==0)
    }
  }
  it should "return List ResultAlgorithm only with date exist in result end date is 31/7/2020 1/4/2020-31/7/2020" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalThree,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(one._1.head.head.dateIDateF.last.date.compareTo(Date.valueOf(LocalDate.of(2020,7,31)))==0)
    }
  }
  it should "if driver contains not active contract, so this element is Senza Contratto in time frame  1/6/2020-31/7/2020" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalThree,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(one._1.head.head.dateIDateF.head.turno.equals(SENZA_CONTRATTO))
    }
  }
  it should "if driver is full-time, then infoDates contains two shift" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalThree,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(Option(one._1.head.head.dateIDateF.head.turno).isDefined && one._1.head.head.dateIDateF.head.turno2.isDefined)
    }
  }
  it should "if driver is part-time, then infoDates contains one shift" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminal,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(Option(one._1.head.reverse.head.dateIDateF.head.turno).isDefined)
    }
  }

  it should "if driver contains assenza, so this time frame will be assenza" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalThree,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(one._1.head.head.dateIDateF.count(value => value.turno == ASSENZA)==12)
    }
  }

  it should "if driver is full-time, and not contains straordinario then infoDates contains one shift not defined" in {
    val parameters: Future[(Option[List[ResultAlgorithm]],List[Date])] = RisultatoOperation.getResultAlgorithm(idTerminalThree,dateIWithInfo,dateFWithInfo)
    parameters map { one =>
      assert(one._1.head.head.dateIDateF.head.straordinario.isEmpty)
    }
  }
}
