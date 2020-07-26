package servermodel.routes.masterroute

import java.sql.Date

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{Dates, Id, Request}
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.subroute.StipendioRoute._

/**
 * @author Fabian Aspée Encina
 *         This object manage routes that act on the stipendio entity and its related entities
 */
trait MasterRouteStipendio {

  @Path("/getinfostipendio")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get Information Salary", description = "Information for salary of the person in the date selected",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Request[(Date, Int)]])))),
    responses = Array(
      new ApiResponse(responseCode = "302", description = "Found person and return stipendio",
        content = Array(new Content(schema = new Schema(implementation = classOf[Stipendio])))),
      new ApiResponse(responseCode = "404", description = "Not Found person and return None"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def getInfoStipendio: Route

  @Path("/getstipendio")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get Salary", description = "get salary for a person by id",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Id])))),
    responses = Array(
      new ApiResponse(responseCode = "302", description = "Found person and return stipendio",
        content = Array(new Content(schema = new Schema(implementation = classOf[Stipendio])))),
      new ApiResponse(responseCode = "404", description = "Not Found person and return None"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def getSalaryPersona: Route

  @Path("/calcolostipendio")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Salary Calculus", description = "calculus all salary for all person into database, this operation is make one time in the month",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Dates])))),
    responses = Array(
      new ApiResponse(responseCode = "201", description = "calculus salary create"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def salaryCalculusAll: Route
}
object MasterRouteStipendio extends Directives with MasterRouteStipendio {

  override def salaryCalculusAll: Route =
    path("calcolostipendio"){
      salaryCalculus()
    }

  override def getSalaryPersona: Route =
    path("getstipendio"){
      getStipendio
    }

  override def getInfoStipendio: Route =
    path("getinfostipendio"){
      infoStipendio
    }

  val routeStipendio: Route =
    concat(getInfoStipendio, salaryCalculusAll,getSalaryPersona)
}
