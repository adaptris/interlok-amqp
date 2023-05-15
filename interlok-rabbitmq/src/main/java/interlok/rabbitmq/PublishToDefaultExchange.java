package interlok.rabbitmq;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.core.AdaptrisConnection;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.ConnectedService;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.interlok.util.Args;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import interlok.rabbitmq.Translator.BasicPropertiesBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Wraps {@link StandardMessageProducer} as a service for discoverability purposes.
 * <p>
 * This is the simplest way to publish a message to RabbitMQ to an exchange of {@code ""} which is equivalent to the default exchange.
 * </p>
 * <p>
 * It does not expose all the configuration possible to {@link StandardMessageProducer} and is included as a convenience to simply publish a
 * message to a RabbitMQ Queue. It does expose a {@link RabbitMqConnection} as configuration so you will have the opportunity to <i>share
 * connections</i>.
 * </p>
 *
 */
@XStreamAlias("rabbitmq-publish-to-default-exchange")
@ComponentProfile(summary = "Put the message payload onto an AMQP Queue", recommended = {
    RabbitMqConnection.class }, since = "4.3.0", tag = "amqp,rabbitmq")
@AdapterComponent
@DisplayOrder(order = { "queue", "behaviour", "connection", "propertyBuilder" })
@NoArgsConstructor
public class PublishToDefaultExchange extends ServiceImp implements ConnectedService {

  /**
   * Controls behaviour when publishing the messsage.
   */
  public enum Behaviour {
    /**
     * Behave like a normal service and throw an exception on failure.
     *
     */
    TRADITIONAL {

      @Override
      void handleSuccess(AdaptrisMessage msg) {
      }

      @Override
      void handleFailure(AdaptrisMessage msg, Exception e) throws ServiceException {
        throw ExceptionHelper.wrapServiceException(e);
      }

    },
    /**
     * Never throw an exception and add values against {@link MetadataConstants#RMQ_PUBLISH_STATUS} to indicate success/failure.
     *
     */
    NO_EXCEPTION {
      @Override
      void handleSuccess(AdaptrisMessage msg) {
        msg.addMessageHeader(MetadataConstants.RMQ_PUBLISH_STATUS, "success");
      }

      @Override
      void handleFailure(AdaptrisMessage msg, Exception e) throws ServiceException {
        msg.addMessageHeader(MetadataConstants.RMQ_PUBLISH_STATUS, "failed, message:" + ExceptionUtils.getStackTrace(e));
      }

    };

    abstract void handleSuccess(AdaptrisMessage msg);

    abstract void handleFailure(AdaptrisMessage msg, Exception e) throws ServiceException;

  }

  /**
   * The queue to publish to.
   */
  @Getter
  @Setter
  @NotBlank(message = "The queue is required so we know where to publish to")
  @InputFieldHint(expression = true)
  private String queue;

  /**
   * How to build the required {@code BasicProperties} if required.
   * <p>
   * The default if not explicitly specified is to return a {@code null} object which uses the default behaviour of RabbitMQ.
   * </p>
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "no properties")
  @Valid
  @AdvancedConfig(rare = true)
  private BasicPropertiesBuilder propertyBuilder;

  /**
   * What to do after publishing the message depending on whether that was successful or not.
   * <p>
   * <ul>
   * <li>TRADITIONAL : act like a traditional service and throw an exception if publishing was not successful</li>
   * <li>NO_EXCEPTION: Never throw an exception, add metadata against the key {@value MetadataConstants#RMQ_PUBLISH_STATUS} indicating
   * success or failure. Failure will contain the stacktrace from the exception but no exception will be thrown by the service.</li>
   * </ul>
   * </p>
   */
  @Getter
  @Setter
  @InputFieldDefault(value = "TRADITIONAL")
  @NotNull(message = "behaviour should be one of the available enums")
  private Behaviour behaviour = Behaviour.TRADITIONAL;

  /**
   * The RabbitMQ Connection.
   *
   */
  @Getter
  @Setter
  @Valid
  @NotNull(message = "No connection means we don't know how to connect to RabbitMQ")
  private AdaptrisConnection connection;

  private transient StandardMessageProducer wrappedProducer;

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    try {
      wrappedProducer.produce(msg);
      getBehaviour().handleSuccess(msg);
    } catch (Exception e) {
      getBehaviour().handleFailure(msg, e);
    }
  }

  @Override
  public final void prepare() throws CoreException {
    Args.notNull(getConnection(), "connection");
    Args.notBlank(getQueue(), "queue");
    wrappedProducer = new StandardMessageProducer().withPropertyBuilder(getPropertyBuilder()).withQueue(getQueue());
    LifecycleHelper.prepare(wrappedProducer);
    LifecycleHelper.prepare(getConnection());
  }

  @Override
  public final void initService() throws CoreException {
    connection.addExceptionListener(this);
    connection.addMessageProducer(wrappedProducer);
    LifecycleHelper.init(getConnection());
    LifecycleHelper.init(wrappedProducer);

  }

  @Override
  public final void closeService() {
    LifecycleHelper.close(getConnection());
    LifecycleHelper.close(wrappedProducer);
  }

  @Override
  public void start() throws CoreException {
    LifecycleHelper.start(getConnection());
    LifecycleHelper.start(wrappedProducer);
  }

  @Override
  public void stop() {
    LifecycleHelper.stop(getConnection());
    LifecycleHelper.stop(wrappedProducer);
  }

  public PublishToDefaultExchange withConnection(RabbitMqConnection c) {
    setConnection(c);
    return this;
  }

  public PublishToDefaultExchange withQueue(String q) {
    setQueue(q);
    return this;
  }

}
