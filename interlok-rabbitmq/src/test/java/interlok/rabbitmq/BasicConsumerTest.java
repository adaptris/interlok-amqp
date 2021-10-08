package interlok.rabbitmq;

import static interlok.rabbitmq.JunitConfig.MESSAGE_BODY;
import static interlok.rabbitmq.JunitConfig.NAME_GENERATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.stubs.MockMessageListener;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.interlok.junit.scaffolding.ExampleConsumerCase;
import com.rabbitmq.client.AMQP.BasicProperties;

public class BasicConsumerTest extends ExampleConsumerCase {

  private static final String METADATA_KEY = "MY_QUEUE";

  @Test
  public void testConsumeLocationKey() throws Exception {
    String queueName = NAME_GENERATOR.safeUUID();
    BasicConsumer rmq = new BasicConsumer().withQueue(queueName);
    assertEquals(MetadataConstants.RMQ_QUEUE, rmq.consumeLocationKey());
  }

  @Test
  public void testConsume() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    String queueName = NAME_GENERATOR.safeUUID();

    MockMessageListener stub = new MockMessageListener();

    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(new AlwaysRestartExceptionHandler());

    BasicConsumer rmq = new BasicConsumer().withQueue(queueName);

    StandaloneConsumer consumer = new StandaloneConsumer(conn, rmq);
    consumer.registerAdaptrisMessageListener(stub);

    try (BlancDeBouscat client = new BlancDeBouscat(brokerUrl, queueName)) {
      LifecycleHelper.initAndStart(consumer);
      client.publish(MESSAGE_BODY);

      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> stub.getMessages().size() >= 1);

    } finally {
      LifecycleHelper.stopAndClose(consumer);
    }
    assertTrue(stub.getMessages().size() >= 1);
    assertEquals(MESSAGE_BODY, stub.getMessages().get(0).getContent());
  }

  @Test
  public void testConsume_WithHandlers() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    String queueName = NAME_GENERATOR.safeUUID();

    MockMessageListener stub = new MockMessageListener();

    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(new AlwaysRestartExceptionHandler());

    BasicConsumer rmq =
        new BasicConsumer().withQueue(queueName).withEnvelopeHandler(new EnvelopeToMetadata())
            .withPropertiesHandler(new PropertiesToMetadata());

    StandaloneConsumer consumer = new StandaloneConsumer(conn, rmq);
    consumer.registerAdaptrisMessageListener(stub);

    BasicProperties properties = new BasicProperties.Builder()
        .correlationId("MyCorrelationId")
        .build();
    
    try (BlancDeBouscat client = new BlancDeBouscat(brokerUrl, queueName)) {
      LifecycleHelper.initAndStart(consumer);
      client.publish(MESSAGE_BODY, properties);

      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> stub.getMessages().size() >= 1);

    } finally {
      LifecycleHelper.stopAndClose(consumer);
    }
    assertTrue(stub.getMessages().size() >= 1);
    AdaptrisMessage msg = stub.getMessages().get(0);
    assertEquals(MESSAGE_BODY, msg.getContent());
    assertTrue(msg.headersContainsKey(MetadataConstants.RMQ_CORRELATION_ID));

  }

  @Override
  protected StandaloneConsumer retrieveObjectForSampleConfig() {
    RabbitMqConnection c =
        new RabbitMqConnection().withFactoryBuilder(new SimpleConnectionFactoryBuilder()
            .withBrokerUrl("amqp://admin:admin@localhost:5672/vhost"));
    BasicConsumer consumer = new BasicConsumer().withQueue("MyQueue");
    return new StandaloneConsumer(c, consumer);
  }

  // Will be overriden by interop tests.
  protected void checkEnabled() {
    JunitConfig.abortIfNotEnabled();
  }
  
  // Will be overriden by interop tests.
  protected String brokerURL() {
    return JunitConfig.brokerURL();
  }
}
