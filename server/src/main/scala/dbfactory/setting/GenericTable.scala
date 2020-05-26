package dbfactory.setting

import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{AbstractTable, TableQuery}
import scala.reflect.runtime.{universe => runtime}

object Reflection {
  private val runtimeMirror: runtime.Mirror = runtime.runtimeMirror(getClass.getClassLoader)
  private def getTypeTag[C: runtime.TypeTag]: runtime.TypeTag[C] = runtime.typeTag[C]
  def createClassByConstructor[C: runtime.TypeTag](args: Any*): C =
    runtimeMirror.reflectClass(getTypeTag[C].tpe.typeSymbol.asClass)
      .reflectConstructor(runtime.typeOf[C].decl(runtime.termNames.CONSTRUCTOR)
        .asMethod)(args: _*).asInstanceOf[C]
}
abstract class GenericTable[T](tag: Tag, name: String,nameId:String) extends Table[T](tag, name) {
  def id: Rep[Int] = column[Int](nameId, O.PrimaryKey, O.AutoInc)
}
abstract class GenericTableQuery[T, C <: AbstractTable[T]: runtime.TypeTag] {
  import Reflection._
  // look at following code: Students, if you want to initialize Students
  // you're gonna need a tag parameter, that's why we pass tag here
  private val table: TableQuery[C] = TableQuery.apply(tag => createClassByConstructor[C](tag))
  protected def tableDB(): TableQuery[C] = table
}