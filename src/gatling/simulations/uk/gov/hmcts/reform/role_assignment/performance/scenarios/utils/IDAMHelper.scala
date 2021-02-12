package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment._

object IDAMHelper {

  val thinktime = Environment.thinkTime

  val getIdamToken = 

    exec(http("Token_010_015_GetAuthToken")
         .post(idamURL + "/o/token?client_id=" + "am_docker" + "&client_secret=" + "am_docker_secret" + "&grant_type=password&scope=" + "openid%20profile%20roles%20authorities" + "&username=" + "TEST_AM_USER4_BEFTA@test.local" + "&password=" + "Pa55word11")
         .header("Content-Type", "application/x-www-form-urlencoded")
         .header("Content-Length", "0")
         .check(status.is(200))
         .check(jsonPath("$.access_token").saveAs("accessToken")))
    .pause(2)



}