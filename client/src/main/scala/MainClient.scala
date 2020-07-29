import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.LoginViewFX

private class Main extends Application{
    override def start(primaryStage: Stage): Unit = {
        LoginViewFX(primaryStage).show()
    }
}
object MainClient extends App{
    Application.launch(classOf[Main])

}
