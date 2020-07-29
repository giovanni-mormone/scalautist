package servermodel.routes.masterroute

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassDB.Disponibilita
import caseclass.CaseClassHttpMessage.{Dates, Id}
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.exception.SuccessAndFailure.timeoutResponse
import servermodel.routes.subroute.DisponibilitaRoute._

import scala.concurrent.duration.DurationInt
/**
 * @author  Fabian Aspée Encina
 * This object manage routes that act on the disponibilita entity and its related entities
 */
trait MasterRouteDisponibilita {

  @Path("/setdisponibilita")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Set new date of possible day for extra shift", description = "Save the available days to possible extra shift",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Disponibilita, Id)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "OK"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def setAvailability(): Route

  @Path("/getdisponibilitainweek")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Return possible days to overtime", description = "Return possible days to assign availability to overtime",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int, Dates)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "OK"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def chooseExtra(): Route

  @Path("/extraavailability")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Look for available employees", description = "Search available employees to replace a shift",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int, Int, Int)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "OK"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def extraAvailability(): Route
}

object MasterRouteDisponibilita extends Directives with MasterRouteDisponibilita {

  override def extraAvailability(): Route =
    path("extraavailability") {
      withRequestTimeout(10 minute) {
        withRequestTimeoutResponse(request => timeoutResponse) {
          getExtraAvailability
        }
      }
    }

  override def chooseExtra(): Route =
    path("getdisponibilitainweek") {
      getAvailability
    }

  override def setAvailability(): Route =
    path("setdisponibilita") {
      setExtraAvailability
    }

  val routeDisponibilita: Route =
    concat (
      extraAvailability(), chooseExtra(), setAvailability()
    )

}
