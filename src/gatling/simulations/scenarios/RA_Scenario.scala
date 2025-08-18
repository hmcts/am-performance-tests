package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._

object RA_Scenario {
    
  private def UUID(): String = java.util.UUID.randomUUID.toString

  val caseIdFeederFile = csv("case_ids.csv").shuffle.circular
  val actorFeeder = csv("Feeder_file.csv").random

  val RA_Scenario = 

    exec(_.set("UUID",UUID()))

    .feed(actorFeeder)
  
    // posts role assignments from body.json
    .exec(http(requestName="AM_010_PostRoleAssignments")
      .post("/am/role-assignments")
      .header("Authorization", "Bearer #{accessToken}")
      .header("serviceAuthorization", "#{s2sToken}")
      .header("content-type", "application/json")
      .body(ElFileBody("body.json"))
      .check(status.is(201))
      .check(jsonPath("$..actorId").saveAs("actorId"))
      .check(jsonPath("$..id").find(1).saveAs("assignmentId")))

    .pause(Environment.thinkTime)

    // posts role assignments from body3.json
    .exec(http(requestName="AM_020_PostRoleAssignments")
      .post("/am/role-assignments")
      .header("Authorization", "Bearer #{accessToken}")
      .header("serviceAuthorization", "#{s2sToken}")
      .header("content-type", "application/json")
      .body(ElFileBody("body3.json"))
      .check(status.is(201))
      .check(jsonPath("$..process").saveAs("process1"))
      .check(jsonPath("$..reference").saveAs("reference1")))

    .pause(Environment.thinkTime)

  //  // posts role assignments from body4.json
    .exec(http(requestName="AM_030_PostRoleAssignments")
      .post("/am/role-assignments")
      .header("Authorization", "Bearer #{accessToken}")
      .header("serviceAuthorization", "#{s2sToken}")
      .header("content-type", "application/json")
      .body(ElFileBody("body4.json"))
      .check(status.is(201))
      .check(jsonPath("$..process").saveAs("process2"))
      .check(jsonPath("$..reference").saveAs("reference2")))

    .pause(Environment.thinkTime)

    // gets roles
    .exec(http(requestName="AM_040_GetRoles")
      .get("/am/role-assignments/roles")
      .header("Authorization", "Bearer #{accessToken}")
      .header("serviceAuthorization", "#{s2sToken}")
      .check(status.is(200)))

    .pause(Environment.thinkTime)

    // queries role assignments
    .repeat(50){

      feed(caseIdFeederFile)
      
      .exec(http(requestName="AM_060_QueryRoleAssignments")
        .post("/am/role-assignments/query")
        .header("Authorization", "Bearer #{accessToken}")
        .header("serviceAuthorization", "#{s2sToken}")
        .header("content-type", "application/json")
        .body(ElFileBody("body2.json"))
        .check(status.is(200)))

      .pause(Environment.thinkTime)
    }

    // deletes role assignments
    .exec(http(requestName="AM_070_DeleteRoleAssignments")
      .delete("/am/role-assignments/#{assignmentId}")
      .header("Authorization", "Bearer #{accessToken}")
      .header("serviceAuthorization", "#{s2sToken}")
      .header("assignmentId", "#{assignmentId}")
      .check(status.is(204)))

    .pause(Environment.thinkTime)

    // deletes role assignments by process and reference
    .exec(http(requestName="AM_080_DeleteRoleAssignmentsReference")
      .delete("/am/role-assignments?process=#{process1}&reference=#{reference1}")
      .header("Authorization", "Bearer #{accessToken}")
      .header("serviceAuthorization", "#{s2sToken}")
      .check(status.is(204)))

    .pause(Environment.thinkTime)

    // deletes role assignments by process and reference
  //  .exec(http(requestName="AM_090_DeleteRoleAssignmentsReference")
  //    .delete("/am/role-assignments?process=#{process2}&reference=#{reference2}")
  //    .header("Authorization", "Bearer #{accessToken}")
      // .header("serviceAuthorization", "#{s2sToken}")
  //    .check(status.is(204)))

  //  .pause(Environment.thinkTime)

  val getActorById =

    // gets role assignments by actor
    repeat(200) {

      feed(actorFeeder)

      .exec(http(requestName = "AM_050_GetRoleAssignmentsActor")
        .get("/am/role-assignments/actors/#{actor_id}")
        .header("Authorization", "Bearer #{accessToken}")
        .header("serviceAuthorization", "#{s2sToken}")
        .header("actorId", "#{actor_id}")
        .check(status.is(200)))

      .pause(Environment.thinkTime)
  }

}