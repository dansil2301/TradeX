package TestConfigs;

import Eco.TradeX.persistence.Impl.TraderRepository.TraderRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class BaseTest {
    @MockBean
    private TraderRepository databaseRepository;
}
