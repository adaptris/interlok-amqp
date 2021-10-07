package interlok.rabbitmq;

import static interlok.rabbitmq.Translator.ENVELOPE_MAP;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import com.adaptris.core.AdaptrisMessage;
import com.rabbitmq.client.Envelope;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Translator.EnvelopeHandler;
import lombok.NoArgsConstructor;

/** Add metadata to the message from the {@code Delivery#getEnvelope()} object.
 * 
 */
@XStreamAlias("rabbitmq-envelope-to-metadata")
@NoArgsConstructor
public class EnvelopeToMetadata implements EnvelopeHandler {
  
  @Override
  public void handle(Envelope envelope, AdaptrisMessage msg) {
    for (Map.Entry<String, Function<Envelope, String>> e : ENVELOPE_MAP.entrySet()) {
      Optional.ofNullable(e.getValue().apply(envelope))
          .ifPresent((v) -> msg.addMetadata(e.getKey(), v));
    }
  }
  
}
