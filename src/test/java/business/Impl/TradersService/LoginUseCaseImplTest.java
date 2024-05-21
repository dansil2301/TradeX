package business.Impl.TradersService;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.business.Impl.TradersService.LoginUseCaseImpl;
import Eco.TradeX.business.exceptions.InvalidCredentialsException;
import Eco.TradeX.configuration.security.token.AccessTokenEncoder;
import Eco.TradeX.domain.Trader.LoginRequest;
import Eco.TradeX.domain.Trader.LoginResponse;
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
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeXApplication.class)
class LoginUseCaseImplTest {

    @Mock
    private TraderRepository traderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    public void testLogin_Successful() {
        LoginRequest loginRequest = new LoginRequest("valid@example.com", "valid_password");
        TraderEntity trader = new TraderEntity();
        trader.setEmail("valid@example.com");
        trader.setPassword("encoded_password");
        trader.setId(1L);
        trader.setStatus(TraderStatus.TRADER_BASIC);

        when(traderRepository.findByEmail(loginRequest.getEmail())).thenReturn(trader);

        when(passwordEncoder.matches(loginRequest.getPassword(), trader.getPassword())).thenReturn(true);

        String expectedAccessToken = "mocked_access_token";
        when(accessTokenEncoder.encode(any())).thenReturn(expectedAccessToken);

        LoginResponse response = loginUseCase.login(loginRequest);

        assertNotNull(response);
        assertEquals(expectedAccessToken, response.getAccessToken());
    }

    @Test
    public void testLogin_InvalidEmail() {
        LoginRequest loginRequest = new LoginRequest("invalid@example.com", "password");

        when(traderRepository.findByEmail(loginRequest.getEmail())).thenReturn(null);

        try {
            loginUseCase.login(loginRequest);
            fail("Expected InvalidCredentialsException was not thrown");
        } catch (InvalidCredentialsException e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void testLogin_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("valid@example.com", "invalid_password");
        TraderEntity trader = new TraderEntity();
        trader.setEmail("valid@example.com");
        trader.setPassword("encoded_password"); // Encoded password

        when(traderRepository.findByEmail(loginRequest.getEmail())).thenReturn(trader);
        when(passwordEncoder.matches(loginRequest.getPassword(), trader.getPassword())).thenReturn(false);

        try {
            loginUseCase.login(loginRequest);
            fail("Expected InvalidCredentialsException was not thrown");
        } catch (InvalidCredentialsException e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
}