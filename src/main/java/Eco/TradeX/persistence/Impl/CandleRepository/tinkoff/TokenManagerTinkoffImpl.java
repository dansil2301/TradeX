package Eco.TradeX.persistence.Impl.CandleRepository.tinkoff;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenManagerTinkoffImpl {
    @Value("${tinkoffToken}")
    private String token;

    public String readTokenLocally() {
        return token;
    }
}
