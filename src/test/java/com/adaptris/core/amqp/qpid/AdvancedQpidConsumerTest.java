package com.adaptris.core.amqp.qpid;

import com.adaptris.core.ConfiguredConsumeDestination;
import com.adaptris.core.StandaloneConsumer;
import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.PtpConsumer;
import com.adaptris.util.KeyValuePair;

public class AdvancedQpidConsumerTest extends BasicQpidConsumerTest {

  @Override
  protected String createBaseFileName(Object object) {
    return ((StandaloneConsumer) object).getConsumer().getClass().getName() + "-QPID-ADVANCED-AMQP-1.0";
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    PtpConsumer consumer = new PtpConsumer(new ConfiguredConsumeDestination("SOME_MQ_QUEUE"));
    StandaloneConsumer result = new StandaloneConsumer(configure(new JmsConnection()), consumer);
    return result;
  }

  @Override
  protected AdvancedQpidJmsImplementation createVendorImpl() {
    AdvancedQpidJmsImplementation mq = new AdvancedQpidJmsImplementation();
    mq.setBrokerUrl("amqp://localhost:5672?clientid=test-client&remote-host=default");
    mq.getConnectionFactoryProperties().add(new KeyValuePair("QueuePrefix", "myQueuePrefix"));
    mq.getConnectionFactoryProperties().add(new KeyValuePair("TopicPrefix", "myTopicPrefix"));
    return mq;
  }
}
