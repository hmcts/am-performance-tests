package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment

object Scenario2 {
  

  val Scenario2 = scenario("Scenario2")
    //.feed(feederFile)
    // posts role assignments from body4.json
    .pause(10)
    .exec(http(requestName="AM_080_PostRoleAssignments")
      .post("/am/role-assignments")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body4.json"))
      .check(status.is(201))
      .check(jsonPath("$..caseId").saveAs("caseId"))
      .check(jsonPath("$..process").saveAs("process"))
      .check(jsonPath("$..reference").saveAs("reference")))
    .pause(5)

    // queries role assignments
    .exec(http(requestName="AM_090_QueryRoleAssignments")
      .post("/am/role-assignments/query")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body2.json"))
      .check(status.is(200)))
    .pause(5)

    // deletes role assignments based on process and reference
    .exec(http(requestName="AM_100_DeleteRoleAssignmentsReference")
      .delete("/am/role-assignments?process=${process}&reference=${reference}")
      .headers(Environment.headers_1)
      .check(status.is(204)))
    .pause(5)
}
