package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment

object Scenario1 {
  val Scenario1 = scenario("Scenario1")
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
}
