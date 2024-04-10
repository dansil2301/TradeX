package business.utils.CandleUtils;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.CandleService.GetCandlesAPIInformationUseCaseImpl;
import Eco.TradeX.business.utils.CandleUtils.CandlesSeparationAndInitiation;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.ClientTinkoffAPIImpl;
import TestConfigs.BaseTest;
import business.Impl.CreateCandlesDataFake;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class CandlesSeparationAndInitiationTest extends BaseTest {
    private final CreateCandlesDataFake createCandlesDataFake = new CreateCandlesDataFake();

    @Mock
    ClientTinkoffAPIImpl clientAPIRepositoryMock;

    @InjectMocks
    private CandlesSeparationAndInitiation candlesSeparationAndInitiation;

    @Test
    void initiateCandlesProperlySeparateMock() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> mockCandles = createCandlesDataFake.createCandles(30);
        when(clientAPIRepositoryMock.getExtraHistoricalCandlesFromCertainTime(from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN, 10))
                .thenReturn(new ArrayList<>());

        List<List<CandleData>> extraCandlesCandles = candlesSeparationAndInitiation.initiateCandlesProperly(mockCandles, null, 10, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);

        assertEquals(10, extraCandlesCandles.get(0).size());
        assertEquals(20, extraCandlesCandles.get(1).size());
    }

    @Test
    void initiateCandlesProperlyNoSeparateLeaveTheSameMock() {
        Instant to = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant from = to.minus(Duration.ofDays(1));

        List<CandleData> mockCandles = createCandlesDataFake.createCandles(30);
        List<CandleData> mockCandlesExtra = createCandlesDataFake.createCandles(10);

        List<List<CandleData>> extraCandlesCandles = candlesSeparationAndInitiation.initiateCandlesProperly(mockCandles, mockCandlesExtra, 10, from, "testFigi", CandleInterval.CANDLE_INTERVAL_1_MIN);

        assertEquals(10, extraCandlesCandles.get(0).size());
        assertEquals(30, extraCandlesCandles.get(1).size());
    }
}