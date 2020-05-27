package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import servermodel.routes.subroute.PersonaRoute._

object MasterRoutePersona {

  val routePersona: Route =
    concat(
      path("dummyPerson") {
        methodDummy()
      },
      path("getpersona" / IntNumber) {
        id => getPersona(id)
      },
      path("getallpersona") {
        getAllPersona
      },
      path("createpersona" ) {
        createPersona()
      },
      path("createallpersona") {
        createAllPersona()
      },
      path("deletepersona") {
        deletePersona()
      },
      path("deleteallpersona") {
        deleteAllPersona()
      },
      path("updatepersona") {
        updatePersona()
      },
      path("loginpersona") {
        loginPersona()
      }
    )
}
