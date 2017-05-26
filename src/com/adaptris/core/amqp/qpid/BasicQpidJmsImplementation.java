package com.adaptris.core.amqp.qpid;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.qpid.jms.JmsConnectionFactory;

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
 * This was built against {@code org.apache.qpid:qpid-jms-client:0.8.0}. This vendor implementation doesn't appear to be usable with
 * Azure AMQP as is unless you follow the notes on <a href=
 * "http://stackoverflow.com/questions/40281205/how-can-i-send-receive-a-message-from-azure-service-bus-from-qpid-jms-qpid-jms">stack
 * overflow</a>
 * </p>
 * 
 * @config qpid-basic-jms-implementation
 * @since 3.0.3
 */
@XStreamAlias("qpid-basic-jms-implementation")
public class BasicQpidJmsImplementation extends UrlVendorImplementation {

  JmsConnectionFactory createQpidConnectionFactory() throws JMSException {
    JmsConnectionFactory cf = null;
    try {
      cf = new JmsConnectionFactory(getBrokerUrl());
    } catch (Exception e) {
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
    return (vendorImp instanceof BasicQpidJmsImplementation) && super.connectionEquals(vendorImp);
  }
}
