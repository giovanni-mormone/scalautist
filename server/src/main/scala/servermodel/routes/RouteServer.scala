package servermodel.routes

import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import servermodel.routes.masterroute.MasterRoutePersona._
import servermodel.routes.masterroute.MasterRouteTurno._
import servermodel.routes.masterroute.MasterRouteTerminale._
import servermodel.routes.masterroute.MasterRouteZona._

/**
 * Object to manage routes
 */
object RouteServer{
  
  val route: Route = concat(routePersona, routeTurno, routeZona, routeTerminale)
}
