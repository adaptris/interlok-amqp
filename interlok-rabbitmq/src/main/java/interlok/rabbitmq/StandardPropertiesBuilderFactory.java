package interlok.rabbitmq;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.MetadataCollection;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Translator.PropertiesBuilderFactory;

/**
 * Implementation that uses values from {@link MetadataConstants} to populate the {@code BasicProperties#Builder}. 
 */
@XStreamAlias("rabbitmq-standard-properties-builder")
public class StandardPropertiesBuilderFactory implements PropertiesBuilderFactory {

  @Override
  public Builder build(AdaptrisMessage msg) {
    return build(MetadataCollection.asMap(msg.getMetadata()));
  }

  
}
