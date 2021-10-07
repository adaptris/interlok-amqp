package interlok.rabbitmq;

import java.util.Optional;
import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.interlok.resolver.ExternalResolver;
import com.adaptris.security.password.Password;
import com.rabbitmq.client.ConnectionFactory;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A basic connection factory for RabbitMQ.
 * <p>
 * Extends {@link SimpleConnectionFactoryBuilder} and provides obfuscated password support. Both
 * fields are entirely optional and you may still override the username and password via the URL. If
 * the username and password are not explicitly specified then it may well default to
 * {@code guest/guest} which appears to be the RabbitMQ SDK defaults.
 * </p>
 */
@XStreamAlias("rabbitmq-basic-connection-factory")
@AdapterComponent
@ComponentProfile(summary = "Basic RabbitMQ Connection Builder", since="4.3.0")
@NoArgsConstructor
public class BasicConnectionFactoryBuilder extends SimpleConnectionFactoryBuilder {

  /**
   * The username used to connection to the broker.
   * <p>
   * If you do not explicitly override the user then it is likely to default to {@code guest} which
   * appears to be the RabbitMQ SDK default.
   * </p>
   * 
   */
  @Setter
  @Getter
  private String username;
  /**
   * The password.
   * <p>
   * If you do not explicitly override the password then it is likely to default to {@code guest} which
   * appears to be the RabbitMQ SDK default.
   * </p>
   */
  @Setter
  @Getter
  @InputFieldHint(style = "PASSWORD", external = true)
  private String password;

  @Override
  public ConnectionFactory build() throws Exception {
    ConnectionFactory factory = super.build();
    String pw = Password.decode(ExternalResolver.resolve(getPassword()));
    Optional.ofNullable(getUsername()).ifPresent((u) -> factory.setUsername(u));
    Optional.ofNullable(pw).ifPresent((pass) -> factory.setPassword(pass));
    return factory;
  }

  @SuppressWarnings({"unchecked"})
  public <T extends BasicConnectionFactoryBuilder> T withCredentials(String u, String p) {
    setUsername(u);
    setPassword(p);
    return (T) this;
  }
}
