package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.adaptris.core.StartedState;
import com.adaptris.core.stubs.MockChannel;
import com.adaptris.core.util.LifecycleHelper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.TopologyRecoveryException;

public class RestartExceptionHandlerTest {

  @Test
  public void testHandleConnectionException() throws Exception {
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);

    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);
      waitForRecovery(channel, () -> excHandler.handleConnectionException());
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testUnexpectedConnectionDriverException() throws Exception {
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);

    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);
      waitForRecovery(channel, () -> excHandler.handleUnexpectedConnectionDriverException(null, new Exception()));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());

    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testBlockedListenerException() throws Exception {
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);
    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);
      waitForRecovery(channel, () -> excHandler.handleBlockedListenerException(null, new Exception()));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }

  }

  @Test
  public void testConnectionRecoveryException() throws Exception {
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);

    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);
      waitForRecovery(channel, () -> excHandler.handleConnectionRecoveryException(null, new Exception()));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testReturnListenerException() throws Exception {

    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);
    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);
      waitForRecovery(channel, () -> excHandler.handleReturnListenerException(null, new Exception()));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());

    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testConfirmListenerException() throws Exception {

    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);
    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);

      waitForRecovery(channel, () -> excHandler.handleConfirmListenerException(null, new Exception()));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }

  }

  @Test
  public void testConsumerException() throws Exception {
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);
    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);

      waitForRecovery(channel, () -> excHandler.handleConsumerException(null, new Exception(), null, null, null));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());

    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testChannelRecoveryException() throws Exception {
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);

    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);

      waitForRecovery(channel, () -> excHandler.handleChannelRecoveryException(null, new Exception()));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testTopologyRecoveryException() throws Exception {
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    MockChannel channel = createChannelForRecovery(excHandler);

    try {
      LifecycleHelper.initAndStart(channel);
      RabbitMqConnection conn = (RabbitMqConnection) channel.getProduceConnection();
      Connection origCon = conn.wrappedConnection();
      assertNotNull(origCon);

      waitForRecovery(channel,
          () -> excHandler.handleTopologyRecoveryException(null, null, new TopologyRecoveryException("", new Exception())));
      assertNotNull(conn.wrappedConnection());
      assertNotSame(origCon, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  private MockChannel createChannelForRecovery(AlwaysRestartExceptionHandler exc) throws Exception {
    RabbitMqConnection conn = new RabbitMqConnection().withErrorHandler(exc);
    if (JunitConfig.nonMockTests()) {
      String brokerUrl = brokerURL();
      conn.withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    } else {
      ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder() {

        @Override
        public ConnectionFactory build() throws Exception {
          return new ConnectionFactory() {
            @Override
            public Connection newConnection(String connectionName) throws IOException, TimeoutException {
              return Mockito.mock(Connection.class);
            }
          };
        }
      };
      conn.withFactoryBuilder(builder);
    }
    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);
    return channel;
  }

  private void waitForRecovery(MockChannel channel, Action action) throws Exception {
    action.trigger();
    // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
    // the states...
    Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50)).until(() -> channel.getStopCount() >= 1);

    // Now wait for it to be restarted.
    Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
        .until(() -> channel.retrieveComponentState() == StartedState.getInstance());
  }

  protected String brokerURL() {
    return JunitConfig.brokerURL();
  }

  @FunctionalInterface
  private interface Action {
    public void trigger();
  }

}
