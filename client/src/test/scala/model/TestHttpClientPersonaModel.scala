package model

import akka.http.scaladsl.model.StatusCodes
import caseclass.CaseClassDB.Persona
import model.entity.PersonaModel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.ClientAkkaHttp

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class TestHttpClientPersonaModel extends AsyncFlatSpec with BeforeAndAfterEach with ClientAkkaHttp{

  behavior of "ClientHttpRequestLogin"
  it should "return Persona instance on login request" in {
    val user: String = "admin2"
    val password: String = "admin2"
    val http = PersonaModel.apply()
    val result = Await.result(http.login(user, password), Duration.Inf)
    assert(result.payload.head.isInstanceOf[Persona])
  }

  it should "return None with wrong credential" in {
    val user: String = "Aladin"
    val password: String = "admi"
    val http = PersonaModel.apply()
    val result = Await.result(http.login(user, password), Duration.Inf)
    assert(result.payload.isEmpty)
  }

  it should "Success code when it try to change password" in {
    val user: Int = 2
    val oldPassword: String = "admin2"
    val newPassword: String = "admin2"
    val http = PersonaModel.apply()
    val result = Await.result(http.changePassword(user, oldPassword, newPassword), Duration.Inf)
    assert(result.statusCode.equals(StatusCodes.OK.intValue))
  }

  it should "Not Found code when it try to change password with wrong" in {
    val user: Int = 3
    val oldPassword: String = "min"
    val newPassword: String = "admin2"
    val http = PersonaModel.apply()
    val result = Await.result(http.changePassword(user, oldPassword, newPassword), Duration.Inf)
    assert(result.statusCode.equals(StatusCodes.NotFound.intValue))
  }
}
