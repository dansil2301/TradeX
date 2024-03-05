package business.Impl.Strategies;

import Eco.TradeX.business.Impl.Strategies.MA.StrategyMAUseCaseImpl;
import Eco.TradeX.business.Impl.Strategies.RSI.StrategyRSIUseCaseImpl;
import Eco.TradeX.business.Impl.Strategies.StrategyFactoryUseCaseImpl;
import Eco.TradeX.business.StrategyUseCase;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.impl.tinkoff.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.impl.tinkoff.TokenManagerTinkoffImpl;
import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyFactoryUseCaseImplTest {

    @Test
    void getCandlesStrategiesParameters() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        StrategyMAUseCaseImpl strategyMAUseCase = new StrategyMAUseCaseImpl(client);
        StrategyRSIUseCaseImpl strategyRSIUseCase = new StrategyRSIUseCaseImpl(client);

        List<StrategyUseCase> strategies = new ArrayList<>();
        strategies.add(strategyMAUseCase);
        strategies.add(strategyRSIUseCase);

        StrategyFactoryUseCaseImpl strategyFactoryUseCase = new StrategyFactoryUseCaseImpl(strategies);

        Instant to = LocalDate.of(2023, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));
        List<CandleData> candles = client.getHistoricalCandles(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);

        List<String> strategyNames = new ArrayList<>();
        strategyNames.add("RSI");
        strategyNames.add("MA");

        var allParams = strategyFactoryUseCase.getCandlesStrategiesParameters(strategyNames, candles, from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(candles.size(), allParams.size());
    }
}