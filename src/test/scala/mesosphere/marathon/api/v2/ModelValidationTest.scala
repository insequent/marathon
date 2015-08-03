package mesosphere.marathon.api.v2

import mesosphere.marathon.api.v2.json.{ V2AppDefinition, V2GroupUpdate }
import mesosphere.marathon.state.Container.Docker.PortMapping
import mesosphere.marathon.state.Container._
import mesosphere.marathon.state.PathId._
import mesosphere.marathon.state._
import mesosphere.marathon.{ MarathonSchedulerService, MarathonSpec }
import org.mockito.Mockito.when
import org.scalatest.{ BeforeAndAfterAll, Matchers, OptionValues }

import scala.collection.immutable.Seq

class ModelValidationTest
    extends MarathonSpec
    with Matchers
    with BeforeAndAfterAll
    with OptionValues {

  test("A group update should pass validation") {
    val update = V2GroupUpdate(id = Some("/a/b/c".toPath))

    val violations = ModelValidation.checkGroupUpdate(update, true)
    violations should have size (0)
  }

  test("Model validation should catch new apps that conflict with service ports in existing apps") {

    val existingApp = createServicePortApp("/app1".toPath, 3200)
    val service = mock[MarathonSchedulerService]
    when(service.listApps()).thenReturn(List(existingApp.toAppDefinition))

    val conflictingApp = createServicePortApp("/app2".toPath, 3200).toAppDefinition
    val validations = ModelValidation.checkAppConflicts(conflictingApp, service)

    validations should not be Nil
  }

  test("Model validation should allow new apps that do not conflict with service ports in existing apps") {

    val existingApp = createServicePortApp("/app1".toPath, 3200)
    val service = mock[MarathonSchedulerService]
    when(service.listApps()).thenReturn(List(existingApp.toAppDefinition))

    val conflictingApp = createServicePortApp("/app2".toPath, 3201).toAppDefinition
    val validations = ModelValidation.checkAppConflicts(conflictingApp, service)

    validations should be(Nil)
  }

  test("Model validation should check for application conflicts") {
    val existingApp = createServicePortApp("/app1".toPath, 3200)
    val service = mock[MarathonSchedulerService]
    when(service.listApps()).thenReturn(List(existingApp.toAppDefinition))

    val conflictingApp = existingApp.copy(id = "/app2".toPath).toAppDefinition
    val validations = ModelValidation.checkAppConflicts(conflictingApp, service)

    validations should not be Nil
  }

  test("Null groups should be validated correctly") {
    val result = ModelValidation.checkGroupUpdate(null, needsId = true)
    result should have size 1
    result.head.getMessage should be("Given group is empty!")
  }

  private def createServicePortApp(id: PathId, servicePort: Int) =
    V2AppDefinition(
      id,
      container = Some(Container(
        docker = Some(Docker(
          image = "demothing",
          network = "bridge",
          portMappings = Some(Seq(PortMapping(2000, servicePort = servicePort)))
        ))
      ))
    )

}
