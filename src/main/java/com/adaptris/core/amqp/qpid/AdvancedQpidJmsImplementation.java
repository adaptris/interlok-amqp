package com.adaptris.core.amqp.qpid;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.validation.constraints.NotNull;

import org.apache.qpid.jms.JmsConnectionFactory;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 1.0 implementation of {@link VendorImplementation} using Apache Qpid.
 * 
 * <p>
 * Note that any custom properties as passed directly to
 * {@link JmsConnectionFactory#setProperties(Map)}. Behaviour for unsupported properties will be
 * implementation dependent. If you configure a username and password on the wrapping
 * {@link JmsConnection} in which case {@link ConnectionFactory#createConnection(String, String)} is
 * used when creating the connection otherwise {@link ConnectionFactory#createConnection()} will be
 * used.
 * </p>
 * <p>
 * This was built against {@code org.apache.qpid:qpid-jms-client:0.8.0}. This vendor implementation doesn't appear to be usable with
 * Azure AMQP as is unless you follow the notes on <a href=
 * "http://stackoverflow.com/questions/40281205/how-can-i-send-receive-a-message-from-azure-service-bus-from-qpid-jms-qpid-jms">stack
 * overflow</a>
 * </p>
 * 
 * @config qpid-advanced-jms-implementation
 * @since 3.0.3
 */
@XStreamAlias("qpid-advanced-jms-implementation")
public class AdvancedQpidJmsImplementation extends BasicQpidJmsImplementation {

  @NotNull
  @AutoPopulated
  private KeyValuePairSet connectionFactoryProperties;

  public AdvancedQpidJmsImplementation() {
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
   * 
   * @param kvps the additional connectionFactoryProperties to set
   */
  public void setConnectionFactoryProperties(KeyValuePairSet kvps) {
    this.connectionFactoryProperties = kvps;
  }

  @Override
  JmsConnectionFactory createQpidConnectionFactory() throws JMSException {
    JmsConnectionFactory cf = super.createQpidConnectionFactory();
    Map<String, String> ignored = cf.setProperties(asStringMap(getConnectionFactoryProperties()));
    if (!ignored.isEmpty()) {
      log.trace("Ignored Properties : {}", ignored);
    }
    return cf;
  }

  private static Map<String, String> asStringMap(KeyValuePairSet kvps) {
    LinkedHashMap<String, String> result = new LinkedHashMap<>();
    for (KeyValuePair kvp : kvps.getKeyValuePairs()) {
      result.put(kvp.getKey(), kvp.getValue());
    }
    return result;
  }
  
}
