package servermodel.routes.masterroute

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassHttpMessage.InfoAlgorithm
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.subroute.ParametroRoute._

/**
 * @author Fabian Aspée Encina
 * This object manage routes that act on the parametro entity and its related entities
 */
trait MasterRouteParametro {

  @Path("/getalloldparameters")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get All Parameters", description = "Return all parameters existing in database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def oldParameters(): Route

  @Path("/getoldparametersbyid")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get Parameters By Id", description = "Return all parameters that correspond by id",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Int])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def oldParametersById(): Route

  @Path("/saveparameter")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Save Parameters", description = "Save parameter insert in view",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[InfoAlgorithm])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def saveParameter(): Route
}
object MasterRouteParametro  extends Directives with MasterRouteParametro {

  override def oldParameters(): Route =
    path("getalloldparameters") {
      getAllOldParameters
    }

  override def oldParametersById(): Route =
    path("getoldparametersbyid") {
      getParametersById
    }

  override def saveParameter(): Route =
    path("saveparameter") {
      saveParameters
    }

  val routeParametro: Route =
    concat(
      oldParameters(),oldParametersById(),saveParameter()
    )
}
