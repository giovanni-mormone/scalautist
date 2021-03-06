package dbfactory.setting

import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{FlatShapeLevel, Shape}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.runtime.{universe => runtime}

/**
 * @author Fabian Aspée Encina
 *  Trait which allow extra operation for any table in database, this trait it's like a extension of [[dbfactory.setting.GenericCRUD]]
 * @tparam C case class that represent instance in database [[caseclass.CaseClassDB]]
 * @tparam T class that represent the table in database, allow make query in database [[dbfactory.table]]
 */
trait GenericOperation[C,T <: GenericTable[C]] extends GenericTableQuery[C,T] with DB[C,T]  {
  /**
   *  Generic SelectFilter which allow select any record in any table in database indicating a filter operation
   * @param filter function that define filter operation e.g: x=>x.nome=="Fran" && x.cognome=="roma" where x is PersonaTableRep
   * @return all record that satisfies  filter operation
   */
  def selectFilter(filter:T=>Rep[Boolean]): Future[Option[List[C]]]

  /**
   *  Generic method por execute any join created in this system
   * @param join is a Query which is the result of apply for yield in table of the database [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam A represent the field we want return in mod Tuple(Rep[Int],Rep[String],etc)
   * @tparam B represent the field we want return in mod Tuple(Int,String,etc)
   * @return Tuple the all field that we have first selected
   */
  def execJoin[A,B](join:Query[A,B,Seq]): Future[Option[List[B]]]

  /**
   *  Generic method which allow execute the any query in the database in the following mode Select [any field] from table
   *
   * @param selectField     query which represent the field we want select in the table in the database
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *              `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *              fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return  List the type A which the tuple lenght C
   */
  def execQueryAll[F, G, A](selectField:T=>F)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]):Future[Option[List[A]]]

  /**
   * Generic Operation which enable make operation of type select, in this method you have send field that you want
   * select.
   * @param selectField     query which represent the field we want select in the table in the database
   * @param id    identifies instance into database we want selected
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *             `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *             fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return    Option the type A which the tuple lenght C
   */
  def execQuery[F, G, A](selectField:T=>F,id:Int)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]):Future[Option[A]]

  /**
   * Generic Operation which enable make operation of type select, in this method you have send field that you want
   * select and the filter operation
   * @param selectField    query which represent the field we want select in the table in the database
   * @param filter    function which enable make select operation with filter
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *             `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *             fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return    List of case class that satisfies condition f. The List is a List of Tuples that has the types of the selected fields
   *            of the query.
   */
  def execQueryFilter[F, G, A](selectField:T=>F,filter:T=>Rep[Boolean])(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Option[List[A]]]

  /**
   * Generic Operation which enable make operation of type select, in this method you have send field that you want
   * select and the filter operation, this operation select all field distinct
   * that is, no two fields are the same as a result
   *
   * @param selectField    query which represent the field we want select in the table in the database
   * @param filter    function which enable make select operation with filter
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *             `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *             fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return    List of case class that satisfies condition f. The List is a List of Tuples that has the types of the selected fields
   *            of the query.
   */
  def execQueryFilterDistinct[F, G, A](selectField:T=>F,filter:T=>Rep[Boolean])(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Option[List[A]]]
  /**
   * Generic Operation which enable make operation of type update, in this method you have send field that you want
   * select for make update, the filter operation and the new values for this fields
   * @param selectField  query which represent the field we want select for make update operation in the table in the database
   * @param filter  function which enable make select operation with filter
   * @param tupleUpdate  tuple that contains new field that make update into old field
   * @param shape A type class that encodes the unpacking `Mixed => Unpacked` of a
   *             `Query[Mixed]` to its result element type `Unpacked` and the packing to a
   *             fully packed type `Packed` [[https://scala-slick.org/doc/3.2.3/ Slick]]
   * @tparam F  Tuple which represent the field what we want to select
   * @tparam G  Represent the element Packed
   * @tparam A  Represent the element unpacked which are a tuple
   * @return Future of Int that represent status operation, if status is == 1 so ok
   */
  def execQueryUpdate[F, G, A](selectField:T=>F,filter:T=>Rep[Boolean],tupleUpdate:A)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Option[Int]]
}
object GenericOperation{
  def apply[C,T<: GenericTable[C]:runtime.TypeTag](): GenericOperation[C,T] = new Operation[C,T]()
  private class Operation[C,T<: GenericTable[C]:runtime.TypeTag]() extends GenericOperation[C,T] {
    import dbfactory.util.Helper._
    override def selectFilter(filter:T=>Rep[Boolean]): Future[Option[List[C]]] = super.run(tableDB().withFilter(filter).result.transactionally).result()
    override def execJoin[A,B](join:Query[A,B,Seq]): Future[Option[List[B]]] = super.run(join.result.transactionally).result()
    override def execQueryAll[F, G, A](selectField:T=>F)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Option[List[A]]] = super.run(tableDB().map(selectField).result.transactionally).result()
    override def execQuery[F, G, A](selectField:T=>F,id:Int)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]):Future[Option[A]]= super.run(tableDB().filter(_.id===id).map(selectField).result.headOption.transactionally)
    override def execQueryFilter[F, G, A](selectField:T=>F,filter:T=>Rep[Boolean])(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Option[List[A]]] = super.run(tableDB().withFilter(filter).map(selectField).result.transactionally).result()
    override def execQueryFilterDistinct[F, G, A](selectField:T=>F,filter:T=>Rep[Boolean])(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Option[List[A]]] = super.run(tableDB().withFilter(filter).map(selectField).distinct.result.transactionally).result()
    override def execQueryUpdate[F, G, A](selectField:T=>F,filter:T=>Rep[Boolean],tupleUpdate:A)(implicit shape: Shape[_ <: FlatShapeLevel, F, A, G]): Future[Option[Int]] = super.run(tableDB().withFilter(filter).map(selectField).update(tupleUpdate).transactionally).map(t => Option(t))

  }
}