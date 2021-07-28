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
    .headers(headers_authorisation)
    .headers(headers_actor_id)
    .check(status.is(200)))
  .pause(thinkTime)

  val getRoleAssignmentsByActorHC = scenario("Get role assignments by actor (Hardcoded tokens)")

  // gets role assignments by actor
  .exec(http(requestName="AM_040_GetRoleAssignmentsActorHC")
    .get("/am/role-assignments/actors/${actorId}")
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiOHAyaWo4NktKU3hDSnhnL3lKL1dsN043MTFzPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJhbW9naC1wZXJmb3JtYW5jZS10ZXN0ZXJAbWFpbGluYXRvci5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiYjg4ODgwM2EtNGQ4Yi00NjA2LWExYjYtZmQzNWU5YzlhNjU4LTM1OTY3MTI4IiwiaXNzIjoiaHR0cHM6Ly9mb3JnZXJvY2stYW0uc2VydmljZS5jb3JlLWNvbXB1dGUtaWRhbS1wZXJmdGVzdC5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiX0hHOHQxR0RSRFdmT1FjNlRveXpkRkNpV1JvIiwiYXVkIjoiYW1fcm9sZV9hc3NpZ25tZW50IiwibmJmIjoxNjI3NDYzMjM3LCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJhdXRob3JpdGllcyJdLCJhdXRoX3RpbWUiOjE2Mjc0NjMyMzcsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNjI3NDkyMDM3LCJpYXQiOjE2Mjc0NjMyMzcsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJRY0VTTzVLakJkNWNkalg2YU9UZ2djQnl6c28ifQ.G09y7m99fhOCsWMWRa7jEU8H_ggjS6Bw5BenxEMKzeIkhys0LMiQDRuMiWeecCMJCXWt_rpyKzXg0OM_OeQ_ioShY1yp_cYllIkMrMAmv-KoZxZmh827AkYZhZen5iqyc_NHYIb7Dq2SWIYRqL01e6ai0W3J3jLP0OSGMMGGocWrInvquYd1BvOu94HqHelFh7ScsKFEYDO7c_vkGtv5w5B4hso4JdwNptd5tuaPi3i-2y2dCylGwaKG-oLKnk2wftLT6DNoxHNiVnFKkPa0JUFMbRXfizDcOXpRvpVGDLnQ4uVqOH6KXW4GLYwoeWC_ofdHiNw2cszFV_2JAp24gA")
    .header("ServiceAuthorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbV9yb2xlX2Fzc2lnbm1lbnRfc2VydmljZSIsImV4cCI6MTYyNzQ3NzY3MX0.pzEOXcj2YrMo0tFpyUS9birhGONZsx_BMlVmqJpS8pgVUmvltqN5qjWEPk-tMPKlby_mDea-QjJUPgOezGES3g")
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
