package scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object S2SHelper {
  
  val S2SAuthToken =

    exec(http("Token_020_GetServiceToken")
      .post(Environment.s2sURL + "/lease")
      .header("Content-Type", "application/json")
      .body(StringBody(
        """{
       "microservice": "am_role_assignment_service"
        }"""
      )).asJson
      .check(bodyString.saveAs("s2sToken")))

    .pause(2)
}