package interlok.rabbitmq;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisConnectionImp;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.interlok.util.Args;
import com.adaptris.interlok.util.Closer;
import com.rabbitmq.client.Connection;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Wraps a connection for RabbitMQ.
 * 
 */
@XStreamAlias("rabbitmq-connection")
@AdapterComponent
@ComponentProfile(summary = "Connection to RabbitMQ")
@NoArgsConstructor
public class RabbitMqConnection extends AdaptrisConnectionImp implements ConnectionWrapper {

  /** Configures the underlying {@code com.rabbitmq.client.ConnectionFactory} which is responsible for 
   * creating {@code Connections} and {@code Channels}.
   */
  @Getter
  @Setter
  @Valid
  @NotNull  
  private ConnectionFactoryBuilder factoryBuilder = new SimpleConnectionFactoryBuilder();
  
  private transient Connection rabbitConnection = null;
  
  @Override
  protected void prepareConnection() throws CoreException {
  }

  @Override
  protected void initConnection() throws CoreException {
    try {
      rabbitConnection = getFactoryBuilder().build().newConnection(getUniqueId());
    } catch (Exception e) {
      throw ExceptionHelper.wrapCoreException(e);
    }
  }

  @Override
  protected void startConnection() throws CoreException {
  }

  @Override
  protected void stopConnection() {
  }

  @Override
  protected void closeConnection() {
    Closer.closeQuietly(rabbitConnection);
    rabbitConnection = null;
  }

  @Override
  public Connection wrappedConnection() {
    return Args.notNull(rabbitConnection, "rabbit-connection");
  }

  @Override
  public ConnectionFactoryBuilder connectionFactoryBuilder() {
    return getFactoryBuilder();
  }
  
  @SuppressWarnings("unchecked")
  public <T extends RabbitMqConnection> T withFactoryBuilder(ConnectionFactoryBuilder builder) {
    setFactoryBuilder(builder);
    return (T) this;
  }
}
