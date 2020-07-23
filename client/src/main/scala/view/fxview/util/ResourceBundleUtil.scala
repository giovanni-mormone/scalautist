package view.fxview.util

import java.util.ResourceBundle

object ResourceBundleUtil {
  implicit class Resource(resource:ResourceBundle){
    def println[A](key:A,string:A):String= resource.getString(key.toString).concat(" ").concat(string.toString)
    def getResource(key:String):String = new String(resource.getString(key).getBytes("ISO-8859-1"), "UTF-8")
  }
}
