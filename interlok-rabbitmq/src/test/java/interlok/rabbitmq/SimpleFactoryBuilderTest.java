package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import interlok.rabbitmq.AlwaysRestartExceptionHandler;
import interlok.rabbitmq.SimpleConnectionFactoryBuilder;

public class SimpleFactoryBuilderTest {

  @Test
  public void testBuild() throws Exception {
    SimpleConnectionFactoryBuilder builder = new SimpleConnectionFactoryBuilder()
        .withBrokerUrl("amqp://admin:admin@localhost:5672/vhost");
    assertNotNull(builder.build());
  }

  @Test
  public void testBuildWithExceptionHandler() throws Exception  {
    SimpleConnectionFactoryBuilder builder = new SimpleConnectionFactoryBuilder()
        .withBrokerUrl("amqp://admin:admin@localhost:5672/vhost");
    builder.setExceptionHandler(new AlwaysRestartExceptionHandler());
    assertNotNull(builder.build());
  }
}
