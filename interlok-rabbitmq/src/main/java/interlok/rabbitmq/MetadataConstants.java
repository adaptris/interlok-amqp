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
  public static String RMQ_QUEUE = "rmqQueue";

  /**
   * {@value #RMQ_CONSUMER_TAG}.
   * 
   */
  public static String RMQ_CONSUMER_TAG = "rmqConsumerTag";

  public static String RMQ_DELIVERY_TAG = "rmqDeliveryTag";

  public static String RMQ_IS_REDELIVERY = "rmqIsRedelivery";

  public static String RMQ_EXCHANGE = "rmqExchange";

  public static String RMQ_ROUTING_KEY = "rmqRoutingKey";

  public static String RMQ_APP_ID = "rmqAppId";

  public static String RMQ_CLASS_ID = "rmqClassId";

  public static String RMQ_CLUSTER_ID = "rmqClusterId";

  public static String RMQ_CONTENT_ENCODING = "rmqContentEncoding";

  public static String RMQ_CONTENT_TYPE = "rmqContentType";

  public static String RMQ_CORRELATION_ID = "rmqCorrelationId";

  public static String RMQ_DELIVERY_MODE = "rmqDeliveryMode";

  public static String RMQ_EXPIRATION = "rmqExpiration";

  public static String RMQ_MESSAGE_ID = "rmqMessageId";

  public static String RMQ_PRIORITY = "rmqPriority";

  public static String RMQ_REPLY_TO = "rmqReplyTo";

  public static String RMQ_TIMESTAMP = "rmqTimestamp";

  public static String RMQ_TYPE = "rmqType";

  public static String RMQ_USER_ID = "rmqUserId";

}
