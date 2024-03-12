package persistence.impl.tinkoff;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.persistence.impl.CandleRepository.tinkoff.TokenManagerTinkoffImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradeXApplication.class)
class TokenManagerTinkoffImplTest {
    @Autowired
    private TokenManagerTinkoffImpl manager;

    @Test
    void readTokenLocally() {
        String token = manager.readTokenLocally();
        assertEquals("t.AVH_IIYgRqrERjuodLPcba0eCh82iUCRcGeKpCeVnG-ea9kmUpxRRrj9AtK6_pu41bj11cF90OGScfA2LI7K0Q", token);
    }
}