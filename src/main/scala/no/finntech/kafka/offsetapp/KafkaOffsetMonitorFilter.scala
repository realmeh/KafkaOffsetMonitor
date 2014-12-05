package no.finntech.kafka.offsetapp

import unfiltered.filter.Plan
import kafka.utils.Logging
import no.finntech.commons.service.Log4jConfigurator
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit
import com.quantifind.kafka.offsetapp.{OffsetGetterWeb, OWArgs}
import org.constretto.util.StaticlyCachedConfiguration.config
import org.constretto.ConstrettoConfiguration

class KafkaOffsetMonitorFilter extends Plan with Logging {
  Log4jConfigurator.startUp()

  object KafkaOffsetMonitorFilter {
    lazy val plan = OffsetGetterWeb.setup(new ConstrettoArgs)
  }

  override def intent: Plan.Intent = KafkaOffsetMonitorFilter.plan.intent
}


class ConstrettoArgs extends OWArgs {
  lazy val monitorConfig: ConstrettoConfiguration = {
    config("classpath:kafka.ini")
  }

  zk = monitorConfig.evaluateToString("zk")
  refresh = FiniteDuration(monitorConfig.evaluateToLong("refresh"), TimeUnit.SECONDS)
  retain = FiniteDuration(monitorConfig.evaluateToLong("retain"), TimeUnit.DAYS)
}
