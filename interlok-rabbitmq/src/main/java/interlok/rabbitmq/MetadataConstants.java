package interlok.rabbitmq;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Various metadata keys that represent the corresponding Delivery/Envelope properties.
 * 
 * <p>
 * The metadata value associated with {@link #RMQ_CORRELATION_ID} corresponds to
 * {@code BasicProperties#getCorrelationId()}. The naming of each key generally conforms to the
 * convention {@code rmq[FieldName]} in camel case (so {@code rmqQueue} for instance}.
 * </p>
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetadataConstants {

  /**
   * {@value #RMQ_QUEUE}.
   * 
   */
  public static final String RMQ_QUEUE = "rmqQueue";

  /**
   * {@value #RMQ_CONSUMER_TAG}.
   * 
   */
  public static final String RMQ_CONSUMER_TAG = "rmqConsumerTag";

  /**
   * {@value #RMQ_DELIVERY_TAG}.
   * 
   */
  public static final String RMQ_DELIVERY_TAG = "rmqDeliveryTag";

  /**
   * {@value #RMQ_DELIVERY_TAG}.
   * 
   */
  public static final String RMQ_IS_REDELIVERY = "rmqIsRedelivery";

  /**
   * {@value #RMQ_EXCHANGE}.
   * 
   */
  public static final String RMQ_EXCHANGE = "rmqExchange";

  /**
   * {@value #RMQ_ROUTING_KEY}.
   * 
   */
  public static final String RMQ_ROUTING_KEY = "rmqRoutingKey";

  /**
   * {@value #RMQ_APP_ID}.
   * 
   */
  public static final String RMQ_APP_ID = "rmqAppId";

  /**
   * {@value #RMQ_CLASS_ID}.
   * 
   */
  public static final String RMQ_CLASS_ID = "rmqClassId";

  /**
   * {@value #RMQ_CLUSTER_ID}.
   * 
   */
  public static final String RMQ_CLUSTER_ID = "rmqClusterId";

  /**
   * {@value #RMQ_CONTENT_ENCODING}.
   * 
   */
  public static final String RMQ_CONTENT_ENCODING = "rmqContentEncoding";

  /**
   * {@value #RMQ_CONTENT_TYPE}.
   * 
   */
  public static final String RMQ_CONTENT_TYPE = "rmqContentType";

  /**
   * {@value #RMQ_CORRELATION_ID}.
   * 
   */
  public static final String RMQ_CORRELATION_ID = "rmqCorrelationId";

  /**
   * {@value #RMQ_DELIVERY_MODE}.
   * 
   */
  public static final String RMQ_DELIVERY_MODE = "rmqDeliveryMode";

  /**
   * {@value #RMQ_EXPIRATION}.
   * 
   */
  public static final String RMQ_EXPIRATION = "rmqExpiration";

  /**
   * {@value #RMQ_MESSAGE_ID}.
   * 
   */
  public static final String RMQ_MESSAGE_ID = "rmqMessageId";

  /**
   * {@value #RMQ_PRIORITY}.
   * 
   */
  public static final String RMQ_PRIORITY = "rmqPriority";

  /**
   * {@value #RMQ_REPLY_TO}.
   * 
   */
  public static final String RMQ_REPLY_TO = "rmqReplyTo";

  /**
   * {@value #RMQ_TIMESTAMP}.
   * 
   */
  public static final String RMQ_TIMESTAMP = "rmqTimestamp";

  /**
   * {@value #RMQ_TYPE}.
   * 
   */
  public static final String RMQ_TYPE = "rmqType";

  /**
   * {@value #RMQ_USER_ID}.
   * 
   */
  public static final String RMQ_USER_ID = "rmqUserId";

  /**
   * Used by {@link PublishToDefaultExchange} to indicate success/failure via metadata.
   * <p>
   * It intentionally doesn't use the 'rmq' prefix and is {@value #RMQ_PUBLISH_STATUS}.
   * </p>
   */
  public static final String RMQ_PUBLISH_STATUS = "queue_publish_status";

}
