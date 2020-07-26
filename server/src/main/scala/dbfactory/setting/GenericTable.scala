package dbfactory.setting

import slick.jdbc.SQLServerProfile.api._


/** @author Fabian Aspée Encina
 * Abstract Class which extend [[dbfactory.setting.Table]] table is instance of [[https://scala-slick.org/doc/3.2.3/ Slick]], all table defined in this project extends
 * GenericTable
 * @param tag it's like a SQL alias. It distinguishes different instances of the same table within a query.
 * @param name is the name of the table in database
 * @param nameId is the name of id of any table in database
 * @tparam T case class for represent database
 */
abstract class GenericTable[T](tag: Tag, name: String,nameId:String) extends Table[T](tag, name) {
  def id: Rep[Int] = column[Int](nameId, O.PrimaryKey, O.AutoInc)
}

