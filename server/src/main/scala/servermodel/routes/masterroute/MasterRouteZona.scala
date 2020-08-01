package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.{Consumes, POST, Path, Produces}
import javax.ws.rs.core.MediaType
import servermodel.routes.subroute.ZonaRoute._

/**
 * @author Francesco Cassano, Fabian Aspee Encina
 * This object manage routes that act on the zona entity and its related entities
 */
trait MasterRouteZona{

  @Path("/getallzona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Zona Operation"),summary = "Get All Zona", description = "Return All Zone existing in database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allZona:Route

  @Path("/createzona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Zona Operation"),summary = "Create Zona", description = "Create a Zona into database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "create success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def zonaCreate:Route

  @Path("/createallzona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Zona Operation"),summary = "Create All Zona", description = "Create all Zona into database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "create all success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allZonaCreate:Route

  @Path("/deletezona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Zona Operation"),summary = "Delete Zona", description = "Delete a Zona by id",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "delete success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def zonaDelete:Route

  @Path("/deleteallzona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Zona Operation"),summary = "Delete All Zona", description = "Delete all zona by ids",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "delete all success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allZonaDelete:Route

  @Path("/updatezona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Zona Operation"),summary = "Update Zona", description = "Update Zona into database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "update success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def zonaUpdate:Route
}
object MasterRouteZona extends MasterRouteZona{

  override def allZona: Route =
    path("getallzona") {
      getAllZona
    }

  override def zonaCreate: Route =
    path("createzona" ) {
      createZona()
    }

  override def allZonaCreate: Route =
    path("createallzona") {
      createAllZona()
    }

  override def zonaDelete: Route =
    path("deletezona") {
      deleteZona()
    }

  override def allZonaDelete: Route =
    path("deleteallzona") {
      deleteAllZona()
    }

  override def zonaUpdate: Route =
    path("updatezona") {
      updateZona()
    }

  val routeZona: Route =
    concat(allZona,zonaCreate,allZonaCreate,zonaDelete,allZonaDelete,zonaUpdate)
}
