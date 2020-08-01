package testdboperation.richiestateorica

import java.sql.Date

import caseclass.CaseClassDB.{Giorno, RichiestaTeorica}
import caseclass.CaseClassHttpMessage.{AssignRichiestaTeorica, RequestGiorno}

object RichiestaTeoricaTestValues {

  private val giorni = List(RequestGiorno(Giorno(10,"Lunedi",1,None),6), RequestGiorno(Giorno(15,"Lunedi",1,None),5),
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
    RequestGiorno(Giorno(7,"Domenica",7,None),2), RequestGiorno(Giorno(37,"Domenica",7,None),1))

  private val giorni2 = List(RequestGiorno(Giorno(11,"Lunedi",1,None),6), RequestGiorno(Giorno(169,"Lunedi",1,None),5),
    RequestGiorno(Giorno(12,"Lunedi",1,None),4), RequestGiorno(Giorno(185,"Lunedi",1,None),3),
    RequestGiorno(Giorno(14,"Lunedi",1,None),2), RequestGiorno(Giorno(179,"Lunedi",1,None),1),
    RequestGiorno(Giorno(12,"Martedi",2,None),6), RequestGiorno(Giorno(152,"Martedi",2,None),5),
    RequestGiorno(Giorno(32,"Martedi",2,None),4), RequestGiorno(Giorno(232,"Martedi",2,None),3),
    RequestGiorno(Giorno(24,"Martedi",2,None),2), RequestGiorno(Giorno(244,"Martedi",2,None),1),
    RequestGiorno(Giorno(113,"Mercoledi",3,None),6), RequestGiorno(Giorno(321,"Mercoledi",3,None),5),
    RequestGiorno(Giorno(24,"Mercoledi",3,None),4), RequestGiorno(Giorno(32,"Mercoledi",3,None),3),
    RequestGiorno(Giorno(35,"Mercoledi",3,None),2), RequestGiorno(Giorno(313,"Mercoledi",3,None),1),
    RequestGiorno(Giorno(46,"Giovedi",4,None),6), RequestGiorno(Giorno(471,"Giovedi",4,None),5),
    RequestGiorno(Giorno(47,"Giovedi",4,None),4), RequestGiorno(Giorno(411,"Giovedi",4,None),3),
    RequestGiorno(Giorno(114,"Giovedi",4,None),2), RequestGiorno(Giorno(433,"Giovedi",4,None),1),
    RequestGiorno(Giorno(15,"Venerdi",5,None),6), RequestGiorno(Giorno(556,"Venerdi",5,None),5),
    RequestGiorno(Giorno(25,"Venerdi",5,None),4), RequestGiorno(Giorno(523,"Venerdi",5,None),3),
    RequestGiorno(Giorno(35,"Venerdi",5,None),2), RequestGiorno(Giorno(51,"Venerdi",5,None),1),
    RequestGiorno(Giorno(46,"Sabato",6,None),6), RequestGiorno(Giorno(116,"Sabato",6,None),5),
    RequestGiorno(Giorno(56,"Sabato",6,None),4), RequestGiorno(Giorno(69,"Sabato",6,None),3),
    RequestGiorno(Giorno(66,"Sabato",6,None),2), RequestGiorno(Giorno(61,"Sabato",6,None),1),
    RequestGiorno(Giorno(777,"Domenica",7,None),6), RequestGiorno(Giorno(117,"Domenica",7,None),5),
    RequestGiorno(Giorno(78,"Domenica",7,None),4), RequestGiorno(Giorno(227,"Domenica",7,None),3),
    RequestGiorno(Giorno(79,"Domenica",7,None),2), RequestGiorno(Giorno(357,"Domenica",7,None),1))


  val startNotInitMonth = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-04-05"),Date.valueOf("2020-06-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-04-05"),Date.valueOf("2020-06-30"),3,None),
    RichiestaTeorica(Date.valueOf("2020-04-05"),Date.valueOf("2020-06-30"),4,None)), giorni)

  val endNotEndMonth = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-20"),2,None),
    RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-20"),3,None),
    RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-20"),4,None)), giorni)

  val startAfterEnd = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-07-01"),Date.valueOf("2020-06-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-07-01"),Date.valueOf("2020-06-30"),3,None),
    RichiestaTeorica(Date.valueOf("2020-07-01"),Date.valueOf("2020-06-30"),4,None)), giorni)

  val notSamePeriod = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-05-01"),Date.valueOf("2020-06-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),3,None),
    RichiestaTeorica(Date.valueOf("2020-06-01"),Date.valueOf("2020-06-30"),4,None)), giorni)

  val duplicatedTerminal = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),3,None),
    RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),3,None)), giorni)

  val noneRequest = AssignRichiestaTeorica(List(), giorni)

  val baseGoodRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),2,None),
      RichiestaTeorica(Date.valueOf("2020-04-01"),Date.valueOf("2020-06-30"),3,None)), giorni)

  val splitStartRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-05-01"),Date.valueOf("2020-06-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-05-01"),Date.valueOf("2020-06-30"),3,None)), giorni)

  val splitEndRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-05-01"),Date.valueOf("2020-05-31"),2,None),
    RichiestaTeorica(Date.valueOf("2020-05-01"),Date.valueOf("2020-05-31"),3,None)), giorni)

  val overrideRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-08-31"),2,None),
    RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-08-31"),3,None)), giorni)

  val updateRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-08-31"),2,None),
    RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-08-31"),3,None)), giorni)

  val splitAndInsert = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-05-31"),2,None),
    RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-05-31"),3,None),
    RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-05-31"),1,None)), giorni)

  val splitSomeRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-04-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-04-30"),3,None)), giorni)

  val updateAndSplitRequest = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-04-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-04-30"),3,None),
    RichiestaTeorica(Date.valueOf("2020-02-01"),Date.valueOf("2020-04-30"),1,None)), giorni)

  val insertSplitAndAddDays = AssignRichiestaTeorica(List(RichiestaTeorica(Date.valueOf("2020-03-01"),Date.valueOf("2020-09-30"),2,None),
    RichiestaTeorica(Date.valueOf("2020-03-01"),Date.valueOf("2020-09-30"),3,None),
    RichiestaTeorica(Date.valueOf("2020-03-01"),Date.valueOf("2020-09-30"),1,None),
    RichiestaTeorica(Date.valueOf("2020-03-01"),Date.valueOf("2020-09-30"),4,None)), giorni2)


}
