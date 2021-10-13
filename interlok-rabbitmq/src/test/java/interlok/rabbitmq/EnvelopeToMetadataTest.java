package interlok.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.rabbitmq.client.Envelope;
import interlok.rabbitmq.EnvelopeToMetadata;
import interlok.rabbitmq.MetadataConstants;

public class EnvelopeToMetadataTest {


  @Test
  public void testEnvelopeToMessage_NullExchangeRoutingKey() throws Exception {
    // No configuration
    EnvelopeToMetadata env = new EnvelopeToMetadata();
    Envelope rmqEnv = new Envelope(0, false, null, null);
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    env.handle(rmqEnv, msg);
    assertEquals("0", msg.getMetadataValue(MetadataConstants.RMQ_DELIVERY_TAG));
    assertEquals("false", msg.getMetadataValue(MetadataConstants.RMQ_IS_REDELIVERY));
    assertFalse(msg.headersContainsKey(MetadataConstants.RMQ_ROUTING_KEY));
    assertFalse(msg.headersContainsKey(MetadataConstants.RMQ_EXCHANGE));
  }
  
  @Test
  public void testEnvelopeToMessage_WithExchangeRoutingKey() throws Exception {
    // No configuration
    EnvelopeToMetadata env = new EnvelopeToMetadata();
    Envelope rmqEnv = new Envelope(0, false, "myExchange", "myRoutingKey");
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    env.handle(rmqEnv, msg);
    assertEquals("0", msg.getMetadataValue(MetadataConstants.RMQ_DELIVERY_TAG));
    assertEquals("false", msg.getMetadataValue(MetadataConstants.RMQ_IS_REDELIVERY));
    assertEquals("myExchange", msg.getMetadataValue(MetadataConstants.RMQ_EXCHANGE));
    assertEquals("myRoutingKey", msg.getMetadataValue(MetadataConstants.RMQ_ROUTING_KEY));
  }

}
