package swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.{Contact, Info}
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.models.ExternalDocumentation
import servermodel.routes.masterroute._


object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses: Set[Class[_]] = Set(MasterRoutePersona.getClass,MasterRouteAssenza.getClass,MasterRouteContratto.getClass
    ,MasterRouteDisponibilita.getClass,MasterRouteStipendio.getClass,MasterRouteTerminale.getClass,MasterRouteTurno.getClass
    ,MasterRouteZona.getClass,MasterRouteParametro.getClass,MasterRouteRisultato.getClass,MasterRouteRichiestaTeorica.getClass
    ,MasterRouteRegola.getClass
    )
  override val host = "localhost:8080"
  override val apiDocsPath = "api-docs"
  override val info: Info = Info(description = "Information of all method availability for consume"
  ,"1.0","Scalautist",contact = Option(Contact("Aspee F.,Cassano F., Mormone G.","","giovanni.mormone@studio.unibo.it")))
  override val externalDocs: Option[ExternalDocumentation] = Some(new ExternalDocumentation().description("Find out more about").url("http://swagger.io"))
  override val schemes:List[String] = List("http")
  //override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}
