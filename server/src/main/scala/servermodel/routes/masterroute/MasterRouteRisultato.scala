package servermodel.routes.masterroute

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassHttpMessage.Dates
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.subroute.PersonaRoute.changePassword

/**
 * @author Francesco Cassano
 * This object manage routes that act on the Risultato entity and its related entities
 */
object MasterRouteRisultato extends Directives {

  @Path("/allabsences")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get all daily Absences", description = "Return all absent employees in a chosen date",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Dates])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "all  Absences back"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allAbsences: Route =
    path("allabsences") {

    }

  val routeRisultato: Route =
}
