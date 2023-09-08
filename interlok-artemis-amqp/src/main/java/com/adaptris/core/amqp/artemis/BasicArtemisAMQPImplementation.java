package com.adaptris.core.amqp.artemis;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.qpid.jms.JmsConnectionFactory;

import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.core.jms.VendorImplementationBase;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 1.0 implementation of {@link VendorImplementation} using Apache Artemis.
 * 
 * <p>
 * Everything required to configure the connection needs to be specified on the URL. If you
 * configure a username and password on the wrapping {@link JmsConnection} in which case
 * {@link ConnectionFactory#createConnection(String, String)} is used when creating the connection
 * otherwise {@link ConnectionFactory#createConnection()} will be used.
 * </p>
 * <p>
 * This vendor implementation is suitable for use with Azure AMQP. If you are using a topic consumer then it must be a durable
 * subscriber where the subscriptionID is the same as the subscription created in the Azure portal; the topic name should be
 * {@code [topic-name]/subscriptions/[subscriptionID]}. Your mileage may vary but during testing this was the the only destination
 * configuration that seemed to work; the documentation suggests that this might actually pretending to be a {@code queue}.
 * </p>
 * 
 * @config artemis-basic-amqp-implementation
 */
@XStreamAlias("artemis-basic-amqp-implementation")
public class BasicArtemisAMQPImplementation extends UrlVendorImplementation {

  ConnectionFactory createArtemisAMQPConnectionFactory() throws JMSException {
    ConnectionFactory cf;
    try {
      cf = new JmsConnectionFactory(getBrokerUrl());
    } catch (Exception e) {
      throw new JMSException(e.getMessage());
    }
    return cf;
  }

  @Override
  public ConnectionFactory createConnectionFactory() throws JMSException {
    return createArtemisAMQPConnectionFactory();
  }

  @Override
  public boolean connectionEquals(VendorImplementationBase vendorImp) {
    return vendorImp instanceof BasicArtemisAMQPImplementation && super.connectionEquals(vendorImp);
  }

}
