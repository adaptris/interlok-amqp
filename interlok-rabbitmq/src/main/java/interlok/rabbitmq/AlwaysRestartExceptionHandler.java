package interlok.rabbitmq;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisConnection;
import com.adaptris.core.ConnectionErrorHandler;
import com.adaptris.core.ConnectionErrorHandlerImp;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.TopologyRecoveryException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.NoArgsConstructor;

/**
 * {@link ConnectionErrorHandler} implementation always restarts always restarts everything.
 */
@XStreamAlias("rabbitmq-on-error-restart")
@AdapterComponent
@ComponentProfile(summary = "When RabbitMQ reports an exception, always restart",
    recommended = {RabbitMqConnection.class}, since = "4.3.0")
@NoArgsConstructor
public class AlwaysRestartExceptionHandler extends ConnectionErrorHandlerImp
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
  
  @Override
  public void handleUnexpectedConnectionDriverException(Connection c, Throwable e) {
    logAndRestart(e, "UnexpectedConnectionDriverException");
  }

  @Override
  public void handleReturnListenerException(Channel ch, Throwable e) {
    logAndRestart(e, "ReturnListenerException");
  }

  @Override
  public void handleConfirmListenerException(Channel ch, Throwable e) {
    logAndRestart(e, "ConfirmListenerException");
  }

  @Override
  public void handleBlockedListenerException(Connection c, Throwable e) {
    logAndRestart(e, "BlockedListenerException");
  }

  // This is never going to fire because our parent service never adds a consumer.
  @Override
  public void handleConsumerException(Channel c, Throwable e, Consumer consumer, String consumerTag,
      String methodName) {
    logAndRestart(e, "ConsumerException");
  }

  @Override
  public void handleConnectionRecoveryException(Connection c, Throwable e) {
    logAndRestart(e, "ConnectionRecoveryException");
  }

  @Override
  public void handleChannelRecoveryException(Channel ch, Throwable exception) {
    logAndRestart(exception, "ChannelRecoveryException");
  }

  @Override
  public void handleTopologyRecoveryException(Connection c, Channel ch,
      TopologyRecoveryException e) {
    logAndRestart(e, "TopologyRecoveryException");
  }

  private void logAndRestart(Throwable exception, String logMessage) {
    log.warn("Restarting because of {} Reason: [{}]", logMessage, exception.getMessage());
    super.restartAffectedComponents();
  }


}
