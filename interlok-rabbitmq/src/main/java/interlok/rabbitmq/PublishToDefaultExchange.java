package interlok.rabbitmq;

import java.nio.charset.StandardCharsets;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
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
import interlok.rabbitmq.Translator.BasicPropertiesBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Publishes a message to the default exchange.
 * <p>
 * This is the simplest way to publish a message to RabbitMQ to an exchange of {@code ""} which is
 * equivalent to the default exchange.
 * </p>
 * <p>
 * If there is metadata configured to be passed in then a {@code BasicProperties} object will be
 * created where the headers contain the metadata. No attempt will be made to configure other
 * property likes content-type etc.
 * </p>
 */
@XStreamAlias("rabbitmq-publish-to-default-exchange")
@NoArgsConstructor
@ComponentProfile(summary = "Publishes a message to the default exchange", tag = "amqp, rabbitmq", since = "4.3.0")
@DisplayOrder(order = {"queue", "metadataFilter"})
public class PublishToDefaultExchange extends ProduceOnlyProducerImp {

  /**
   * The queue to publish to.
   * 
   */
  @InputFieldHint(expression = true)
  @NotBlank
  @Getter
  @Setter
  private String queue;
  
  /** How to build the required {@code BasicProperties} if required.
   *  <p>The default if not explicitly specified is to return a {@code null} object which uses the default behaviour of RabbitMQ</p>
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "no properties")
  @Valid  
  private BasicPropertiesBuilder propertyBuilder;
  
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
      // If the queue isn't an expression we can declare it now and keep a flag as to 
      // whether we want to re-declare. QueueDeclaration shouldn't be an expensive
      // operation, but why do it more than you need to?
      if (!InputFieldExpression.isExpression(getQueue())) {
        rabbitChannel.queueDeclare(getQueue(), true, false, false, null);
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
        rabbitChannel.queueDeclare(endpoint, true, false, false, null);
      }
      rabbitChannel.basicPublish("", endpoint, propertyBuilder().build(msg), msg.getContent().getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw ExceptionHelper.wrapProduceException(e);
    }
  }

  private BasicPropertiesBuilder propertyBuilder() {
    return ObjectUtils.defaultIfNull(getPropertyBuilder(), Translator.NO_BASIC_PROPERTIES);
  }
  
  public PublishToDefaultExchange withPropertyBuilder(BasicPropertiesBuilder b) {
    setPropertyBuilder(b);
    return this;
  }
  
  @Override
  public String endpoint(AdaptrisMessage msg) throws ProduceException {
    return msg.resolve(getQueue());
  }

  public PublishToDefaultExchange withQueue(String s) {
    setQueue(s);
    return this;
  }
}
