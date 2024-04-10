package business.Impl.CandleService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.CandleService.GetCurrentCandleIntervalParametersUseCaseImpl;
import Eco.TradeX.domain.CandleData;
import Eco.TradeX.persistence.Interfaces.CandleRepositoryInterfaces.ClientAPIRepository;
import TestConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class GetCurrentCandleIntervalParametersUseCaseImplTest extends BaseTest {
    @Mock
    ClientAPIRepository clientAPIRepository;

    @InjectMocks
    GetCurrentCandleIntervalParametersUseCaseImpl getCurrentCandleIntervalParametersUseCase;

    @Test
    void updateLastCandleTestSameInterval() {
        Instant time = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();

        CandleData initialCandle = CandleData.builder()
                .low(BigDecimal.valueOf(10))
                .high(BigDecimal.valueOf(10))
                .open(BigDecimal.valueOf(10))
                .close(BigDecimal.valueOf(10))
                .time(time)
                .volume(100)
                .build();

        CandleData newCandle = CandleData.builder()
                .low(BigDecimal.valueOf(20))
                .high(BigDecimal.valueOf(20))
                .open(BigDecimal.valueOf(20))
                .close(BigDecimal.valueOf(20))
                .time(LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant())
                .volume(200)
                .build();

        List<CandleData> toReturn = new ArrayList<>();
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);

        when(clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(any(Instant.class), any(String.class), any(CandleInterval.class), any(int.class)))
                .thenReturn(toReturn);

        getCurrentCandleIntervalParametersUseCase.setLastCandleAndInterval("test_figi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        getCurrentCandleIntervalParametersUseCase.updateLastCandle(newCandle);

        CandleData result = getCurrentCandleIntervalParametersUseCase.getLastCandle();

        assertEquals(BigDecimal.valueOf(20), result.getClose());
        assertEquals(BigDecimal.valueOf(10), result.getOpen());
        assertEquals(BigDecimal.valueOf(20), result.getHigh());
        assertEquals(BigDecimal.valueOf(20), result.getLow());
        assertEquals(100, result.getVolume());
        assertEquals(time, result.getTime());
    }

    @Test
    void updateLastCandleTestSameIntervalVolumeCheck() {
        Instant time = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();

        CandleData initialCandle = CandleData.builder()
                .low(BigDecimal.valueOf(10))
                .high(BigDecimal.valueOf(10))
                .open(BigDecimal.valueOf(10))
                .close(BigDecimal.valueOf(10))
                .time(time)
                .volume(100)
                .build();

        CandleData newCandle = CandleData.builder()
                .low(BigDecimal.valueOf(20))
                .high(BigDecimal.valueOf(20))
                .open(BigDecimal.valueOf(20))
                .close(BigDecimal.valueOf(20))
                .time(LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant())
                .volume(200)
                .build();

        CandleData newCandleLast = CandleData.builder()
                .low(BigDecimal.valueOf(20))
                .high(BigDecimal.valueOf(20))
                .open(BigDecimal.valueOf(20))
                .close(BigDecimal.valueOf(20))
                .time(LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant())
                .volume(300)
                .build();

        List<CandleData> toReturn = new ArrayList<>();
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);

        when(clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(any(Instant.class), any(String.class), any(CandleInterval.class), any(int.class)))
                .thenReturn(toReturn);

        getCurrentCandleIntervalParametersUseCase.setLastCandleAndInterval("test_figi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        getCurrentCandleIntervalParametersUseCase.updateLastCandle(newCandle);
        getCurrentCandleIntervalParametersUseCase.updateLastCandle(newCandleLast);

        CandleData result = getCurrentCandleIntervalParametersUseCase.getLastCandle();

        assertEquals(BigDecimal.valueOf(20), result.getClose());
        assertEquals(BigDecimal.valueOf(10), result.getOpen());
        assertEquals(BigDecimal.valueOf(20), result.getHigh());
        assertEquals(BigDecimal.valueOf(20), result.getLow());
        assertEquals(200, result.getVolume());
        assertEquals(time, result.getTime());
    }

    @Test
    void updateLastCandleTestDifferentIntervals() {
        Instant time = LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant();

        CandleData initialCandle = CandleData.builder()
                .low(BigDecimal.valueOf(10))
                .high(BigDecimal.valueOf(10))
                .open(BigDecimal.valueOf(10))
                .close(BigDecimal.valueOf(10))
                .time(time)
                .volume(100)
                .build();

        CandleData newCandle = CandleData.builder()
                .low(BigDecimal.valueOf(20))
                .high(BigDecimal.valueOf(20))
                .open(BigDecimal.valueOf(20))
                .close(BigDecimal.valueOf(20))
                .time(LocalDate.of(2023, 1, 3).atStartOfDay(ZoneId.systemDefault()).toInstant())
                .volume(200)
                .build();

        List<CandleData> toReturn = new ArrayList<>();
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);
        toReturn.add(initialCandle);

        when(clientAPIRepository.getExtraHistoricalCandlesFromCertainTime(any(Instant.class), any(String.class), any(CandleInterval.class), any(int.class)))
                .thenReturn(toReturn);

        getCurrentCandleIntervalParametersUseCase.setLastCandleAndInterval("test_figi", CandleInterval.CANDLE_INTERVAL_1_MIN);
        getCurrentCandleIntervalParametersUseCase.updateLastCandle(newCandle);

        CandleData result = getCurrentCandleIntervalParametersUseCase.getLastCandle();

        assertEquals(BigDecimal.valueOf(20), result.getClose());
        assertEquals(BigDecimal.valueOf(20), result.getOpen());
        assertEquals(BigDecimal.valueOf(20), result.getHigh());
        assertEquals(BigDecimal.valueOf(20), result.getLow());
        assertEquals(200, result.getVolume());
        assertEquals(LocalDate.of(2023, 1, 3).atStartOfDay(ZoneId.systemDefault()).toInstant(), result.getTime());
    }
}