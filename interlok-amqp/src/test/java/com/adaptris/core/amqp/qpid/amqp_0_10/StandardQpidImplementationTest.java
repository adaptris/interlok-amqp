package com.adaptris.core.amqp.qpid.amqp_0_10;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import javax.jms.JMSException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adaptris.core.amqp.qpid.BasicQpidImplementationTest;
import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;
import com.adaptris.interlok.junit.scaffolding.BaseCase;

public class StandardQpidImplementationTest extends BaseCase {

  private static Logger log = LoggerFactory.getLogger(BasicQpidImplementationTest.class);

  @Test
  public void testConnectionFactory() throws Exception {
    try {
      StandardQpidImplementation vendor = createVendorImpl();
      assertNotNull(vendor.createConnectionFactory());
    }
    finally {
    }
  }

  @Test
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
