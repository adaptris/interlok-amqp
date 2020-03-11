package com.adaptris.core.amqp.rabbitmq;

import com.adaptris.core.ConfiguredConsumeDestination;
import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.PtpConsumer;
import com.adaptris.util.KeyValuePair;

public class AdvancedRabbitMqConsumerTest extends BasicRabbitMqConsumerTest {


  @Override
  protected String createBaseFileName(Object object) {
    return ((StandaloneConsumer) object).getConsumer().getClass().getName() + "-RabbitMQ-ADVANCED";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    PtpConsumer consumer = new PtpConsumer(new ConfiguredConsumeDestination("SOME_MQ_QUEUE"));
    StandaloneConsumer result = new StandaloneConsumer(configure(new JmsConnection()), consumer);
    return result;
  }

  @Override
  protected AdvancedRabbitMqJmsImplementation createVendorImpl() {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672/vhost");
    mq.getConnectionFactoryProperties().add(new KeyValuePair("OnMessageTimeoutMs", "10000"));
    return mq;
  }
}
