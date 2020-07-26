package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.ManagerViewFX

class ManagerLaunch extends Application{

  override def start(primaryStage: Stage): Unit =
    ManagerViewFX(primaryStage,"aa","0")

}
