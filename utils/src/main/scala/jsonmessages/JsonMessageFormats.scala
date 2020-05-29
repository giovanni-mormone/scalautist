package jsonmessages

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import caseclass.CaseClassHttpMessage.ChangePassword

object JsonMessageFormats extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val changePasswordJsonFormat: RootJsonFormat[ChangePassword] = jsonFormat3(ChangePassword)
}
