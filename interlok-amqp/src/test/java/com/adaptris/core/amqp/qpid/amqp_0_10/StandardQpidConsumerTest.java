package com.adaptris.core.amqp.qpid.amqp_0_10;

import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.interlok.junit.scaffolding.jms.JmsConsumerCase;
import com.adaptris.core.jms.PtpConsumer;

public class StandardQpidConsumerTest extends JmsConsumerCase {

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-QPID-AMQP-0.10";
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

  protected StandardQpidImplementation createVendorImpl() {
    StandardQpidImplementation mq = new StandardQpidImplementation();
    mq.setBrokerUrl("amqp://clientId/vhost?brokerlist='tcp://localhost:5672'");
    return mq;
  }
}
