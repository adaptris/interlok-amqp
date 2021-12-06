package interlok.rabbitmq;

import com.adaptris.core.AdaptrisConnection;
import com.adaptris.core.ConnectionErrorHandlerImp;
import com.rabbitmq.client.ExceptionHandler;

/** Abstract implementation of {@code ExceptionHandler} for RabbitMQ.
 * 
 */
public abstract class ExceptionHandlerImpl extends ConnectionErrorHandlerImp
    implements ExceptionHandler {
  
  // This happens on prepare() which is before lifecycle happens
  // Since we have to set the exception listener on the connection factory; that's the right place.
  @Override
  public void registerConnection(AdaptrisConnection connection) {
    super.registerConnection(connection);
    retrieveConnection(ConnectionWrapper.class).connectionFactoryBuilder().setExceptionHandler(this);
  }

  @Override
  public void handleConnectionException() {
    super.restartAffectedComponents();
  }
  
}
