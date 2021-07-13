package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.{ASBHelper, Environment}

object Scenario2 {



  val url = "sb://rd-servicebus-sandbox.servicebus.windows.net"
  val keyName = "SendAndListenSharedAccessKey"
  val keyVale = "aWiUfjUgY9kUteIqJ3h5CoHfgVwVFjMbt2rel0VBleo="

  private def sasToken(): String = String.valueOf(ASBHelper.getSaSToken(url, keyName, keyVale))

  val Scenario2 = scenario("Scenario2")
    //.feed(feederFile)
    .exec(_.setAll(
      ("sasToken",sasToken())
    ))

    .exec(http(requestName="ORM_050_publishCaseworkers")
      .post("/messages")
      .headers(Environment.headers_asb_auth)
      .headers(Environment.headers_4)
      .body(ElFileBody("caseworker_ids.json"))
      .check(status.is(201)))

}
