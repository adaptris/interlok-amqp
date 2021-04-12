package com.adaptris.core.amqp.rabbitmq;

import com.adaptris.core.StandaloneProducer;
import com.adaptris.util.KeyValuePair;

public class AdvancedRabbitMqProducerTest extends BasicRabbitMqProducerTest {

  @Override
  protected String createBaseFileName(Object object) {
    return ((StandaloneProducer) object).getProducer().getClass().getName() + "-RabbitMQ-ADVANCED";
  }

  @Override
  protected AdvancedRabbitMqJmsImplementation createVendorImpl() {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672/vhost");
    mq.getConnectionFactoryProperties().add(new KeyValuePair("OnMessageTimeoutMs", "10000"));
    return mq;
  }

}
