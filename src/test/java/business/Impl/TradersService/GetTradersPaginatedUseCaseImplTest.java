package business.Impl.TradersService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.TradersService.GetTradersPaginatedUseCaseImpl;
import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.business.exceptions.UnauthorizedDataAccessException;
import Eco.TradeX.business.utils.TraderUtils.TraderConverter;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.TraderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class GetTradersPaginatedUseCaseImplTest {
    @Mock
    private TraderRepository traderRepository;

    @Mock
    private TraderConverter converter;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private GetTradersPaginatedUseCaseImpl getTradersPaginatedUseCase;

    @Test
    public void testGetTraderByPage_UnauthorizedAccess() {
        int pageNumber = 0;
        int pageSize = 10;

        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.TRADER_PLUS);

        try {
            getTradersPaginatedUseCase.getTraderByPage(pageNumber, pageSize);
            fail("Expected UnauthorizedDataAccessException was not thrown");
        } catch (UnauthorizedDataAccessException e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void testGetTraderByPage_NoTradersFound() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.ADMIN); // Admin
        when(traderRepository.findAllByOrderByUsernameAsc(pageable)).thenReturn(Page.empty());

        // Perform the operation
        try {
            getTradersPaginatedUseCase.getTraderByPage(pageNumber, pageSize);
            fail("Expected TraderExceptions was not thrown");
        } catch (TraderExceptions e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void testGetTraderByPage_Successful() {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<TraderEntity> traderPage = mock(Page.class);
        Page<TraderData> expectedPage = mock(Page.class);

        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.ADMIN); // Admin
        when(traderRepository.findAllByOrderByUsernameAsc(pageable)).thenReturn(traderPage);
        when(converter.convertToTraderData(any(TraderEntity.class))).thenCallRealMethod();
        when(converter.convertToTraderData(any(TraderEntity.class))).thenCallRealMethod();

        try {
            Page<TraderData> result = getTradersPaginatedUseCase.getTraderByPage(pageNumber, pageSize);
            assertEquals(null, result);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void testGetTraderByUniversalSearch_NoTradersFound() {
        int pageNumber = 0;
        int pageSize = 10;
        String searchString = "search";
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        when(traderRepository.searchTraderEntityBy(searchString, pageable)).thenReturn(Page.empty());

        try {
            getTradersPaginatedUseCase.getTraderByUniversalSearch(pageNumber, pageSize, searchString);
            fail("Expected TraderExceptions was not thrown");
        } catch (TraderExceptions e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
}