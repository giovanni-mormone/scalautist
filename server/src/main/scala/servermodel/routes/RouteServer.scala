package servermodel.routes

import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import servermodel.routes.masterroute.MasterRoutePersona.routePersona
import servermodel.routes.masterroute.MasterRouteTurno.routeTurno
import servermodel.routes.masterroute.MasterRouteTerminale.routeTerminale
import servermodel.routes.masterroute.MasterRouteZona.routeZona
import servermodel.routes.masterroute.MasterRouteContratto.routeContratto

/**
 * Object to manage routes
 */
object RouteServer{
  
  val route: Route = concat(routePersona, routeTurno, routeZona, routeTerminale,routeContratto)
}
