import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.{HumanResourceViewFX, LoginViewFX}

object MainClient{
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main])
  }
}

private class Main extends Application{
  override def start(primaryStage: Stage): Unit = {
   // ModelDispatcher()
    //ManagerView(primaryStage,"Juan","123").show()
<<<<<<< HEAD
=======
    //HumanResourceViewFX(primaryStage,"Juan","123").show()
>>>>>>> 28ae9644112d5cea0b2bd947812e39d14276c56d
    LoginViewFX(primaryStage).show()
  }
}
