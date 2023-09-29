package com.adaptris.core.amqp.artemis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.jms.ConnectionFactory;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;
import com.adaptris.interlok.junit.scaffolding.BaseCase;

public class BasicArtemisAMQPImplementationTest  extends BaseCase {

  private BasicArtemisAMQPImplementation impl;
  
  @BeforeEach
  public void setUp() throws Exception {
    impl = new BasicArtemisAMQPImplementation();
  }
  
  @Test
  public void testConnectionFactory() throws Exception {
    impl.setBrokerUrl("amqp://localhost:5672");
    ConnectionFactory connFact = impl.createConnectionFactory();
    
    assertTrue(connFact instanceof JmsConnectionFactory);
    assertEquals("amqp://localhost:5672", ((JmsConnectionFactory) connFact).getRemoteURI().toString());
  }
  
  @Test
  public void testConnectionFactoryFails() throws Exception {
    try {
      impl.createConnectionFactory();
      fail("No URL provided, should fail.");
    } catch (Exception ex) {
      // expected
    }
  }

  @Test
  public void testConnectionEquals() throws Exception {
    BasicArtemisAMQPImplementation vendor1 = createVendorImpl("amqp://localhost:5672");
    BasicArtemisAMQPImplementation vendor2 = createVendorImpl("amqp://localhost:5672");
    
    assertTrue(vendor1.connectionEquals(vendor2));
    assertFalse(vendor1.connectionEquals(createVendorImpl("amqp://localhost:5673")));
    assertFalse(vendor1.connectionEquals(new BasicActiveMqImplementation()));
  }

  protected BasicArtemisAMQPImplementation createVendorImpl(String brokerUrl) {
    BasicArtemisAMQPImplementation mq = new BasicArtemisAMQPImplementation();
    mq.setBrokerUrl(brokerUrl);
    return mq;
  }

}
