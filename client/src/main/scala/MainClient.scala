import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.{DriverView, HumanResourceView, LoginView, ManagerView}

object MainClient extends App{
  Application.launch(classOf[Main])
}

private class Main extends Application{

  override def start(primaryStage: Stage): Unit =

  //LoginView(primaryStage).show()
    DriverView(primaryStage).show()
//  ManagerView(primaryStage).show()
  //HumanResourceView(primaryStage).show()
}
