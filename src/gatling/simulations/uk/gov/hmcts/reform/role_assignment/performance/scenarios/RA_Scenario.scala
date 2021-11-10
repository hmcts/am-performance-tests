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
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiOHAyaWo4NktKU3hDSnhnL3lKL1dsN043MTFzPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJhbW9naC1wZXJmb3JtYW5jZS10ZXN0ZXJAbWFpbGluYXRvci5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiNDBmNTAwNWEtNTUxNC00M2NkLWJkYzctOGQxY2FiMmIxN2ZjLTY1MjQ2MzgiLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiIweGtrVWhLVlJKd1EyblFVR2gwbHRHbWhlWk0iLCJhdWQiOiJhbV9yb2xlX2Fzc2lnbm1lbnQiLCJuYmYiOjE2MzY1NTg1NTMsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJyb2xlcyIsImF1dGhvcml0aWVzIl0sImF1dGhfdGltZSI6MTYzNjU1ODU1MywicmVhbG0iOiIvaG1jdHMiLCJleHAiOjE2MzY1ODczNTMsImlhdCI6MTYzNjU1ODU1MywiZXhwaXJlc19pbiI6Mjg4MDAsImp0aSI6Ik11TThtaFNDc09zMDBNZFFVOUU0RVRReThtbyJ9.etbECL4BDrH8FlRfPI47WPb5LrRNRwQLSBZefL33aFXnmW73VoGtBSg4wMfG8Qfw53swXK8H90wc1LFjuHganw_ghy5QOrF2WH-4MuczMIY3R8zeVGm2iMTnWOxVM4IkEgnp0tIoZV7vr5t-eyMVd7rxDIvvdYBhzMIlt547k-AwxRG8hf3HpSH5DH7Tm_HdPMm3DOqU8VRp1kdOccnPcui2qow8Ox7fibUTNe7Craz7wzXOKrLeMRC8ESNgphOZxdyNERXvHUKB753sv6PFJA_90HthcaxGuwb1u8wwC90qDKZGAJxYKUTG0H6Au3aPHEX1elQLiWAGMx9yOayMWw")
    .header("ServiceAuthorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbV9yb2xlX2Fzc2lnbm1lbnRfc2VydmljZSIsImV4cCI6MTYzNjU3Mjk1Nn0.yXJbm15zAnmVZ3l0aM83rarG6xDk2nG0wZZPbmLPWNRuTNQ_lq-_cXOowF3BBkChxFdjYA4Xze2NpEail57Izw")
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
