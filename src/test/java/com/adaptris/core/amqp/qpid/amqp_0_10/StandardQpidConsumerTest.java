package com.adaptris.core.amqp.qpid.amqp_0_10;

import com.adaptris.core.ConfiguredConsumeDestination;
import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.JmsConsumerCase;
import com.adaptris.core.jms.PtpConsumer;

public class StandardQpidConsumerTest extends JmsConsumerCase {

  public StandardQpidConsumerTest(String name) {
    super(name);
  }


  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-QPID-AMQP-0.10";
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

  protected StandardQpidImplementation createVendorImpl() {
    StandardQpidImplementation mq = new StandardQpidImplementation();
    mq.setBrokerUrl("amqp://clientId/vhost?brokerlist='tcp://localhost:5672'");
    return mq;
  }
}
