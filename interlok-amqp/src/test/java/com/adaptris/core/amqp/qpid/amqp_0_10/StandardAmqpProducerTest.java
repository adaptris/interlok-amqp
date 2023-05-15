package com.adaptris.core.amqp.qpid.amqp_0_10;

import com.adaptris.core.StandaloneProducer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.PtpProducer;
import com.adaptris.interlok.junit.scaffolding.jms.JmsProducerExample;

public class StandardAmqpProducerTest extends JmsProducerExample {

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-QPID-AMQP-0.10";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    PtpProducer producer = new PtpProducer().withQueue("SOME_QUEUE_NAME");
    StandaloneProducer result = new StandaloneProducer(configure(new JmsConnection()), producer);

    return result;
  }

  protected JmsConnection configure(JmsConnection con) {
    con.setUserName("BrokerUsername");
    con.setPassword("BrokerPassword");
    con.setVendorImplementation(createVendorImpl());
    con.setConnectionErrorHandler(new JmsConnectionErrorHandler());
    return con;
  }

  protected StandardQpidImplementation createVendorImpl() {
    StandardQpidImplementation mq = new StandardQpidImplementation();
    mq.setBrokerUrl("amqp://clientId/vhost?brokerlist='tcp://localhost:5672'");
    return mq;
  }

}
