package com.adaptris.core.amqp.qpid;

import javax.jms.JMSException;
import javax.validation.constraints.NotNull;

import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.security.password.Password;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 1.0 implementation of {@link VendorImplementation} using Apache Qpid.
 * <p>
 * This vendor implementation class directly exposes almost all the setters that are available in
 * the ConnectionFactory for maximum flexibility in configuration. The main difference between this
 * and {@link BasicQpidImplementation} is that you do not have to configure all the properties as
 * part of the URL, which is simply a bonus for readability. The Keystore and Truststore passwords
 * may also be encoded using {@link Password#encode(String, String)} rather than plain text.
 * </p>
 * <p>
 * The key from the {@code connection-factory-properties} element should match the name of the
 * underlying Qpid ConnectionFactory property.
 * 
 * <pre>
 * {@code 
 *   <connection-factory-properties>
 *     <key-value-pair>
 *        <key>QueuePrefix&lt;/key>
 *        <value>abc&lt;/value>
 *     </key-value-pair>
 *   </connection-factory-properties>
 * }
 * </pre>
 * 
 * will invoke {@link ConnectionFactoryImpl#setQueuePrefix(String)}, setting the QueuePrefix
 * property to "abc". Only explicitly configured properties will invoke the associated setter
 * method; unmatched properties are ignored and property keys are not case sensitive
 * </p>
 * <p>
 * This was built against {@code org.apache.qpid:qpid-amqp-1-0-client-jms:0.32}
 * </p>
 * 
 * @config qpid-advanced-amqp-implementation
 * @deprecated since 3.0.3 use {@link AdvancedQpidJmsImplementation} instead for AMQP 1.0
 */
@XStreamAlias("qpid-advanced-amqp-implementation")
@Deprecated
public class AdvancedQpidImplementation extends BasicQpidImplementation {

  /**
   * Properties matched against various ConnectionFactoryImpl methods.
   */
  public enum ConnectionFactoryProperty {
    /**
     * @see ConnectionFactoryImpl#setQueuePrefix(String)
     */
    QueuePrefix {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setQueuePrefix(o);
      }
    },
    /**
     * @see ConnectionFactoryImpl#setTopicPrefix(String)
     */
    TopicPrefix {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setTopicPrefix(o);
      }
    },
    /**
     * @see ConnectionFactoryImpl#setUseBinaryMessageId(boolean)
     */
    UseBinaryMessageId {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setUseBinaryMessageId(Boolean.valueOf(o));
      }
    },
    /**
     * @see ConnectionFactoryImpl#setSyncPublish(Boolean)
     */
    SyncPublish {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setSyncPublish(Boolean.valueOf(o));
      }
    },
    /**
     * @see ConnectionFactoryImpl#setMaxPrefetch(int)
     */
    MaxPrefetch {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setMaxPrefetch(Integer.valueOf(o));
      }
    },
    /**
     * @see ConnectionFactoryImpl#setKeyStorePath(String)
     */
    KeyStorePath {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setKeyStorePath(o);
      }
    },
    /**
     * Set the keystore password.
     * <p>
     * This password may be encoded using {@link Password#encode(String, String)}
     * </p>
     * 
     * @see ConnectionFactoryImpl#setKeyStorePassword(String)
     */
    KeyStorePassword {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) throws Exception {
        cf.setKeyStorePassword(Password.decode(o));
      }
    },
    /**
     * @see ConnectionFactoryImpl#setKeyStoreCertAlias(String)
     */
    KeyStoreCertAlias {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setKeyStoreCertAlias(o);
      }
    },
    /**
     * @see ConnectionFactoryImpl#setTrustStorePath(String)
     */
    TrustStorePath {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) {
        cf.setTrustStorePath(o);
      }
    },
    /**
     * Set the truststore password.
     * <p>
     * This password may be encoded using {@link Password#encode(String, String)}
     * </p>
     * 
     * @see ConnectionFactoryImpl#setTrustStorePassword(String)
     */
    TrustStorePassword {
      @Override
      void applyProperty(ConnectionFactoryImpl cf, String o) throws Exception {
        cf.setTrustStorePassword(Password.decode(o));
      }
    };

    abstract void applyProperty(ConnectionFactoryImpl cf, String s) throws Exception;
  }

  @NotNull
  @AutoPopulated
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
   * Set any additional ConnectionFactory properties that are required.
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
   * </code> will invoke {@link ConnectionFactoryImpl#setQueuePrefix(String)}, setting the QueuePrefix property to "abc". Only
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
