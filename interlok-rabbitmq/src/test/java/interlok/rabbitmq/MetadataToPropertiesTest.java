package interlok.rabbitmq;

import static interlok.rabbitmq.JunitConfig.MESSAGE_BODY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import org.junit.jupiter.api.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.metadata.NoOpMetadataFilter;
import com.rabbitmq.client.AMQP.BasicProperties;

public class MetadataToPropertiesTest {

  @Test
  public void testBuildProperties() throws Exception {
    AdaptrisMessage msg = new DefaultMessageFactory().newMessage(MESSAGE_BODY);
    msg.addMessageHeader("MY_KEY", "MY_VALUE");
    
    MetadataToProperties converter = new MetadataToProperties().withFilter(new NoOpMetadataFilter());
    BasicProperties props = converter.build(msg);
    Map<String, Object> hdrs = props.getHeaders();
    assertNotNull(hdrs);
    assertTrue(hdrs.containsKey("MY_KEY"));
    assertEquals("MY_VALUE", hdrs.get("MY_KEY").toString());
  }
}
