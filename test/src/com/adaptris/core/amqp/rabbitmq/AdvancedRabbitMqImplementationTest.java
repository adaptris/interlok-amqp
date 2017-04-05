package com.adaptris.core.amqp.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.util.KeyValuePair;

public class AdvancedRabbitMqImplementationTest extends BasicRabbitMqImplementationTest {

  private static Logger log = LoggerFactory.getLogger(AdvancedRabbitMqImplementationTest.class);

  public AdvancedRabbitMqImplementationTest(String name) {
    super(name);
  }

  protected AdvancedRabbitMqJmsImplementation createVendorImpl(String brokerUrl) {
    AdvancedRabbitMqJmsImplementation mq = new AdvancedRabbitMqJmsImplementation();
    mq.setBrokerUrl(brokerUrl);
    mq.getConnectionFactoryProperties().add(new KeyValuePair("OnMessageTimeoutMs", "10000"));
    return mq;
  }

}
