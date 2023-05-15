package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.rabbitmq.client.AMQP.Exchange;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class ExplicitExchangeTest {

  @Test
  public void testDeclareExchange() throws Exception {
    Channel channel = Mockito.mock(Channel.class);
    Exchange.DeclareOk ok = Mockito.mock(Exchange.DeclareOk.class);
    Mockito.when(channel.exchangeDeclare(anyString(), any(BuiltinExchangeType.class), anyBoolean())).thenReturn(ok);
    ExplicitExchange declaration = new ExplicitExchange().withDurable(true).withName("myExch").withType(BuiltinExchangeType.DIRECT);
    declaration.declare(channel);
  }

  @Test
  public void testName() throws Exception {
    ExplicitExchange declaration = new ExplicitExchange().withDurable(true).withName("myExch").withType(BuiltinExchangeType.DIRECT);
    assertEquals("myExch", declaration.name());
  }

}
