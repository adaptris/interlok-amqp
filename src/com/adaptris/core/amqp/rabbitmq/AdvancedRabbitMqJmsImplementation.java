package com.adaptris.core.amqp.rabbitmq;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.validation.constraints.NotNull;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.CoreException;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.core.jms.VendorImplementationBase;
import com.adaptris.security.exc.PasswordException;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
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
   * 
   *
   */
  public enum ConnectionFactoryProperty {
    
    /**
     * {@code RMQConnectionFactory.setChannelQos(int)}
     * 
     */
    Channel_QoS {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setChannelsQos(Integer.parseInt(value));
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setHost(String)}
     * 
     */
    Host {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setHost(value);
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setOnMessageTimeoutMs(int)}
     * 
     */
    OnMessageTimeoutMs {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setOnMessageTimeoutMs(Integer.parseInt(value));
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setPassword(String)}, may be encoded.
     * 
     */
    Password {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        try {
          connectionFactory.setPassword(com.adaptris.security.password.Password.decode(value));
        } catch (PasswordException e) {
          throw new CoreException(e);
        }
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setPort(int)}
     * 
     */
    Port {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setPort(Integer.parseInt(value));
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setQueueBrowserReadMax(int)}
     * 
     */
    QueueBrowserReadMax {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setQueueBrowserReadMax(Integer.parseInt(value));
      }
    } ,
    /**
     * {@code RMQConnectionFactory.useSslProtocol()}
     * 
     */
    Ssl {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        if(Boolean.getBoolean(value))
          try {
            connectionFactory.useSslProtocol();
          } catch (NoSuchAlgorithmException e) {
            throw new CoreException(e);
          }
      }
    } ,
    /**
     * {@code RMQConnectionFactory.useSslProtocol(String)}
     * 
     */
    UseSslProtocol {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.useSslProtocol(value);
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setTerminationTimeout(long)}
     * 
     */
    TerminationTimeout {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setTerminationTimeout(Long.parseLong(value));
      }
    } ,
    
    /**
     * Comma separated list of packages for {@code RMQConnectionFactory.setTrustedPackages(List<String>)}
     * 
     */
    TrustedPackages {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setTrustedPackages(Arrays.asList(value.split(",")));
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setUri(String)}
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
     * {@code RMQConnectionFactory.setUseDefaultSslContext(boolean)}
     * 
     */
    UseDefaultSslContext {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setUseDefaultSslContext(Boolean.getBoolean(value));
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setUsername(String)}
     * 
     */
    Username {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setUsername(value);
      }
    } ,
    /**
     * {@code RMQConnectionFactory.setVirtualHost(String)}
     * 
     */
    VirtualHost {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException {
        connectionFactory.setVirtualHost(value);
      }
    } ;
    
    public abstract void apply(RMQConnectionFactory connectionFactory, String value) throws CoreException;
    
    public static ConnectionFactoryProperty findEnumValue(String enumValue) {
      for(ConnectionFactoryProperty value : ConnectionFactoryProperty.values()) {
        if(enumValue.equalsIgnoreCase(value.name()))
          return value;
      }
      return null;
    }
  }
  
  public AdvancedRabbitMqJmsImplementation() {
    setConnectionFactoryProperties(new KeyValuePairSet());
  }
  
  @Override
  public RMQConnectionFactory createConnectionFactory() throws JMSException {
    RMQConnectionFactory connectionFactory = super.createConnectionFactory();
    try {
      this.applyConnectionFactoryProperties(connectionFactory, this.getConnectionFactoryProperties());
    } catch (CoreException e) {
      throw new JMSException(e.getMessage());
    }
    return connectionFactory;
  }

  private void applyConnectionFactoryProperties(RMQConnectionFactory connectionFactory, KeyValuePairSet connectionFactoryProperties2) throws CoreException {
    for (KeyValuePair kvp : this.getConnectionFactoryProperties().getKeyValuePairs()) {
      try {
        ConnectionFactoryProperty foundEnum = ConnectionFactoryProperty.findEnumValue(kvp.getKey());
        if(foundEnum != null)
          foundEnum.apply(connectionFactory, kvp.getValue());
        else 
          log.warn("Connection factory property {} not found, ignoring.", kvp.getKey());
        
      } catch(Exception ex) {
        throw new CoreException(ex);
      }
    }
  }

  @Override
  public boolean connectionEquals(VendorImplementationBase vendorImp) {
    return (vendorImp instanceof BasicRabbitMqJmsImplementation) && super.connectionEquals(vendorImp);
  }

  public KeyValuePairSet getConnectionFactoryProperties() {
    return connectionFactoryProperties;
  }

  public void setConnectionFactoryProperties(KeyValuePairSet connectionFactoryProperties) {
    this.connectionFactoryProperties = connectionFactoryProperties;
  }

}
