package business.Impl.TradersService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.TradersService.EditTraderUseCaseImpl;
import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Requests.EditTraderRequest;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.TraderRepository.TraderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TradeXApplication.class)
class EditTraderUseCaseImplTest {
    @Mock
    private TraderRepository traderRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private EditTraderUseCaseImpl editTraderUseCase;

    @Test
    void editTrader() {
        Long traderId = 1L;
        EditTraderRequest request = new EditTraderRequest();
        request.setUsername("newUsername");
        request.setEmail("newEmail@example.com");
        request.setStatus(TraderStatus.ADMIN);

        TraderEntity trader = new TraderEntity();
        trader.setId(traderId);
        trader.setUsername("oldUsername");
        trader.setEmail("oldEmail@example.com");
        trader.setStatus(TraderStatus.TRADER_BASIC);

        when(requestAccessToken.getId()).thenReturn(traderId);
        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.ADMIN);
        when(traderRepository.findById(traderId)).thenReturn(Optional.of(trader));

        editTraderUseCase.editTrader(traderId, request);

        verify(traderRepository).save(trader);
        assertEquals(request.getUsername(), trader.getUsername());
        assertEquals(request.getEmail(), trader.getEmail());
        assertEquals(request.getStatus(), trader.getStatus());
    }

    @Test
    public void testEditTrader_TraderNotFound() {
        // Mock data
        Long traderId = 1L;
        EditTraderRequest request = new EditTraderRequest();
        request.setUsername("newUsername");
        request.setEmail("newEmail@example.com");
        request.setStatus(TraderStatus.TRADER_BASIC);

        when(requestAccessToken.getId()).thenReturn(traderId);
        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.ADMIN);
        when(traderRepository.findById(traderId)).thenReturn(Optional.empty());

        // Perform the edit
        try {
            editTraderUseCase.editTrader(traderId, request);
            fail("Expected TraderExceptions was not thrown");
        } catch (TraderExceptions e) {
            // Expected behavior
        }

        verify(traderRepository, never()).save(any());
    }

    @Test
    public void testEditTrader_AdminEdit() {
        // Mock data
        Long traderId = 1L;
        EditTraderRequest request = new EditTraderRequest();
        request.setUsername("newUsername");
        request.setEmail("newEmail@example.com");
        request.setStatus(TraderStatus.ADMIN);

        TraderEntity trader = new TraderEntity();
        trader.setId(traderId);
        trader.setUsername("oldUsername");
        trader.setEmail("oldEmail@example.com");
        trader.setStatus(TraderStatus.TRADER_BASIC);

        when(requestAccessToken.getId()).thenReturn(traderId);
        when(requestAccessToken.getStatus()).thenReturn(TraderStatus.ADMIN);
        when(traderRepository.findById(traderId)).thenReturn(Optional.of(trader));

        // Perform the edit
        editTraderUseCase.editTrader(traderId, request);

        // Verify the trader is updated
        verify(traderRepository).save(trader);
        assertEquals(request.getUsername(), trader.getUsername());
        assertEquals(request.getEmail(), trader.getEmail());
        assertEquals(request.getStatus(), trader.getStatus());
    }
}