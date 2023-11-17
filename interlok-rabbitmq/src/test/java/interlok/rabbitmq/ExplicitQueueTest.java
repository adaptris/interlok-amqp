package interlok.rabbitmq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.Channel;

public class ExplicitQueueTest {

  @Test
  public void testDeclareQueue() throws Exception {
    Channel channel = Mockito.mock(Channel.class);
    Queue.DeclareOk ok = Mockito.mock(Queue.DeclareOk.class);
    Mockito.when(channel.queueDeclare(anyString(), anyBoolean(), anyBoolean(), anyBoolean(), any())).thenReturn(ok);
    ExplicitQueue declaration = new ExplicitQueue();
    declaration.declare(channel, "myQueue");
  }

}
