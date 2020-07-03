package jsonmessages


import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, _}
import jsonmessages.ImplicitDate._
import spray.json._

object JsonFormats extends SprayJsonSupport with DefaultJsonProtocol{

    implicit val zonaJsonFormat: RootJsonFormat[Zona] = jsonFormat2(Zona)
    implicit val zonaTerminaleJsonFormat: RootJsonFormat[ZonaTerminale] = jsonFormat4(ZonaTerminale)
    implicit val turnoJsonFormat: RootJsonFormat[Turno] = jsonFormat4(Turno)
    implicit val terminaleJsonFormat: RootJsonFormat[Terminale] = jsonFormat3(Terminale)
    implicit val storicoContrattoJsonFormat: RootJsonFormat[StoricoContratto] = jsonFormat7(StoricoContratto)
    implicit val risultatoJsonFormat: RootJsonFormat[Risultato] = jsonFormat4(Risultato)
    implicit val richiestaTeoricaJsonFormat: RootJsonFormat[RichiestaTeorica] = jsonFormat4(RichiestaTeorica)
    implicit val richiestaJsonFormat: RootJsonFormat[Richiesta] = jsonFormat4(Richiesta)
    implicit val presenzaJsonFormat: RootJsonFormat[Presenza] = jsonFormat5(Presenza)
    implicit val personaJsonFormat: RootJsonFormat[Persona] = jsonFormat10(Persona)
    implicit val parametroJsonFormat: RootJsonFormat[Parametro] = jsonFormat3(Parametro)
    implicit val gruppoTerminaleJsonFormat: RootJsonFormat[GruppoTerminale] = jsonFormat2(GruppoTerminale)
    implicit val giornoJsonFormat: RootJsonFormat[Giorno] = jsonFormat4(Giorno)
    implicit val giornoInSettimanaJsonFormat: RootJsonFormat[GiornoInSettimana] = jsonFormat5(GiornoInSettimana)
    implicit val regolaJsonFormat: RootJsonFormat[Regola] = jsonFormat2(Regola)
    implicit val contrattoJsonFormat: RootJsonFormat[Contratto] = jsonFormat5(Contratto)
    implicit val loginJsonFormat: RootJsonFormat[Login] = jsonFormat2(Login)
    implicit val stipendioJsonFormat: RootJsonFormat[Stipendio] = jsonFormat4(Stipendio)
    implicit val assenzaJsonFormat: RootJsonFormat[Assenza] = jsonFormat5(Assenza)
    implicit val changePasswordJsonFormat: RootJsonFormat[ChangePassword] = jsonFormat3(ChangePassword)
    implicit val intJsonFormat:RootJsonFormat[Id] = jsonFormat1(Id)
    implicit val disponibilitaJsonFormat:RootJsonFormat[Disponibilita] = jsonFormat4(Disponibilita)
    implicit val assumiJsonFormat:RootJsonFormat[Assumi] = jsonFormat3(Assumi)
    implicit val ferieJsonFormat:RootJsonFormat[Ferie] = jsonFormat3(Ferie)
    implicit val infoPresenzaJsonFormat:RootJsonFormat[InfoPresenza] = jsonFormat5(InfoPresenza)
    implicit val InfoAssenzaJsonFormat:RootJsonFormat[InfoAssenza] = jsonFormat2(InfoAssenza)
    implicit val infoValorePresenzaJsonFormat:RootJsonFormat[InfoValorePresenza] = jsonFormat3(InfoValorePresenza)
    implicit val stipendioInformationsJsonFormat:RootJsonFormat[StipendioInformations] = jsonFormat3(StipendioInformations)
    implicit val infoHomeJsonFormat:RootJsonFormat[InfoHome] = jsonFormat2(InfoHome)
    implicit val shiftDayJsonFormat:RootJsonFormat[ShiftDay] = jsonFormat2(ShiftDay)
    implicit val infoShiftDayJsonFormat:RootJsonFormat[InfoShift] = jsonFormat2(InfoShift)
    implicit val dateJsonFormatJsonFormat:RootJsonFormat[Dates] = jsonFormat1(Dates)
    implicit val infoVacantShiftJsonFormat: RootJsonFormat[InfoVacantShift] = jsonFormat2(InfoVacantShift)
    implicit val infoReplacementJsonFormat: RootJsonFormat[InfoReplacement] = jsonFormat4(InfoReplacement)
    implicit val infoAbsenceOnDayJsonFormat: RootJsonFormat[InfoAbsenceOnDay] = jsonFormat5(InfoAbsenceOnDay)
    implicit val requestGiornoJsonFormat:RootJsonFormat[RequestGiorno] = jsonFormat2(RequestGiorno)
    implicit val assignRichiestaTeoricaJsonFormat: RootJsonFormat[AssignRichiestaTeorica] = jsonFormat2(AssignRichiestaTeorica)
    implicit val gruppoAJsonFormat: RootJsonFormat[GruppoA] = jsonFormat3(GruppoA)
    implicit val settimanaNSJsonFormat: RootJsonFormat[SettimanaNS] = jsonFormat3(SettimanaNS)
    implicit val algorithmExecuteJsonFormat: RootJsonFormat[AlgorithmExecute] = jsonFormat7(AlgorithmExecute)
    implicit val infoAlgorithmJsonFormat: RootJsonFormat[InfoAlgorithm] = jsonFormat3(InfoAlgorithm)
    implicit val infoDatesJsonFormat: RootJsonFormat[InfoDates] = jsonFormat4(InfoDates)
    implicit val resultAlgorithmJsonFormat: RootJsonFormat[ResultAlgorithm] = jsonFormat3(ResultAlgorithm)
    implicit def requestJsonFormat[O:JsonFormat]:RootJsonFormat[Request[O]] = jsonFormat1(Request.apply[O])
    implicit def responseJsonFormat[V: JsonFormat]: RootJsonFormat[Response[V]] = jsonFormat2(Response.apply[V])
}