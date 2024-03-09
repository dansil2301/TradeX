package business.Impl;

import Eco.TradeX.business.Impl.CandleService.GetCandlesAPIInformationUseCaseImpl;
import Eco.TradeX.persistence.impl.tinkoff.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.impl.tinkoff.TokenManagerTinkoffImpl;
import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class GetCandlesAPIInformationUseCaseImplTest {

    @Test
    void getHistoricalCandlesAPI1Minute() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        GetCandlesAPIInformationUseCaseImpl getCandlesAPIInformationUseCase = new GetCandlesAPIInformationUseCaseImpl(client);

        // period _from _to
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        var candles = getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        var close = candles.get(0).getClose();
        assertEquals(new BigDecimal("141.060000000"), close);
    }
}