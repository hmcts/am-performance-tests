package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._;

object IDAMHelper {

  val thinktime = Environment.thinkTime

  val getIdamToken = 

    exec(http("PaymentAPI_010_015_GetAuthToken")
         .post(Env.getIdamUrl()+"/o/token?client_id="+Env.getOAuthClient()+"&client_secret="+Env.getOAuthSecret()+"&grant_type=password&scope="+Env.getScope()+"&username="+Env.getUsername()+"&password="+Env.getPassword())
         .header("Content-Type", "application/x-www-form-urlencoded")
         .header("Content-Length", "0")
         .check(status is 200)
         .check(jsonPath("$.access_token").saveAs("accessToken")))
    .pause(2)



}
