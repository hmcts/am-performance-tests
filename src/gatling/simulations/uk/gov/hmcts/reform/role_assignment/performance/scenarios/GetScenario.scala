package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment

object GetScenario {
  val getScenario = scenario("GetScenario")
    .exec(http(requestName="AM_020_GetRoleAssignments")
      .get("/am/role-assignments/roles")
      .headers(Environment.headers_1)
      .check(status.is(200))
      .check(bodyString.saveAs("BODY2")))
    .exec{
      session =>
        println("This is the response body of AM_020_GetRoleAssignments:" + session("BODY2").as[String])
        session
    }
    .pause(2)

    .exec(http(requestName="AM_030_GetRoleAssignmentsActor")
      .get("/am/role-assignments/actors/${actorId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_2)
      .check(status.is(200))
      .check(bodyString.saveAs("BODY3")))
    .exec{
      session =>
        println("This is the response body of AM_030_GetRoleAssignmentsActor:" + session("BODY3").as[String])
        session
    }
    .pause(2)


    .exec(http(requestName="AM_040_GetRoleAssignmentsTypeActor")
      .get("/am/role-assignments?roleType=CASE&actorId=${actorId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_3)
      .check(status.is(200))
      .check(bodyString.saveAs("BODY4")))
    .exec{
      session =>
        println("This is the response body of AM_040_GetRoleAssignmentsTypeActor:" + session("BODY4").as[String])
        session
    }
    .pause(2)

    .exec(http(requestName="AM_050_GetRoleAssignmentsTypeCase")
      .get("/am/role-assignments?roleType=CASE&caseId=${caseId}")
      .headers(Environment.headers_1)
      .headers(Environment.headers_3)
      .check(status.is(200))
      .check(bodyString.saveAs("BODY5")))
    .exec{
      session =>
        println("This is the response body of AM_050_GetRoleAssignmentsTypeCase:" + session("BODY5").as[String])
        session
    }
    .pause(2)
}
