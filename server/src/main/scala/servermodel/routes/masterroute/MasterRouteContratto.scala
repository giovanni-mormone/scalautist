package servermodel.routes.masterroute

import akka.http.scaladsl.server.Directives.{concat, path}
import akka.http.scaladsl.server.Route
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.subroute.ContrattoRoute._

/**
 * @author Fabian Aspée Encina
 * This object manage routes that act on the contratto entity and its related entities
 */
trait MasterRouteContratto{

  @Path("/getcontratto")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags =Array("Contract Operation") ,summary = "Get Contract", description = "Return Contract from database by id",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def contratto:Route

  @Path("/getallcontratto")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags =Array("Contract Operation") ,summary = "Get All Contract", description = "Return all Contract existing in database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get all success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allContratto:Route

  @Path("/createcontratto")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags =Array("Contract Operation") ,summary = "Create Contract", description = "Insert Contract into database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "create success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def contrattoCreate:Route

  @Path("/updatecontratto")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags =Array("Contract Operation") ,summary = "Update Contract", description = "Update Contrat into database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "update success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def contrattoUpdate:Route
}
object MasterRouteContratto extends MasterRouteContratto {

  override def contratto: Route =
    path("getcontratto") {
      getContratto
    }

  override def allContratto: Route =
    path("getallcontratto") {
      getAllContratto
    }

  override def contrattoCreate: Route =
    path("createcontratto" ) {
      createContratto()
    }

  override def contrattoUpdate: Route =
    path("updatecontratto") {
      updateContratto()
    }

  val routeContratto: Route =
    concat(contratto,allContratto,contrattoCreate,contrattoUpdate)
}
