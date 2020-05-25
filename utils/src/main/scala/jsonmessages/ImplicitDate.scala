package jsonmessages

import java.sql.Date

import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

object ImplicitDate {
  implicit object DateFormat extends JsonFormat[Date] {
    override def write(obj: Date): JsString = JsString(obj.toString)
    override def read(json: JsValue): Date = json match {
      case JsString(s) => Date.valueOf(s)
      case _ => throw DeserializationException("Invalid date format: " + json)
    }
  }

}
