package interlok.rabbitmq;

import static interlok.rabbitmq.MetadataConstants.RMQ_APP_ID;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.util.text.DateFormatUtil;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;

public class PropertiesBuilderFactoryTest {

  @Test
  public void testStandardPropertiesBuilder_Empty() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    StandardPropertiesBuilderFactory factory = new StandardPropertiesBuilderFactory();
    Builder builder = factory.build(msg);
    BasicProperties props = builder.build();
    assertNull(props.getContentType());
  }

  @Test
  public void testStandardPropertiesBuilder() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();

    Date now = new Date();

    msg.addMetadata(RMQ_APP_ID, "myAppId");
    msg.addMetadata(RMQ_CLUSTER_ID, "myClusterId");
    msg.addMetadata(RMQ_CONTENT_ENCODING, "myContentEncoding");
    msg.addMetadata(RMQ_CONTENT_TYPE, "myContentType");
    msg.addMetadata(RMQ_CORRELATION_ID, "myCorrelationId");
    msg.addMetadata(RMQ_DELIVERY_MODE, "9");
    msg.addMetadata(RMQ_EXPIRATION, "myExpiration");
    msg.addMetadata(RMQ_MESSAGE_ID, "myMessageId");
    msg.addMetadata(RMQ_PRIORITY, "99");
    msg.addMetadata(RMQ_REPLY_TO, "myReplyTo");
    msg.addMetadata(RMQ_TIMESTAMP, DateFormatUtil.format(now));
    msg.addMetadata(RMQ_TYPE, "myType");
    msg.addMetadata(RMQ_USER_ID, "myUserId");

    StandardPropertiesBuilderFactory factory = new StandardPropertiesBuilderFactory();
    Builder builder = factory.build(msg);
    BasicProperties props = builder.build();
    assertEquals("myAppId", props.getAppId());
    assertEquals("myClusterId", props.getClusterId());
    assertEquals("myContentEncoding", props.getContentEncoding());
    assertEquals("myContentType", props.getContentType());
    assertEquals("myCorrelationId", props.getCorrelationId());
    assertEquals(9, props.getDeliveryMode());
    assertEquals("myExpiration", props.getExpiration());
    assertEquals("myMessageId", props.getMessageId());
    assertEquals(99, props.getPriority());
    assertEquals("myReplyTo", props.getReplyTo());
    // Date loses precision because the default formatter doesn't emit milliseconds.
    assertEquals(now.toString(), props.getTimestamp().toString());
    assertEquals("myType", props.getType());
    assertEquals("myUserId", props.getUserId());
  }

  @Test
  public void testConfiguredPropertiesBuilder_Empty() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    ConfiguredPropertiesBuilderFactory factory = new ConfiguredPropertiesBuilderFactory();
    Builder builder = factory.build(msg);
    BasicProperties props = builder.build();
    assertNull(props.getContentType());
  }

  @Test
  public void testConfiguredPropertiesBuilder() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();

    Date now = new Date();

    ConfiguredPropertiesBuilderFactory factory = new ConfiguredPropertiesBuilderFactory();
    factory.setAppId("myAppId");
    factory.setClusterId("myClusterId");
    factory.setContentEncoding("myContentEncoding");
    factory.setContentType("myContentType");
    factory.setCorrelationId("myCorrelationId");
    factory.setDeliveryMode("9");
    factory.setExpiration("myExpiration");
    factory.setMessageId("myMessageId");
    factory.setPriority("99");
    factory.setReplyTo("myReplyTo");
    factory.setTimestamp(DateFormatUtil.format(now));
    factory.setType("myType");
    factory.setUserId("myUserId");

    Builder builder = factory.build(msg);
    BasicProperties props = builder.build();
    assertEquals("myAppId", props.getAppId());
    assertEquals("myClusterId", props.getClusterId());
    assertEquals("myContentEncoding", props.getContentEncoding());
    assertEquals("myContentType", props.getContentType());
    assertEquals("myCorrelationId", props.getCorrelationId());
    assertEquals(9, props.getDeliveryMode());
    assertEquals("myExpiration", props.getExpiration());
    assertEquals("myMessageId", props.getMessageId());
    assertEquals(99, props.getPriority());
    assertEquals("myReplyTo", props.getReplyTo());
    // Date loses precision because the default formatter doesn't emit milliseconds.
    assertEquals(now.toString(), props.getTimestamp().toString());
    assertEquals("myType", props.getType());
    assertEquals("myUserId", props.getUserId());
  }

}
