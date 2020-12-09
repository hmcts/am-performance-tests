package uk.gov.hmcts.reform.role_assignment.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios._

class RoleAssignmentSimulation extends Simulation{

  val httpProtocol = http.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .baseUrl(Environment.baseURL)

  val feederFile = csv("Feeder_file.csv").random

  val roleAssignmentScenario = scenario("RoleAssignmentScenario")

        .exec(IDAMHelper.getIdamToken)
        .exec(S2SHelper.S2SAuthToken)
    .repeat(100)
    {
      feed(feederFile)
        .exec(Scenario1.Scenario1)
        .exec(Scenario2.Scenario2)
    }

  setUp(roleAssignmentScenario.inject(rampUsers(1) during(3))).protocols(httpProtocol)
  .assertions(global.successfulRequests.percent.is(100))
  //.assertions(forAll.responseTime.percentile3.lte(500))
}
