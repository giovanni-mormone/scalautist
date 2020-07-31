package util

trait RunJar {
  def runJar():Unit
}
object RunJar extends RunJar {
  override def runJar(): Unit = {
    val e = Runtime.getRuntime.exec("java -jar scalautist-server-scala.jar")
  }
}