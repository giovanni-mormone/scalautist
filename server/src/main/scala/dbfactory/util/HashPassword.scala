package dbfactory.util

import org.mindrot.jbcrypt.BCrypt

object HashPassword {
  def apply(string:Option[String]):Option[String]= new HashPassword(string).hashPassword()

  private class HashPassword(string:Option[String]){
    def hashPassword():Option[String] =string.map(password=> BCrypt.hashpw(password,BCrypt.gensalt()))
  }
}

object a extends App {

  println(HashPassword(Some("admin2")))
  println(HashPassword(Some("root")))
  println(HashPassword(Some("yoyo")))
  println(HashPassword(Some("tutu")))
  println(HashPassword(Some("tutu2")))
}