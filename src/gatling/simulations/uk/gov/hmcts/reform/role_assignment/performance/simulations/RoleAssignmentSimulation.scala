package uk.gov.hmcts.reform.role_assignment.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios._

class RoleAssignmentSimulation extends Simulation{

  val httpProtocol = http.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .baseUrl(Environment.baseURL)

  val roleAssignmentScenario = scenario("RoleAssignmentScenario").repeat(1)
    {
         exec(IDAMHelper.getIdamToken)
        .exec(S2SHelper.S2SAuthToken)
        .exec(Scenario1.Scenario1)
        .exec(Scenario2.Scenario2)
    }

  setUp(roleAssignmentScenario.inject(rampUsers(10) during(300))).protocols(httpProtocol)
  .assertions(global.successfulRequests.percent.is(100))
  //.assertions(forAll.responseTime.percentile3.lte(500))
}
