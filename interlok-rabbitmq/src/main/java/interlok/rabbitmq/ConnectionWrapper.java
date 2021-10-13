package interlok.rabbitmq;

import com.rabbitmq.client.Connection;

/** Interface that wraps connections and builders usually implemented by {@link RabbitMqConnection}.
 * 
 */
public interface ConnectionWrapper {

  /**
   * 
   * @return the wrapped connection.
   */
  Connection wrappedConnection();
  
  /**
   * 
   * @return the wrapped connection factory builder.
   */
  ConnectionFactoryBuilder connectionFactoryBuilder();
    
}
