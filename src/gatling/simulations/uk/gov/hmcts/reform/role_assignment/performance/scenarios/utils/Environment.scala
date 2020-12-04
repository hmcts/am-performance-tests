package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils
import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val env = "aat"
  val baseURL = "http://localhost:4096"
  val idamURL = "http://localhost:5000"
  val idamClient = "am_role_assignment"
  //val idamClient = "paybubble"
  val idamSecret = ConfigFactory.load.getString("auth.clientSecret")
  val idamScope = "openid%20profile%20roles%20authorities"
  val idamUsername = "befta.caseworker.2.solicitor.2@gmail.com"
  //val idamUsername = "amogh.role.assignments@mailinator.com"
  val idamPassword = "PesZvqrb78"
  //val idamPassword = "Pass19word"
  val s2sURL = "http://localhost:4502"
  val s2sService = "am_role_assignment_service"
  val s2sSecret = ConfigFactory.load.getString("aat_service.pass")


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
