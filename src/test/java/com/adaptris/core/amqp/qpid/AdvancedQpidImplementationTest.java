package com.adaptris.core.amqp.qpid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import javax.jms.JMSException;
import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;

public class AdvancedQpidImplementationTest extends BasicQpidImplementationTest {

  private static Logger log = LoggerFactory.getLogger(AdvancedQpidImplementationTest.class);

  @Test
  public void testConnectionProperties() throws Exception {
    AdvancedQpidImplementation vendor = createVendorImpl("amqp://localhost:5672");
    KeyValuePairSet connectionProperties = new KeyValuePairSet();
    connectionProperties.addKeyValuePair(new KeyValuePair("queuePrefix", "myQueuePrefix"));
    connectionProperties.addKeyValuePair(new KeyValuePair("TopicPrefix", "myTopicPrefix"));
    connectionProperties.addKeyValuePair(new KeyValuePair("UseBinaryMessageId", "true"));
    connectionProperties.addKeyValuePair(new KeyValuePair("SyncPublish", "true"));
    connectionProperties.addKeyValuePair(new KeyValuePair("MaxPrefetch", "10"));
    connectionProperties.addKeyValuePair(new KeyValuePair("KeyStorePath", "myKeyStorePath"));
    connectionProperties.addKeyValuePair(new KeyValuePair("KeyStorePassword", "myKeyStorePassword"));
    connectionProperties.addKeyValuePair(new KeyValuePair("KeyStoreCertAlias", "myKeyStoreCertAlias"));
    connectionProperties.addKeyValuePair(new KeyValuePair("TrustStorePath", "myTrustStorePath"));
    connectionProperties.addKeyValuePair(new KeyValuePair("TrustStorePassword", "myTrustStorePassword"));
    connectionProperties.addKeyValuePair(new KeyValuePair("SomeRubbish", "blahblah"));
    vendor.setConnectionFactoryProperties(connectionProperties);
    ConnectionFactoryImpl factory = vendor.createQpidConnectionFactory();

    assertNotNull(factory);
    assertEquals("myQueuePrefix", factory.getQueuePrefix());
    assertEquals("myTopicPrefix", factory.getTopicPrefix());

    connectionProperties.clear();
    connectionProperties.add(new KeyValuePair("KeyStorePassword", "PW:1234ddasd"));
    vendor.setConnectionFactoryProperties(connectionProperties);
    try {
      vendor.createQpidConnectionFactory();
      fail(); // Fail becuse password surely can't be decoded.
    }
    catch (JMSException expected) {

    }

    connectionProperties.clear();
    connectionProperties.add(new KeyValuePair("TrustStorePassword", "PW:1234ddasd"));
    vendor.setConnectionFactoryProperties(connectionProperties);
    try {
      vendor.createQpidConnectionFactory();
      fail(); // Fail becuse password surely can't be decoded.
    }
    catch (JMSException expected) {

    }
  }

  @Override
  protected AdvancedQpidImplementation createVendorImpl(String brokerUrl) {
    AdvancedQpidImplementation mq = new AdvancedQpidImplementation();
    mq.setBrokerUrl(brokerUrl);
    // mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    return mq;
  }

}
