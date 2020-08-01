package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.LoginViewFX

class DriverLaunch  extends Application{
    override def start(primaryStage: Stage): Unit =
      LoginViewFX(primaryStage)
}
