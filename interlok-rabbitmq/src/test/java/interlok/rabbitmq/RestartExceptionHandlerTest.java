package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import com.adaptris.core.StartedState;
import com.adaptris.core.stubs.MockChannel;
import com.adaptris.core.util.LifecycleHelper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.TopologyRecoveryException;

public class RestartExceptionHandlerTest {

  @Test
  public void testHandleConnectionException() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);

    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the connection.
      excHandler.handleConnectionException();
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testUnexpectedConnectionDriverException() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);

    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the connection.
      excHandler.handleUnexpectedConnectionDriverException(null, new Exception());
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testBlockedListenerException() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);

    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the connection.
      excHandler.handleBlockedListenerException(null, new Exception());
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }

  }



  @Test
  public void testConnectionRecoveryException() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);

    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the connection.
      excHandler.handleConnectionRecoveryException(null, new Exception());
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }


  @Test
  public void testReturnListenerException() throws Exception {

    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);


    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the param.
      excHandler.handleReturnListenerException(null, new Exception());
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testConfirmListenerException() throws Exception {

    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);


    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the param.
      excHandler.handleConfirmListenerException(null, new Exception());
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }

  }


  @Test
  public void testConsumerException() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);


    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the param.
      excHandler.handleConsumerException(null, new Exception(), null, null, null);
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testChannelRecoveryException() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);


    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the param.
      excHandler.handleChannelRecoveryException(null, new Exception());
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  @Test
  public void testTopologyRecoveryException() throws Exception {
    checkEnabled();
    String brokerUrl = brokerURL();
    AlwaysRestartExceptionHandler excHandler = new AlwaysRestartExceptionHandler();
    RabbitMqConnection conn = new RabbitMqConnection()
        .withFactoryBuilder(new SimpleConnectionFactoryBuilder().withBrokerUrl(brokerUrl));
    conn.setConnectionErrorHandler(excHandler);


    MockChannel channel = new MockChannel();
    channel.setProduceConnection(conn);

    try {
      LifecycleHelper.initAndStart(channel);

      Connection origConn = conn.wrappedConnection();
      // bit of a cheat really but we know we ignore the param.
      excHandler.handleTopologyRecoveryException(null, null,
          new TopologyRecoveryException("", new Exception()));
      // Wait for the transition to include a StoppedState (init,start,stop,close,init,start should
      // the states...
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.getStopCount() >= 1);

      // Now wait for it to be restarted.
      Awaitility.await().atMost(Duration.ofSeconds(5)).with().pollInterval(Duration.ofMillis(50))
          .until(() -> channel.retrieveComponentState() == StartedState.getInstance());

      assertNotSame(origConn, conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(channel);
    }
  }

  // Will be overriden by interop tests.
  protected void checkEnabled() {
    JunitConfig.rmqTestsEnabled();
  }
  
  // Will be overriden by interop tests.
  protected String brokerURL() {
    return JunitConfig.brokerURL();
  }
}
