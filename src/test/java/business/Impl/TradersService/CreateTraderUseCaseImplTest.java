package business.Impl.TradersService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.TradersService.CreateTraderUseCaseImpl;
import Eco.TradeX.business.exceptions.TraderAlreadyExistsException;
import Eco.TradeX.configuration.security.token.AccessToken;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.TraderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TradeXApplication.class)
class CreateTraderUseCaseImplTest {
    @Mock
    private TraderRepository traderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private CreateTraderUseCaseImpl createTraderUseCase;

    @Test
    void createTraderTest() {
        TraderEntity traderEntity = new TraderEntity();
        traderEntity.setId(1L);
        traderEntity.setUsername("test_user");
        traderEntity.setEmail("test@example.com");
        traderEntity.setPassword("Password_123");
        traderEntity.setStatus(TraderStatus.TRADER_BASIC);

        CreateTraderRequest request = CreateTraderRequest.builder()
                .username("test_user")
                .email("test@example.com")
                .password("Password_123")
                .status(TraderStatus.TRADER_BASIC)
                .build();

        when(accessToken.getStatus()).thenReturn(TraderStatus.TRADER_PLUS);
        when(traderRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(traderRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(traderRepository.save(any(TraderEntity.class))).thenReturn(null);
        when(traderRepository.findByEmail("test@example.com")).thenReturn(traderEntity);
        when(passwordEncoder.encode(any(String.class))).thenReturn("PWOIUGHWPIOUGH");

        Long id = createTraderUseCase.createTrader(request);

        assertEquals(Long.parseLong("1"), id);
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