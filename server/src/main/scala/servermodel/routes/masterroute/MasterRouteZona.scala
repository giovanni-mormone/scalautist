package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import servermodel.routes.subroute.ZonaRoute._

/**
 * @author Francesco Cassano
 * This object manage routes that act on the zona entity and its related entities
 */
object MasterRouteZona {

  val routeZona: Route =
    concat(
      path("dummyZona") {
        methodDummy()
      },
      path("getzona" / IntNumber) {
        id => getZona(id)
      },
      path("getallzona") {
        getAllZona
      },
      path("createzona" ) {
        createZona()
      },
      path("createallzona") {
        createAllZona()
      },
      path("deletezona") {
        deleteZona()
      },
      path("deleteallzona") {
        deleteAllZona()
      },
      path("updatezona") {
        updateZona()
      }
    )
}
