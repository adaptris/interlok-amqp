package com.adaptris.core.amqp.qpid;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;

import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.core.jms.VendorImplementationBase;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 1.0 implementation of {@link VendorImplementation} using Apache Qpid.
 * 
 * <p>
 * Everything required to configure the connection needs to be specified on the URL. If you
 * configure a username and password on the wrapping {@link JmsConnection} in which case
 * {@link ConnectionFactory#createConnection(String, String)} is used when creating the connection
 * otherwise {@link ConnectionFactory#createConnection()} will be used.
 * </p>
 * <p>
 * This vendor implementation is suitable for use with Azure AMQP. If you are using topics then it must be a durable subscriber
 * where the subscriptionID is the same as the subscription created in the Azure portal; the topic name should be
 * {@code [topic-name]/subscriptions/[subscriptionID]}. Your mileage may vary but during testing this was the the only destination
 * configuration that seemed to work.
 * </p>
 * <p>
 * This was built against {@code org.apache.qpid:qpid-amqp-1-0-client-jms:0.32}.
 * </p>
 * 
 * @config qpid-basic-amqp-implementation
 */
@XStreamAlias("qpid-basic-amqp-implementation")
public class BasicQpidImplementation extends UrlVendorImplementation {

  ConnectionFactoryImpl createQpidConnectionFactory() throws JMSException {
    ConnectionFactoryImpl cf;
    try {
      cf = ConnectionFactoryImpl.createFromURL(getBrokerUrl());
    }
    catch (Exception e) {
      throw new JMSException(e.getMessage());
    }
    return cf;
  }

  @Override
  public ConnectionFactory createConnectionFactory() throws JMSException {
    return createQpidConnectionFactory();
  }

  @Override
  public boolean connectionEquals(VendorImplementationBase vendorImp) {
    return (vendorImp instanceof BasicQpidImplementation) && super.connectionEquals(vendorImp);
  }
}
