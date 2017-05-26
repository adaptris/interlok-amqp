package com.adaptris.core.amqp.rabbitmq;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.core.jms.VendorImplementationBase;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 0.9.1 implementation of {@link VendorImplementation} using RabbitMQ.
 * 
 * <p>
 * Everything required to configure the connection needs to be specified on the URL. If you configure a username and password on the
 * wrapping {@link JmsConnection} in which case {@link ConnectionFactory#createConnection(String, String)} is used when creating the
 * connection otherwise {@link ConnectionFactory#createConnection()} will be used.
 * </p>
 * <p>
 * This was built against {@code com.rabbitmq.jms:rabbitmq-jms:1.6.0} and {@code com.rabbitmq:amqp-client:4.0.2}
 * </p>
 * 
 * @config rabbitmq-basic-jms-implementation
 * @since 3.6.0
 */
@XStreamAlias("rabbitmq-basic-jms-implementation")
public class BasicRabbitMqJmsImplementation extends UrlVendorImplementation {

  protected static final int DEFAULT_OM_TIMEOUT = 60000;

  @Override
  public RMQConnectionFactory createConnectionFactory() throws JMSException {
    RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
    connectionFactory.setUri(this.getBrokerUrl());
    connectionFactory.setOnMessageTimeoutMs(DEFAULT_OM_TIMEOUT);
    return connectionFactory;
  }

  @Override
  public boolean connectionEquals(VendorImplementationBase vendorImp) {
    return (vendorImp instanceof BasicRabbitMqJmsImplementation) && super.connectionEquals(vendorImp);
  }
}
