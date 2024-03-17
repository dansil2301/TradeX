package business.Impl.CandleService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.CandleService.GetCandlesAPIInformationUseCaseImpl;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import TestConfigs.BaseTest;
import business.Impl.CreateCandlesDataFake;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.core.InvestApi;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class GetCandlesAPIInformationUseCaseImplTest extends BaseTest {
    private final CreateCandlesDataFake createCandlesDataFake = new CreateCandlesDataFake();

    @Mock
    ClientTinkoffAPIImpl clientAPIRepositoryMock;

    @InjectMocks
    private GetCandlesAPIInformationUseCaseImpl clientMock;

    @Test
    void getHistoricalCandlesAPIMock() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> mockCandles = createCandlesDataFake.createCandles(30);
        when(clientAPIRepositoryMock.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(mockCandles);

        List<CandleData> candles = clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(30, candles.size());
    }

    @Test
    void getHistoricalCandlesNoCandlesMock() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        when(clientAPIRepositoryMock.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(null);

        List<CandleData> candles = clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(null, candles);
    }

    @Test
    void getHistoricalCandlesLengthLimitErrorMock() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        when(clientAPIRepositoryMock.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenThrow(new RuntimeException("Error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clientMock.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );

        String expectedMessage = "Error";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}