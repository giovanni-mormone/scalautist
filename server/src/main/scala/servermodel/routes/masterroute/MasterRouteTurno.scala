package servermodel.routes.masterroute

import java.sql.Date

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import caseclass.CaseClassDB.{Disponibilita, Turno}
import caseclass.CaseClassHttpMessage.InfoHome
import servermodel.routes.subroute.TurnoRoute._
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
/**
 * @author Francesco Cassano
 * This object manage routes that act on the Turni entity and its related entities
 */
object MasterRouteTurno{

  @Path("/getallturno")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get All Turni", description = "Obtain all turni in database",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Nothing])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Found One or More Turni",
        content = Array(new Content(schema = new Schema(implementation = classOf[List[Turno]])))),
      new ApiResponse(responseCode = "404", description = "Not Found Turni"),
      new ApiResponse(responseCode = "500", description = "Internal server error")))
  def getAllTurniDatabase: Route =
    path("getallturno"){
      getAllTurno
    }

  @Path("/getturniinday")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get turni for one day", description = "Obtain turni for a specific date for a person in db",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Date,Int)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Turni for the day of the person in the date selected",
        content = Array(new Content(schema = new Schema(implementation = classOf[InfoHome])))),
      new ApiResponse(responseCode = "404", description = "Not Found Turni"),
      new ApiResponse(responseCode = "500", description = "Internal server error")))
  def getTurniInDayDatabase: Route =
    path("getturniinday"){
      getTurniInDay
    }

  @Path("/getturniinweek")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get turni settimanali", description = "Get turni settimanali for a specific person in db",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[(Date,Int)])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Disponibilita for the persona selected",
        content = Array(new Content(schema = new Schema(implementation = classOf[List[InfoHome]])))),
      new ApiResponse(responseCode = "404", description = "Not Found Turni"),
      new ApiResponse(responseCode = "500", description = "Internal server error")))
  def getTurniSettimanaliDatabase: Route =
    path("getturniinweek"){
      getTurniSettimanali
    }

  val routeTurno: Route =
    concat(getAllTurniDatabase, getTurniInDayDatabase, getTurniSettimanaliDatabase)

}
