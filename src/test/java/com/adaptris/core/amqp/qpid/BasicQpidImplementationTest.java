package com.adaptris.core.amqp.qpid;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.BaseCase;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;
import com.adaptris.util.TimeInterval;

public class BasicQpidImplementationTest extends BaseCase {

  private static Logger log = LoggerFactory.getLogger(BasicQpidImplementationTest.class);

  public BasicQpidImplementationTest(String name) {
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

  public void testConnectionFactory() throws Exception {
    EmbeddedAMQP broker = new EmbeddedAMQP();
    broker.start();
    JmsConnection connection = configureForTests(new JmsConnection(), broker);
    try {
      start(connection);
      assertNotNull(connection.currentConnection());
      assertTrue(connection.currentConnection() instanceof javax.jms.Connection);
      assertNotNull(connection.currentConnection().getMetaData());
    }
    finally {
      stop(connection);
      broker.destroy();
    }
  }

  public void testConnectionFactory_withException() throws Exception {
    try {
      BasicQpidImplementation vendor = createVendorImpl("amqp://clientId/vhost?brokerlist='tcp://localhost:5672'");
      // SHould fail as brokerList: isn't a supported attribute.
      vendor.createConnectionFactory();
      fail();
    }
    catch (JMSException expected) {

    }

  }
  public void testConnectionEquals() throws Exception {
    BasicQpidImplementation vendor = createVendorImpl("amqp://localhost:5672");
    BasicQpidImplementation vendor2 = createVendorImpl("amqp://localhost:5672");
    assertTrue(vendor.connectionEquals(vendor2));
    assertFalse(vendor.connectionEquals(createVendorImpl("amqp://localhost:5673")));
    assertFalse(vendor.connectionEquals(new BasicActiveMqImplementation()));
  }

  protected JmsConnection configureForTests(JmsConnection connection, EmbeddedAMQP broker) {
    connection.setVendorImplementation(createVendorImpl(broker));
    connection.setConnectionAttempts(1);
    connection.setConnectionRetryInterval(new TimeInterval(1L, TimeUnit.SECONDS));
    return connection;
  }

  protected BasicQpidImplementation createVendorImpl(EmbeddedAMQP broker) {
    return createVendorImpl(broker.getBrokerUrl());
  }

  protected BasicQpidImplementation createVendorImpl(String brokerUrl) {
    BasicQpidImplementation mq = new BasicQpidImplementation();
    // mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    mq.setBrokerUrl(brokerUrl);
    return mq;
  }

}
