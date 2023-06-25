package interlok.rabbitmq;

import static interlok.rabbitmq.MetadataConstants.RMQ_APP_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_CLASS_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_CLUSTER_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_CONTENT_ENCODING;
import static interlok.rabbitmq.MetadataConstants.RMQ_CONTENT_TYPE;
import static interlok.rabbitmq.MetadataConstants.RMQ_CORRELATION_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_DELIVERY_MODE;
import static interlok.rabbitmq.MetadataConstants.RMQ_EXPIRATION;
import static interlok.rabbitmq.MetadataConstants.RMQ_MESSAGE_ID;
import static interlok.rabbitmq.MetadataConstants.RMQ_PRIORITY;
import static interlok.rabbitmq.MetadataConstants.RMQ_REPLY_TO;
import static interlok.rabbitmq.MetadataConstants.RMQ_TIMESTAMP;
import static interlok.rabbitmq.MetadataConstants.RMQ_TYPE;
import static interlok.rabbitmq.MetadataConstants.RMQ_USER_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.rabbitmq.client.AMQP.BasicProperties;

public class PropertiesToMetadataTest {

  private static final String[] ALL_BASIC_PROPERTIES = {
      RMQ_APP_ID, // classID is special because it's an int so it'll always exist : RMQ_CLASS_ID,
      RMQ_CLUSTER_ID, RMQ_CONTENT_ENCODING,
      RMQ_CONTENT_TYPE, RMQ_CORRELATION_ID, RMQ_DELIVERY_MODE, 
      RMQ_EXPIRATION, RMQ_MESSAGE_ID, RMQ_PRIORITY, RMQ_REPLY_TO,
      RMQ_TIMESTAMP, RMQ_TYPE, RMQ_USER_ID
  };

  @Test
  public void testPropertiesToMetadata_Null() throws Exception {

    PropertiesToMetadata props = new PropertiesToMetadata();
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    props.handle(null, msg);
    for (String s : ALL_BASIC_PROPERTIES) {
      assertFalse(msg.headersContainsKey(s));
    }
    assertFalse(msg.headersContainsKey(RMQ_CLASS_ID));
  }

  @Test
  public void testPropertiesToMetadata_EmptyProperties() throws Exception {

    PropertiesToMetadata props = new PropertiesToMetadata().withPrefix("_");

    BasicProperties empty = new BasicProperties.Builder().build();
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    props.handle(empty, msg);
    for (String s : ALL_BASIC_PROPERTIES) {
      assertFalse(msg.headersContainsKey(s));
    }
    assertTrue(msg.headersContainsKey(RMQ_CLASS_ID));
  }

  @Test
  public void testPropertiesToMetadata() throws Exception {

    PropertiesToMetadata props = new PropertiesToMetadata();
    Map<String, Object> headers = new HashMap<>();
    headers.put("MyHeader", "MyHeaderValue");
    BasicProperties full = new BasicProperties.Builder().appId("MyAppId").clusterId("MyClusterId").contentEncoding("base64")
        .contentType("text/plain").correlationId("MyCorrelationId").deliveryMode(5).expiration("MyExpiration").messageId("MyMessageId")
        .priority(9).replyTo("MyReplyTo").timestamp(new Date()).type("MyType").userId("MyUserId").headers(headers).build();
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    props.handle(full, msg);
    for (String s : ALL_BASIC_PROPERTIES) {
      assertTrue(msg.headersContainsKey(s));
    }
    assertTrue(msg.headersContainsKey(RMQ_CLASS_ID));
    assertTrue(msg.headersContainsKey("MyHeader"));
  }

}
