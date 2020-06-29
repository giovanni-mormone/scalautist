package view.launchview

import javafx.application.Application
import javafx.stage.Stage
import view.fxview.mainview.ManagerView

class ManagerLaunch extends Application{

  override def start(primaryStage: Stage): Unit =
    ManagerView(primaryStage)

}
