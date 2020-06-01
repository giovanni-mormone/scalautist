package dbfactory.setting

import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{AbstractTable, TableQuery}
import scala.reflect.runtime.{universe => runtime}

/** @author Fabian Aspée Encina
 *  object which allow create a instance of table in real-time, avoiding generating more code
 */
object Reflection {
  private val runtimeMirror: runtime.Mirror = runtime.runtimeMirror(getClass.getClassLoader)
  private def getTypeTag[C: runtime.TypeTag]: runtime.TypeTag[C] = runtime.typeTag[C]

  /**
   *
   * @param args
   * @tparam C is anything which represent tag of table in database
   * @return instance table we want in real-time
   */
  def createClassByConstructor[C: runtime.TypeTag](args: Any*): C =
    runtimeMirror.reflectClass(getTypeTag[C].tpe.typeSymbol.asClass)
      .reflectConstructor(runtime.typeOf[C].decl(runtime.termNames.CONSTRUCTOR)
        .asMethod)(args: _*).asInstanceOf[C]
}

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

/** @author Fabian Aspée Encina
 *  This Abstract Class allows us generate instance of table in database in real-time
 * @tparam T case class that represent table in this system
 * @tparam C class which represent instance table in database
 */
abstract class GenericTableQuery[T, C <: AbstractTable[T]: runtime.TypeTag] {
  import Reflection._
  // look at following code: Students, if you want to initialize Students
  // you're gonna need a tag parameter, that's why we pass tag here
  private val table: TableQuery[C] = TableQuery.apply(tag => createClassByConstructor[C](tag))

  /**
   * method which call table which call scalaReflection to generate instance table
   * @return instance table we want in real-time
   */
  protected def tableDB(): TableQuery[C] = table
}