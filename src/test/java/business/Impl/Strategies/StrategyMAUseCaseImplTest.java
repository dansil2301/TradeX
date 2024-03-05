package business.Impl.Strategies;

import Eco.TradeX.business.Impl.Strategies.MA.StrategyMAUseCaseImpl;
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

class StrategyMAUseCaseImplTest {

    @Test
    void getStrategyParametersForCandles1MinCandle1DaysLongMA() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);

        var parameters = strategyMAUseCase.getStrategyParametersForCandles(candles, from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        for (Map.Entry<String, List<BigDecimal>> entry : parameters.entrySet()){
            List<BigDecimal> all_MA = entry.getValue();
            assertEquals(candles.size(), all_MA.size());
        }
    }

    @Test
    void getStrategyParametersForCandles5MinCandle1DaysLongMA() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_5_MIN);

        var parameters = strategyMAUseCase.getStrategyParametersForCandles(candles, from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_5_MIN);
        for (Map.Entry<String, List<BigDecimal>> entry : parameters.entrySet()){
            List<BigDecimal> all_MA = entry.getValue();
            assertEquals(candles.size(), all_MA.size());
        }
    }
}