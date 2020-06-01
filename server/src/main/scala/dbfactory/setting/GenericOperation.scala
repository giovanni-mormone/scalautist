package dbfactory.setting

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{FlatShapeLevel, Shape}

import scala.concurrent.Future
import scala.reflect.runtime.{universe => runtime}

/**
 * @author Fabian Aspée Encina
 *  Trait which allow extra operation for any table in database, this trait it's like a extension of [[dbfactory.setting.GenericCRUD]]
 * @tparam T case class that represent instance in database [[caseclass.CaseClassDB]]
 * @tparam C class that represent the table in database, allow make query in database [[dbfactory.table]]
 */
trait GenericOperation[T,C <: GenericTable[T]] extends GenericTableQuery[T,C] with DB[C,T]  {
  /**
   *  Generic SelectFilter which allow select any record in any table in database indicating a filter operation
   * @param f function that define filter operation e.g: x=>x.nome=="Fran" && x.cognome=="roma" where x is PersonaTableRep
   * @return all record that satisfies  filter operation
   */
  def selectFilter(f:C=>Rep[Boolean]): Future[List[T]]

  /**
   *  Generic method por execute any join created in this system
   * @param f is a Query which is the result of apply for yield in table of the database [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam A represent the field we want return in mod Tuple(Rep[Int],Rep[String],etc)
   * @tparam B represent the field we want return in mod Tuple(Int,String,etc)
   * @return Tuple the all field that we have first selected
   */
  def execJoin[A,B](f:Query[A,B,Seq]): Future[List[B]]

  /**
   *  Generic method which allow execute the any query in the database in the following mode Select [any field] from table
   *
   * @param q     query which represent the field we want select in the table in the database
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *              `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *              fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return  List the type A which the tuple lenght C
   */
  def execQueryAll[F, G, A](q:C=>F)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]):Future[List[A]]

  /**
   * Generic Operation which enable make operation of type select, in this method you have send field that you want
   * select.
   * @param q     query which represent the field we want select in the table in the database
   * @param id    identifies instance into database we want selected
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *             `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *             fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return    Option the type A which the tuple lenght C
   */
  def execQuery[F, G, A](q:C=>F,id:Int)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]):Future[Option[A]]

  /**
   * Generic Operation which enable make operation of type select, in this method you have send field that you want
   * select and the filter operation
   * @param q    query which represent the field we want select in the table in the database
   * @param f    function which enable make select operation with filter
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *             `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *             fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return    List of case class that satisfies condition f
   */
  def execQueryFilter[F, G, A](q:C=>F,f:C=>Rep[Boolean])(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[List[A]]

  /**
   * Generic Operation which enable make operation of type update, in this method you have send field that you want
   * select for make update, the filter operation and the new values for this fields
   * @param q  query which represent the field we want select for make update operation in the table in the database
   * @param f  function which enable make select operation with filter
   * @param u  tuple that contains new field that make update into old field
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *             `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *             fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return Future of Int that represent status operation, if status is == 1 so ok
   */
  def execQueryUpdate[F, G, A](q:C=>F,f:C=>Rep[Boolean],u:A)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Int]
}
object GenericOperation{
  case class Operation[T,C<: GenericTable[T]:runtime.TypeTag]() extends GenericOperation[T,C] {
    override def selectFilter(f:C=>Rep[Boolean]): Future[List[T]] = super.run(tableDB().withFilter(f).result).map(_.toList)
    override def execJoin[A,B](f:Query[A,B,Seq]): Future[List[B]] = super.run(f.result).map(_.toList)
    override def execQueryAll[F, G, A](q:C=>F)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[List[A]] =super.run(tableDB().map(q).result).map(_.toList)
    override def execQuery[F, G, A](q:C=>F,id:Int)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]):Future[Option[A]]= super.run(tableDB().filter(_.id===id).map(q).result.headOption)
    override def execQueryFilter[F, G, A](q:C=>F,f:C=>Rep[Boolean])(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[List[A]] = super.run(tableDB().withFilter(f).map(q).result).map(_.toList)
    override def execQueryUpdate[F, G, A](q:C=>F,f:C=>Rep[Boolean],u:A)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Int] = super.run(tableDB().withFilter(f).map(q).update(u))

  }
}
