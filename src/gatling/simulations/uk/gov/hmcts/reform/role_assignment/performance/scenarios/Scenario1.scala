package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment

object Scenario1 {
  
  val feederFile = csv("Feeder_file.csv").random
  
  val Scenario1 = scenario("Scenario1")
    .feed(feederFile)
    .exec(http(requestName="AM_010_PostRoleAssignments")
      .post("/am/role-assignments")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body.json"))
      .check(status.is(201))
      .check(jsonPath("$..actorId").saveAs("actorId"))
      .check(jsonPath("$..caseId").saveAs("caseId"))
      .check(jsonPath("$..id").saveAs("assignmentId"))
      .check(jsonPath("$..process").saveAs("process"))
      .check(jsonPath("$..reference").saveAs("reference")))
    .pause(2)

    .exec(http(requestName="AM_020_GetRoles")
      .get("/am/role-assignments/roles")
      .headers(Environment.headers_1)
      .check(status.is(200)))
    .pause(2)

    .exec(http(requestName="AM_030_GetRoleAssignmentsActor")
      .get("/am/role-assignments/actors/${actorId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_2)
      .check(status.is(200)))
    .pause(2)

    .exec(http(requestName="AM_040_GetRoleAssignmentsTypeActor")
      .get("/am/role-assignments?roleType=CASE&actorId=${actorId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_3)
      .check(status.is(200)))
    .pause(2)

    .exec(http(requestName="AM_050_GetRoleAssignmentsTypeCase")
      .get("/am/role-assignments?roleType=CASE&caseId=${caseId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_3)
      .check(status.is(200)))
    .pause(2)

    .exec(http(requestName="AM_060_DeleteRoleAssignments")
      .delete("/am/role-assignments/${assignmentId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_5)
      .check(status.is(204)))
    .pause(2)
}
