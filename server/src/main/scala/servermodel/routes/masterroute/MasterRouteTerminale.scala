package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import servermodel.routes.subroute.TerminaleRoute._

object MasterRouteTerminale {

  val routeTerminale: Route =
    concat(
      path("dummyTerminal") {
        methodDummy()
      },
      path("getterminale" / IntNumber) {
        id => getTerminale(id)
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
      }
    )

}
