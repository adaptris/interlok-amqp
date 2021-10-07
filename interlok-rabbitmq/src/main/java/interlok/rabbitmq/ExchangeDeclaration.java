package interlok.rabbitmq;

import java.io.IOException;
import org.apache.commons.lang3.BooleanUtils;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.annotation.InputFieldHint;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ExchangeDeclaration {

  /** The exchange type.
   * 
   */
  @Setter
  @Getter
  private BuiltinExchangeType type = BuiltinExchangeType.DIRECT;

  /** The exchange name.
   * 
   */
  @InputFieldHint(style="BLANKABLE")
  @InputFieldDefault(value = "")
  @Getter
  @Setter
  private String name = "";
  
  /** Whether the exchange should be duerable.
   * 
   */
  @InputFieldDefault(value = "true")
  @Getter
  @Setter
  private Boolean durable;
  
  public void declare(Channel ch) throws Exception {
    ch.exchangeDeclare(name, type, durable());
  }
  
  private boolean durable() {
    return BooleanUtils.toBooleanDefaultIfNull(getDurable(), true);
  }
  
  public ExchangeDeclaration withName(String s) {
    setName(s);
    return this;
  }
  
  public ExchangeDeclaration withType(BuiltinExchangeType t) {
    setType(t);
    return this;
  }

  public ExchangeDeclaration withDurable(Boolean b) {
    setDurable(b);
    return this;
  }
}
