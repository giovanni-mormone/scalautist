package jsonmessages

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import caseclass.CaseClassHttpMessage.ChangePassword
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonMessageFormats extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val changePasswordJsonFormat: RootJsonFormat[ChangePassword] = jsonFormat3(ChangePassword)
}
