package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import interlok.rabbitmq.AlwaysRestartExceptionHandler;
import interlok.rabbitmq.BasicConnectionFactoryBuilder;

public class BasicFactoryBuilderTest {

  @Test
  public void testBuild() throws Exception {
    BasicConnectionFactoryBuilder builder = new BasicConnectionFactoryBuilder()
        .withBrokerUrl("amqp://admin:admin@localhost:5672/vhost");
    assertNotNull(builder.build());
  }

  @Test
  public void testBuildWithCredentials() throws Exception  {
    BasicConnectionFactoryBuilder builder = new BasicConnectionFactoryBuilder()
        .withCredentials("admin", "admin")
        .withBrokerUrl("amqp://admin:admin@localhost:5672/vhost");
    builder.setExceptionHandler(new AlwaysRestartExceptionHandler());
    assertNotNull(builder.build());
  }
}
