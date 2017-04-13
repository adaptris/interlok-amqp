package com.adaptris.core.amqp.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.BaseCase;
import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;

public class BasicRabbitMqImplementationTest extends BaseCase {

  private static Logger log = LoggerFactory.getLogger(BasicRabbitMqImplementationTest.class);

  public BasicRabbitMqImplementationTest(String name) {
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
    try {
      BasicRabbitMqJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
      assertNotNull(vendor.createConnectionFactory());
    }
    finally {
    }
  }

  public void testConnectionEquals() throws Exception {
    BasicRabbitMqJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
    BasicRabbitMqJmsImplementation vendor2 = createVendorImpl("amqp://localhost:5672");
    assertTrue(vendor.connectionEquals(vendor2));
    assertFalse(vendor.connectionEquals(createVendorImpl("amqp://localhost:5673")));
    assertFalse(vendor.connectionEquals(new BasicActiveMqImplementation()));
  }

  protected BasicRabbitMqJmsImplementation createVendorImpl(String brokerUrl) {
    BasicRabbitMqJmsImplementation mq = new BasicRabbitMqJmsImplementation();
    mq.setBrokerUrl(brokerUrl);
    return mq;
  }

}
