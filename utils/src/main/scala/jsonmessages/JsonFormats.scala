package jsonmessages

import caseclass.CaseClassDB._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import ImplicitDate._
import caseclass.CaseClassHttpMessage.{Assumi, ChangePassword, Id}

object JsonFormats extends SprayJsonSupport with DefaultJsonProtocol{
    implicit val zonaJsonFormat: RootJsonFormat[Zona] = jsonFormat2(Zona)
    implicit val turnoJsonFormat: RootJsonFormat[Turno] = jsonFormat3(Turno)
    implicit val terminaleJsonFormat: RootJsonFormat[Terminale] = jsonFormat3(Terminale)
    implicit val straordinarioJsonFormat: RootJsonFormat[Straordinario] = jsonFormat4(Straordinario)
    implicit val storicoContrattoJsonFormat: RootJsonFormat[StoricoContratto] = jsonFormat7(StoricoContratto)
    implicit val settimanaJsonFormat: RootJsonFormat[Settimana] = jsonFormat2(Settimana)
    implicit val risultatoJsonFormat: RootJsonFormat[Risultato] = jsonFormat4(Risultato)
    implicit val richiestaTeoricaJsonFormat: RootJsonFormat[RichiestaTeorica] = jsonFormat3(RichiestaTeorica)
    implicit val richiestaJsonFormat: RootJsonFormat[Richiesta] = jsonFormat4(Richiesta)
    implicit val presenzaJsonFormat: RootJsonFormat[Presenza] = jsonFormat4(Presenza)
    implicit val personaJsonFormat: RootJsonFormat[Persona] = jsonFormat10(Persona)
    implicit val parametroJsonFormat: RootJsonFormat[Parametro] = jsonFormat3(Parametro)
    implicit val gruppoTerminaleJsonFormat: RootJsonFormat[GruppoTerminale] = jsonFormat2(GruppoTerminale)
    implicit val giornoJsonFormat: RootJsonFormat[Giorno] = jsonFormat3(Giorno)
    implicit val giornoInSettimanaJsonFormat: RootJsonFormat[GiornoInSettimana] = jsonFormat5(GiornoInSettimana)
    implicit val contrattoJsonFormat: RootJsonFormat[Contratto] = jsonFormat3(Contratto)
    implicit val loginJsonFormat: RootJsonFormat[Login] = jsonFormat2(Login)
    implicit val stipendioJsonFormat: RootJsonFormat[Stipendio] = jsonFormat4(Stipendio)
    implicit val assenzaJsonFormat: RootJsonFormat[Assenza] = jsonFormat5(Assenza)
    implicit val changePasswordJsonFormat: RootJsonFormat[ChangePassword] = jsonFormat3(ChangePassword)
    implicit val intJsonFormat:RootJsonFormat[Id] = jsonFormat1(Id)
    implicit val disponibilitaJsonFormat:RootJsonFormat[Disponibilita] = jsonFormat3(Disponibilita)
    implicit val assumiJsonFormat:RootJsonFormat[Assumi] = jsonFormat3(Assumi)
}