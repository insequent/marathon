package mesosphere.marathon.core.task.tracker.impl

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import mesosphere.marathon.MarathonSpec

class TaskStatusUpdateActorTest extends MarathonSpec {

  private[this] implicit var actorSystem: ActorSystem = _
  private[this] var statusUpdateRef: TestActorRef[_] = _
}
