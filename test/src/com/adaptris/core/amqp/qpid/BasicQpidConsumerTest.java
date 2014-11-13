package com.adaptris.core.amqp.qpid;

import com.adaptris.core.ConfiguredConsumeDestination;
import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.JmsConsumerCase;
import com.adaptris.core.jms.PtpConsumer;

public class BasicQpidConsumerTest extends JmsConsumerCase {

  public BasicQpidConsumerTest(String name) {
    super(name);
  }


  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-QPID-BASIC-AMQP-1.0";
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

  protected BasicQpidImplementation createVendorImpl() {
    BasicQpidImplementation mq = new BasicQpidImplementation();
    mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    return mq;
  }
}
