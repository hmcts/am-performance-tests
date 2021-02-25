package uk.gov.hmcts.reform.role_assignment.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios._

class RoleAssignmentSimulation extends Simulation{

  val httpProtocol = http.baseUrl(Environment.baseURL)

  val feederFile = csv("Feeder_file.csv").random

  val roleAssignmentScenario = scenario("RoleAssignmentScenario")

        .exec(IDAMHelper.getIdamToken)
        .exec(S2SHelper.S2SAuthToken)
    .repeat(1)
    {
      feed(feederFile)
        .exec(Scenario1.Scenario1)
    }

  setUp(roleAssignmentScenario.inject(rampUsers(100) during(60))).protocols(httpProtocol)
  .assertions(global.successfulRequests.percent.is(100))
  //.assertions(forAll.responseTime.percentile3.lte(500))
}
