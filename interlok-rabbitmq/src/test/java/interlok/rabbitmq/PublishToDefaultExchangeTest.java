package interlok.rabbitmq;

import static interlok.rabbitmq.JunitConfig.MESSAGE_BODY;
import static interlok.rabbitmq.JunitConfig.NAME_GENERATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ServiceException;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.interlok.junit.scaffolding.services.ExampleServiceCase;

import interlok.rabbitmq.PublishToDefaultExchange.Behaviour;

public class PublishToDefaultExchangeTest extends ExampleServiceCase {

  @Test
  public void testPublish() throws Exception {
    JunitConfig.abortIfNotEnabled();
    String brokerUrl = JunitConfig.brokerURL();
    String queueName = NAME_GENERATOR.safeUUID();

    RabbitMqConnection c = new RabbitMqConnection().withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));

    PublishToDefaultExchange service = new PublishToDefaultExchange().withQueue(queueName).withConnection(c);

    try (BlancDeBouscat reader = new BlancDeBouscat(brokerUrl, queueName)) {
      LifecycleHelper.initAndStart(service);
      AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
      service.doService(msg);
      reader.getAndVerify(MESSAGE_BODY);
    } finally {
      LifecycleHelper.stopAndClose(service);
    }
  }

  @Test
  public void testHandler_Normal() throws Exception {
    Behaviour behaviour = Behaviour.TRADITIONAL;
    AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
    behaviour.handleSuccess(msg);
    assertThrows(ServiceException.class, () -> {
      behaviour.handleFailure(msg, new Exception("myException"));
    });
  }

  @Test
  public void testHandler_NoException() throws Exception {
    Behaviour behaviour = Behaviour.NO_EXCEPTION;
    AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
    behaviour.handleSuccess(msg);
    assertEquals("success", msg.getMetadataValue(MetadataConstants.RMQ_PUBLISH_STATUS));
    behaviour.handleFailure(msg, new Exception("myException"));
    assertTrue(msg.getMetadataValue(MetadataConstants.RMQ_PUBLISH_STATUS).contains("myException"));
  }

  @Override
  protected PublishToDefaultExchange retrieveObjectForSampleConfig() {
    RabbitMqConnection c = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl("amqp://admin:admin@localhost:5672/vhost"));
    return new PublishToDefaultExchange().withQueue("MyQueue").withConnection(c);
  }

}
