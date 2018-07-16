package com.adaptris.core.amqp.qpid;

import javax.jms.JMSException;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;

public class AdvancedQpidJmsImplementationTest extends BasicQpidJmsImplementationTest {

  private static Logger log = LoggerFactory.getLogger(AdvancedQpidJmsImplementationTest.class);

  public AdvancedQpidJmsImplementationTest(String name) {
    super(name);
  }

  public void testConnectionProperties() throws Exception {
    AdvancedQpidJmsImplementation vendor = createVendorImpl("amqp://localhost:5672");
    KeyValuePairSet props = new KeyValuePairSet();
    props.addKeyValuePair(new KeyValuePair("queuePrefix", "myQueuePrefix"));
    props.addKeyValuePair(new KeyValuePair("TopicPrefix", "myTopicPrefix"));
    props.addKeyValuePair(new KeyValuePair("UseBinaryMessageId", "true"));
    props.addKeyValuePair(new KeyValuePair("SyncPublish", "true"));
    props.addKeyValuePair(new KeyValuePair("MaxPrefetch", "10"));
    props.addKeyValuePair(new KeyValuePair("KeyStorePath", "myKeyStorePath"));
    props.addKeyValuePair(new KeyValuePair("KeyStorePassword", "myKeyStorePassword"));
    props.addKeyValuePair(new KeyValuePair("KeyStoreCertAlias", "myKeyStoreCertAlias"));
    props.addKeyValuePair(new KeyValuePair("TrustStorePath", "myTrustStorePath"));
    props.addKeyValuePair(new KeyValuePair("TrustStorePassword", "myTrustStorePassword"));
    props.addKeyValuePair(new KeyValuePair("SomeRubbish", "blahblah"));
    vendor.setConnectionFactoryProperties(props);
    JmsConnectionFactory factory = vendor.createQpidConnectionFactory();

    assertNotNull(factory);
    assertEquals("myQueuePrefix", factory.getQueuePrefix());
    assertEquals("myTopicPrefix", factory.getTopicPrefix());
  }

  @Override
  protected AdvancedQpidJmsImplementation createVendorImpl(String brokerUrl) {
    AdvancedQpidJmsImplementation mq = new AdvancedQpidJmsImplementation();
    mq.setBrokerUrl(brokerUrl + "?transport.connectTimeout=1000&jms.connectTimeout=1000");
    // mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    return mq;
  }

}
