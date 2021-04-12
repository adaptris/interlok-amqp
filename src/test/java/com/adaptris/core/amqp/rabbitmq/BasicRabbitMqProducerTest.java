package com.adaptris.core.amqp.rabbitmq;

import com.adaptris.core.StandaloneProducer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.PtpProducer;
import com.adaptris.interlok.junit.scaffolding.jms.JmsProducerExample;

public class BasicRabbitMqProducerTest extends JmsProducerExample {

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-RabbitMQ-BASIC";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {

    PtpProducer producer = new PtpProducer().withQueue("SOME_QUEUE_NAME");
    StandaloneProducer result = new StandaloneProducer(configure(new JmsConnection()), producer);

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
