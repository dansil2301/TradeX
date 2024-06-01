package persistence.impl.tinkoff;

import Eco.TradeX.TradeXApplication;
import Eco.TradeX.persistence.Impl.CandleRepository.tinkoff.TokenManagerTinkoffImpl;
import TestConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TradeXApplication.class})
class TokenManagerTinkoffImplTest extends BaseTest{

    @Autowired
    private TokenManagerTinkoffImpl manager;

    @Test
    void readTokenLocally() {
        String token = manager.readTokenLocally();
        assertEquals("test", token);
    }
}