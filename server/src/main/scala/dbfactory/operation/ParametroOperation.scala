package dbfactory.operation

import caseclass.CaseClassDB.{GiornoInSettimana, Parametro}
import caseclass.CaseClassHttpMessage.InfoAlgorithm
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceGiornoInSettimana
import dbfactory.implicitOperation.OperationCrud
import slick.jdbc.SQLServerProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import dbfactory.util.Helper._
import messagecodes.StatusCodes
import scala.concurrent.Future

/** @author Fabian Aspee Encina
 *  Trait which allows to perform operations on the parametro table.
 */
trait ParametroOperation extends OperationCrud[Parametro]{

  /**
   * Method which allow save info of a parametrization, this info contains name parameters, saturday rule and
   * info for normal week that can be day in week and modification for teoric request, and ruler for this operation
   * that can be a integer or percent or media o moda
   * @param infoAlgorithm case class that represent info that user want save in database
   * @return Future of Option of Int that can be :
   *         None if delete all have a problem, this is a extreme case
   *          [[messagecodes.StatusCodes.ERROR_CODE1]] if exist error while insert Parametro
   *          [[messagecodes.StatusCodes.ERROR_CODE2]] if exist error while insert GiornoInSettimana
   *          [[messagecodes.StatusCodes.SUCCES_CODE]] if not exist error in operation
   */
  def saveInfoAlgorithm(infoAlgorithm: InfoAlgorithm):Future[Option[Int]]

  /**
   * Method that allow return info for specific parametrization this contains name parameter, saturday rule and all
   * info for normal week
   * @param idParameter represent a parametrization in Parametro Table
   * @return Future of Option of InfoAlgorithm
   */
  def getParameter(idParameter:Int):Future[Option[InfoAlgorithm]]
}
object ParametroOperation extends ParametroOperation {

  override def saveInfoAlgorithm(infoAlgorithm: InfoAlgorithm): Future[Option[Int]] = {
    for{
      idParametri <- insert(infoAlgorithm.parametro)
      result <- insertIdParametriToGiornoInSettimana(idParametri,infoAlgorithm.giornoInSettimana)
    }yield result
  }

  private def insertIdParametriToGiornoInSettimana(idParametri:Option[Int],giornoInSettimana: List[GiornoInSettimana]):Future[Option[Int]]={
    idParametri match {
      case Some(id) =>GiornoInSettimanaOperation.insertAll(giornoInSettimana.map(value=>value.copy(parametriId = id))).flatMap {
        case Some(_) => Future.successful(Some(StatusCodes.SUCCES_CODE))
        case None =>idParametri.map(result=>delete(result)).convert().collect {
          case Some(_) => Some(StatusCodes.ERROR_CODE2)
          case None =>None
        }
      }
      case None =>Future.successful(Some(StatusCodes.ERROR_CODE1))
    }
  }

  override def getParameter(idParameter: Int): Future[Option[InfoAlgorithm]] = {
    for{
      parameter<-select(idParameter)
      giornoInSettimana<-InstanceGiornoInSettimana.operation()
        .selectFilter(giornoInSettimana=>giornoInSettimana.parametriId===idParameter)
    }yield parameter match {
      case Some(value) => Some(InfoAlgorithm(value, giornoInSettimana match {
        case Some(value) => value
        case None =>Nil
      }))
      case None => None
    }
  }
}