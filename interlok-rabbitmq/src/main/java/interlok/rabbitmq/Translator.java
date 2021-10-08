package interlok.rabbitmq;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.BooleanUtils;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.util.text.DateFormatUtil;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Translating between {@link AdaptrisMessage} and their RabbitMQ Equivalents.
 * 
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Translator {

  static final EnvelopeHandler IGNORE_ENVELOPE = (e, m) -> {}; 
  static final BasicPropertiesHandler IGNORE_PROPERTIES = (e, m) -> {}; 
  static final BasicPropertiesBuilder NO_BASIC_PROPERTIES = (msg) -> null;
  
  // Iteraters over BasicProperties and gives us keys + string values.
  // So, we can expect to do something like
  // for (Map.Key e : map) {
  //   Optional.ofNullable(e.getValue().apply(properties)).ifPresent((v) -> msg.addMetadata(e.getKey(), v));
  // }
  // @see HeadersToMetadata
  static final Map<String, Function<BasicProperties, String>> PROPERTY_MAP;
  
  static final Map<String, Function<Envelope, String>> ENVELOPE_MAP;
  
  static {
    HashMap<String, Function<BasicProperties, String>> props = new HashMap<>();
    props.put(RMQ_APP_ID, (p) -> p.getAppId());
    props.put(RMQ_CONTENT_ENCODING, (p) -> p.getContentEncoding());
    props.put(RMQ_CLUSTER_ID, (p) -> p.getClusterId());
    // It's an int, so can't be null.
    props.put(RMQ_CLASS_ID, (p) -> String.valueOf(p.getClassId()));
    props.put(RMQ_CONTENT_TYPE, (p) -> p.getContentType());
    props.put(RMQ_CORRELATION_ID, (p) -> p.getCorrelationId());
    // Return null if Integer is null (since it might be).
    props.put(RMQ_DELIVERY_MODE, (p) -> Optional.ofNullable(p.getDeliveryMode()).map((i) -> i.toString()).orElse(null));
    props.put(RMQ_EXPIRATION, (p) -> p.getExpiration());
    props.put(RMQ_MESSAGE_ID, (p) -> p.getMessageId());
    // Return null if Integer is null (since it might be).
    props.put(RMQ_PRIORITY, (p) -> Optional.ofNullable(p.getPriority()).map((i) -> i.toString()).orElse(null));
    props.put(RMQ_REPLY_TO, (p) -> p.getReplyTo());
    // Return null if Date is null (since it might be).
    props.put(RMQ_TIMESTAMP, (p) -> Optional.ofNullable(p.getTimestamp()).map((d) -> DateFormatUtil.format(d)).orElse(null));
    props.put(RMQ_TYPE, (p)-> p.getType());
    props.put(RMQ_USER_ID, (p) -> p.getUserId());
    PROPERTY_MAP = Collections.unmodifiableMap(props);
    
    HashMap<String, Function<Envelope, String>> env = new HashMap<>();
    env.put(RMQ_DELIVERY_TAG, (e) -> String.valueOf(e.getDeliveryTag()));
    env.put(RMQ_EXCHANGE, (e) -> e.getExchange());
    env.put(RMQ_ROUTING_KEY, (e) -> e.getRoutingKey());
    env.put(RMQ_IS_REDELIVERY, (e) -> BooleanUtils.toStringTrueFalse(e.isRedeliver()));   
    ENVELOPE_MAP = Collections.unmodifiableMap(env);
    
  }
  
  
  /**
   * Create an {@link AdaptrisMessage} from the incoming {@code Delivery}.
   * 
   * 
   * @param delivery the message from the RabbitMQ server
   * @param mf the message factory (default factory is used if null)
   * @return the new AdaptrisMessage.
   */
  public static AdaptrisMessage build(Delivery delivery, AdaptrisMessageFactory mf) {
    return build(delivery, IGNORE_PROPERTIES, mf);
  }

  /**
   * Create an {@link AdaptrisMessage} from the incoming {@code Delivery}.
   * 
   * 
   * @param delivery the message from the RabbitMQ server
   * @param propsHandler how to handle the {@code BasicProperties} object
   * @param mf the message factory (default factory is used if null)
   * @return the new AdaptrisMessage.
   */
  public static AdaptrisMessage build(Delivery delivery, BasicPropertiesHandler propsHandler,
      AdaptrisMessageFactory mf) {
    return build(delivery, propsHandler, IGNORE_ENVELOPE, mf);
  }

  /**
   * Create an {@link AdaptrisMessage} from the incoming {@code Delivery}.
   * 
   * 
   * @param delivery the message from the RabbitMQ server
   * @param propsHandler how to handle the {@code BasicProperties} object
   * @param envHandler how to handle the {@code Envelope} object
   * @param mf the message factory (default factory is used if null)
   * @return the new AdaptrisMessage.
   */
  public static AdaptrisMessage build(Delivery delivery, BasicPropertiesHandler propsHandler,
      EnvelopeHandler envHandler, AdaptrisMessageFactory mf) {
    final AdaptrisMessage msg = AdaptrisMessageFactory.defaultIfNull(mf).newMessage(delivery.getBody());
    Optional.ofNullable(delivery.getProperties()).ifPresent((p) -> { propsHandler.handle(p, msg); });
    Optional.ofNullable(delivery.getEnvelope()).ifPresent((e) -> { envHandler.handle(e, msg); });    
    return msg;
  }
  
  /**
   * Create {@code BasicProperties} from an {@link AdaptrisMessage}
   * 
   */
  @FunctionalInterface
  public interface BasicPropertiesBuilder {
    BasicProperties build(AdaptrisMessage msg);
  }

  /**
   * Transfer the contents of {@code Delivery#getProperties()} into the {@link AdaptrisMessage}
   * 
   */
  @FunctionalInterface
  public interface BasicPropertiesHandler {
    void handle(BasicProperties properties, AdaptrisMessage msg);
  }

  /**
   * Transfer the contents of {@code Delivery#getEnvelope()} into the {@link AdaptrisMessage}
   * 
   */
  @FunctionalInterface
  public interface EnvelopeHandler {
    void handle(Envelope envelope, AdaptrisMessage msg);
  }
  
}
