package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import caseclass.CaseClassHttpMessage.AssignRichiestaTeorica
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.masterroute.MasterRouteRichiestaTeorica.{concat, path}
import servermodel.routes.subroute.RegolaRoute._


/**
 * @author Fabian Aspée Encina
 *         This object manage routes that act on the regola entity and its related entities
 */
trait MasterRouteRegola {

  @Path("/regolagroup")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Return Regola for Group", description = "Return all Rule for Group ",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[AssignRichiestaTeorica])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Found Rule"),
      new ApiResponse(responseCode = "404", description = "Not Found"),
      new ApiResponse(responseCode = "400", description = "Bad Request"))
  )
  def ruleForGroup(): Route

  @Path("/regolaweek")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Return Regola for Normal and Special Week", description = "Return all Rule for week",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[AssignRichiestaTeorica])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Found Rule For Week"),
      new ApiResponse(responseCode = "404", description = "Not Found"),
      new ApiResponse(responseCode = "400", description = "Bad Request"))
  )
  def ruleForWeek(): Route
}


object MasterRouteRegola extends MasterRouteRegola {

  override def ruleForGroup(): Route =
    path("regolagroup") {
      getRuleForGroup
    }

  override def ruleForWeek(): Route =
    path("regolaweek") {
      getRuleForWeek
    }
  val routeRegola: Route =
    concat(
      ruleForGroup(),ruleForWeek()
    )

}
