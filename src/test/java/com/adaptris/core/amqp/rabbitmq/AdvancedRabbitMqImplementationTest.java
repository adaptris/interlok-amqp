package com.adaptris.core.amqp.rabbitmq;
import static org.junit.Assert.assertNotNull;
import javax.jms.JMSException;
import org.junit.Test;
import com.adaptris.core.amqp.rabbitmq.AdvancedRabbitMqJmsImplementation.ConnectionFactoryProperty;
import com.adaptris.util.KeyValuePair;

public class AdvancedRabbitMqImplementationTest extends BasicRabbitMqImplementationTest {


  @Override
  protected AdvancedRabbitMqJmsImplementation createVendorImpl(String brokerUrl) {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl(brokerUrl);
    mq.getConnectionFactoryProperties().add(new KeyValuePair("OnMessageTimeoutMs", "10000"));
    return mq;
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testApplyConnectionFactoryProperties() throws Exception {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672");
    mq.getConnectionFactoryProperties()
        .add(new KeyValuePair(ConnectionFactoryProperty.ChannelQoS.name(), "10000"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Channel_QoS.name(), "10000"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Host.name(), "localhost"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.OnMessageTimeoutMs.name(), "10000"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Password.name(), "password"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Port.name(), "5672"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.QueueBrowserReadMax.name(), "5672"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Ssl.name(), "false"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.UseSslProtocol.name(), "TLS1.0"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.TerminationTimeout.name(), "10000"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.TrustedPackages.name(), "com.adaptris"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Uri.name(), "amqp://localhost:5672"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.UseDefaultSslContext.name(), "true"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Username.name(), "admin"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.VirtualHost.name(), "vhost"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.DeclareReplyToDestination.name(), "false"));
    // This is a property that's not in the enum, and with no setSomethingElse method.
    mq.getConnectionFactoryProperties().add(new KeyValuePair("SomethingElse", "amqp://localhost:5672"));
    // This is a property that's not in the enum, but does exist as a setter.
    mq.getConnectionFactoryProperties().add(new KeyValuePair("PreferProducerMessageProperty", "true"));
    assertNotNull(mq.createConnectionFactory());
  }

  @Test(expected = JMSException.class)
  public void testApplyConnectionFactoryProperties_CheckedExcetpion() throws Exception {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672");
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Password.name(), "PW:password"));
    mq.createConnectionFactory();
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testApplyConnectionFactoryProperties_UseSsl() throws Exception {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672");
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.Ssl.name(), "true"));
    mq.createConnectionFactory();
  }

  @Test(expected = JMSException.class)
  public void testApplyConnectionFactoryProperties_UncheckedExcetpion() throws Exception {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672");
    mq.getConnectionFactoryProperties().add(new KeyValuePair(ConnectionFactoryProperty.OnMessageTimeoutMs.name(), "PW:password"));
    mq.createConnectionFactory();
  }
}
