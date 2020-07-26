package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.ChangePasswordViewFX

class ChangePasswordLaunch  extends Application{
  override def start(primaryStage: Stage): Unit =
    ChangePasswordViewFX(primaryStage, None)
}
