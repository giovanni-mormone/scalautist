package algoritmo

import java.sql.Date

import algoritmo.InfoAssigned.Info


trait InfoAssigned{
  def upsertListInfo(resultNew: List[Info]): Unit
}
object InfoAssigned{
  def apply(): InfoAssigned = new InfoAssignedImp()
  final case class InfoDay(data: Date, shift: Option[Int] = None, shift2: Option[Int] = None, straordinario: Option[Int] = None, freeDay: Boolean = false, absence: Boolean = false)
  final case class Info(idDriver: Int, idTerminal: Int, isFisso: Boolean, tipoContratto: Int, infoDay: List[InfoDay])
  private class InfoAssignedImp() extends InfoAssigned{
   var result:List[Info]=List[Info]()
    //+= listbuffer
    override def upsertListInfo(resultNew: List[Info]): Unit = {
      result = resultNew.flatMap{
        case info if result.exists(_.idDriver == info.idDriver)=>result.filter(_.idDriver==info.idDriver).map(x=>{
          val infoNew =x.copy(infoDay = info.infoDay.flatMap{
            case s if x.infoDay.exists(_.data.compareTo(s.data)==0)=>
              x.infoDay.filter(_.data.compareTo(s.data)==0).map(_=>s)
            case s => List(s)
          })
          infoNew.copy(infoDay = infoNew.infoDay:::x.infoDay.filter(x=> !infoNew.infoDay.exists(_.data.compareTo(x.data)==0)))
        })
        case x => List(x)
      }:::result.filter(xs=> !result.exists(_.idDriver==xs.idDriver))
    }
  }

}

