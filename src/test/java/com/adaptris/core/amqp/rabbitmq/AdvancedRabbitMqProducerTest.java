package com.adaptris.core.amqp.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.StandaloneProducer;
import com.adaptris.util.KeyValuePair;

public class AdvancedRabbitMqProducerTest extends BasicRabbitMqProducerTest {

  private static Logger log = LoggerFactory.getLogger(AdvancedRabbitMqProducerTest.class);

  public AdvancedRabbitMqProducerTest(String name) {
    super(name);
  }


  @Override
  protected String createBaseFileName(Object object) {
    return ((StandaloneProducer) object).getProducer().getClass().getName() + "-RabbitMQ-ADVANCED";
  }

  protected AdvancedRabbitMqJmsImplementation createVendorImpl() {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672/vhost");
    mq.getConnectionFactoryProperties().add(new KeyValuePair("OnMessageTimeoutMs", "10000"));
    return mq;
  }

}
