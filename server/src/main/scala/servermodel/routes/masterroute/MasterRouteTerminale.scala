package servermodel.routes.masterroute

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{path, _}
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.{Consumes, POST, Path, Produces}
import javax.ws.rs.core.MediaType
import servermodel.routes.subroute.TerminaleRoute._

/**
 * @author Francesco Cassano, Fabian Aspee Encina
 * This object manage routes that act on the terminale entity and its related entities
 */
trait MasterRouteTerminale{

  @Path("/getterminale")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Get Terminal", description = "Return Terminal from database by id",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def terminale:Route

  @Path("/getallterminale")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Get All Terminal", description = "Return all Terminal from database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get all success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allTerminale:Route

  @Path("/createterminale")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Create Terminal", description = "Create Terminal in database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "create success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def terminaleCreate:Route

  @Path("/createallterminale")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Create All Terminal", description = "Create All Terminal in database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "create all success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allTerminaleCreate:Route

  @Path("/deleteterminale")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Delete Terminal", description = "Delete Terminal in database by id",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "create all success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def terminaleDelete:Route

  @Path("/deleteallterminale")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Delete All Terminal", description = "Delete All Terminal in database by ids",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "delete all success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def allTerminaleDelete:Route

  @Path("/updateterminale")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Update Terminal", description = "Update Terminal in database",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Update success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def terminaleUpdate:Route

  @Path("/getterminalebyzona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(tags = Array("Terminal Operation"),summary = "Get Terminale By Zona", description = "Get all Terminal of one Zona by id",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "get all by id success"),
      new ApiResponse(responseCode = "400", description = "Bad Request"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def terminaleById:Route
}
object MasterRouteTerminale extends MasterRouteTerminale{

  override def terminale: Route =
    path("getterminale") {
      getTerminale
    }

  override def allTerminale: Route =
    path("getallterminale") {
      getAllTerminale
    }

  override def terminaleCreate: Route =
    path("createterminale" ) {
      createTerminale()
    }

  override def allTerminaleCreate: Route =
    path("createallterminale") {
      createAllTerminale()
    }

  override def terminaleDelete: Route =
    path("deleteterminale") {
      deleteTerminale()
    }

  override def allTerminaleDelete: Route =
    path("deleteallterminale") {
      deleteTerminale()
    }

  override def terminaleUpdate: Route =
    path("updateterminale") {
      updateTerminale()
    }

  override def terminaleById: Route =
    path("getterminalebyzona") {
      getTerminaleByZona
    }

  val routeTerminale: Route =
    concat(terminale,allTerminale,terminaleCreate,allTerminaleCreate,terminaleDelete,allTerminaleDelete,terminaleUpdate,
      terminaleById)

}
