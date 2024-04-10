package business.Impl.TradersService;

import Eco.TradeX.business.Impl.TradersService.CreateTraderUseCaseImpl;
import Eco.TradeX.business.exceptions.TraderAlreadyExistsException;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Impl.TraderRepository.TraderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreateTraderUseCaseImplTest {
    @Mock
    private TraderRepository traderRepository;

    @InjectMocks
    private CreateTraderUseCaseImpl createTraderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraderTest() {
        TraderEntity traderEntity = new TraderEntity();
        traderEntity.setId(1L);
        traderEntity.setUsername("test_user");
        traderEntity.setEmail("test@example.com");
        traderEntity.setPassword("password123");
        traderEntity.setStatus(TraderStatus.TRADER_BASIC);

        CreateTraderRequest request = CreateTraderRequest.builder()
                .username("test_user")
                .email("test@example.com")
                .password("password123")
                .status(TraderStatus.TRADER_BASIC)
                .build();

        when(traderRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(traderRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(traderRepository.save(any(TraderEntity.class))).thenReturn(null);
        when(traderRepository.findByEmail(any(String.class))).thenReturn(traderEntity);

        Long id = createTraderUseCase.createTrader(request);

        assertEquals(Long.parseLong("1"), id);
    }

    @Test
    public void testCreateTrader_WithExistingUsername() {
        when(traderRepository.existsByUsername(anyString())).thenReturn(true);

        CreateTraderRequest request = new CreateTraderRequest();
        request.setUsername("existingUsername");
        request.setEmail("newEmail@example.com");

        assertThrows(TraderAlreadyExistsException.class, () -> createTraderUseCase.createTrader(request));

        verify(traderRepository, times(1)).existsByUsername("existingUsername");
    }

    @Test
    public void testCreateTrader_WithExistingEmail() {
        when(traderRepository.existsByEmail(anyString())).thenReturn(true);

        CreateTraderRequest request = new CreateTraderRequest();
        request.setUsername("newUsername");
        request.setEmail("existingEmail@example.com");

        assertThrows(TraderAlreadyExistsException.class, () -> createTraderUseCase.createTrader(request));

        verify(traderRepository, times(1)).existsByEmail("existingEmail@example.com");
    }
}