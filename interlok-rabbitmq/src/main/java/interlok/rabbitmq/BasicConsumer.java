package interlok.rabbitmq;

import static interlok.rabbitmq.MetadataConstants.RMQ_CONSUMER_TAG;
import static interlok.rabbitmq.MetadataConstants.RMQ_QUEUE;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageConsumerImp;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.DestinationHelper;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.interlok.util.Args;
import com.adaptris.interlok.util.Closer;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Translator.BasicPropertiesBuilder;
import interlok.rabbitmq.Translator.BasicPropertiesHandler;
import interlok.rabbitmq.Translator.EnvelopeHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Connects to a RabbitMQ Queue and consumes messages.
 * <p>
 * This is the simplest way to consume a message from RabbitMQ.
 * </p>
 * <p>
 * Any {@code BasicProperties} will be copied as metadata based on the
 * {@link BasicPropertiesBuilder} implementation specified. The default is always to ignore incoming
 * properties.
 * </p>
 */
@XStreamAlias("rabbitmq-basic-consumer")
@ComponentProfile(summary = "Basic consumer for RabbitMQ", recommended = {RabbitMqConnection.class},
    tag = "amqp, rabbitmq", since = "4.3.0")
@NoArgsConstructor
@DisplayOrder(order = {"queue"})
public class BasicConsumer extends AdaptrisMessageConsumerImp {

  /**
   * The queue to consume from.
   * 
   */
  @NotBlank
  @Getter
  @Setter
  private String queue;

  /**
   * How to handle the {@code Delivery#getEnvelope()}.
   * <p>
   * If not explicitly configured, then the envelope associated with the incoming message is
   * ignored.
   * </p>
   */
  @Getter
  @Setter
  @AdvancedConfig
  @InputFieldDefault(value = "ignore envelope")
  @Valid
  private EnvelopeHandler envelopeHandler;
  /**
   * How to handle the {@code Delivery#getProperties()}.
   * <p>
   * If not explicitly configured, then any properties associated with the incoming message are
   * ignored
   * </p>
   */
  @Getter
  @Setter
  @AdvancedConfig
  @InputFieldDefault(value = "ignore properties")
  @Valid
  private BasicPropertiesHandler propertiesHandler;

  private transient Channel rabbitChannel;

  @Override
  public void prepare() throws CoreException {
    Args.notBlank(getQueue(), "queue");
  }

  @Override
  public void start() throws CoreException {
    try {
      rabbitChannel =
          retrieveConnection(ConnectionWrapper.class).wrappedConnection().createChannel();

      rabbitChannel.queueDeclare(getQueue(), true, false, false, null);
      DeliverCallback onMessage = (consumerTag, delivery) -> {
        String oldName = renameThread();
        AdaptrisMessage am =
            Translator.build(delivery, propertiesHandler(), envelopeHandler(), getMessageFactory());
        am.addMetadata(RMQ_CONSUMER_TAG, consumerTag);
        am.addMetadata(RMQ_QUEUE, getQueue());
        retrieveAdaptrisMessageListener().onAdaptrisMessage(am);
        Thread.currentThread().setName(oldName);
      };
      CancelCallback onCancel = (consumerTag) -> {
      };
      rabbitChannel.basicConsume(getQueue(), true, onMessage, onCancel);
    } catch (Exception e) {
      throw ExceptionHelper.wrapCoreException(e);
    }
  }

  @Override
  public void stop() {
    Closer.closeQuietly(rabbitChannel);
  }

  public BasicConsumer withQueue(String s) {
    setQueue(s);
    return this;
  }

  @Override
  public String consumeLocationKey() {
    return MetadataConstants.RMQ_QUEUE;
  }


  @Override
  protected String newThreadName() {
    return DestinationHelper.threadName(retrieveAdaptrisMessageListener());
  }

  public BasicConsumer withEnvelopeHandler(EnvelopeHandler h) {
    setEnvelopeHandler(h);
    return this;
  }

  private EnvelopeHandler envelopeHandler() {
    return ObjectUtils.defaultIfNull(getEnvelopeHandler(), Translator.IGNORE_ENVELOPE);
  }

  public BasicConsumer withPropertiesHandler(BasicPropertiesHandler h) {
    setPropertiesHandler(h);
    return this;
  }


  private BasicPropertiesHandler propertiesHandler() {
    return ObjectUtils.defaultIfNull(getPropertiesHandler(), Translator.IGNORE_PROPERTIES);
  }


}
