package com.adaptris.core.amqp.qpid.amqp_0_10;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.BaseCase;
import com.adaptris.core.amqp.qpid.BasicQpidImplementationTest;
import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;

public class StandardQpidImplementationTest extends BaseCase {

  private static Logger log = LoggerFactory.getLogger(BasicQpidImplementationTest.class);

  public StandardQpidImplementationTest(String name) {
    super(name);
  }

  public void testConnectionFactory() throws Exception {
    try {
      StandardQpidImplementation vendor = createVendorImpl();
      assertNotNull(vendor.createConnectionFactory());
    }
    finally {
    }
  }

  public void testConnectionFactory_WithException() throws Exception {
    try {
      StandardQpidImplementation vendor = new StandardQpidImplementation();
      vendor.createConnectionFactory();
      fail();
    }
    catch (JMSException expected) {

    }
    finally {
    }
  }

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
