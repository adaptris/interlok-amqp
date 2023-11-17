package interlok.rabbitmq;

import com.rabbitmq.client.Channel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Declaration {
  /**
   * Interface that allows exchange declaration to be composed.
   *
   */
  @FunctionalInterface
  public interface ExchangeDeclaration {

    /**
     * Declare the exchange.
     *
     * @param ch
     *          the channel to use to declare the exchange.
     */
    void declare(Channel ch) throws Exception;

    /**
     * The name of the exchange that was declared.
     *
     */
    default String name() {
      return "";
    }

  }

  /**
   * Interface that allows queue declaration to be composed.
   *
   */
  @FunctionalInterface
  public interface QueueDeclaration {

    /**
     * Declare the queue.
     *
     * @param ch
     *          the channel to use to declare the queue.
     * @param name
     *          the name of the queue.
     */
    void declare(Channel ch, String name) throws Exception;

  }

}
