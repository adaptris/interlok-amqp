package com.adaptris.core.amqp.qpid;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;
import com.adaptris.interlok.junit.scaffolding.BaseCase;
import com.adaptris.util.TimeInterval;

public class BasicQpidImplementationTest extends BaseCase {

  @Test
  public void testConnectionFactory() throws Exception {
    EmbeddedAMQP broker = new EmbeddedAMQP();
    broker.start();
    JmsConnection connection = configureForTests(new JmsConnection(), broker);
    try {
      connection.prepare();
      connection.init();
      connection.start();
      // start(connection);
      assertNotNull(connection.currentConnection());
      assertTrue(connection.currentConnection() instanceof javax.jms.Connection);
      assertNotNull(connection.currentConnection().getMetaData());

    } catch (Exception ex) {

    } finally {
      stop(connection);
      broker.destroy();
    }
  }

  @Test
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
