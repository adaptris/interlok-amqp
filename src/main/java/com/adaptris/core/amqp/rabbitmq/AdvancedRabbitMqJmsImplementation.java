package com.adaptris.core.amqp.rabbitmq;

import java.util.Arrays;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.BooleanUtils;
import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.Removal;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.JmsUtils;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.util.SimpleBeanUtil;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
 * 
 * @config rabbitmq-advanced-jms-implementation
 * @since 3.6.0
 */
@XStreamAlias("rabbitmq-advanced-jms-implementation")
public class AdvancedRabbitMqJmsImplementation extends BasicRabbitMqJmsImplementation {

  
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
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setChannelsQos(Integer.parseInt(value)));
      }
    },
    /**
     * Maps to {@code RMQConnectionFactory.setChannelQos(int)}
     * 
     */
    Channel_QoS {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setChannelsQos(Integer.parseInt(value)));
      }
    },
    /**
     * Maps to {@code RMQConnectionFactory.setDeclareReplyToDestination(boolean)}
     * 
     */
    DeclareReplyToDestination {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() ->connectionFactory.setDeclareReplyToDestination(BooleanUtils.toBoolean(value)));
      }
    },
    /**
     * Maps to {@code RMQConnectionFactory.setHost(String)}
     * 
     */
    Host {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setHost(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setOnMessageTimeoutMs(int)}
     * 
     */
    OnMessageTimeoutMs {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setOnMessageTimeoutMs(Integer.parseInt(value)));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setPassword(String)}, may be encoded.
     * 
     */
    Password {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setPassword(com.adaptris.security.password.Password.decode(value)));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setPort(int)}
     * 
     */
    Port {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setPort(Integer.parseInt(value)));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setQueueBrowserReadMax(int)}
     * 
     */
    QueueBrowserReadMax {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setQueueBrowserReadMax(Integer.parseInt(value)));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.useSslProtocol()}
     * 
     * @deprecated since 3.9.1 use {@link #SSL} instead.
     */
    @Deprecated
    @Removal(version = "3.11.0", message = "use SSL instead")
    Ssl {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        SSL.apply(connectionFactory, value);

      }
    },
    SSL {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        if (BooleanUtils.toBoolean(value)) {
          applyConfiguration(() -> connectionFactory.useSslProtocol());
        }
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.useSslProtocol(String)}
     * 
     */
    UseSslProtocol {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.useSslProtocol(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setTerminationTimeout(long)}
     * 
     */
    TerminationTimeout {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setTerminationTimeout(Long.parseLong(value)));
      }
    } ,
    
    /**
     * Maps to Comma separated list of packages for {@code RMQConnectionFactory.setTrustedPackages(List<String>)}
     * 
     */
    TrustedPackages {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setTrustedPackages(Arrays.asList(value.split(","))));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setUri(String)}
     * 
     */
    Uri {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setUri(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setUseDefaultSslContext(boolean)}
     * 
     */
    UseDefaultSslContext {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setUseDefaultSslContext(BooleanUtils.toBoolean(value)));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setUsername(String)}
     * 
     */
    Username {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setUsername(value));
      }
    } ,
    /**
     * Maps to {@code RMQConnectionFactory.setVirtualHost(String)}
     * 
     */
    VirtualHost {
      @Override
      public void apply(RMQConnectionFactory connectionFactory, String value) {
        applyConfiguration(() -> connectionFactory.setVirtualHost(value));
      }
    } ;
    
    public abstract void apply(RMQConnectionFactory connectionFactory, String value);
    
  }
  
  /**
   * Any additional properties that are required on the {@code RMQConnectionFactory}.
   * 
   * 
   * @see ConnectionFactoryProperty
   */
  @NotNull
  @AutoPopulated
  @Getter
  @Setter
  @NonNull
  private KeyValuePairSet connectionFactoryProperties;
  /**
   * If {@link #setAmqpMode(Boolean)} is set to true, then additionally set the exchange name as well when creating
   * the {@code RMQDestination}.
   * <p>
   * Note that no validation is done on this value, and it is passed as-is into the constructor for RMQDestination.
   * </p>
   */
  @Getter
  @Setter
  @AdvancedConfig
  private String exchangeName;
  /**
   * If {@link #setAmqpMode(Boolean)} is set to true, then additionally set the exchange name as well when creating
   * the {@code RMQDestination}.
   * <p>
   * Note that no validation is done on this value, and it is passed as-is into the constructor for RMQDestination.
   * </p>
   */
  @Getter
  @Setter
  @AdvancedConfig
  private String routingKey;

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
    catch (Exception e) {
      throw JmsUtils.wrapJMSException(e);

    }
    return connectionFactory;
  }

  @Override
  protected RMQDestinationBuilder builder() {
    return (name) -> new RMQDestination(name, getExchangeName(), getRoutingKey(), name);
  }

  private static RuntimeException asRuntimeException(Exception e) {
    if (e instanceof RuntimeException) {
      return (RuntimeException) e;
    }
    return new RuntimeException(e);
  }

  private static void applyConfiguration(Applicator a) {
    try {
      a.apply();
    } catch (Exception e) {
      throw asRuntimeException(e);
    }
  }

  @FunctionalInterface
  protected interface Applicator {
    void apply() throws Exception;
  }
}
