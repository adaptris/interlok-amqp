package com.adaptris.core.amqp.qpid.amqp_0_10;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.jms.JMSException;

import org.junit.jupiter.api.Test;

import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;
import com.adaptris.interlok.junit.scaffolding.BaseCase;

public class StandardQpidImplementationTest extends BaseCase {

  @Test
  public void testConnectionFactory() throws Exception {
    try {
      StandardQpidImplementation vendor = createVendorImpl();
      assertNotNull(vendor.createConnectionFactory());
    } finally {
    }
  }

  @Test
  public void testConnectionFactory_WithException() throws Exception {
    try {
      StandardQpidImplementation vendor = new StandardQpidImplementation();
      vendor.createConnectionFactory();
      fail();
    } catch (JMSException expected) {

    } finally {
    }
  }

  @Test
  public void testConnectionEquals() throws Exception {
    StandardQpidImplementation vendor = createVendorImpl();
    StandardQpidImplementation vendor2 = createVendorImpl();
    assertTrue(vendor.connectionEquals(vendor2));
    assertFalse(vendor.connectionEquals(new StandardQpidImplementation()));
    assertFalse(vendor.connectionEquals(new BasicActiveMqImplementation()));
  }

  protected StandardQpidImplementation createVendorImpl() {
    StandardQpidImplementation mq = new StandardQpidImplementation();
    mq.setBrokerUrl("amqp://clientId/vhost?brokerlist='tcp://localhost:5672'");
    return mq;
  }

}
