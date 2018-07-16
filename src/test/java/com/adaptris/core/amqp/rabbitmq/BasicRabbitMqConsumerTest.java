package com.adaptris.core.amqp.rabbitmq;

import com.adaptris.core.ConfiguredConsumeDestination;
import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.JmsConsumerCase;
import com.adaptris.core.jms.PtpConsumer;

public class BasicRabbitMqConsumerTest extends JmsConsumerCase {

  public BasicRabbitMqConsumerTest(String name) {
    super(name);
  }


  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-RabbitMQ-BASIC";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    PtpConsumer consumer = new PtpConsumer(new ConfiguredConsumeDestination("SOME_MQ_QUEUE"));
    StandaloneConsumer result = new StandaloneConsumer(configure(new JmsConnection()), consumer);
    return result;
  }

  protected JmsConnection configure(JmsConnection c) {
    c.setUserName("BrokerUsername");
    c.setPassword("BrokerPassword");
    c.setVendorImplementation(createVendorImpl());
    c.setConnectionErrorHandler(new JmsConnectionErrorHandler());
    return c;
  }

  protected BasicRabbitMqJmsImplementation createVendorImpl() {
    BasicRabbitMqJmsImplementation mq = new BasicRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672/vhost");
    return mq;
  }
}