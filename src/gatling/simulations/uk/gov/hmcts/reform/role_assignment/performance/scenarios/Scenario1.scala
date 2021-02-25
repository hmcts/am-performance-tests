package uk.gov.hmcts.reform.role_assignment.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils.Environment
import scala.util.Random

object Scenario1 {
    
  private val rng: Random = new Random()
  private def String1(): String = rng.alphanumeric.take(10).mkString
  private def String2(): String = rng.alphanumeric.take(10).mkString
  private def UUID(): String = java.util.UUID.randomUUID.toString
  
  //val feederFile = csv("Feeder_file.csv").random
  
  val Scenario1 = scenario("Scenario1")
    //.feed(feederFile)
    .exec(_.setAll(
    ("String1",String1()),
    ("String2",String2()),
    ("UUID",UUID())
  ))
  
    // posts role assignments from body.json
    .exec(http(requestName="AM_010_PostRoleAssignments")
      .post("/am/role-assignments")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body.json"))
      .check(status.is(201))
      .check(jsonPath("$..actorId").saveAs("actorId"))
      .check(jsonPath("$..id").saveAs("assignmentId"))
      .check(jsonPath("$..caseId").saveAs("caseId"))
      .check(jsonPath("$..process").saveAs("process"))
      .check(jsonPath("$..reference").saveAs("reference")))

    // posts role assignments from body3.json
    .exec(http(requestName="AM_020_PostRoleAssignments")
      .post("/am/role-assignments")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body3.json"))
      .check(status.is(201))
      .check(jsonPath("$..process").saveAs("process1"))
      .check(jsonPath("$..reference").saveAs("reference1")))

    // posts role assignments from body4.json
    .exec(http(requestName="AM_030_PostRoleAssignments")
      .post("/am/role-assignments")
      .headers(Environment.headers_1)
      .headers(Environment.headers_4)
      .body(ElFileBody("body4.json"))
      .check(status.is(201))
      .check(jsonPath("$..process").saveAs("process2"))
      .check(jsonPath("$..reference").saveAs("reference2")))

}
