package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import servermodel.routes.subroute.PersonaRoute._

/**
 * @author Francesco Cassano, Fabian Aspée Encina
 * This object manage routes that act on the persona entity and its related entities
 */
object MasterRoutePersona {



  val routePersona: Route =
    concat(
      path("getpersona" ) {
        getPersona
      },
      path("getallpersona") {
        getAllPersona
      },
      path("hirePerson" ) {
        hirePerson
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
      },
      path("changepassword") {            //TODO update password
        changePassword()
      },
      path("recoverypassword"){
        recoveryPassword()
      },
      path("calcolostipendio"){
        salaryCalculus()
      },
      path("getstipendio"){
        getStipendio
      }
     /* path("addabsence") {          //TODO add absence
          addAbsence()
      },*/
      /*path("getnewpassword") {            //TODO update password
        getNewPassword()
      }*/
    )
}
