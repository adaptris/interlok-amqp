package interlok.rabbitmq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.BooleanUtils;

import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldDefault;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import interlok.rabbitmq.Declaration.ExchangeDeclaration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Declaring an explicit exchange.
 * <p>
 * Depending on your RabbitMQ configuration you may wish to explicitly declare an exchange that is not the 'default exchange' when producing
 * messages.
 * </p>
 */
@XStreamAlias("rabbitmq-explicit-exchange-declaration")
@NoArgsConstructor
@ComponentProfile(summary = "Explicitly declare a RabbitMQ Exchange", since = "4.3.0")
public class ExplicitExchange implements ExchangeDeclaration {

  /**
   * The exchange type.
   *
   */
  @Setter
  @Getter
  @NotNull
  private BuiltinExchangeType type = BuiltinExchangeType.DIRECT;

  /**
   * The exchange name.
   *
   */
  @Getter
  @Setter
  @NotBlank(message = "Blank means default exchange; that doesn't need explicit declaration")
  private String name;

  /**
   * Whether or not the exchange should survive a server restart.
   *
   */
  @InputFieldDefault(value = "true")
  @Getter
  @Setter
  private Boolean durable;

  @Override
  public void declare(Channel ch) throws Exception {
    ch.exchangeDeclare(name, type, durable());
  }

  private boolean durable() {
    return BooleanUtils.toBooleanDefaultIfNull(getDurable(), true);
  }

  public ExplicitExchange withName(String s) {
    setName(s);
    return this;
  }

  public ExplicitExchange withType(BuiltinExchangeType t) {
    setType(t);
    return this;
  }

  public ExplicitExchange withDurable(Boolean b) {
    setDurable(b);
    return this;
  }

  @Override
  public String name() {
    return getName();
  }

}
