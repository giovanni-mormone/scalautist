import caseclass.CaseClassDB.Persona
import model.entity.PersonaModel
import model.utils.ResponceCode
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait HttpClientRequest {
  //Await.result(DatabaseHelper.runScript(), Duration.Inf)
}

class TestHttpClientPersonaModel extends AsyncFlatSpec with BeforeAndAfterEach with HttpClientRequest {

  behavior of "ClientHttpRequestLogin"
  it should "return Persona instance on login request" in {
    val user: String = "admin2"
    val password: String = "admin2"
    val http = PersonaModel.apply()
    val result = Await.result(http.login(user, password), Duration.Inf)
    assert(result.head.isInstanceOf[Persona])
  }

  it should "return None with wrong credential" in {
    val user: String = "Aladin"
    val password: String = "admi"
    val http = PersonaModel.apply()
    val result = Await.result(http.login(user, password), Duration.Inf)
    assert(!result.isDefined)
  }

  it should "Success code when it try to change password" in {
    val user: Int = 2
    val oldPassword: String = "admin2"
    val newPassword: String = "admin2"
    val http = PersonaModel.apply()
    val result = Await.result(http.changePassword(user, oldPassword, newPassword), Duration.Inf)
    assert(result.equals(ResponceCode.Success))
  }

  it should "Not Found code when it try to change password with wrong" in {
    val user: Int = 3
    val oldPassword: String = "min"
    val newPassword: String = "admin2"
    val http = PersonaModel.apply()
    val result = Await.result(http.changePassword(user, oldPassword, newPassword), Duration.Inf)
    assert(result.equals(ResponceCode.NotFound))
  }
}
