package com.adaptris.core.amqp.qpid;

import javax.jms.JMSException;

import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;

import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.security.password.Password;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 1.0 implementation of {@link VendorImplementation} using Apache Qpid
 * <p>
 * This vendor implementation class directly exposes almost all the getter and setters that are available in the ConnectionFactory
 * for maximum flexibility in configuration. The main difference between this and {@link BasicQpidImplementation} is that you do not
 * have to configure all the properties inside the URL, which is simply a bonus for readability.
 * </p>
 * <p>
 * The key from the <code>connection-factory-properties</code> element should match the name of the underlying Qpid
 * ConnectionFactory property. <code>
 * <pre>
 *   &lt;connection-factory-properties>
 *     &lt;key-value-pair>
 *        &lt;key>QueuePrefix&lt;/key>
 *        &lt;value>abc&lt;/value>
 *     &lt;/key-value-pair>
 *   &lt;/connection-factory-properties>
 * </pre>
 * </code> will invoke {@link ConnectionFactoryImpl#setQueuePrefix("abc")}, setting the QueuePrefix property to "abc". Only
 * explicitly configured properties will invoke the associated setter method.
 * </p>
 * <p>
 * <b>This was built against Qpid 0.30</b>
 * </p>
 * 
 * @config qpid-advanced-amqp-implementation
 * @license BASIC
 */
@XStreamAlias("qpid-advanced-amqp-implementation")
public class AdvancedQpidImplementation extends BasicQpidImplementation {

  /**
   * Properties matched against various ConnectionFactoryImpl methods.
   */
  public enum ConnectionFactoryProperty {
    /**
     * @see ConnectionFactoryImpl#setAutoGroup(boolean)
     */
    QueuePrefix {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setQueuePrefix(o);
      }
    },
    TopicPrefix {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setTopicPrefix(o);
      }
    },
    UseBinaryMessageId {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setUseBinaryMessageId(Boolean.valueOf(o));
      }
    },
    SyncPublish {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setSyncPublish(Boolean.valueOf(o));
      }
    },
    MaxPrefetch {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setMaxPrefetch(Integer.valueOf(o));
      }
    },
    KeyStorePath {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setKeyStorePath(o);
      }
    },
    KeyStorePassword {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) throws Exception {
        cf.setKeyStorePassword(Password.decode(o));
      }
    },
    KeyStoreCertAlias {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setKeyStoreCertAlias(o);
      }
    },
    TrustStorePath {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setTrustStorePath(o);
      }
    },
    TrustStorePassword {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) throws Exception {
        cf.setTrustStorePassword(Password.decode(o));
      }
    };

    abstract void applyProperty(ConnectionFactoryImpl cf, String s) throws Exception;
  }

  private KeyValuePairSet connectionFactoryProperties;

  public AdvancedQpidImplementation() {
    setConnectionFactoryProperties(new KeyValuePairSet());
  }

  /**
   * @return The additional connection factory properties.
   */
  public KeyValuePairSet getConnectionFactoryProperties() {
    return connectionFactoryProperties;
  }

  /**
   * Set any additional HornetQConnectionFactory properties that are required.
   * <p>
   * The key from the <code>connection-factory-properties</code> element should match the name of the underlying Qpid
   * ConnectionFactory property. <code>
   * <pre>
   *   &lt;connection-factory-properties>
   *     &lt;key-value-pair>
   *        &lt;key>QueuePrefix&lt;/key>
   *        &lt;value>abc&lt;/value>
   *     &lt;/key-value-pair>
   *   &lt;/connection-factory-properties>
   * </pre>
   * </code> will invoke {@link ConnectionFactoryImpl#setQueuePrefix("abc")}, setting the QueuePrefix property to "abc". Only
   * explicitly configured properties will invoke the associated setter method.
   * </p>
   * 
   * @param kvps the additional connectionFactoryProperties to set
   */
  public void setConnectionFactoryProperties(KeyValuePairSet kvps) {
    this.connectionFactoryProperties = kvps;
  }

  @Override
  ConnectionFactoryImpl createQpidConnectionFactory() throws JMSException {
    ConnectionFactoryImpl cf = super.createQpidConnectionFactory();
    try {
      for (KeyValuePair kvp : getConnectionFactoryProperties().getKeyValuePairs()) {
        if (directValueOf(cf, kvp)) continue;
        boolean matched = false;
        for (ConnectionFactoryProperty sp : ConnectionFactoryProperty.values()) {
          if (kvp.getKey().equalsIgnoreCase(sp.toString())) {
            sp.applyProperty(cf, kvp.getValue());
            matched = true;
            break;
          }
        }
        if (!matched) {
          log.trace("Ignoring unsupported Property " + kvp.getKey());
        }
      }
    }
    catch (Exception e) {
      throw new JMSException(e.getMessage());
    }
    return cf;
  }

  private boolean directValueOf(ConnectionFactoryImpl factory, KeyValuePair kvp) throws Exception {
    boolean result = true;
    try {
      ConnectionFactoryProperty sp = ConnectionFactoryProperty.valueOf(kvp.getKey());
      sp.applyProperty(factory, kvp.getValue());
    }
    catch (IllegalArgumentException | NullPointerException e) {
      result = false;
    }
    return result;
  }
}
