package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.DriverView

class DriverLaunch  extends Application{
    override def start(primaryStage: Stage): Unit =
      DriverView(primaryStage)
}
