package com.adaptris.core.amqp.qpid;

import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.JmsConsumerCase;
import com.adaptris.core.jms.PtpConsumer;

public class BasicQpidConsumerTest extends JmsConsumerCase {
  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-QPID-BASIC-AMQP-1.0";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    PtpConsumer consumer = new PtpConsumer().withQueue("SOME_MQ_QUEUE");
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

  protected BasicQpidJmsImplementation createVendorImpl() {
    BasicQpidJmsImplementation mq = new BasicQpidJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    return mq;
  }
}
