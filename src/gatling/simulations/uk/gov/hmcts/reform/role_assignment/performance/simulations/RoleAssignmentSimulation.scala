package uk.gov.hmcts.reform.role_assignment.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils._
import scala.concurrent.duration.DurationInt

class RoleAssignmentSimulation extends Simulation{

  val rampUpDurationMins = 2
  val rampDownDurationMins = 2
  val testDurationMins = 60
  val HourlyTarget:Double = 83
  val RatePerSec = HourlyTarget / 3600

  val httpProtocol = http
    //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .baseUrl(Environment.baseURL)

  val feederFile = csv("Feeder_file.csv").circular

  val roleAssignmentScenario = scenario("RoleAssignmentScenario")

        .exec(IDAMHelper.getIdamToken)
        .exec(S2SHelper.S2SAuthToken)
    .repeat(1)
    {
      feed(feederFile)
        .exec(RA_Scenario.RA_Scenario)
    }

  setUp(roleAssignmentScenario.inject(rampUsersPerSec(0.00) to (RatePerSec) during (rampUpDurationMins minutes),
    constantUsersPerSec(RatePerSec) during (testDurationMins minutes),
    rampUsersPerSec(RatePerSec) to (0.00) during (rampDownDurationMins minutes)))
  .protocols(httpProtocol)

}
