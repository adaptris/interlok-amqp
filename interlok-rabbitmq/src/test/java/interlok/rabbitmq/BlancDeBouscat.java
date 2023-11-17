package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;

import org.awaitility.Awaitility;

import com.adaptris.core.MetadataElement;
import com.adaptris.interlok.util.Closer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

// The Blanc de Bouscat is a large white rabbit originally bred in France in 1906.. It's at risk
// so we immortalize it in code (!).
public class BlancDeBouscat implements AutoCloseable {

  private Connection connection;
  private Channel channel;
  private String myQueue;

  public BlancDeBouscat(String brokerUrl, String queue) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(brokerUrl);
    connection = factory.newConnection();
    channel = connection.createChannel();
    myQueue = queue;
    channel.queueDeclare(myQueue, true, false, false, null);
  }

  public GetResponse getAndVerify(String expected) throws Exception {
    // Some timing issues because we have to go through the exchange
    // so wait
    Awaitility.await().atMost(Duration.ofSeconds(2)).with().pollInterval(Duration.ofMillis(50))
        .until(() -> channel.messageCount(myQueue) >= 1);

    GetResponse response = channel.basicGet(myQueue, true);
    byte[] bytes = response.getBody();
    assertArrayEquals(expected.getBytes(StandardCharsets.UTF_8), bytes);
    return response;
  }

  public void publish(String data, BasicProperties properties) throws Exception {
    channel.basicPublish("", myQueue, properties, data.getBytes(StandardCharsets.UTF_8));
  }

  public void publish(String data) throws Exception {
    publish(data, null);
  }

  public GetResponse getAndVerify(String expected, Collection<MetadataElement> metadata) throws Exception {
    GetResponse response = getAndVerify(expected);
    Map<String, Object> msgHeaders = response.getProps().getHeaders();
    assertNotNull(msgHeaders);
    for (MetadataElement e : metadata) {
      assertEquals(e.getValue(), msgHeaders.get(e.getKey()).toString());
    }
    return response;
  }

  @Override
  public void close() throws IOException {
    executeQuietly(() -> channel.queuePurge(myQueue));
    executeQuietly(() -> channel.queueDelete(myQueue));
    Closer.closeQuietly(channel, connection);
  }

  private static void executeQuietly(ChannelOperation op) {
    try {
      op.execute();
    } catch (Exception e) {

    }
  }

  @FunctionalInterface
  private interface ChannelOperation {
    void execute() throws IOException;
  }

}
