package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.HumanResourceViewFX

class HumanResourceLaunch  extends Application{
  override def start(primaryStage: Stage): Unit =
    HumanResourceViewFX(primaryStage,"aa","0")
}