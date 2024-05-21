package persistence.Repositories.TraderRepository;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.TraderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = { TradeXApplication.class })
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
public class TraderRepositoryTests {

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testExistsByUsername() {
        String username = "testUser";
        String email = "test@example.com";

        TraderEntity trader = new TraderEntity();
        trader.setUsername(username);
        trader.setEmail(email);
        trader.setStatus(TraderStatus.TRADER_BASIC);
        trader.setRegisteredAt(LocalDateTime.now());
        trader.setPassword("Password(123)");

        entityManager.persistAndFlush(trader);

        assertTrue(traderRepository.existsByUsername(username));
    }

    @Test
    public void testExistsByEmail() {
        String username = "testUser";
        String email = "test@example.com";

        TraderEntity trader = new TraderEntity();
        trader.setUsername(username);
        trader.setEmail(email);
        trader.setStatus(TraderStatus.TRADER_BASIC);
        trader.setRegisteredAt(LocalDateTime.now());
        trader.setPassword("Password(123)");

        entityManager.persistAndFlush(trader);

        assertTrue(traderRepository.existsByEmail(email));
    }

    @Test
    public void testFindByEmail() {
        String username = "testUser";
        String email = "test@example.com";

        TraderEntity trader = new TraderEntity();
        trader.setUsername(username);
        trader.setEmail(email);
        trader.setStatus(TraderStatus.TRADER_BASIC);
        trader.setRegisteredAt(LocalDateTime.now());
        trader.setPassword("Password(123)");

        entityManager.persistAndFlush(trader);

        assertEquals(trader, traderRepository.findByEmail(email));
    }
}
