package business.Impl.TradersService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.TradersService.GetTradersMethodsUseCaseImpl;
import Eco.TradeX.business.exceptions.UnauthorizedDataAccessException;
import Eco.TradeX.business.utils.TraderUtils.TraderConverter;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.TraderRepository.TraderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TradeXApplication.class)
class GetTradersMethodsUseCaseImplTest {
    @Mock
    private TraderConverter traderConverter;

    @Mock
    private TraderRepository traderRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private GetTradersMethodsUseCaseImpl getTradersMethodsUseCase;

    @Test
    public void testGetTraderById_Admin() {
        Long traderId = 1L;
        TraderEntity traderEntity = new TraderEntity();
        traderEntity.setId(traderId);
        traderEntity.setEmail("test");
        traderEntity.setStatus(TraderStatus.TRADER_BASIC);
        traderEntity.setUsername("adadad");
        traderEntity.setPassword("adadad");
        traderEntity.setRegisteredAt(LocalDateTime.now());
        Optional<TraderEntity> optionalTraderEntity = Optional.of(traderEntity);

        TraderData traderData = TraderData.builder()
                .id(traderId)
                .build();

        when(requestAccessToken.getId()).thenReturn(traderId);
        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.ADMIN);
        when(traderRepository.findById(traderId)).thenReturn(optionalTraderEntity);
        when(traderConverter.convertToTraderData(optionalTraderEntity)).thenCallRealMethod();

        TraderData result = getTradersMethodsUseCase.getTraderById(traderId);

        assertEquals(traderData.getId(), result.getId());
    }

    @Test
    public void testGetTraderById_UnauthorizedAccess() {
        Long traderId = 1L;

        when(requestAccessToken.getId()).thenReturn(traderId + 1);
        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.TRADER_BASIC);

        try {
            getTradersMethodsUseCase.getTraderById(traderId);
            fail("Expected UnauthorizedDataAccessException was not thrown");
        } catch (UnauthorizedDataAccessException e) {
            // Expected behavior
            assertEquals("403 FORBIDDEN \"TRADER_ID_NOT_FROM_LOGGED_IN_USER\"", e.getMessage());
        }
    }

    @Test
    public void testGetAllTraders_AdminAccess() {
        List<TraderEntity> traderEntities = Arrays.asList(
                new TraderEntity(), new TraderEntity()
        );

        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.ADMIN);
        when(traderRepository.findAll()).thenReturn(traderEntities);

        List<TraderData> traderDataList = Arrays.asList(
                TraderData.builder().build(), TraderData.builder().build()
        );
        when(traderConverter.convertToTraderData(traderEntities)).thenReturn(traderDataList);

        List<TraderData> result = getTradersMethodsUseCase.getAllTraders();

        assertEquals(traderDataList, result);
    }

    @Test
    public void testGetAllTraders_UnauthorizedAccess() {
        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.TRADER_BASIC);

        try {
            getTradersMethodsUseCase.getAllTraders();
            fail("Expected UnauthorizedDataAccessException was not thrown");
        } catch (UnauthorizedDataAccessException e) {
            // Expected behavior
            assertEquals("403 FORBIDDEN \"TRADER_ID_NOT_FROM_LOGGED_IN_USER\"", e.getMessage());
        }
    }

    @Test
    public void testGetCountOfTradersStatus() {
        TraderStatus status = TraderStatus.TRADER_BASIC;
        long expectedCount = 5L;

        when(traderRepository.countByStatus(status)).thenReturn(expectedCount);

        Long result = getTradersMethodsUseCase.getCountOfTradersStatus(status);

        assertEquals(expectedCount, result.longValue());
    }

    @Test
    public void testGetCountAllTraders() {
        long expectedCount = 10L;

        when(traderRepository.count()).thenReturn(expectedCount);

        Long result = getTradersMethodsUseCase.getCountAllTraders();

        assertEquals(expectedCount, result.longValue());
    }
}