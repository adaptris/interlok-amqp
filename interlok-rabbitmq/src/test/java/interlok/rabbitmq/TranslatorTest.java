package interlok.rabbitmq;

import static interlok.rabbitmq.JunitConfig.MESSAGE_BODY;
import static interlok.rabbitmq.MetadataConstants.RMQ_APP_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_CLASS_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_CLUSTER_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_CONTENT_ENCODING;
import static interlok.rabbitmq.MetadataConstants.RMQ_CONTENT_TYPE;
import static interlok.rabbitmq.MetadataConstants.RMQ_CORRELATION_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_DELIVERY_MODE;
import static interlok.rabbitmq.MetadataConstants.RMQ_DELIVERY_TAG;
import static interlok.rabbitmq.MetadataConstants.RMQ_EXCHANGE;
import static interlok.rabbitmq.MetadataConstants.RMQ_EXPIRATION;
import static interlok.rabbitmq.MetadataConstants.RMQ_IS_REDELIVERY;
import static interlok.rabbitmq.MetadataConstants.RMQ_MESSAGE_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_PRIORITY;
import static interlok.rabbitmq.MetadataConstants.RMQ_REPLY_TO;
import static interlok.rabbitmq.MetadataConstants.RMQ_ROUTING_KEY;
import static interlok.rabbitmq.MetadataConstants.RMQ_TIMESTAMP;
import static interlok.rabbitmq.MetadataConstants.RMQ_TYPE;
import static interlok.rabbitmq.MetadataConstants.RMQ_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import com.adaptris.core.AdaptrisMessage;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;

public class TranslatorTest {

  private static final String[] EXPECTED_PROPERTIES = {RMQ_DELIVERY_TAG, RMQ_IS_REDELIVERY,
      RMQ_ROUTING_KEY, RMQ_EXCHANGE, RMQ_APP_ID, RMQ_CLASS_ID, RMQ_CLUSTER_ID, RMQ_CONTENT_ENCODING,
      RMQ_CONTENT_TYPE, RMQ_CORRELATION_ID, RMQ_DELIVERY_MODE, RMQ_EXPIRATION, RMQ_MESSAGE_ID,
      RMQ_PRIORITY, RMQ_REPLY_TO, RMQ_TIMESTAMP, RMQ_TYPE, RMQ_USER_ID};
  
  @Test
  public void testBuildMessage() throws Exception {
    Delivery dummy = new Delivery(null, null, MESSAGE_BODY.getBytes(StandardCharsets.UTF_8));
    AdaptrisMessage msg = Translator.build(dummy, null);
    assertNotNull(msg);
    assertEquals(MESSAGE_BODY, msg.getContent());
    for (String s : EXPECTED_PROPERTIES) {
      assertFalse(msg.headersContainsKey(s));      
    }    
  }
  
  @Test
  public void testBuildMessage_WithHandlers() {
    Envelope rmqEnv = new Envelope(0, false, "myExchange", "myRoutingKey");
    Map<String, Object> headers = new HashMap<>();
    headers.put("MyHeader", "MyHeaderValue");
    BasicProperties full = new BasicProperties.Builder()
        .appId("MyAppId")
        .clusterId("MyClusterId")
        .contentEncoding("base64")
        .contentType("text/plain")
        .correlationId("MyCorrelationId")
        .deliveryMode(5)
        .expiration("MyExpiration")
        .messageId("MyMessageId")
        .priority(9)
        .replyTo("MyReplyTo")
        .timestamp(new Date())
        .type("MyType")
        .userId("MyUserId")
        .headers(headers)
        .build();
    Delivery dummy = new Delivery(rmqEnv, full, MESSAGE_BODY.getBytes(StandardCharsets.UTF_8));
    AdaptrisMessage msg = Translator.build(dummy, new PropertiesToMetadata(), new EnvelopeToMetadata(),null);
    assertNotNull(msg);
    assertEquals(MESSAGE_BODY, msg.getContent());
    for (String s : EXPECTED_PROPERTIES) {
      assertTrue(msg.headersContainsKey(s));      
    } 
  }
  
}
