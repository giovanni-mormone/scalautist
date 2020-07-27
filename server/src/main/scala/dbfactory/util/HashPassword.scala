package dbfactory.util

import org.mindrot.jbcrypt.BCrypt

object HashPassword {
  def apply(string:Option[String]):Option[String]= new HashPassword(string).hashPassword()

  private class HashPassword(string:Option[String]){
    def hashPassword():Option[String] =string.map(password=> BCrypt.hashpw(password,"$2$11#aM4rc0RdMer10tS4nG10ves3p1no7"))
  }
}
