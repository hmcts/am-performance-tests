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
  val HourlyTarget1:Double = 78
  val RatePerSec1 = HourlyTarget1 / 3600
  val HourlyTarget2:Double = 6240
  val RatePerSec2 = HourlyTarget2 / 3600
  val HourlyTarget3:Double = 1
  val RatePerSec3 = HourlyTarget3 / 3600
  val HourlyTarget4:Double = 4992
  val RatePerSec4 = HourlyTarget4 / 3600
  val HourlyTarget5:Double = 296
  val RatePerSec5 = HourlyTarget5 / 3600
  val HourlyTarget6:Double = 780
  val RatePerSec6 = HourlyTarget6 / 3600

  val httpProtocol = http
    //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .baseUrl(Environment.baseURL)

  val createFeederFile = csv("create.csv").circular
  val caseIdFeederFile = csv("case_ids.csv").circular
  val actorIdFeederFile = csv("actor_ids.csv").circular
  val assignmentIdFeederFile = csv("assignment_ids.csv").circular
  val referencesFeederFile = csv("references.csv").circular
  val processesFeederFile = csv("processes.csv").circular

  val createRoleAssignmentsCaseScenario = scenario("Create Role Assignments (Case) Scenario")

    .feed(createFeederFile)
    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.createRoleAssignmentsCase)

  val createRoleAssignmentsOrgScenario = scenario("Create Role Assignments (Org) Scenario")

    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.createRoleAssignmentsOrg)

  val getRolesScenario = scenario("Get Roles Scenario")

    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.getRoles)

  val getRoleAssignmentsByActorScenario = scenario("Get Role Assignments By Actor Scenario")

    .feed(actorIdFeederFile)
    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.getRoleAssignmentsByActor)

  val queryRoleAssignmentsScenario = scenario("Query Role Assignments Scenario")

    .feed(caseIdFeederFile)
    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.queryRoleAssignments)

  val deleteRoleAssignmentsScenario = scenario("Delete Role Assignments Scenario")

    .feed(assignmentIdFeederFile)
    .feed(referencesFeederFile)
    .feed(processesFeederFile)
    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.deleteRoleAssignments)

  setUp(createRoleAssignmentsCaseScenario.inject(rampUsersPerSec(0.00) to (RatePerSec1) during (rampUpDurationMins minutes),
    constantUsersPerSec(RatePerSec1) during (testDurationMins minutes),
    rampUsersPerSec(RatePerSec1) to (0.00) during (rampDownDurationMins minutes)),

    createRoleAssignmentsOrgScenario.inject(rampUsersPerSec(0.00) to (RatePerSec2) during (rampUpDurationMins minutes),
    constantUsersPerSec(RatePerSec2) during (testDurationMins minutes),
    rampUsersPerSec(RatePerSec2) to (0.00) during (rampDownDurationMins minutes)),

    getRolesScenario.inject(rampUsersPerSec(0.00) to (RatePerSec3) during (rampUpDurationMins minutes),
    constantUsersPerSec(RatePerSec3) during (testDurationMins minutes),
    rampUsersPerSec(RatePerSec3) to (0.00) during (rampDownDurationMins minutes)),

    getRoleAssignmentsByActorScenario.inject(rampUsersPerSec(0.00) to (RatePerSec4) during (rampUpDurationMins minutes),
    constantUsersPerSec(RatePerSec4) during (testDurationMins minutes),
    rampUsersPerSec(RatePerSec4) to (0.00) during (rampDownDurationMins minutes)),

    queryRoleAssignmentsScenario.inject(rampUsersPerSec(0.00) to (RatePerSec5) during (rampUpDurationMins minutes),
    constantUsersPerSec(RatePerSec5) during (testDurationMins minutes),
    rampUsersPerSec(RatePerSec5) to (0.00) during (rampDownDurationMins minutes)),

    deleteRoleAssignmentsScenario.inject(rampUsersPerSec(0.00) to (RatePerSec6) during (rampUpDurationMins minutes),
    constantUsersPerSec(RatePerSec6) during (testDurationMins minutes),
    rampUsersPerSec(RatePerSec6) to (0.00) during (rampDownDurationMins minutes))
  )
  .protocols(httpProtocol)

}
