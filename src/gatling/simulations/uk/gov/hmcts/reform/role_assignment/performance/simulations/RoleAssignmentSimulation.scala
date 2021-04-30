package uk.gov.hmcts.reform.role_assignment.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.feeder.SourceFeederBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import uk.gov.hmcts.reform.role_assignment.performance.scenarios._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils._

import scala.concurrent.duration.DurationInt

class RoleAssignmentSimulation extends Simulation{

  val rampUpDurationMins = 1
  val rampDownDurationMins = 1
  val testDurationMins = 60

  val createCaseHourlyTarget:Double = 300
  val createCaseScenarioRate: Double = createCaseHourlyTarget / 3600

  val createOrgHourlyTarget:Double = 104
  val createOrgHourlyRate: Double = createOrgHourlyTarget / 3600

  val getRolesHourlyTarget:Double = 10
  val getRolesHourlyRate: Double = getRolesHourlyTarget / 3600

  val getRoleAssignmentsByActorIdHourlyTarget:Double = 4992
  val getRoleAssignmentsByActorIdRate: Double = getRoleAssignmentsByActorIdHourlyTarget / 3600

  val queryRoleAssignmentsHourlyTarget:Double = 312
  val queryRoleAssignmentsRate: Double = queryRoleAssignmentsHourlyTarget / 3600

  val deleteRoleAssignmentsHourlyTarget:Double = 390 // There are 2 queries within this block, hence rate = 390*2
  val deleteRoleAssignmentsRate: Double = deleteRoleAssignmentsHourlyTarget / 3600

  val httpProtocol: HttpProtocolBuilder = http
    //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .baseUrl(Environment.baseURL)

  val createFeederFile: SourceFeederBuilder[String] = csv("create.csv").circular
  val caseIdFeederFile: SourceFeederBuilder[String] = csv("case_ids.csv").circular
  val actorIdFeederFile: SourceFeederBuilder[String] = csv("actor_ids.csv").circular
  val assignmentIdFeederFile: SourceFeederBuilder[String] = csv("assignment_ids.csv").circular
  val referencesFeederFile: SourceFeederBuilder[String] = csv("references.csv").circular
  val processesFeederFile: SourceFeederBuilder[String] = csv("processes.csv").circular

  val createRoleAssignmentsCaseScenario = scenario("Create Role Assignments (Case) Scenario")

    .feed(createFeederFile)
    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.createRoleAssignmentsCase)

  val createRoleAssignmentsOrgScenarioReplaceTrue = scenario("Create Role Assignments (Org) Scenario Replace True")

    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .exec(RA_Scenario.createRoleAssignmentsOrgReplaceTrue)

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

  setUp(createRoleAssignmentsCaseScenario.inject(rampUsersPerSec(0.00) to (createCaseScenarioRate) during (rampUpDurationMins minutes),
    constantUsersPerSec(createCaseScenarioRate) during (testDurationMins minutes),
    rampUsersPerSec(createCaseScenarioRate) to (0.00) during (rampDownDurationMins minutes)),

    createRoleAssignmentsOrgScenarioReplaceTrue.inject(rampUsersPerSec(0.00) to (createOrgHourlyRate) during (rampUpDurationMins minutes),
    constantUsersPerSec(createOrgHourlyRate) during (testDurationMins minutes),
    rampUsersPerSec(createOrgHourlyRate) to (0.00) during (rampDownDurationMins minutes)),

    getRolesScenario.inject(rampUsersPerSec(0.00) to (getRolesHourlyRate) during (rampUpDurationMins minutes),
    constantUsersPerSec(getRolesHourlyRate) during (testDurationMins minutes),
    rampUsersPerSec(getRolesHourlyRate) to (0.00) during (rampDownDurationMins minutes)),

    getRoleAssignmentsByActorScenario.inject(rampUsersPerSec(0.00) to (getRoleAssignmentsByActorIdRate) during (rampUpDurationMins minutes),
    constantUsersPerSec(getRoleAssignmentsByActorIdRate) during (testDurationMins minutes),
    rampUsersPerSec(getRoleAssignmentsByActorIdRate) to (0.00) during (rampDownDurationMins minutes)),

    queryRoleAssignmentsScenario.inject(rampUsersPerSec(0.00) to (queryRoleAssignmentsRate) during (rampUpDurationMins minutes),
    constantUsersPerSec(queryRoleAssignmentsRate) during (testDurationMins minutes),
    rampUsersPerSec(queryRoleAssignmentsRate) to (0.00) during (rampDownDurationMins minutes)),

    deleteRoleAssignmentsScenario.inject(rampUsersPerSec(0.00) to (deleteRoleAssignmentsRate) during (rampUpDurationMins minutes),
    constantUsersPerSec(deleteRoleAssignmentsRate) during (testDurationMins minutes),
    rampUsersPerSec(deleteRoleAssignmentsRate) to (0.00) during (rampDownDurationMins minutes))
  )
  .protocols(httpProtocol)

}
