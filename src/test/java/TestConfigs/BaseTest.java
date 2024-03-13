package TestConfigs;

import Eco.TradeX.business.Impl.TradersService.CreateTraderUseCaseImpl;
import Eco.TradeX.persistence.Interfaces.TraderRepositoryInterfaces.TraderRepository;
import Eco.TradeX.persistence.impl.TraderRepository.TraderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BaseTest {
    @MockBean
    private TraderRepository databaseRepository;
}
