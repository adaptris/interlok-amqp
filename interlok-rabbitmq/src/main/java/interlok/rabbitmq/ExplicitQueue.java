package interlok.rabbitmq;

import org.apache.commons.lang3.BooleanUtils;

import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldDefault;
import com.rabbitmq.client.Channel;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import interlok.rabbitmq.Declaration.QueueDeclaration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Explicitly configured Queue declaration.
 * <p>
 * Depending on your RabbitMQ Configuration you may wish to explicitly declare a queue before use. In the absence of any other configuration
 * Interlok assumes that queues should be durable, non-exclusive and should not be automatically deleted if not in use. If your expectations
 * are different then you should definitely configure one of these.
 * </p>
 * <p>
 * This implementation does not support queue declaration arguments (these are are a {@code Map<String, Object>} which is hard to support in
 * configuration).
 * </p>
 */
@NoArgsConstructor
@XStreamAlias("rabbitmq-explicit-queue-declaration")
@ComponentProfile(summary = "Explicitly declare a RabbitMQ Queue before use", since = "4.3.0")
public class ExplicitQueue implements QueueDeclaration {

  /**
   * Whether or not the queue should survive a server restart.
   *
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "true")
  private Boolean durable;
  /**
   * Whether or not the queue is exlusive to the connection.
   *
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "false")
  private Boolean exclusive;
  /**
   * Whether or not the queue is deleted when no longer in use.
   *
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "false")
  private Boolean autoDelete;

  @Override
  public void declare(Channel ch, String name) throws Exception {
    ch.queueDeclare(name, durable(), exclusive(), autoDelete(), null);
  }

  private boolean durable() {
    return BooleanUtils.toBooleanDefaultIfNull(getDurable(), true);
  }

  private boolean exclusive() {
    return BooleanUtils.toBooleanDefaultIfNull(getDurable(), false);
  }

  private boolean autoDelete() {
    return BooleanUtils.toBooleanDefaultIfNull(getAutoDelete(), false);
  }

}
