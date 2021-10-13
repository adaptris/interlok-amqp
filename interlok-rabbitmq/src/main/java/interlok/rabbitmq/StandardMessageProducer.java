package interlok.rabbitmq;

import java.nio.charset.StandardCharsets;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ProduceException;
import com.adaptris.core.ProduceOnlyProducerImp;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.core.util.InputFieldExpression;
import com.adaptris.interlok.util.Args;
import com.adaptris.interlok.util.Closer;
import com.rabbitmq.client.Channel;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interlok.rabbitmq.Declaration.ExchangeDeclaration;
import interlok.rabbitmq.Declaration.QueueDeclaration;
import interlok.rabbitmq.Translator.BasicPropertiesBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Publishes a message to RabbitMQ.
 * <p>
 * RabbitMQ requires you to programatically declare both the exchange and queue before use. If you
 * are not targetting the <strong>default exchange</strong> or have specific requirements about the
 * queue declaration then please remember to configure a {@link ExchangeDeclaration} and
 * {@link QueueDeclaration} respectively. By default we simply use the default exchange and the
 * queue is declared to be durable, non-exclusive, with no auto-delete and no additional arguments.
 * </p>
 * <p>
 * Use a property-builder to configure how the {@code BasicProperties} object is created. Unless
 * explicitly configured there will be no additional properties in the call to
 * {@code Channel#basicPublish}.
 * </p>
 */
@XStreamAlias("rabbitmq-message-producer")
@NoArgsConstructor
@ComponentProfile(summary = "Publishes a message to RabbitMQ", tag = "amqp, rabbitmq",
    recommended = {RabbitMqConnection.class}, since = "4.3.0")
@DisplayOrder(order = {"queue", "propertyBuilder", "queueDeclaration", "exchangeDeclaration"})
public class StandardMessageProducer extends ProduceOnlyProducerImp {

  private static ExchangeDeclaration DEFAULT_EXCHANGE = (channel) -> {};
  private static QueueDeclaration DEFAULT_QUEUE =
      (ch, name) -> ch.queueDeclare(name, true, false, false, null);


  /**
   * The queue to publish to.
   * 
   */
  @InputFieldHint(expression = true)
  @NotBlank(message = "Queue may not be blank")
  @Getter
  @Setter
  private String queue;

  /**
   * How to build the required {@code BasicProperties} if required.
   * <p>
   * The default if not explicitly specified is to return a {@code null} object which uses the
   * default behaviour of RabbitMQ
   * </p>
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "no properties")
  @Valid
  private BasicPropertiesBuilder propertyBuilder;

  /**
   * How the Exchange will be declared prior to use.
   * 
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "the default exchange")
  @AdvancedConfig
  @Valid
  private ExchangeDeclaration exchangeDeclaration;

  /**
   * How the Queue will be declared prior to use.
   * 
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "Durable+Non-Exclusive+No AutoDelete")
  @AdvancedConfig
  @Valid
  private QueueDeclaration queueDeclaration;

  // Channels aren't thread safe
  private transient Channel rabbitChannel = null;
  private transient boolean queueExpression = true;

  @Override
  public void prepare() throws CoreException {
    Args.notBlank(getQueue(), "queue");
  }

  @Override
  public void start() throws CoreException {
    try {
      rabbitChannel =
          retrieveConnection(ConnectionWrapper.class).wrappedConnection().createChannel();
      exchangeDeclaration().declare(rabbitChannel);
      // If the queue isn't an expression we can declare it now and keep a flag as to
      // whether we want to re-declare. QueueDeclaration shouldn't be an expensive
      // operation, but why do it more than you need to?
      if (!InputFieldExpression.isExpression(getQueue())) {
        queueDeclaration().declare(rabbitChannel, getQueue());
        queueExpression = false;
      }
    } catch (Exception e) {
      throw ExceptionHelper.wrapCoreException(e);
    }
  }


  @Override
  public void stop() {
    Closer.closeQuietly(rabbitChannel);
  }

  @Override
  protected void doProduce(AdaptrisMessage msg, String endpoint) throws ProduceException {
    try {
      if (queueExpression) {
        queueDeclaration().declare(rabbitChannel, endpoint);
      }
      rabbitChannel.basicPublish(exchangeDeclaration().name(), endpoint,
          propertyBuilder().build(msg), msg.getContent().getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw ExceptionHelper.wrapProduceException(e);
    }
  }

  private BasicPropertiesBuilder propertyBuilder() {
    return ObjectUtils.defaultIfNull(getPropertyBuilder(), Translator.NO_BASIC_PROPERTIES);
  }

  public StandardMessageProducer withPropertyBuilder(BasicPropertiesBuilder b) {
    setPropertyBuilder(b);
    return this;
  }

  @Override
  public String endpoint(AdaptrisMessage msg) throws ProduceException {
    return msg.resolve(getQueue());
  }

  public StandardMessageProducer withQueue(String s) {
    setQueue(s);
    return this;
  }

  private ExchangeDeclaration exchangeDeclaration() {
    return ObjectUtils.defaultIfNull(getExchangeDeclaration(), DEFAULT_EXCHANGE);
  }


  private QueueDeclaration queueDeclaration() {
    return ObjectUtils.defaultIfNull(getQueueDeclaration(), DEFAULT_QUEUE);
  }
}
