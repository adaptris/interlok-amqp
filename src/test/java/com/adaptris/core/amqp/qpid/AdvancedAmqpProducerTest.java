package com.adaptris.core.amqp.qpid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.StandaloneProducer;
import com.adaptris.util.KeyValuePair;

public class AdvancedAmqpProducerTest extends BasicAmqpProducerTest {

  private static Logger log = LoggerFactory.getLogger(AdvancedAmqpProducerTest.class);

  public AdvancedAmqpProducerTest(String name) {
    super(name);
  }


  @Override
  protected String createBaseFileName(Object object) {
    return ((StandaloneProducer) object).getProducer().getClass().getName() + "-QPID-ADVANCED-AMQP-1.0";
  }


  protected AdvancedQpidJmsImplementation createVendorImpl() {
    AdvancedQpidJmsImplementation mq = new AdvancedQpidJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    mq.getConnectionFactoryProperties().add(new KeyValuePair("QueuePrefix", "myQueuePrefix"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair("TopicPrefix", "myTopicPrefix"));
    return mq;
  }

}
