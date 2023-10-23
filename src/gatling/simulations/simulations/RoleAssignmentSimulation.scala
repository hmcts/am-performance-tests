package simulations

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation
import scenarios._
import scenarios.utils._
import scala.concurrent.duration._
import io.gatling.core.controller.inject.open.{AtOnceOpenInjection, OpenInjectionStep}
import io.gatling.commons.stats.assertion.Assertion
import io.gatling.core.pause.PauseType

class RoleAssignmentSimulation extends Simulation{

  /* TEST TYPE DEFINITION */
	/* pipeline = nightly pipeline against the AAT environment (see the Jenkins_nightly file) */
	/* perftest (default) = performance test against the perftest environment */
	val testType = scala.util.Properties.envOrElse("TEST_TYPE", "perftest")

	//set the environment based on the test type
	val environment = testType match{
		case "perftest" => "perftest"
		case "pipeline" => "aat"
		case _ => "**INVALID**"
	}

	/* ******************************** */
	/* ADDITIONAL COMMAND LINE ARGUMENT OPTIONS */
	val debugMode = System.getProperty("debug", "off") //runs a single user e.g. ./gradlew gatlingRun -Ddebug=on (default: off)
	val env = System.getProperty("env", environment) //manually override the environment aat|perftest e.g. ./gradlew gatlingRun -Denv=aat
	/* ******************************** */

  /* PERFORMANCE TEST CONFIGURATION */
	val roleAssignmentTarget:Double = 100

  val rampUpDurationMins = 10
	val rampDownDurationMins = 10
	val testDurationMins = 60

	val numberOfPipelineUsers = 5
	val pipelinePausesMillis:Long = 3000 //3 seconds

	//Determine the pause pattern to use:
	//Performance test = use the pauses defined in the scripts
	//Pipeline = override pauses in the script with a fixed value (pipelinePauseMillis)
	//Debug mode = disable all pauses
	val pauseOption:PauseType = debugMode match{
		case "off" if testType == "perftest" => constantPauses
		case "off" if testType == "pipeline" => customPauses(pipelinePausesMillis)
		case _ => disabledPauses
	}

  //Gatling specific configs, required for perf testing
  val BaseURL = Environment.baseURL
  val config: Config = ConfigFactory.load()

  val httpProtocol = Environment.HttpProtocol
    .baseUrl(Environment.baseURL.replace("#{env}", s"${env}"))
    .doNotTrackHeader("1")
    .disableCaching

  val feederFile = csv("Feeder_file.csv").shuffle
  val createFeederFile = csv("create.csv").circular
  val caseIdFeederFile = csv("case_ids.csv").circular
  val actorIdFeederFile = csv("role_assignments.csv").random
  // val actorIdFeederFile = csv("actor_cache_control_202107081104-V1.0.csv").random
  val assignmentIdFeederFile = csv("assignment_ids.csv").circular
  val referencesFeederFile = csv("references.csv").circular
  val processesFeederFile = csv("processes.csv").circular

  val roleAssignmentScenario = scenario("Role Assignment Scenario")
    .exitBlockOnFail {
      exec(_.set("env", s"${env}"))
      .exec(IDAMHelper.getIdamToken)
      .exec(S2SHelper.S2SAuthToken)
      .feed(feederFile)
      .feed(caseIdFeederFile)
      .exec(RA_Scenario.RA_Scenario)
    }

	def simulationProfile(simulationType: String, userPerHourRate: Double, numberOfPipelineUsers: Double): Seq[OpenInjectionStep] = {
		val userPerSecRate = userPerHourRate / 3600
		simulationType match {
			case "perftest" =>
				if (debugMode == "off") {
					Seq(
						rampUsersPerSec(0.00) to (userPerSecRate) during (rampUpDurationMins.minutes),
						constantUsersPerSec(userPerSecRate) during (testDurationMins.minutes),
						rampUsersPerSec(userPerSecRate) to (0.00) during (rampDownDurationMins.minutes)
					)
				}
				else{
					Seq(atOnceUsers(1))
				}
			case "pipeline" =>
				Seq(rampUsers(numberOfPipelineUsers.toInt) during (2.minutes))
			case _ =>
				Seq(nothingFor(0))
		}
	}

  //defines the test assertions, based on the test type
  def assertions(simulationType: String): Seq[Assertion] = {
    simulationType match {
      case "perftest" =>
        if (debugMode == "off") {
          Seq(global.successfulRequests.percent.gte(95)
          )
        }
        else{
          Seq(global.successfulRequests.percent.gte(95)
          )
        }
      case "pipeline" =>
        Seq(global.successfulRequests.percent.gte(95),
            forAll.successfulRequests.percent.gte(90)
        )
      case _ =>
        Seq()
    }
  }

  setUp(
    roleAssignmentScenario.inject(simulationProfile(testType, roleAssignmentTarget, numberOfPipelineUsers)).pauses(pauseOption),
  )
  .protocols(httpProtocol)
  .assertions(assertions(testType))
  .maxDuration(85.minutes)
}
