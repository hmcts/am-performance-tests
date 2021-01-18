package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment

object Scenario2 {
  

  val Scenario2 = scenario("Scenario2")
    //.feed(feederFile)
    .exec(http(requestName="AM_090_PostRoleAssignments")
      .post("/am/role-assignments")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body.json"))
      .check(status.is(201))
      .check(jsonPath("$..caseId").saveAs("caseId"))
      .check(jsonPath("$..process").saveAs("process"))
      .check(jsonPath("$..reference").saveAs("reference")))
    .pause(7)

    .exec(http(requestName="AM_100_QueryRoleAssignments")
      .post("/am/role-assignments/query")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body2.json"))
      .check(status.is(200)))
    .pause(7)

    .exec(http(requestName="AM_110_DeleteRoleAssignmentsReference")
      .delete("/am/role-assignments?process=${process}&reference=${reference}")
      .headers(Environment.headers_1)
      .check(status.is(204)))
    .pause(7)
}
