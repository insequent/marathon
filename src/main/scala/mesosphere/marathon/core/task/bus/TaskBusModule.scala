package mesosphere.marathon.core.task.bus

import mesosphere.marathon.core.task.bus.impl.{
  TaskStatusEmitterImpl,
  TaskStatusObservablesImpl,
  InternalTaskStatusEventStream
}

class TaskBusModule {
  lazy val taskStatusEmitter: TaskStatusEmitter =
    new TaskStatusEmitterImpl(internalTaskStatusEventStream)
  lazy val taskStatusObservables: TaskStatusObservables =
    new TaskStatusObservablesImpl(internalTaskStatusEventStream)

  private[this] lazy val internalTaskStatusEventStream = new InternalTaskStatusEventStream()
}
