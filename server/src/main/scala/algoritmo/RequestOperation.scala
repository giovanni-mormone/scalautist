package algoritmo

import java.sql.Date

import algoritmo.RequestOperation.InfoReq

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait RequestOperation{
  def updateInfoReq(date: Date, shift: Int,quantity:Int): Unit
  def createInfoReq(infoReq:Option[List[InfoReq]]):Unit
}
object RequestOperation {
  def apply(): RequestOperation = new RequestOperationImp()
  final case class InfoReq(idShift: Int, request: Int, assigned: Int, idDay: Int, data: Date, idTerminal: Int)
  class RequestOperationImp() extends RequestOperation{

    var infoReq:Option[List[InfoReq]]= _
    override def updateInfoReq(date: Date, shift: Int,quantity:Int):Unit = {
      infoReq.map(_.collect{
        case x if x.data.compareTo(date) == 0 && x.idShift == shift => x.copy(assigned = x.assigned +quantity)
        case x => x
      })
    }
    //get
    //update
    override def createInfoReq(infoReq: Option[List[InfoReq]]): Unit =
      this.infoReq=this.infoReq.map(_=>infoReq.toList.flatten)
  }
}
object sdalasldk extends App{
  val s = ListBuffer[Int]()
  s+=1
  s.map(_=>s+=22)
  println(s)
}