package servermodel.routes

import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import servermodel.routes.masterroute.MasterRoutePersona._
import servermodel.routes.masterroute.MasterRouteTurni._
import servermodel.routes.masterroute.MasterRouteTerminale._
import servermodel.routes.masterroute.MasterRouteZona._

object Route {
  
  val route: Route = concat(routePersona, routeTurni, routeZona, routeTerminale)
}
