package view

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.{ChangePasswordView}

class ChangePasswordLaunch  extends Application{
  override def start(primaryStage: Stage): Unit =
    ChangePasswordView(primaryStage, None)
}
