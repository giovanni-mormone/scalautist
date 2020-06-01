package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import servermodel.routes.subroute.TurnoRoute._

/**
 * @author Francesco Cassano
 * This object manage routes that act on the Turni entity and its related entities
 */
object MasterRouteTurni{

  val routeTurni: Route =
    concat(
      path("dummyTurno") {
        methodDummy()
      },
      path("getturno" / IntNumber) {
        id => getTurno(id)
      },
      path("getallturno") {
        getAllTurno
      },
      path("createturno" ) {
        createTurno()
      },
      path("createallturno") {
        createAllTurno()
      },
      path("deleteturno") {
        deleteTurno()
      },
      path("deleteallturno") {
        deleteAllTurno()
      },
      path("updateturno") {
        updateTurno()
      }
    )

}
