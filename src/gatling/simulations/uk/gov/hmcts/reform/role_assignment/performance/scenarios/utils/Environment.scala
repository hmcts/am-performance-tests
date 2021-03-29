package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils
import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val env = "aat"
  val baseURL = "http://am-role-assignment-service-" + env + ".service.core-compute-" + env + ".internal"
  val idamURL = "https://idam-api." + env + ".platform.hmcts.net"
  val idamClient = "am_role_assignment"
  //val idamClient = "paybubble"
  val idamSecret = ConfigFactory.load.getString("auth.clientSecret")
  val idamScope = "openid%20profile%20roles%20authorities"
  val idamUsername = "befta.caseworker.2.solicitor.2@gmail.com"
  //val idamUsername = "amogh.role.assignments@mailinator.com"
  val idamPassword = "PesZvqrb78"
  //val idamPassword = "Pass19word"
  val s2sURL = "http://rpe-service-auth-provider-" + env + ".service.core-compute-" + env + ".internal/testing-support"
  val s2sService = "am_role_assignment_service"
  val s2sSecret = ConfigFactory.load.getString("aat_service.pass")


  val headers_authorisation = Map(
   "Authorization" -> "Bearer ${accessToken}",
   "serviceAuthorization" -> "${s2sToken}"
  )

  val headers_actor_id = Map(
  "actorId" -> "${actorId}"
  )

  val headers_content_type = Map(
  "content-type" -> "application/json"
  )

  val headers_assignment_id = Map(
  "assignmentId" -> "${assignmentId}"
  )

  val thinkTime = 10

}
