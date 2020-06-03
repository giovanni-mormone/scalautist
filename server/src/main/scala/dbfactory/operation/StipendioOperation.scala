package dbfactory.operation

import caseclass.CaseClassDB.Stipendio
import dbfactory.implicitOperation.OperationCrud


/** @author Giovanni Mormone
 *  Trait which allows to perform operations on the stipendio table.
 */
trait StipendioOperation extends OperationCrud[Stipendio]{

  def calculateStipendi
}

object StipendioOperation extends StipendioOperation{

}
