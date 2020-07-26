package servermodel.routes.masterroute

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassHttpMessage.AssignRichiestaTeorica
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.exception.SuccessAndFailure.timeoutResponse
import servermodel.routes.subroute.RichiestaTeoricaRoute._
import scala.concurrent.duration._

/**
 * @author Francesco Cassano
 *         This object manage routes that act on the RichiestaTeorica entity and its related entities
 */
trait MasterRouteRichiestaTeorica {

  @Path("/definedailyrequest")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Save Theoretical request", description = "Save all information about theoretical request",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[AssignRichiestaTeorica])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "replace success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def saveRequest(): Route
}


object MasterRouteRichiestaTeorica extends Directives with MasterRouteRichiestaTeorica {

  override def saveRequest(): Route =
    path("definedailyrequest") {
      withRequestTimeout(10 minute) {
        withRequestTimeoutResponse(request => timeoutResponse) {
          saveRichiestaTeorica()
        }
      }
    }

  val routeRichiestaTeorica: Route =
    concat(
      saveRequest()
    )
}
