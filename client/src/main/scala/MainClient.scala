<<<<<<< HEAD
object MainClient {

=======
<<<<<<< HEAD
import javafx.application.Application
import javafx.stage.Stage
import model.AddressManager
import view.fxview.mainview.LoginViewFX

private class Main extends Application{
    override def start(primaryStage: Stage): Unit = {
        LoginViewFX(primaryStage).show()
    }
}

object MainClient{
    def main(args: Array[String]): Unit ={
        if(args.length >= 1 && args(0).equals("remote")){
            AddressManager.remoteServer()
        }

        Application.launch(classOf[Main])
        System.exit(0)
    }
=======
object MainClient {

>>>>>>> 1ba3c3074a7ddf4f6e65bc964b56d7e086f29836
>>>>>>> develop
}
