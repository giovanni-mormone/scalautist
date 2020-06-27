import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.{LoginView, ManagerView}

object MainClient extends App{
  Application.launch(classOf[Main])
}

private class Main extends Application{

  override def start(primaryStage: Stage): Unit =
    ManagerView(primaryStage).show()
    //LoginView(primaryStage).show()
}
