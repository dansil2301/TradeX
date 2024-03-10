package business.Impl.CandleService;

import Eco.TradeX.business.Impl.CandleService.GetCandlesAPIInformationUseCaseImpl;
import Eco.TradeX.persistence.impl.tinkoff.CandleRepository.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.impl.tinkoff.CandleRepository.TokenManagerTinkoffImpl;
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

    @Test
    void getHistoricalCandles1MinuteNoCandles() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        GetCandlesAPIInformationUseCaseImpl getCandlesAPIInformationUseCase = new GetCandlesAPIInformationUseCaseImpl(client);

        // period _from _to
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minusSeconds(1);

        var candles = getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(null, candles);
    }

    @Test
    void getHistoricalCandles1MinuteLengthLimitError() {
        TokenManagerTinkoffImpl tokenManager = new TokenManagerTinkoffImpl();
        ClientTinkoffAPIImpl client = new ClientTinkoffAPIImpl(tokenManager);
        GetCandlesAPIInformationUseCaseImpl getCandlesAPIInformationUseCase = new GetCandlesAPIInformationUseCaseImpl(client);

        // period _from _to
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(500));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> getCandlesAPIInformationUseCase.getHistoricalCandlesAPI(from, to, "BBG004730N88", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );

        String expectedMessage = "500 INTERNAL_SERVER_ERROR \"Candles Error: Превышен максимальный период запроса для данного интервала свечи. Укажите корректный интервал.\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}