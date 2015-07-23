package mesosphere.marathon.core.task.tracker

import akka.actor.ActorRef
import akka.event.EventStream
import mesosphere.marathon.MarathonSchedulerDriverHolder
import mesosphere.marathon.core.leadership.LeadershipModule
import mesosphere.marathon.core.task.bus.TaskStatusObservables
import mesosphere.marathon.core.task.tracker.impl.{ TaskStatusUpdateActor, KillOverdueStagedTasksActor }
import mesosphere.marathon.health.HealthCheckManager
import mesosphere.marathon.tasks.{ TaskIdUtil, TaskTracker }

class TaskTrackerModule(leadershipModule: LeadershipModule) {
  def killOverdueTasks(taskTracker: TaskTracker, marathonSchedulerDriverHolder: MarathonSchedulerDriverHolder): Unit = {
    leadershipModule.startWhenLeader(
      KillOverdueStagedTasksActor.props(taskTracker, marathonSchedulerDriverHolder),
      "killOverdueStagedTasks")
  }

  def processTaskStatusUpdates(
    taskStatusObservable: TaskStatusObservables,
    eventBus: EventStream,
    schedulerActor: ActorRef,
    taskIdUtil: TaskIdUtil,
    healthCheckManager: HealthCheckManager,
    taskTracker: TaskTracker,
    marathonSchedulerDriverHolder: MarathonSchedulerDriverHolder): ActorRef = {

    val props = TaskStatusUpdateActor.props(
      taskStatusObservable, eventBus, schedulerActor, taskIdUtil, healthCheckManager, taskTracker,
      marathonSchedulerDriverHolder
    )
    leadershipModule.startWhenLeader(props, "taskStatusUpdate", considerPreparedOnStart = false)
  }
}
