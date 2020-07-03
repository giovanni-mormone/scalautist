package testdboperation.richiestateorica

import java.sql.Date

import caseclass.CaseClassDB.{Giorno, RichiestaTeorica}
import caseclass.CaseClassHttpMessage.{AssignRichiestaTeorica, RequestGiorno}

object RichiestaTeoricaTestValues {

  val baseGoodRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),2,None),
      RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),3,None),
      RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),4,None)),
    List(RequestGiorno(Giorno(10,"Lunedi",1,None),6), RequestGiorno(Giorno(15,"Lunedi",1,None),5),
      RequestGiorno(Giorno(1,"Lunedi",1,None),4), RequestGiorno(Giorno(15,"Lunedi",1,None),3),
      RequestGiorno(Giorno(1,"Lunedi",1,None),2), RequestGiorno(Giorno(19,"Lunedi",1,None),1),
      RequestGiorno(Giorno(2,"Martedi",2,None),6), RequestGiorno(Giorno(12,"Martedi",2,None),5),
      RequestGiorno(Giorno(2,"Martedi",2,None),4), RequestGiorno(Giorno(22,"Martedi",2,None),3),
      RequestGiorno(Giorno(2,"Martedi",2,None),2), RequestGiorno(Giorno(24,"Martedi",2,None),1),
      RequestGiorno(Giorno(13,"Mercoledi",3,None),6), RequestGiorno(Giorno(31,"Mercoledi",3,None),5),
      RequestGiorno(Giorno(23,"Mercoledi",3,None),4), RequestGiorno(Giorno(3,"Mercoledi",3,None),3),
      RequestGiorno(Giorno(35,"Mercoledi",3,None),2), RequestGiorno(Giorno(33,"Mercoledi",3,None),1),
      RequestGiorno(Giorno(4,"Giovedi",4,None),6), RequestGiorno(Giorno(47,"Giovedi",4,None),5),
      RequestGiorno(Giorno(4,"Giovedi",4,None),4), RequestGiorno(Giorno(41,"Giovedi",4,None),3),
      RequestGiorno(Giorno(4,"Giovedi",4,None),2), RequestGiorno(Giorno(4,"Giovedi",4,None),1),
      RequestGiorno(Giorno(5,"Venerdi",5,None),6), RequestGiorno(Giorno(5,"Venerdi",5,None),5),
      RequestGiorno(Giorno(5,"Venerdi",5,None),4), RequestGiorno(Giorno(52,"Venerdi",5,None),3),
      RequestGiorno(Giorno(5,"Venerdi",5,None),2), RequestGiorno(Giorno(5,"Venerdi",5,None),1),
      RequestGiorno(Giorno(6,"Sabato",6,None),6), RequestGiorno(Giorno(16,"Sabato",6,None),5),
      RequestGiorno(Giorno(6,"Sabato",6,None),4), RequestGiorno(Giorno(6,"Sabato",6,None),3),
      RequestGiorno(Giorno(6,"Sabato",6,None),2), RequestGiorno(Giorno(6,"Sabato",6,None),1),
      RequestGiorno(Giorno(7,"Domenica",7,None),6), RequestGiorno(Giorno(17,"Domenica",7,None),5),
      RequestGiorno(Giorno(7,"Domenica",7,None),4), RequestGiorno(Giorno(27,"Domenica",7,None),3),
      RequestGiorno(Giorno(7,"Domenica",7,None),2), RequestGiorno(Giorno(37,"Domenica",7,None),1)))


}
