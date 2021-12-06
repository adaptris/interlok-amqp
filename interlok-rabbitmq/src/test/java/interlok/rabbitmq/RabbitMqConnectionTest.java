package interlok.rabbitmq;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.LifecycleHelper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqConnectionTest {


  @Test
  public void testLifecycle() throws Exception {
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
    RabbitMqConnection conn =
        new RabbitMqConnection().withFactoryBuilder(builder)
            .withErrorHandler(new AlwaysRestartExceptionHandler());
    try {
      LifecycleHelper.initAndStart(conn);
      assertNotNull(conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(conn);
    }
  }


  @Test
  public void testLifecycle_HasErrors() throws Exception {
    ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder() {

      @Override
      public ConnectionFactory build() throws Exception {
        throw new IOException();
      }
    };
    RabbitMqConnection conn = new RabbitMqConnection().withFactoryBuilder(builder);
    try {
      assertThrows(CoreException.class, () -> {
        LifecycleHelper.prepare(conn);
        LifecycleHelper.init(conn);
        LifecycleHelper.start(conn);
      });
      assertThrows(IllegalArgumentException.class, () -> conn.wrappedConnection());
    } finally {
      LifecycleHelper.stopAndClose(conn);
    }
  }

  private class MyConnectionFactory extends ConnectionFactory {

    @Override
    public Connection newConnection(String connectionName) throws IOException, TimeoutException {
      return Mockito.mock(Connection.class);
    }
  }
}

