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
import java.util.HashMap;
import java.util.Map;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.util.text.DateFormatUtil;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Translator.PropertiesBuilderFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implementation that allows you to explicit configure values in config.
 * <p>
 * Expressions are supported but no checking of values is done. If you leave something unconfigured
 * (i.e. 'null' then the corresponding value will never be set
 * </p>
 * </p>
 * 
 */
@XStreamAlias("rabbitmq-configured-properties-builder")
@NoArgsConstructor
public class ConfiguredPropertiesBuilderFactory implements PropertiesBuilderFactory {

  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getAppId()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String appId;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getClusterId()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String clusterId;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getContentEncoding()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String contentEncoding;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getContentType()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String contentType;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getCorrelationId()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String correlationId;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getDeliveryMode()}
   * <p>
   * Note that this should be an integer; it is defined as a String here for expression purposes.
   * </p>
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String deliveryMode;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getExpiration()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String expiration;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getMessageId()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String messageId;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getPriority()}
   * <p>
   * Note that this should be an integer; it is defined as a String here for expression purposes.
   * </p>
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String priority;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getReplyTo()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String replyTo;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getTimestamp()}
   * <p>
   * Note that this should be an Date; it is defined as a String here for expression purposes.
   * Ultimately we use {@link DateFormatUtil#parse(String)} to build a Date object.
   * </p>
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String timestamp;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getType()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String type;
  /**
   * Maps onto {@code com.rabbitmq.client.AMQP.BasicProperties#getUserId()}
   */
  @Getter
  @Setter
  @InputFieldHint(expression = true)
  private String userId;


  @Override
  public Builder build(AdaptrisMessage msg) {
    Map<String, String> m = new HashMap<>();
    m.put(RMQ_APP_ID, msg.resolve(getAppId()));
    m.put(RMQ_CLUSTER_ID, msg.resolve(getClusterId()));
    m.put(RMQ_CONTENT_ENCODING, msg.resolve(getContentEncoding()));
    m.put(RMQ_CONTENT_TYPE, msg.resolve(getContentType()));
    m.put(RMQ_CORRELATION_ID, msg.resolve(getCorrelationId()));
    m.put(RMQ_DELIVERY_MODE, msg.resolve(getDeliveryMode()));
    m.put(RMQ_EXPIRATION, msg.resolve(getExpiration()));
    m.put(RMQ_MESSAGE_ID, msg.resolve(getMessageId()));
    m.put(RMQ_PRIORITY, msg.resolve(getPriority()));
    m.put(RMQ_REPLY_TO, msg.resolve(getReplyTo()));
    m.put(RMQ_TIMESTAMP, msg.resolve(getTimestamp()));
    m.put(RMQ_TYPE, msg.resolve(getType()));
    m.put(RMQ_USER_ID, msg.resolve(getUserId()));
    return build(m);
  }



}
