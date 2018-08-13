package com.adaptris.core.amqp.rabbitmq;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.BooleanUtils;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.CoreException;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsUtils;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.core.jms.VendorImplementationBase;
import com.adaptris.core.util.Args;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.security.exc.PasswordException;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.util.SimpleBeanUtil;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 0.9.1 implementation of {@link VendorImplementation} using RabbitMQ.
 * 
 * <p>
 * All available connection factory properties are exposed. If you configure a username and password on the wrapping
 * {@link JmsConnection} in which case {@link ConnectionFactory#createConnection(String, String)} is used when creating the
 * connection otherwise {@link ConnectionFactory#createConnection()} will be used.
 * </p>
 * <p>
 * The enum values from {@link ConnectionFactoryProperty} are matched on a case insensitve basis against the keys in
 * {@link #getConnectionFactoryProperties()} when configuring the {@code RMQConnectionFactory}. In the event that a key does not
 * match one the enums, an attempt will be made to invoke (via reflection) the implied setter with the configured value (e.g.
 * {@code DnsServer=a.b.c.d} would cause {@code setDnsServer("a.b.c.d")} to be invoked if it exists as a method on
 * {@code RMQConnectionFactory}).
 * </p>
 * 
 * <p>
 * This was built against {@code com.rabbitmq.jms:rabbitmq-jms:1.6.0} and {@code com.rabbitmq:amqp-client:4.0.2}
 * </p>
 * 
 * @config rabbitmq-advanced-jms-implementation
 * @since 3.6.0
 */
@XStreamAlias("rabbitmq-advanced-jms-implementation")
public class AdvancedRabbitMqJmsImplementation extends BasicRabbitMqJmsImplementation {
  
  @NotNull
  @AutoPopulated
  private KeyValuePairSet connectionFactoryProperties;
  
  /**
   * Connection Properties that map to {@code RMQConnectionFactory} setters.
   */
  public enum ConnectionFactoryProperty {
    
    /**
     * Maps to {@code RMQConnectionFactory.setChannelQos(int)}
     * 
     */
    ChannelQoS {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setChannelsQos(Integer.parseInt(value));
      }
    },
    /**
     * Maps to {@code RMQConnectionFactory.setChannelQos(int)}
     * 
     */
    Channel_QoS {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setChannelsQos(Integer.parseInt(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setHost(String)}
     * 
     */
    Host {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setHost(value);
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setOnMessageTimeoutMs(int)}
     * 
     */
    OnMessageTimeoutMs {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setOnMessageTimeoutMs(Integer.parseInt(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setPassword(String)}, may be encoded.
     * 
     */
    Password {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        try {
          connectionFactory.setPassword(com.adaptris.security.password.Password.decode(value));
        } catch (PasswordException e) {
          throw ExceptionHelper.wrapCoreException(e);
        }
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setPort(int)}
     * 
     */
    Port {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setPort(Integer.parseInt(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setQueueBrowserReadMax(int)}
     * 
     */
    QueueBrowserReadMax {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setQueueBrowserReadMax(Integer.parseInt(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.useSslProtocol()}
     * 
     */
    Ssl {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        if (BooleanUtils.toBoolean(value)) {
          try {
            connectionFactory.useSslProtocol();
          }
          catch (NoSuchAlgorithmException e) {
            throw ExceptionHelper.wrapCoreException(e);
          }
        }
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.useSslProtocol(String)}
     * 
     */
    UseSslProtocol {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.useSslProtocol(value);
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setTerminationTimeout(long)}
     * 
     */
    TerminationTimeout {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setTerminationTimeout(Long.parseLong(value));
      }
    } ,
    
    /**
     * Maps to Comma separated list of packages for {@code RMQConnectionFactory.setTrustedPackages(List<String>)}
     * 
     */
    TrustedPackages {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setTrustedPackages(Arrays.asList(value.split(",")));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setUri(String)}
     * 
     */
    Uri {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        try {
          connectionFactory.setUri(value);
        } catch (JMSException e) {
          throw new CoreException(e);
        }
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setUseDefaultSslContext(boolean)}
     * 
     */
    UseDefaultSslContext {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setUseDefaultSslContext(BooleanUtils.toBoolean(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setUsername(String)}
     * 
     */
    Username {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setUsername(value);
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setVirtualHost(String)}
     * 
     */
    VirtualHost {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setVirtualHost(value);
      }
    } ;
    
    public abstract void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException;
    
  }
  
  public AdvancedRabbitMqJmsImplementation() {
    setConnectionFactoryProperties(new KeyValuePairSet());
  }
  
  @Override
  public RMQConnectionFactory createConnectionFactory() throws JMSException {
    RMQConnectionFactory connectionFactory = super.createConnectionFactory();
    return applyConnectionFactoryProperties(connectionFactory);
  }

  private RMQConnectionFactory applyConnectionFactoryProperties(RMQConnectionFactory connectionFactory) throws JMSException {
    try {
      for (KeyValuePair kvp : getConnectionFactoryProperties().getKeyValuePairs()) {
        boolean matched = false;
        for (ConnectionFactoryProperty sp : ConnectionFactoryProperty.values()) {
          if (sp.name().equalsIgnoreCase(kvp.getKey())) {
            sp.apply(connectionFactory, kvp.getValue());
            matched = true;
            break;
          }
        }
        if (!matched) {
          if (!SimpleBeanUtil.callSetter(connectionFactory, "set" + kvp.getKey(), kvp.getValue())) {
            log.trace("Ignoring unsupported Property {}", kvp.getKey());
          }
        }
      }
    }
    catch (CoreException e) {
      throw JmsUtils.wrapJMSException(e);

    }
    return connectionFactory;
  }

  @Override
  public boolean connectionEquals(VendorImplementationBase vendorImp) {
    return (vendorImp instanceof BasicRabbitMqJmsImplementation) && super.connectionEquals(vendorImp);
  }

  public KeyValuePairSet getConnectionFactoryProperties() {
    return connectionFactoryProperties;
  }

  /**
   * Set any additional properties that are required on the {@code RMQConnectionFactory}.
   * 
   * 
   * @param properties
   * @see ConnectionFactoryProperty
   */
  public void setConnectionFactoryProperties(KeyValuePairSet properties) {
    this.connectionFactoryProperties = Args.notNull(properties, "connectionFactoryProperties");
  }

}
