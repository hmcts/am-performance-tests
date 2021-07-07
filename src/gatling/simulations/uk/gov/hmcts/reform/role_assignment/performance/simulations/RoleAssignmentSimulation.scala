package uk.gov.hmcts.reform.role_assignment.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.feeder.SourceFeederBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import uk.gov.hmcts.reform.role_assignment.performance.scenarios._
import uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils._

import scala.concurrent.duration.DurationInt

class RoleAssignmentSimulation extends Simulation{

  val httpProtocol = 
    http
    //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .baseUrl(Environment.baseURL)

  val createFeederFile: SourceFeederBuilder[String] = csv("create.csv").circular
  val caseIdFeederFile: SourceFeederBuilder[String] = csv("case_ids.csv").circular
  val actorIdFeederFile: SourceFeederBuilder[String] = csv("actor_ids.csv").circular
  val actorIdFeederFileAC: SourceFeederBuilder[String] = csv("actor_cache_control_202107061809_New.csv").random
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

  val getRoleAssignmentsByActorScenarioAccessControl = scenario("Get Role Assignments By Actor Scenario (For Access Control)")
    
    .exec(IDAMHelper.getIdamToken)
    .exec(S2SHelper.S2SAuthToken)
    .repeat(10) {
      feed(actorIdFeederFileAC)
      .exec(RA_Scenario.getRoleAssignmentsByActor)
    }
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

  // setUp(createRoleAssignmentsCaseScenario.inject(rampUsersPerSec(0.00) to (createCaseRate) during (rampUpDurationMins minutes),
  //   constantUsersPerSec(createCaseRate) during (testDurationMins minutes),
  //   rampUsersPerSec(createCaseRate) to (0.00) during (rampDownDurationMins minutes)),

  //   createRoleAssignmentsOrgScenarioReplaceTrue.inject(rampUsersPerSec(0.00) to (createOrgRate) during (rampUpDurationMins minutes),
  //   constantUsersPerSec(createOrgRate) during (testDurationMins minutes),
  //   rampUsersPerSec(createOrgRate) to (0.00) during (rampDownDurationMins minutes)),

  //   getRolesScenario.inject(rampUsersPerSec(0.00) to (getRolesRate) during (rampUpDurationMins minutes),
  //   constantUsersPerSec(getRolesRate) during (testDurationMins minutes),
  //   rampUsersPerSec(getRolesRate) to (0.00) during (rampDownDurationMins minutes)),

  //   getRoleAssignmentsByActorScenario.inject(rampUsersPerSec(0.00) to (getRoleAssignmentsByActorRate) during (rampUpDurationMins minutes),
  //   constantUsersPerSec(getRoleAssignmentsByActorRate) during (testDurationMins minutes),
  //   rampUsersPerSec(getRoleAssignmentsByActorRate) to (0.00) during (rampDownDurationMins minutes)),

  //   queryRoleAssignmentsScenario.inject(rampUsersPerSec(0.00) to (queryRoleAssignmentsRate) during (rampUpDurationMins minutes),
  //   constantUsersPerSec(queryRoleAssignmentsRate) during (testDurationMins minutes),
  //   rampUsersPerSec(queryRoleAssignmentsRate) to (0.00) during (rampDownDurationMins minutes)),

  //   deleteRoleAssignmentsScenario.inject(rampUsersPerSec(0.00) to (deleteRoleAssignmentsRate) during (rampUpDurationMins minutes),
  //   constantUsersPerSec(deleteRoleAssignmentsRate) during (testDurationMins minutes),
  //   rampUsersPerSec(deleteRoleAssignmentsRate) to (0.00) during (rampDownDurationMins minutes))
  // )
  // .protocols(httpProtocol)

  setUp(getRoleAssignmentsByActorScenarioAccessControl.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
