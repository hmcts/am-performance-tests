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
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiOHAyaWo4NktKU3hDSnhnL3lKL1dsN043MTFzPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJhbW9naC1wZXJmb3JtYW5jZS10ZXN0ZXJAbWFpbGluYXRvci5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiYWFhNzZiYjktMjliMi00MmUzLWFjNjEtMDAwOWE4MWNiZDE4LTM0MDM4MDgxIiwiaXNzIjoiaHR0cHM6Ly9mb3JnZXJvY2stYW0uc2VydmljZS5jb3JlLWNvbXB1dGUtaWRhbS1wZXJmdGVzdC5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiWTNfTVQ5blE5MnhGT29iT1Ixdm1fVU51Z0ZzIiwiYXVkIjoiYW1fcm9sZV9hc3NpZ25tZW50IiwibmJmIjoxNjI2MjU4NjYzLCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJhdXRob3JpdGllcyJdLCJhdXRoX3RpbWUiOjE2MjYyNTg2NjMsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNjI2Mjg3NDYzLCJpYXQiOjE2MjYyNTg2NjMsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJ6T3RzZXNmaE9YdFhMLURlLVBDSTlLcVcyWUUifQ.ZY9hyDcecb2OdCp01k-MQRIlnn024PqEJ2_dmGrPCa9hXC8DNsiFuYKy1-OCIenQfbfw6wLhUqo0-Sg6HGsSSOmCwlW6B1sFEIwiaSmBg4r2POB23cRAkjfoCgmtW7TxvGfusAa1m8ic4YA_MvzhZEgp54V9NPve1GQOpZ6Gtf_WwsGxSmcEA-J6_yCNUAUtTSjJIwdAtWFl-LQdv9caanAZ6UJ55orQFTSD-00erKTDRjP4Vn9j6LfgKFY_6ai0mMHRcQoKENNMntiyIm84NEJ4ywumkft93ijHjsAPF-TGf_RnrztSHHYhHGVUgRuGzwn4dT0uhw7z73qlieA96A")
    .header("serviceAuthorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbV9yb2xlX2Fzc2lnbm1lbnRfc2VydmljZSIsImV4cCI6MTYyNjI3MzA2NX0.xsekD9skFooImICDupeYbTdZC_ZmT5cJBxuGY080UkuvNEr4uIm1AvF6TAr0gCWVUYVrvIM_QGE0aYRVwFimLg")
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

}
