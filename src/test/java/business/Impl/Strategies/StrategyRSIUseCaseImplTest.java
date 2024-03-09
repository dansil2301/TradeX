package business.Impl.Strategies;

import Eco.TradeX.business.Impl.Strategies.RSI.StrategyRSIUseCaseImpl;
import Eco.TradeX.business.utils.CandlesSeparationAndInitiation;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.impl.tinkoff.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.impl.tinkoff.TokenManagerTinkoffImpl;
import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StrategyRSIUseCaseImplTest {

    @Test
    void getStrategyParametersForCandles() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        CandlesSeparationAndInitiation candlesSeparationAndInitiation = new CandlesSeparationAndInitiation(client);
        StrategyRSIUseCaseImpl strategyRSIUseCaseImpl = new StrategyRSIUseCaseImpl(client, candlesSeparationAndInitiation);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);

        var parameters = strategyRSIUseCaseImpl.getStrategyParametersForCandles(candles, from, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(candles.size(), parameters.size());
    }
}