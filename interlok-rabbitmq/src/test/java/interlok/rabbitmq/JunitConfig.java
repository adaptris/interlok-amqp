package interlok.rabbitmq;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import com.adaptris.interlok.junit.scaffolding.BaseCase;
import com.adaptris.util.SafeGuidGenerator;

public class JunitConfig {

  public static final String TESTS_ENABLED = "rabbitmq.tests.enabled";
  private static final String BROKER_URL = "rabbitmq.url";
  public static final String MESSAGE_BODY = "hello world";
  public static final SafeGuidGenerator NAME_GENERATOR = new SafeGuidGenerator();

  // Since CloudAMQP has a free tier, you could use that for your tests.
  // This overrides TESTS_ENABLED if set.
  private static final String CLOUDAMQP_ENV = "CLOUDAMQP_URL";
  
  
  public static void rmqTestsEnabled() {
    if (!cloudAMQP()) {
      assumeTrue(BooleanUtils.toBoolean(BaseCase.getConfiguration(TESTS_ENABLED)));
    }
  }

  private static boolean cloudAMQP() {
    return StringUtils.isNotBlank(System.getenv(CLOUDAMQP_ENV));
  }
  
  public static String brokerURL() {
    if (cloudAMQP()) {
      return System.getenv(CLOUDAMQP_ENV);
    }
    return BaseCase.getConfiguration(BROKER_URL);
  }
}
