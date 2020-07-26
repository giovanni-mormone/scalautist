package dbfactory.setting

import slick.lifted.{AbstractTable, TableQuery}
import scala.reflect.runtime.{universe => runtime}

/** @author Fabian Aspée Encina
 *  object which allow create a instance of table in real-time, avoiding generating more code
 */
object GenericTableQuery {
  private val runtimeMirror: runtime.Mirror = runtime.runtimeMirror(getClass.getClassLoader)
  private def getTypeTag[T: runtime.TypeTag]: runtime.TypeTag[T] = runtime.typeTag[T]

  /**
   *
   * @param args
   * @tparam T is anything which represent tag of table in database
   * @return instance table we want in real-time
   */
  private def createClassByConstructor[T: runtime.TypeTag](args: Any*): T =
    runtimeMirror.reflectClass(getTypeTag[T].tpe.typeSymbol.asClass)
      .reflectConstructor(runtime.typeOf[T].decl(runtime.termNames.CONSTRUCTOR)
        .asMethod)(args: _*).asInstanceOf[T]
}
/** @author Fabian Aspée Encina
 *  This Abstract Class allows us generate instance of table in database in real-time
 * @tparam C case class that represent table in this system
 * @tparam T class which represent instance table in database
 */
abstract class GenericTableQuery[C, T <: AbstractTable[C]: runtime.TypeTag] {

  private val table: TableQuery[T] = TableQuery.apply(tag => GenericTableQuery.createClassByConstructor[T](tag))
  /**
   * method which call table which call scalaReflection to generate instance table
   * @return instance table we want in real-time
   */
  protected def tableDB(): TableQuery[T] = table
}