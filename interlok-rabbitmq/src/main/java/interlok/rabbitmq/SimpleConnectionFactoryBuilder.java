package interlok.rabbitmq;

import java.util.Optional;
import javax.validation.constraints.NotBlank;
import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.interlok.util.Args;
import com.rabbitmq.client.ConnectionFactory;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple connection factory for RabbitMQ.
 * <p>
 * This is the simplest connection factory possible; everything required to connect must be specified as part of the broker-url. 
 * In the event that you want to have obfuscated passwords then you want to use {@link BasicConnectionFactoryBuilder} instead.
 * </p>
 * <p>If you don't override the username and password in the broker-url then it's likely to default to {@code guest/guest} which 
 * is the underlying SDK default.
 * </p>  
 */
@XStreamAlias("rabbitmq-simple-connection-factory")
@AdapterComponent
@ComponentProfile(summary = "Simple RabbitMQ Connection Builder", since="4.3.0")
@NoArgsConstructor
public class SimpleConnectionFactoryBuilder extends ConnectionFactoryBuilder {

  /** The RabbitMQ Broker URL.
   *  <p>Everything including the username and password may be defined on the broker url so you can configure something like 
   *  {@code amqp://admin:admin@localhost:5672/vhost} as required. 
   *  </p>
   *  
   */
  @NotBlank
  @Setter
  @Getter
  private String brokerUrl;
  
  @Override
  public ConnectionFactory build() throws Exception{
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(Args.notBlank(getBrokerUrl(), "url"));
    Optional.ofNullable(getExceptionHandler()).ifPresent((e) -> factory.setExceptionHandler(e));
    return factory;
  }
  
  @SuppressWarnings({"unchecked"})
  public <T extends SimpleConnectionFactoryBuilder> T withBrokerUrl(String s) {
    setBrokerUrl(s);
    return (T) this;
  }
}
