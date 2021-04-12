package com.adaptris.core.amqp.qpid;

import com.adaptris.core.StandaloneProducer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.PtpProducer;
import com.adaptris.interlok.junit.scaffolding.jms.JmsProducerExample;

public class BasicAmqpProducerTest extends JmsProducerExample {

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-QPID-BASIC-AMQP-1.0";
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

  protected BasicQpidJmsImplementation createVendorImpl() {
    BasicQpidJmsImplementation mq = new BasicQpidJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    return mq;
  }

}
