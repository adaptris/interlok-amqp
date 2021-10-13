package interlok.rabbitmq;

import static interlok.rabbitmq.JunitConfig.MESSAGE_BODY;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import org.junit.jupiter.api.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.metadata.NoOpMetadataFilter;
import com.adaptris.core.metadata.RemoveAllMetadataFilter;
import com.rabbitmq.client.AMQP.BasicProperties;

public class MetadataToPropertiesTest {

  @Test
  public void testBuildProperties() throws Exception {
    AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
    msg.addMessageHeader("MY_KEY", "MY_VALUE");
    msg.addMessageHeader(MetadataConstants.RMQ_CONTENT_TYPE, "text/plain");

    MetadataToProperties converter =
        new MetadataToProperties().withFilter(new NoOpMetadataFilter());
    BasicProperties props = converter.build(msg);
    Map<String, Object> hdrs = props.getHeaders();
    assertNotNull(hdrs);
    assertTrue(hdrs.containsKey("MY_KEY"));
    assertEquals("MY_VALUE", hdrs.get("MY_KEY").toString());
    assertNotSame("text/plain", props.getContentType());
  }

  @Test
  public void testBuildProperties_WithBuilder() throws Exception {
    AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
    msg.addMessageHeader("MY_KEY", "MY_VALUE");
    msg.addMessageHeader(MetadataConstants.RMQ_CONTENT_TYPE, "text/plain");

    MetadataToProperties converter = new MetadataToProperties().withFilter(new NoOpMetadataFilter())
        .withBuilder(new StandardPropertiesBuilderFactory());

    BasicProperties props = converter.build(msg);
    Map<String, Object> hdrs = props.getHeaders();
    assertNotNull(hdrs);
    assertTrue(hdrs.containsKey("MY_KEY"));
    assertEquals("MY_VALUE", hdrs.get("MY_KEY").toString());
    assertEquals("text/plain", props.getContentType());

  }

  @Test
  public void testBuildProperties_WithBuilder_NoMetadata() throws Exception {
    AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
    msg.addMessageHeader("MY_KEY", "MY_VALUE");
    msg.addMessageHeader(MetadataConstants.RMQ_CONTENT_TYPE, "text/plain");

    MetadataToProperties converter = new MetadataToProperties().withFilter(new RemoveAllMetadataFilter())
        .withBuilder(new StandardPropertiesBuilderFactory());

    BasicProperties props = converter.build(msg);
    Map<String, Object> hdrs = props.getHeaders();
    assertNull(hdrs);
    assertEquals("text/plain", props.getContentType());

  }

}
