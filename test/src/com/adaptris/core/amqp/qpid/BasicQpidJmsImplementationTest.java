package com.adaptris.core.amqp.qpid;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.BaseCase;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;
import com.adaptris.util.TimeInterval;

public class BasicQpidJmsImplementationTest extends BaseCase {

  private static Logger log = LoggerFactory.getLogger(BasicQpidJmsImplementationTest.class);

  public BasicQpidJmsImplementationTest(String name) {
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

  // Can't actually test against a real ActiveMQ broker,
  // 5.9.0 "hangs"; 5.11.1 fails with a stupid NoMethodError
  // public void testConnectionFactory() throws Exception {
  // EmbeddedAMQP broker = new EmbeddedAMQP();
  // broker.start();
  // JmsConnection connection = configureForTests(new JmsConnection(), broker);
  // try {
  // start(connection);
  // assertNotNull(connection.currentConnection());
  // assertTrue(connection.currentConnection() instanceof javax.jms.Connection);
  // assertNotNull(connection.currentConnection().getMetaData());
  // }
  // finally {
  // stop(connection);
  // broker.destroy();
  // }
  // }

  public void testConnectionFactory() throws Exception {
    BasicQpidJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
    assertNotNull(vendor.createConnectionFactory());
  }

  public void testConnectionFactory_withException() throws Exception {
    try {
      BasicQpidJmsImplementation vendor = createVendorImpl("isn't a URL");
      vendor.createConnectionFactory();
      fail();
    } catch (JMSException expected) {

    }

  }

  public void testConnectionEquals() throws Exception {
    BasicQpidJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
    BasicQpidJmsImplementation vendor2 = createVendorImpl("amqp://localhost:5672");
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

  protected BasicQpidJmsImplementation createVendorImpl(EmbeddedAMQP broker) {
    return createVendorImpl(broker.getBrokerUrl());
  }

  protected BasicQpidJmsImplementation createVendorImpl(String brokerUrl) {
    BasicQpidJmsImplementation mq = new BasicQpidJmsImplementation();
    // mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    mq.setBrokerUrl(brokerUrl + "?transport.connectTimeout=1000&jms.connectTimeout=1000");
    return mq;
  }

}
