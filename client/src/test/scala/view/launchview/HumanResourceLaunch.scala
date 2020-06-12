package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.HumanResourceView

class HumanResourceLaunch  extends Application{
  override def start(primaryStage: Stage): Unit =
    HumanResourceView(primaryStage)
}