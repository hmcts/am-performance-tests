package scenarios.utils

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object IDAMHelper {

  val config: Config = ConfigFactory.load()
  val thinktime = Environment.thinkTime
  val idamScope = "openid%20profile%20roles%20authorities"
  val idamSecret = config.getString("auth.clientSecret")

  val getIdamToken = 

    exec(http("Token_010_015_GetAuthToken")
      .post(Environment.idamURL + "/o/token?client_id=am_role_assignment&client_secret=" + idamSecret + "&grant_type=password&scope=" + idamScope + "&username=" + Environment.idamUsername + "&password=" + Environment.idamPassword)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Content-Length", "0")
      .check(status.is(200))
      .check(jsonPath("$.access_token").saveAs("accessToken")))

    .pause(2)
}