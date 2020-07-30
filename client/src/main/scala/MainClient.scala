import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.LoginViewFX

private class Main extends Application{
    override def start(primaryStage: Stage): Unit = {
        LoginViewFX(primaryStage).show()
    }
}

object MainClient{
    def main(Args: Array[String]): Unit ={
        Application.launch(classOf[Main])
        System.exit(0)
    }
}
