package com.adaptris.core.amqp.qpid.amqp_0_10;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;

import com.adaptris.core.jms.JmsConnection;
import com.adaptris.core.jms.UrlVendorImplementation;
import com.adaptris.core.jms.VendorImplementation;
import com.adaptris.core.jms.VendorImplementationBase;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.qpid.jms.JmsConnectionFactory;

/**
 * AMQP 0.10 / 0.9.1 / 0.9 / 0.8 implementation of {@link VendorImplementation} using Apache Qpid.
 * <p>
 * Everything required to configure the connection needs to be specified on the URL. If you configure a username and password on the
 * wrapping {@link JmsConnection} in which case {@link ConnectionFactory#createConnection(String, String)} is used when creating the
 * connection otherwise {@link ConnectionFactory#createConnection()} will be used.
 * </p>
 * <p>
 * This was built against {@code org.apache.qpid:qpid-client:6.0.1}
 * </p>
 *
 * @config qpid-implementation-0-10
 */
@XStreamAlias("qpid-implementation-0-10")
public class StandardQpidImplementation extends UrlVendorImplementation {

  JmsConnectionFactory createQpidConnectionFactory() throws JMSException {
    JmsConnectionFactory cf;
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
    return vendorImp instanceof StandardQpidImplementation && super.connectionEquals(vendorImp);
  }

}
