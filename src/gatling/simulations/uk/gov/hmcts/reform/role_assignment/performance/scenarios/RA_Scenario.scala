package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment._

object RA_Scenario {
    
  private def UUID(): String = java.util.UUID.randomUUID.toString

  val RA_Scenario = scenario("RA Scenario")
    .exec(_.set("UUID",UUID()))
  
  // posts role assignments from body.json
  .exec(http(requestName="AM_010_PostRoleAssignments")
    .post("/am/role-assignments")
    .headers(headers_authorisation)
    .headers(headers_content_type)
    .body(ElFileBody("body.json"))
    .check(status.is(201))
    .check(jsonPath("$..actorId").saveAs("actorId"))
    .check(jsonPath("$..id").find(1).saveAs("assignmentId")))
  .pause(thinkTime)

  // posts role assignments from body3.json
  .exec(http(requestName="AM_020_PostRoleAssignments")
    .post("/am/role-assignments")
    .headers(headers_authorisation)
    .headers(headers_content_type)
    .body(ElFileBody("body3.json"))
    .check(status.is(201))
    .check(jsonPath("$..process").saveAs("process1"))
    .check(jsonPath("$..reference").saveAs("reference1")))
  .pause(thinkTime)

//  // posts role assignments from body4.json
//  .exec(http(requestName="AM_030_PostRoleAssignments")
//    .post("/am/role-assignments")
//    .headers(headers_authorisation)
//    .headers(headers_content_type)
//    .body(ElFileBody("body4.json"))
//    .check(status.is(201))
//    .check(jsonPath("$..process").saveAs("process2"))
//    .check(jsonPath("$..reference").saveAs("reference2")))
//  .pause(thinkTime)

  // gets roles
  .exec(http(requestName="AM_040_GetRoles")
    .get("/am/role-assignments/roles")
    .headers(headers_authorisation)
    .check(status.is(200)))
  .pause(thinkTime)

  // gets role assignments by actor
  .exec(http(requestName="AM_050_GetRoleAssignmentsActor")
    .get("/am/role-assignments/actors/${actorId}")
    .headers(headers_authorisation)
    .headers(headers_actor_id)
    .check(status.is(200)))
  .pause(thinkTime)

  // queries role assignments
  .exec(http(requestName="AM_060_QueryRoleAssignments")
    .post("/am/role-assignments/query")
    .headers(headers_authorisation)
    .headers(headers_content_type)
    .body(ElFileBody("body2.json"))
    .check(status.is(200)))
  .pause(thinkTime)

  // deletes role assignments
  .exec(http(requestName="AM_070_DeleteRoleAssignments")
    .delete("/am/role-assignments/${assignmentId}")
    .headers(headers_authorisation)
    .headers(headers_assignment_id)
    .check(status.is(204)))
  .pause(thinkTime)

  // deletes role assignments by process and reference
  .exec(http(requestName="AM_080_DeleteRoleAssignmentsReference")
    .delete("/am/role-assignments?process=${process1}&reference=${reference1}")
    .headers(headers_authorisation)
    .check(status.is(204)))
  .pause(thinkTime)

  // deletes role assignments by process and reference
//  .exec(http(requestName="AM_090_DeleteRoleAssignmentsReference")
//    .delete("/am/role-assignments?process=${process2}&reference=${reference2}")
//    .headers(headers_authorisation)
//    .check(status.is(204)))
//  .pause(thinkTime)

}
