package com.adaptris.core.amqp.rabbitmq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.jms.JmsActorConfig;
import com.adaptris.core.jms.activemq.BasicActiveMqImplementation;
import com.rabbitmq.jms.admin.RMQDestination;

public class BasicRabbitMqImplementationTest {

  protected static Logger log = LoggerFactory.getLogger(BasicRabbitMqImplementationTest.class);


  @Test
  public void testConnectionFactory() throws Exception {
    try {
      BasicRabbitMqJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
      assertNotNull(vendor.createConnectionFactory());
    }
    finally {
    }
  }

  @Test
  public void testConnectionEquals() throws Exception {
    BasicRabbitMqJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
    BasicRabbitMqJmsImplementation vendor2 = createVendorImpl("amqp://localhost:5672");
    assertTrue(vendor.connectionEquals(vendor2));
    assertFalse(vendor.connectionEquals(createVendorImpl("amqp://localhost:5673")));
    assertFalse(vendor.connectionEquals(new BasicActiveMqImplementation()));
  }

  @Test
  public void testWithAmqpMode() throws Exception {
    BasicRabbitMqJmsImplementation vendor = createVendorImpl("amqp://localhost:5672").withAmqpMode(true);
    assertEquals(true, vendor.getAmqpMode());
  }

  // It's arguable that this isn't a valid test, since if we had a real session, it would still be a RMQ Destination
  @Test
  public void testCreateQueue() throws Exception {
    BasicRabbitMqJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
    JmsActorConfig config = mockConfig();
    assertNotSame(RMQDestination.class, vendor.createQueue("name", config).getClass());
    RMQDestination d = (RMQDestination) vendor.withAmqpMode(true).createQueue("name", config);
    assertTrue(d.isAmqp());
  }

  @Test
  public void testCreateTopic() throws Exception {
    BasicRabbitMqJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
    JmsActorConfig config = mockConfig();
    assertNotSame(RMQDestination.class, vendor.createTopic("name", config).getClass());
    RMQDestination d = (RMQDestination) vendor.withAmqpMode(true).createTopic("name", config);
    assertTrue(d.isAmqp());
  }

  protected BasicRabbitMqJmsImplementation createVendorImpl(String brokerUrl) {
    BasicRabbitMqJmsImplementation mq = new BasicRabbitMqJmsImplementation();
    mq.setBrokerUrl(brokerUrl);
    return mq;
  }

  protected JmsActorConfig mockConfig() throws Exception {
    JmsActorConfig config = Mockito.mock(JmsActorConfig.class);
    Session session = Mockito.mock(Session.class);
    Topic topic = Mockito.mock(Topic.class);
    Queue queue = Mockito.mock(Queue.class);
    Mockito.when(config.currentSession()).thenReturn(session);
    Mockito.when(session.createQueue(any())).thenReturn(queue);
    Mockito.when(session.createTopic(any())).thenReturn(topic);
    return config;
  }
}
