package TestConfigs;

import Eco.TradeX.persistence.Repositories.TraderRepository;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;

public class BaseTest {
    @MockBean
    private TraderRepository databaseRepository;
}
