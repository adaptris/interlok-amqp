package interlok.rabbitmq;

import static interlok.rabbitmq.Translator.PROPERTY_MAP;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.core.AdaptrisMessage;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Translator.BasicPropertiesHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Add metadata to the message from the {@code BasicProperties} object.
 * 
 */
@XStreamAlias("rabbitmq-properties-to-metadata")
@NoArgsConstructor
public class PropertiesToMetadata implements BasicPropertiesHandler {


  /**
   * Set the prefix (if any) for metadata keys when retrieving {@code BasicProperties#getHeaders()}.
   * <p>
   * Note that standard properties such as {@code BasicProperties#getClusterId()} will always be
   * added with the appropriate key from {@link MetadataConstants}. This allows us to have
   * predictable behaviour around those properties as required.
   * </p>
   * 
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "")
  @InputFieldHint(style = "BLANKABLE")
  private String prefix;

  @Override
  public void handle(BasicProperties properties, AdaptrisMessage msg) {
    if (properties == null) {
      return;
    }
    final String pfx = prefix();
    // First all the properties.
    for (Map.Entry<String, Function<BasicProperties, String>> e : PROPERTY_MAP.entrySet()) {
      Optional.ofNullable(e.getValue().apply(properties))
          .ifPresent((v) -> msg.addMetadata(e.getKey(), v));
    }
    // Now Headers -> Metadata.
    Optional.ofNullable(properties.getHeaders()).ifPresent((h) -> {
      // RabbitMQ appears to wrap everything in an opaque object so we call toString to
      // get the right thing.
      h.entrySet().forEach((e) -> msg.addMetadata(pfx + e.getKey(), e.getValue().toString()));
    });
  }

  private String prefix() {
    return StringUtils.defaultIfEmpty(getPrefix(), "");
  }

  public PropertiesToMetadata withPrefix(String s) {
    setPrefix(s);
    return this;
  }
}
