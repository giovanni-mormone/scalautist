package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{path, _}
import servermodel.routes.subroute.TerminaleRoute._

/**
 * @author Francesco Cassano
 * This object manage routes that act on the terminale entity and its related entities
 */
object MasterRouteTerminale {

  val routeTerminale: Route =
    concat(
      path("getterminale") {
        getTerminale
      },
      path("getallterminale") {
        getAllTerminale
      },
      path("createterminale" ) {
        createTerminale()
      },
      path("createallterminale") {
        createAllTerminale()
      },
      path("deleteterminale") {
        deleteTerminale()
      },
      path("deleteallterminale") {
        deleteTerminale()
      },
      path("updateterminale") {
        updateTerminale()
      },
      path("getterminalebyzona") {
        getTerminaleByZona
      }
    )

}
