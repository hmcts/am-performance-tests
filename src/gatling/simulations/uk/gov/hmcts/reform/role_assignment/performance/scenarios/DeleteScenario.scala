package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment

object DeleteScenario {
  val deleteScenario = scenario("DeleteScenario")
    .exec(http(requestName="AM_060_DeleteRoleAssignments")
      .delete("/am/role-assignments/${assignmentId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_5)
      .check(status.is(204))
      .check(bodyString.saveAs("BODY6")))
    .exec{
      session =>
        println("This is the response body of AM_060_DeleteRoleAssignments:" + session("BODY6").as[String])
        session
    }
    .pause(2)

    .exec(http(requestName="AM_060_DeleteRoleAssignmentsReference")
      .delete("/am/role-assignments?process=${process}&reference=${reference}")
      .headers(Environment.headers_1)
      .check(status.is(204))
      .check(bodyString.saveAs("BODY7")))
    .exec{
      session =>
        println("This is the response body of AM_060_DeleteRoleAssignmentsReference:" + session("BODY7").as[String])
        session
    }
    .pause(2)
}
