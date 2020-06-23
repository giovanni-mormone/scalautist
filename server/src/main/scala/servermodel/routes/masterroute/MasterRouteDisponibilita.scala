package servermodel.routes.masterroute

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassHttpMessage.Dates
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.subroute.DisponibilitaRoute._

/**
 *
 */
object MasterRouteDisponibilita extends Directives {

  @Path("/extraavailability")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Look for available employees", description = "Search available employees to replace a shift",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int, Int, Int)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "OK"),
      new ApiResponse (responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def extraAvailability(): Route =
    path("extraavailability") {
      getExtraAvailability
    }

  @Path("/getdisponibilitainweek")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Return possible days to overtime", description = "Return possible days to assign availability to overtime",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int, Dates)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "OK"),
      new ApiResponse (responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def chooseExtra(): Route =
    path("getdisponibilitainweek") {
      getAvailability
    }

  val routeDisponibilita: Route =
    concat (
      extraAvailability(), chooseExtra()
      )

}
