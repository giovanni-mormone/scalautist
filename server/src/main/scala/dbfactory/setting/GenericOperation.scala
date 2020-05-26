package dbfactory.setting

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLServerProfile.api._
import scala.concurrent.Future

import scala.reflect.runtime.{universe => runtime}
trait GenericOperation[T,C <: GenericTable[T]] extends GenericTableQuery[T,C] with DB[C,T]  {
  def selectFilter(f:C=>Rep[Boolean]): Future[List[T]]
  def execJoin[A,B](f:Query[A,B,Seq]): Future[List[B]]
}
object GenericOperation{
  case class Operation[T,C<: GenericTable[T]:runtime.TypeTag]() extends GenericOperation[T,C] {
    override def selectFilter(f:C=>Rep[Boolean]): Future[List[T]] = super.run(tableDB().withFilter(f).result).map(result=>result.toList)
    override def execJoin[A,B](f:Query[A,B,Seq]): Future[List[B]] = super.run(f.result).map(result=>result.toList)
  }
}