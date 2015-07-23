package mesosphere.marathon.core.task.bus

import mesosphere.marathon.core.task.bus.impl.{
  DefaultTaskStatusEmitter,
  DefaultTaskStatusObservables,
  InternalTaskStatusEventStream
}

class TaskBusModule {
  lazy val taskStatusEmitter: TaskStatusEmitter =
    new DefaultTaskStatusEmitter(internalTaskStatusEventStream)
  lazy val taskStatusObservables: TaskStatusObservables =
    new DefaultTaskStatusObservables(internalTaskStatusEventStream)

  private[this] lazy val internalTaskStatusEventStream = new InternalTaskStatusEventStream()
}
