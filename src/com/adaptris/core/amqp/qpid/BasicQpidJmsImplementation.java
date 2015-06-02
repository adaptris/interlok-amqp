package com.adaptris.core.amqp.qpid;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.qpid.jms.JmsConnectionFactory;

import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
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
 * This was built against {@code org.apache.qpid:qpid-jms-client:0.2.0}
 * </p>
 * 
 * @config qpid-basic-jms-implementation
 * @license BASIC
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
  public boolean connectionEquals(VendorImplementation vendorImp) {
    return (vendorImp instanceof BasicQpidJmsImplementation) && super.connectionEquals(vendorImp);
  }
}
