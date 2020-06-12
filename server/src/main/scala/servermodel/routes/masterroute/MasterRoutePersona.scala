package servermodel.routes.masterroute

import akka.http.scaladsl.server.{Directives, Route}
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.{Assumi, ChangePassword, Id}
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import javax.ws.rs.core.MediaType
import javax.ws.rs.{Consumes, POST, Path, Produces}
import servermodel.routes.subroute.PersonaRoute._
/**
 * @author Francesco Cassano, Fabian Aspée Encina
 * This object manage routes that act on the persona entity and its related entities
 */
object MasterRoutePersona extends Directives{

  @Path("/getpersona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get Persona", description = "Get one person by Id",
    requestBody =new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Id])))),
    responses = Array(
      new ApiResponse(responseCode = "302", description = "Found Person",
        content = Array(new Content(schema = new Schema(implementation = classOf[Persona])))),
      new ApiResponse(responseCode = "404", description = "Not Found Person"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def getPersonaDatabase: Route =
    path("getpersona" ) {
      getPersona
    }

  @Path("/getallpersona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Get All Person", description = "Obtain all person in database",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Nothing])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Found One or More Person",
        content = Array(new Content(schema = new Schema(implementation = classOf[List[Persona]])))),
      new ApiResponse(responseCode = "404", description = "Not Found Person"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def getAllPersonaDatabase: Route =
    path("getallpersona") {
      getAllPersona
    }

  @Path("/hireperson")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Hire a person into system", description = "Hire person that can be a driver",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Assumi])))),
    responses = Array(
      new ApiResponse(responseCode = "201", description = "Hire Person",
        content = Array(new Content(schema = new Schema(implementation = classOf[Option[Login]])))),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def getHirePersonDatabase: Route =
    path("hireperson" ) {
      hirePerson
    }

  @Path("/deletepersona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Delete Person", description = "Delete a person by Id",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Id])))),
    responses = Array(
      new ApiResponse(responseCode = "410", description = "Person is not available in future",
        content = Array(new Content(schema = new Schema(implementation = classOf[Nothing])))),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def deletePersonaDatabase(): Route =
    path("deletepersona") {
      deletePersona()
    }

  @Path("/deleteallpersona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Delete list of Person", description = "Delete a list of person by Id",
    requestBody =  new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[List[Id]])))),
    responses = Array(
      new ApiResponse(responseCode = "410", description = "Persons is not available in future",
        content = Array(new Content(schema = new Schema(implementation = classOf[Nothing])))),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def deleteAllPersonaDatabase(): Route =
    path("deleteallpersona") {
      deleteAllPersona()
    }

  @Path("/updatepersona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Update Person", description = "Update persons if this exist in other case make a insert",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Persona])))),
    responses = Array(
      new ApiResponse(responseCode = "201", description = "Insert Person into database",
        content = Array(new Content(schema = new Schema(implementation = classOf[Id])))),
      new ApiResponse(responseCode = "200", description = "Update person"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def updatePersonaDatabase(): Route =
    path("updatepersona") {
      updatePersona()
    }

  @Path("/loginpersona")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Log-in Person", description = "Login person in the system",
    parameters = Array(new Parameter(name = "username", in = ParameterIn.PATH, description = "login"),
      new Parameter(name = "password", in = ParameterIn.PATH, description = "login")),
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Login])))),
    responses = Array(
      new ApiResponse(responseCode = "302", description = "Correct Login",
        content = Array(new Content(schema = new Schema(implementation = classOf[Persona])))),
      new ApiResponse(responseCode = "404", description = "Incorrect Login"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def loginPersonaDatabase: Route =
    path("loginpersona") {
      loginPersona()
    }

  @Path("/changepassword")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Change Password", description = "Change the password into sistem for one person",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[ChangePassword])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "change password"),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def changePasswordPersona: Route =
    path("changepassword") {
      changePassword()
    }

  @Path("/recoverypassword")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Recovery Password", description = "Recovery password for a person",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Id])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Password change and return login with new credential",
        content = Array(new Content(schema = new Schema(implementation = classOf[Login])))),
      new ApiResponse(responseCode = "500", description = "Internal server error"))
  )
  def recoveryPasswordPersona: Route =
    path("recoverypassword"){
      recoveryPassword()
    }




  val routePersona: Route =
    concat(
      getAllPersonaDatabase,getPersonaDatabase,getHirePersonDatabase,
      deletePersonaDatabase(),deleteAllPersonaDatabase(),updatePersonaDatabase(),
      loginPersonaDatabase,changePasswordPersona,recoveryPasswordPersona)
}
