package model

object ResponceCode {

  implicit val Success: Int = 0
  implicit val NotFound: Int = 1
  implicit val DbError: Int = -1
  implicit val UnknownError: Int = -100
}
