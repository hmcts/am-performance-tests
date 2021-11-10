package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment._

object RA_Scenario {

  val createRoleAssignmentsCase = scenario("Create role assignments (Case)")

  // posts role assignments from create_010.json
  .exec(http(requestName="AM_010_PostCaseAssignments")
    .post("/am/role-assignments")
    .headers(headers_authorisation)
    .headers(headers_content_type)
    .body(ElFileBody("create_010.json"))
    .check(status.is(201)))
  .pause(thinkTime)

  val createRoleAssignmentsOrgReplaceTrue = scenario("Create role assignments (Org), replace existing true")
  .exec(_.set("UUID", java.util.UUID.randomUUID.toString))

  // posts role assignments from create_020.json
  .exec(http(requestName="AM_020_PostOrgRoleAssignments")
    .post("/am/role-assignments")
    .headers(headers_authorisation)
    .headers(headers_content_type)
    .body(ElFileBody("create_020.json"))
    .check(status.is(201)))
  .pause(thinkTime)

  val getRoles = scenario("Get roles")

  // gets roles
  .exec(http(requestName="AM_030_GetRoles")
    .get("/am/role-assignments/roles")
    .headers(headers_authorisation)
    .check(status.is(200)))
  .pause(thinkTime)

  val getRoleAssignmentsByActor = scenario("Get role assignments by actor")

  // gets role assignments by actor
  .exec(http(requestName="AM_040_GetRoleAssignmentsActor")
    .get("/am/role-assignments/actors/${actorId}")
    // .headers(headers_authorisation)
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiOHAyaWo4NktKU3hDSnhnL3lKL1dsN043MTFzPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJyYXMtdmFsaWRhdGlvbi1zeXN0ZW1AaG1jdHMubmV0IiwiY3RzIjoiT0FVVEgyX1NUQVRFTEVTU19HUkFOVCIsImF1dGhfbGV2ZWwiOjAsImF1ZGl0VHJhY2tpbmdJZCI6IjE4MDIzNGRiLTRmNjgtNGI5OS04YmYyLWIwMmZmM2MzYmJlYy02NDM5OTA5IiwiaXNzIjoiaHR0cHM6Ly9mb3JnZXJvY2stYW0uc2VydmljZS5jb3JlLWNvbXB1dGUtaWRhbS1wZXJmdGVzdC5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiWml3TlRQeENfOXRuN2FiWnN6ZmxtMDBTX2FBIiwiYXVkIjoiYW1fcm9sZV9hc3NpZ25tZW50IiwibmJmIjoxNjM2NTQzMjc0LCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJhdXRob3JpdGllcyJdLCJhdXRoX3RpbWUiOjE2MzY1NDMyNzQsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNjM2NTcyMDc0LCJpYXQiOjE2MzY1NDMyNzQsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJFTzNsa0xhS21MaU1SeWpxUVVDYmJoLWxkNkUifQ.ANNVF1OHeYGOmJMxl_P3mz5C5_d_pR-FSbVdB9ljkdyP0Q14XWcZHwdTwoQTSXtZf9ZWbmq4AFWabwaiW_LRSG3tfiN5trjeRNsHGBcdS9G_AKhmyGpEkDszfLLa1W4X4LT7B1bxCt9MEmgqemz0lh1AU4x1vSih47k5YgkvcGdijrysCau2vKwBLt9iXA5XAZHZptaSqtDYGuOBge-EEj-mooO3RyGDVkil_fB6_nsT4UJhV39pxWpwJBQtz597JuoT9kbh41e75WXVV-g51_tF0NmztcPxgKERxsads2p0-AvlZ9rt6IFzuQx-J0cFGbH1dD_8-ZQ4a3nmq3rfdg")
    .header("ServiceAuthorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbV9yb2xlX2Fzc2lnbm1lbnRfc2VydmljZSIsImV4cCI6MTYzNjU1NzcwN30.MbkyxTQxKY821aA6fz93jFn0goilMJWVvzU4hUVOwO7EQjgk45-ugqqXrlJU_vCOi4mYeiMe0WvOiEGRvPpo5w")
    .headers(headers_actor_id)
    .check(status.is(200)))
  .pause(thinkTime)

  val getRoleAssignmentsByActorHC = scenario("Get role assignments by actor (Hardcoded tokens)")

  // gets role assignments by actor with hardcoded tokens
  .exec(http(requestName="AM_040_GetRoleAssignmentsActorHC")
    .get("/am/role-assignments/actors/${actorId}")
    .header("Authorization", "Bearer ")
    .header("ServiceAuthorization", "")
    .headers(headers_actor_id)
    .check(status.is(200)))
  .pause(thinkTime)

  val queryRoleAssignments = scenario("Query role assignments")

  // queries role assignments
  .exec(http(requestName="AM_050_QueryRoleAssignments")
    .post("/am/role-assignments/query")
    .headers(headers_authorisation)
    .headers(headers_content_type)
    .body(ElFileBody("query.json"))
    .check(status.is(200)))
  .pause(thinkTime)

  val deleteRoleAssignments = scenario("Delete role assignments")

  // deletes role assignments
  .exec(http(requestName="AM_060_DeleteRoleAssignments")
    .delete("/am/role-assignments/${assignmentId}")
    .headers(headers_authorisation)
    .headers(headers_assignment_id)
    .check(status.is(204)))
  .pause(thinkTime)

  // deletes role assignments by process and reference
  .exec(http(requestName="AM_070_DeleteRoleAssignmentsReference")
    .delete("/am/role-assignments?process=${process}&reference=${reference}")
    .headers(headers_authorisation)
    .check(status.is(204)))
  .pause(thinkTime)

  val enhancedDelete = scenario ("Enhanced Delete")

  .exec(http(requestName="AM_080_EnhancedDelete")
    .post("/am/role-assignments/query/delete")
    .headers(headers_authorisation)
    // .header("Authorization", "Bearer ")
    // .header("ServiceAuthorization", "")
    .header("Content-Type", "application/json")
    .body(ElFileBody("EnhancedDelete.json")))
  .pause(thinkTime)

}
