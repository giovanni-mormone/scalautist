package algoritmo

import java.io.{File, FileOutputStream}
import java.sql.Date

import algoritmo.AssignmentOperation.{Info, InfoDay, InfoReq}
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import utils.DateConverter._
import scala.concurrent.blocking

object PrintListToExcel {
  val workbook:XSSFWorkbook= new XSSFWorkbook()
  val sheet: XSSFSheet = workbook.createSheet("Drivers Details")

  private def createObject2(dataI:Date,dateF:Date,info:List[Info],map:Map[String,List[String]],infoReq:Option[List[InfoReq]]):Map[String,List[String]]={
    @scala.annotation.tailrec
    def _createObject(info:List[Info], map:Map[String,List[String]]):Map[String,List[String]]= info match {
      case ::(head, next) =>
        val result = List[String](head.idDriver.toString,head.idTerminal.toString,head.isFisso.toString,head.tipoContratto.toString):::iteraDate(head.infoDay,dataI,dateF)
        val key=(map.keySet.toList.map(_.toInt).sortWith(_<_).last+1).toString
          _createObject(next, map + (key->result))
      case Nil => map
    }
    val resul= _createObject(info,map)
    infoReq match {
      case Some(value) =>createObjectInfoReq(dataI,dateF,resul,value)
      case None =>resul
    }
  }

  private def createObjectInfoReq(dataI:Date,dateF:Date,map:Map[String,List[String]],infoReq:List[InfoReq]):Map[String,List[String]]={

    @scala.annotation.tailrec
    def _createObject(info:List[(Int,List[InfoReq])], map:Map[String,List[String]]):Map[String,List[String]]= info match {
      case ::(head, next) =>
        val result = List[String]("","","",head._1.toString):::iteraDate2(head._2,dataI,dateF)
        val key=(map.keySet.toList.map(_.toInt).sortWith(_<_).last+1).toString
          _createObject(next, map + (key->result))
      case Nil => map
    }
    _createObject(infoReq.groupBy(_.idShift).toList,map)
  }

  private def iteraDate2(infoDay:List[InfoReq],dataI:Date,dateF:Date):List[String]={
    val date = createListDayBetween(dataI,dateF)
    @scala.annotation.tailrec
    def _iteraDate(listDate:List[Date],result:List[String]=List.empty):List[String]=listDate match {
      case ::(head, next) if infoDay.exists(_.data.compareTo(head)==0)=>_iteraDate(next,result:+(infoDay.find(_.data.compareTo(head) == 0) match {
    case Some(value) => value match{
    case InfoReq(idShift, request, assigned, idDay, data, idTerminal)=> request.toString + " -> " + assigned.toString
    }
    case None =>""
    }))
      case ::(head, next)=>_iteraDate(next,result:+"")
      case Nil =>result
    }
    _iteraDate(date)
  }

  def printInfo(dataI:Date,dateF:Date,infoss:List[Info], infoReq: Option[List[InfoReq]] = None): Unit =  blocking{
    val info:Map[String,List[String]]=Map("1"->(List("Matricola","Terminal","IsFisso","Tipo Contratto"):::createListDayBetween(dataI,dateF).map(_.toString)))
    val newInfo=createObject2(dataI,dateF,infoss,info,infoReq)
    val keySet:List[String] = newInfo.keySet.toList.map(_.toInt).sortWith(_<_).map(_.toString)

    iteraKey(keySet,newInfo)
    try {
      // this Writes the workbook gfgcontribute
        val out = new FileOutputStream(new File("drivers"+infoss.head.idTerminal+".xlsx"))
        workbook.write(out)
        out.close()
        System.out.println("gfgcontribute.xlsx written successfully on disk.")
    }
    catch{
      case e:Exception=> e.printStackTrace()
    }
  }

  //List("Matricola","Terminal","IsFisso","Tipo Contratto")
  private def createObject(dataI:Date,dateF:Date,info:List[Info],map:Map[String,List[String]]):Map[String,List[String]]={
    @scala.annotation.tailrec
    def _createObject(info:List[Info], map:Map[String,List[String]]):Map[String,List[String]]= info match {
      case ::(head, next) =>
        val result = List[String](head.idDriver.toString,head.idTerminal.toString,head.isFisso.toString,head.tipoContratto.toString):::iteraDate(head.infoDay,dataI,dateF)
        val key=(map.keySet.toList.map(_.toInt).sortWith(_<_).last+1).toString
        _createObject(next, map + (key->result))
      case Nil =>map
    }
    _createObject(info,map)
  }
  
  

  private def iteraDate(infoDay:List[InfoDay],dataI:Date,dateF:Date):List[String]={
    val date = createListDayBetween(dataI,dateF)
    @scala.annotation.tailrec
    def _iteraDate(listDate:List[Date],result:List[String]=List.empty):List[String]=listDate match {
      case ::(head, next) if infoDay.exists(_.data.compareTo(head)==0)=>_iteraDate(next,result:+(infoDay.find(_.data.compareTo(head) == 0) match {
        case Some(value) => value match {
          case InfoDay(_, Some(shift), Some(shift2), None, false, false)=> shift.toString+" "+ shift2.toString
          case InfoDay(_, Some(shift), None, None, false, false)=>shift.toString
          case InfoDay(_, None, None, None, true, false)=> "L"
          case InfoDay(_, None, None, None, false, true)=>"A"
        }
        case None =>""
      }))
      case ::(head, next)=>_iteraDate(next,result:+"")
      case Nil =>result
    }
    _iteraDate(date)
  }
   
   private def iteraKey(keySet:List[String],info:Map[String,List[String]]): Unit ={
     var rowNUm =0
       for(key <- keySet){
         val objArr = info.get(key)
           writeInExcel(objArr,rowNUm)
         rowNUm=rowNUm+1
       }
   }
   private def writeInExcel(objArr:Option[List[String]],rows:Int): Unit ={
     val row = sheet.createRow(rows)
     objArr match {
       case Some(value) =>var cellAux=0
         for (obj <- value) {
         // this line creates a cell in the next column of that row
         val cell = row.createCell(cellAux)
         cell.setCellValue(obj)
         cellAux=cellAux+1
       }
     }
   }
}
