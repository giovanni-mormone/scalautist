package dbfactory.table
import slick.jdbc.SQLServerProfile.api._
import dbfactory.setting.GenericTable
import dbfactory.table.TerminaleTable.TerminaleTableRep
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Persona, Terminale}

/** @author Fabian Aspée Encina
 *  Object which encapsulates a instance of person in database
 */
object PersonaTable{

  /**
   * Class which represent a instance of person in database, this class define all field into table
   * person and yours relations with another tables if exists
   * @param tag it's like a SQL alias. It distinguishes different instances of the same table within a query.
   */
  class PersonaTableRep(tag: Tag) extends GenericTable[Persona](tag, "PersoneSets","Matricola") {
    def terminaleId:Rep[Option[Int]] = column[Int]("Terminale_IdTerminale").?
    def nome: Rep[String] = column[String]("Nome")
    def cognome: Rep[String] = column[String]("Cognome")
    def numTelefono: Rep[String] = column[String]("NumTelefono")
    def password: Rep[String] = column[String]("Password")
    def ruolo: Rep[Int] = column[Int]("Ruolo")
    def isNew:Rep[Boolean] = column[Boolean]("IsNew")
    def userName:Rep[String]= column[String]("UserName")
    override def * : ProvenShape[Persona] = (nome,cognome,numTelefono,password.?,ruolo,isNew,userName,terminaleId,id.?).mapTo[Persona]
    def terminale: ForeignKeyQuery[TerminaleTableRep, Terminale] = foreignKey("Terminale_IdTerminale", terminaleId, TableQuery[TerminaleTableRep])(_.id.?, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)
  }
}
