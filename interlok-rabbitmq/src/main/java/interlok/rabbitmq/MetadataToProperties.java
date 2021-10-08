package interlok.rabbitmq;

import java.util.HashMap;
import org.apache.commons.lang3.ObjectUtils;
import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.MetadataCollection;
import com.adaptris.core.metadata.MetadataFilter;
import com.adaptris.core.metadata.RemoveAllMetadataFilter;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Translator.BasicPropertiesBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Build a BasicProperties object from metadata.
 * 
 */
@XStreamAlias("rabbitmq-metadata-to-properties")
@NoArgsConstructor
@ComponentProfile(summary = "Convert metadata into RabbitMQ Headers", since="4.3.0")
public class MetadataToProperties implements BasicPropertiesBuilder {

  private static final MetadataFilter NO_METADATA = new RemoveAllMetadataFilter();

  /** Metadata filter that filters metadata so that it is part of {@code com.rabbitmq.client.BasicProperties#getHeaders()}.
   *  <p>If not explicitly configured no metadata is published.</p>
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "RemoveAllMetadataFilter")
  @AdvancedConfig
  private MetadataFilter metadataFilter;
  
  @Override
  public BasicProperties build(AdaptrisMessage msg) {
    MetadataCollection metadata = filter().filter(msg);
    if (!metadata.isEmpty()) {
      // well, the compiler is a bit of a pain isn't it.
      return new BasicProperties.Builder().headers(new HashMap<String, Object>(MetadataCollection.asMap(metadata))).build();
    }
    return null;
  }


  public MetadataToProperties withFilter(MetadataFilter filter) {
    setMetadataFilter(filter);
    return this;
  }
  
  private MetadataFilter filter() {
    return ObjectUtils.defaultIfNull(getMetadataFilter(), NO_METADATA);
  }
  
}
