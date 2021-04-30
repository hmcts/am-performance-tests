package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment._

object IDAMHelper {

  val thinktime = Environment.thinkTime

  val getIdamToken = 

    exec(http("Token_010_015_GetAuthToken")
         .post(idamURL + "/o/token?client_id=" + idamClient + "&client_secret=" + idamSecret + "&grant_type=password&scope=" + idamScope + "&username=" + idamUsername + "&password=" + idamPassword)
         .header("Content-Type", "application/x-www-form-urlencoded")
         .header("Content-Length", "0")
         .check(status.is(200))
         .check(jsonPath("$.access_token").saveAs("accessToken")))
    .pause(2)



}
