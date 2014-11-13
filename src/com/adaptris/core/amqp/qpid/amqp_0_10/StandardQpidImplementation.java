package com.adaptris.core.amqp.qpid.amqp_0_10;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.qpid.client.AMQConnectionFactory;

import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AMQP 0.10 / 0.9.1 / 0.9 / 0.8 implementation of {@link VendorImplementation} using Apache Qpid.
 * <p>
 * <b>This was built against Qpid 0.30</b>
 * </p>
 * 
 * @config 0-10-qpid-implementation
 * @license BASIC
 */
@XStreamAlias("0-10-qpid-implementation")
public class StandardQpidImplementation extends UrlVendorImplementation {

  AMQConnectionFactory createQpidConnectionFactory() throws JMSException {
    AMQConnectionFactory cf;
    try {
      cf = new AMQConnectionFactory(getBrokerUrl());
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
    return (vendorImp instanceof StandardQpidImplementation) && super.connectionEquals(vendorImp);
  }
}
