package com.adaptris.core.amqp.rabbitmq;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.commons.lang3.BooleanUtils;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.jms.JmsActorConfig;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.core.jms.VendorImplementationBase;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

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

  /**
   * Force the underlying JMS destination to be interopable with AMQP.
   * <p>
   * Setting this to true changes queue and topic creation to use the
   * {@code RMQDestination(String,String,String,String)} constructor.
   * rather than delegating it to the underlying session. This allows interopability with AMQP senders such as
   * <a href="https://github.com/ruby-amqp/bunny">bunny</a>
   * JMS Producer/Consumers.
   * </p>
   * <p>
   * The default is false if not specified.
   * </p>
   */
  @InputFieldDefault(value = "false")
  @AdvancedConfig
  @Setter
  @Getter
  private Boolean amqpMode;

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

  @Override
  public Queue createQueue(String name, JmsActorConfig c) throws JMSException {
    if (amqpMode()) {
      return new RMQDestination(name, null, null, name);
    }
    return super.createQueue(name, c);
  }

  @Override
  public Topic createTopic(String name, JmsActorConfig c) throws JMSException {
    if (amqpMode()) {
      return new RMQDestination(name, null, null, name);
    }
    return super.createTopic(name, c);
  }

  @SuppressWarnings("unchecked")
  public <T extends BasicRabbitMqJmsImplementation> T withAmqpMode(Boolean b) {
    setAmqpMode(b);
    return (T) this;
  }

  public boolean amqpMode() {
    return BooleanUtils.toBooleanDefaultIfNull(getAmqpMode(), false);
  }
}
