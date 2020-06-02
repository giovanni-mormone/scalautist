package servermodel.routes.masterroute

import akka.http.scaladsl.server.Directives.{concat, path}
import akka.http.scaladsl.server.Route
import servermodel.routes.subroute.ContrattoRoute._

object MasterRouteContratto {
  val routeContratto: Route =
    concat(
      path("getcontratto") {
        getContratto
      },
      path("getallcontratto") {
        getAllContratto
      },
      path("createcontratto" ) {
        createContratto()
      },
      path("updatecontratto") {
        updateContratto()
      }
    )
}
