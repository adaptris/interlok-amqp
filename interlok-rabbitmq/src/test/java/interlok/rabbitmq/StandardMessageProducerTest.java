package interlok.rabbitmq;

import static interlok.rabbitmq.JunitConfig.MESSAGE_BODY;
import static interlok.rabbitmq.JunitConfig.NAME_GENERATOR;
import org.junit.jupiter.api.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.StandaloneProducer;
import com.adaptris.core.metadata.NoOpMetadataFilter;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.interlok.junit.scaffolding.ExampleProducerCase;

public class StandardMessageProducerTest extends ExampleProducerCase {

  private static final String METADATA_KEY = "MY_QUEUE";

  
  @Test
  public void testPublish() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    String queueName = NAME_GENERATOR.safeUUID();
    
    RabbitMqConnection c = new RabbitMqConnection().withFactoryBuilder(
        new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    c.setConnectionErrorHandler(new AlwaysRestartExceptionHandler());
    
    StandardMessageProducer p = new StandardMessageProducer().withQueue(queueName);      
    StandaloneProducer service = new StandaloneProducer(c, p);
    
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
  public void testPublish_ResolvedQueue() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    String queueName = NAME_GENERATOR.safeUUID();
    
    RabbitMqConnection c =
        new RabbitMqConnection().withFactoryBuilder(new SimpleConnectionFactoryBuilder()
            .withBrokerUrl(brokerUrl));
    StandardMessageProducer p = new StandardMessageProducer().withQueue("%message{" + METADATA_KEY + "}")
        .withPropertyBuilder(new MetadataToProperties());
    StandaloneProducer service = new StandaloneProducer(c, p);
    try (BlancDeBouscat reader = new BlancDeBouscat(brokerUrl, queueName)) {
      LifecycleHelper.initAndStart(service);
      AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
      msg.addMessageHeader(METADATA_KEY, queueName);
      service.doService(msg);
      reader.getAndVerify(MESSAGE_BODY);
    } finally {
      LifecycleHelper.stopAndClose(service);
    }
  }

  @Test
  public void testPublish_IncludesMetadata() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    String queueName = NAME_GENERATOR.safeUUID();
    
    RabbitMqConnection c =
        new RabbitMqConnection().withFactoryBuilder(new SimpleConnectionFactoryBuilder()
            .withBrokerUrl(brokerUrl));  
    StandardMessageProducer p = new StandardMessageProducer().withQueue(queueName)
        .withPropertyBuilder(new MetadataToProperties().withFilter(new NoOpMetadataFilter()));
    StandaloneProducer service = new StandaloneProducer(c, p);
    
    
    try (BlancDeBouscat reader = new BlancDeBouscat(brokerUrl, queueName)) {
      LifecycleHelper.initAndStart(service);
      AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
      msg.addMessageHeader(METADATA_KEY, queueName);      
      service.doService(msg);
      reader.getAndVerify(MESSAGE_BODY, msg.getMetadata());
    } finally {
      LifecycleHelper.stopAndClose(service);
    }
  }


  @Override
  protected StandaloneProducer retrieveObjectForSampleConfig() {
    RabbitMqConnection c =
        new RabbitMqConnection().withFactoryBuilder(new SimpleConnectionFactoryBuilder()
            .withBrokerUrl("amqp://admin:admin@localhost:5672/vhost"));
    StandardMessageProducer p = new StandardMessageProducer().withQueue("MyQueue");
    return new StandaloneProducer(c, p);
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
