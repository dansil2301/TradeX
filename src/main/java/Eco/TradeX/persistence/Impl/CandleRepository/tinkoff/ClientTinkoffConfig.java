package Eco.TradeX.persistence.Impl.CandleRepository.tinkoff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
public class ClientTinkoffConfig {
    @Bean
    public InvestApi investApiTinkoff(TokenManagerTinkoffImpl tokenManager) {
        return InvestApi.createReadonly(tokenManager.readTokenLocally());
    }
}
