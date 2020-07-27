package dbfactory.util

import org.mindrot.jbcrypt.BCrypt

object CheckPassword {
  def apply(string:String,hashPassword:String):Boolean = new CheckPassword(string,hashPassword).hashPassword()

  private class CheckPassword(string:String,hashPassword:String){
    def hashPassword():Boolean = BCrypt.checkpw(string,hashPassword)
  }
}
