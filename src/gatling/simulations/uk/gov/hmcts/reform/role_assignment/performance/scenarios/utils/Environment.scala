package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  //val baseURL = "http://am-role-assignment-service-aat.service.core-compute-aat.internal"
  val baseURL = "https://idam-api.perftest.platform.hmcts.net"

  val headers_1 = Map(
   "Authorization" -> "Bearer ${accessToken}",
   "serviceAuthorization" -> "${s2sToken}"
  )

  val headers_2 = Map(
  "actorId" -> "${actorId}"
  )

  val headers_3 = Map(
  "roleType" -> "CASE"
  )

  val headers_4 = Map(
  "content-type" -> "application/json"
  )

  val headers_5 = Map(
  "assignmentId" -> "${assignmentId}"
  )

  val feederFile = csv("Feeder_file.csv").random

  val thinkTime = 10

}
