package dbfactory.table

import caseclass.CaseClassDB.Disponibilita
import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.ProvenShape

/**
 * @author Giovanni Mormone
 *
 * Encapsulate an instance of Disponibilita in the DB
 */
object DisponibilitaTable {

  /**
   * Class which represent an instance of disponibilita in database, this class defines all the fields in the table
   * disponibilita and relations with other tables if exists
   * @param tag it's like a SQL alias. It distinguishes different instances of the same table within a query.
   */
  class DisponibilitaTableRep(tag:Tag) extends GenericTable[Disponibilita](tag, "DisponibilitaStraordinarioSets","IdDisponibilitaStraordinario"){
    def settimana: Rep[Int] = column[Int]("Settimana")
    def giorno1: Rep[String] = column[String]("Giorno1")
    def giorno2: Rep[String] = column[String]("Giorno2")
    override def * : ProvenShape[Disponibilita] = (settimana,giorno1,giorno2,id.?).mapTo[Disponibilita]
  }

}
