package com.adaptris.core.amqp.qpid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.ConfiguredProduceDestination;
import com.adaptris.core.StandaloneProducer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsConnectionErrorHandler;
import com.adaptris.core.jms.JmsProducerExample;
import com.adaptris.core.jms.PtpProducer;

public class BasicAmqpProducerTest extends JmsProducerExample {

  private static Logger log = LoggerFactory.getLogger(BasicAmqpProducerTest.class);

  public BasicAmqpProducerTest(String name) {
    super(name);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-QPID-BASIC-AMQP-1.0";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {

    PtpProducer producer = new PtpProducer(new ConfiguredProduceDestination("SOME_QUEUE_NAME"));
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
