import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.LoginViewFX

object MainClient{
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main])
  }
}

private class Main extends Application{
  override def start(primaryStage: Stage): Unit = {
    LoginViewFX(primaryStage).show()
  }
}
