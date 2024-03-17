package business.Impl.CandleService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.CandleService.GetCandlesAPIInformationUseCaseImpl;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.ClientTinkoffAPIImpl;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import TestConfigs.BaseTest;
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
    private static final Random random = new Random();

    @Mock
    ClientTinkoffAPIImpl clientAPIRepositoryMock;

    @InjectMocks
    private GetCandlesAPIInformationUseCaseImpl client;

    private BigDecimal generateRandomBigDecimal() {
        return BigDecimal.valueOf(random.nextDouble() * 100);
    }

    private List<CandleData> createCandles() {
        List<CandleData> candlesFake = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            CandleData candleFake = CandleData.builder()
                    .low(generateRandomBigDecimal())
                    .high(generateRandomBigDecimal())
                    .open(generateRandomBigDecimal())
                    .close(generateRandomBigDecimal())
                    .time(LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant())
                    .volume(450)
                    .build();
            candlesFake.add(candleFake);
        }

        return candlesFake;
    }

    @Test
    void getHistoricalCandlesAPIMock() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> mockCandles = createCandles();
        when(clientAPIRepositoryMock.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(mockCandles);

        List<CandleData> candles = client.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        assertEquals(30, candles.size());
    }

    @Test
    void getHistoricalCandlesNoCandlesMock() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        when(clientAPIRepositoryMock.getHistoricalCandles(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN))
                .thenReturn(null);

        List<CandleData> candles = client.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);
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
                () -> client.getHistoricalCandlesAPI(from, to, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN)
        );

        String expectedMessage = "Error";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}