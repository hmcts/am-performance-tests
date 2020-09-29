package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment

object PostScenario {
  val postScenario = scenario("PostScenario")
    .feed(Environment.feederFile)
    .exec(http(requestName="AM_010_PostRoleAssignments")
      .post("/am/role-assignments")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body.json"))
      .check(status.is(201))
      .check(bodyString.saveAs("BODY1"))
      .check(jsonPath("$..actorId").saveAs("actorId"))
      .check(jsonPath("$..caseId").saveAs("caseId"))
      .check(jsonPath("$..id").saveAs("assignmentId"))
      .check(jsonPath("$..process").saveAs("process"))
      .check(jsonPath("$..reference").saveAs("reference")))
    .exec{
      session =>
        println("This is the response body of AM_010_PostRoleAssignments:" + session("BODY1").as[String])
        println("This is the IDAM token:" + session("accessToken").as[String])
        println("This is the S2S token:" + session("s2sToken").as[String])
        session
    }
    .pause(2)
}
