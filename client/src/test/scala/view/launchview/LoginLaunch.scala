package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.LoginView

class LoginLaunch extends Application{
  override def start(primaryStage: Stage): Unit =
    LoginView(primaryStage)
}
