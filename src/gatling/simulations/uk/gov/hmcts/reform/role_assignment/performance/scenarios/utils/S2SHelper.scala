package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils

import com.warrenstrange.googleauth.GoogleAuthenticator
import io.gatling.core.Predef._
import io.gatling.http.Predef._;

object  S2SHelper {
  val thinktime = 5

  val getOTP =
  exec(
    session => {
      val otp: String = String.valueOf(new GoogleAuthenticator().getTotpPassword(Env.getS2sSecret))
      session.set("OTP", otp)

    })

  val otpp="${OTP}"

  val S2SAuthToken =

    exec(http("Token_020_GetServiceToken")
      .post(Env.getS2sUrl+"/lease")
      .header("Content-Type", "application/json")
      .body(StringBody(
        s"""{
       "microservice": "${Env.getS2sMicroservice}"
        }"""
      )).asJson
      .check(bodyString.saveAs("s2sToken"))
      .check(bodyString.saveAs("responseBody")))
    .pause(2)
}
