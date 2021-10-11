package interlok.rabbitmq;

import java.util.HashMap;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.MetadataCollection;
import com.adaptris.core.metadata.MetadataFilter;
import com.adaptris.core.metadata.RemoveAllMetadataFilter;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Translator.BasicPropertiesBuilder;
import interlok.rabbitmq.Translator.PropertiesBuilderFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Build a BasicProperties object from metadata.
 * <p>
 * Creates a {@code BasicProperties} object from metadata. Top level headers such as
 * {@code BasicProperties#getContentType()} will be set by the configured
 * {@link PropertiesBuilderFactory}. Subsequently, any metadata that is still available post
 * filter via {@link #getHeaderFilter()} will be added so they are available via
 * {@link BasicProperties#getHeaders()}.
 * </p>
 * <p>
 * Things like {@code BasicProperties#getContentEncoding()} and
 * {@code BasicProperties#getContentType()} are considered informational by Interlok; there is no
 * special treatment if you configure something as 'base64' or 'application/json'
 * </p>
 */
@XStreamAlias("rabbitmq-metadata-to-properties")
@NoArgsConstructor
@ComponentProfile(summary = "Convert metadata into RabbitMQ Headers", since = "4.3.0")
public class MetadataToProperties implements BasicPropertiesBuilder {

  private static final MetadataFilter NO_METADATA = new RemoveAllMetadataFilter();

  /**
   * Metadata filter that filters metadata so that it is part of
   * {@code com.rabbitmq.client.BasicProperties#getHeaders()}.
   * <p>
   * If not explicitly configured no metadata is published.
   * </p>
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "RemoveAllMetadataFilter")
  private MetadataFilter headerFilter;

  /**
   * Add specific headers to any created {@link BasicProperties} object.
   * <p>
   * If not explicitly configured, then this will default to a no-op implementation which means that
   * any {@code BasicProperties} that is created is determined solely by whether any message
   * metadata should be included as part of the {@code BasicProperties#getHeaders()}.
   * </p>
   * 
   * @see StandardPropertiesBuilderFactory
   * @see ConfiguredPropertiesBuilderFactory
   */
  @Getter
  @Setter
  private PropertiesBuilderFactory builderFactory;

  @Override
  public BasicProperties build(AdaptrisMessage msg) {
    // We need builder to be null to behave according to our logical constraints.
    // if builder-factory == null then a BasicProperties only exists if metadata-post-filtering = !empty
    // if builder-factory != null then we always return a BasicProperties even if metadata-post-filter = empty
    Optional<BasicProperties.Builder> builder = Optional.ofNullable(getBuilderFactory())
        .map((f) -> Optional.of(f.build(msg))).orElse(Optional.empty());

    MetadataCollection metadata = filter().filter(msg);

    if (!metadata.isEmpty()) {
      // Make sure we have a builder object since we must have one, as metadata isn't empty.
      BasicProperties.Builder b = builder.orElse(new BasicProperties.Builder());
      // builder the BasicProperties.
      return b.headers(new HashMap<String, Object>(MetadataCollection.asMap(metadata))).build();
    }
    // If we have something built by our builder factory, return it, otherwise return null (which is
    // ok).
    return builder.map((b) -> b.build()).orElse(null);
  }


  public MetadataToProperties withFilter(MetadataFilter filter) {
    setHeaderFilter(filter);
    return this;
  }

  public MetadataToProperties withBuilder(PropertiesBuilderFactory factory) {
    setBuilderFactory(factory);
    return this;
  }

  private MetadataFilter filter() {
    return ObjectUtils.defaultIfNull(getHeaderFilter(), NO_METADATA);
  }
  
}
