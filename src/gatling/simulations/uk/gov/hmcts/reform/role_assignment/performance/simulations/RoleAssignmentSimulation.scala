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
        .exec(PostScenario.postScenario)
        .exec(GetScenario.getScenario)
        .exec(DeleteScenario.deleteScenario)
    }

  setUp(roleAssignmentScenario.inject(atOnceUsers(users = 1))).protocols(httpProtocol)
}
