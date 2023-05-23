package com.adaptris.core.amqp.rabbitmq;

import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.PtpConsumer;
import com.adaptris.interlok.junit.scaffolding.jms.JmsConsumerCase;

public class BasicRabbitMqConsumerTest extends JmsConsumerCase {

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-RabbitMQ-BASIC";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    PtpConsumer consumer = new PtpConsumer().withQueue("SOME_MQ_QUEUE");
    StandaloneConsumer result = new StandaloneConsumer(configure(new JmsConnection()), consumer);
    return result;
  }

  protected JmsConnection configure(JmsConnection con) {
    con.setUserName("BrokerUsername");
    con.setPassword("BrokerPassword");
    con.setVendorImplementation(createVendorImpl());
    con.setConnectionErrorHandler(new JmsConnectionErrorHandler());
    return con;
  }

  protected BasicRabbitMqJmsImplementation createVendorImpl() {
    BasicRabbitMqJmsImplementation mq = new BasicRabbitMqJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672/vhost");
    return mq;
  }

}
