package servermodel.routes.masterroute

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, Dates}
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.subroute.RisultatoRoute._

/**
 * @author Francesco Cassano, Fabian Aspee Encina
 * This object manage routes that act on the Risultato entity and its related entities
 */
object MasterRouteRisultato extends Directives {

  @Path("/replaceshift")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Replace shift", description = "Reassign a shift to another employee",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int, Int)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "replace success"),
      new ApiResponse (responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def replaceShift(): Route =
    path("replaceshift") {
      updateShift()
    }

  @Path("/executealgorithm")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Run Algorithm", description = "Run algorithm for obtained free day and shift",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[AlgorithmExecute])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "run success"),
      new ApiResponse (responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def executeAlgorithm(): Route =
    path("executealgorithm") {
      runAlgorithm()
    }

  @Path("/getresultalgorithm")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get Result Algorithm", description = "Return result of algorithm from dateI to dateF",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int,Dates,Dates)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse (responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def resultAlgorithm(): Route =
    path("getresultalgorithm") {
      getResultAlgorithm
    }

  @Path("/getalloldparameters")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get All Parameters", description = "Return all parameters existing in database",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int,Dates,Dates)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse (responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def oldParameters(): Route =
    path("getalloldparameters") {
      getAllOldParameters
    }


  @Path("/getoldparametersbyid")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get Parameters By Id", description = "Return all parameters that correspond by id",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Int,Dates,Dates)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse (responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def oldParametersById(): Route =
    path("getoldparametersbyid") {
      getParametersById
    }
  val routeRisultato: Route =
    concat(
      replaceShift(),executeAlgorithm(),resultAlgorithm(),oldParameters(),oldParametersById()
    )
}
