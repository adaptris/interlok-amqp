package com.adaptris.core.amqp.qpid;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;

import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 1.0 implementation of {@link VendorImplementation} using Apache Qpid.
 * 
 * <p>
 * <b>This was built against Qpid 0.30</b>
 * </p>
 * 
 * @config qpid-basic-amqp-implementation
 * @license BASIC
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
  public boolean connectionEquals(VendorImplementation vendorImp) {
    return (vendorImp instanceof BasicQpidImplementation) && super.connectionEquals(vendorImp);
  }
}
