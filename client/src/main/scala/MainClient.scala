import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.{HumanResourceViewFX, LoginViewFX, ManagerViewFX}

object MainClient{
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main])
  }
}

private class Main extends Application{
  override def start(primaryStage: Stage): Unit = {
   // ModelDispatcher()
    ManagerViewFX(primaryStage,"Juan","123").show()
    //HumanResourceViewFX(primaryStage,"Juan","123").show()
    //LoginViewFX(primaryStage).show()
  }
}
