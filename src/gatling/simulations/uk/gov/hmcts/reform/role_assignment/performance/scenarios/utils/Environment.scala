package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils
import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val env = "perftest"
  val baseURL = "http://am-role-assignment-service-" + env + ".service.core-compute-" + env + ".internal"
  val idamURL = "https://idam-api." + env + ".platform.hmcts.net"
  val idamClient = "am_role_assignment"
  //val idamClient = "paybubble"
  val idamSecret = ""
  val idamScope = "openid%20profile%20roles%20authorities"
  //val idamUsername = "befta.caseworker.2.solicitor.2@gmail.com"
  val idamUsername = "amogh-performance-tester@mailinator.com"
  //val idamPassword = "PesZvqrb78"
  val idamPassword = "Testing1234"
  val s2sURL = "http://rpe-service-auth-provider-" + env + ".service.core-compute-" + env + ".internal/testing-support"
  val s2sService = "am_role_assignment_service"
  val s2sSecret = ""


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

  val thinkTime = 10

}
