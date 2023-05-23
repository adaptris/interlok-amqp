package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SimpleFactoryBuilderTest {

  private static final String BROKER_URL = "amqp://admin:admin@localhost:5672/vhost";

  @Test
  public void testBuild() throws Exception {
    SimpleConnectionFactoryBuilder builder = new SimpleConnectionFactoryBuilder().withBrokerUrl(BROKER_URL);
    assertNotNull(builder.build());
  }

  @Test
  public void testBuildWithExceptionHandler() throws Exception {
    SimpleConnectionFactoryBuilder builder = new SimpleConnectionFactoryBuilder().withBrokerUrl(BROKER_URL);
    builder.setExceptionHandler(new AlwaysRestartExceptionHandler());
    assertNotNull(builder.build());
  }

}
