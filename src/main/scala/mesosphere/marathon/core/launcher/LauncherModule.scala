package mesosphere.marathon.core.launcher

import mesosphere.marathon.MarathonSchedulerDriverHolder
import mesosphere.marathon.core.base.Clock
import mesosphere.marathon.core.launcher.impl.{ OfferProcessorImpl, TaskLauncherImpl }
import mesosphere.marathon.core.matcher.base.OfferMatcher
import mesosphere.marathon.metrics.Metrics

class LauncherModule(
    clock: Clock,
    metrics: Metrics,
    offerProcessorConfig: OfferProcessorConfig,
    marathonSchedulerDriverHolder: MarathonSchedulerDriverHolder,
    offerMatcher: OfferMatcher) {

  lazy val offerProcessor: OfferProcessor =
    new OfferProcessorImpl(
      offerProcessorConfig, clock,
      metrics,
      offerMatcher, taskLauncher)

  lazy val taskLauncher: TaskLauncher = new TaskLauncherImpl(
    marathonSchedulerDriverHolder,
    clock)
}
