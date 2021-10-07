package interlok.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ExceptionHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ConnectionFactoryBuilder {

  // Is called via the ConnectionErrorHandler Implementation, thus package since it should
  // never be configurable.
  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private transient ExceptionHandler exceptionHandler;
  
  /** Build the connection factory.
   * 
   */
  public abstract ConnectionFactory build() throws Exception;
  
}
