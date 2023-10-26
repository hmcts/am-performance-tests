package scenarios.utils

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val baseURL = "http://am-role-assignment-service-#{env}.service.core-compute-#{env}.internal"
  val idamURL = "https://idam-api.#{env}.platform.hmcts.net"
  val idamClient = "am_role_assignment"
  val idamSecret = ConfigFactory.load.getString("auth.clientSecret")
  val idamScope = "openid%20profile%20roles%20authorities"
  val idamUsername = "amogh-performance-tester@mailinator.com"
  val idamPassword = "Testing1234"
  val s2sURL = "http://rpe-service-auth-provider-#{env}.service.core-compute-#{env}.internal/testing-support"
  val s2sService = "am_role_assignment_service"

  val thinkTime = 10

  val HttpProtocol = http

}
