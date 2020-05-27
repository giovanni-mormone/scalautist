package servermodel.routes

import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import servermodel.routes.masterroute.MasterRoutePersona._

object Route {
  val route: Route = concat(routePersona)
}
