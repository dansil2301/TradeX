package business.utils.TraderUtils;

import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.business.utils.TraderUtils.TraderConverter;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.domain.Trader.TraderStatus;
import Eco.TradeX.persistence.Entities.TraderEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraderConverterTest {
    @Test
    public void testConvertToTraderData() {
        TraderEntity traderEntity = new TraderEntity();
        traderEntity.setId(1L);
        traderEntity.setUsername("test_user");
        traderEntity.setEmail("test@example.com");
        traderEntity.setPassword("password123");
        traderEntity.setStatus(TraderStatus.TRADER_BASIC);

        TraderConverter converter = new TraderConverter();
        TraderData traderData = converter.convertToTraderData(Optional.of(traderEntity));

        assertEquals(traderEntity.getId(), traderData.getId());
        assertEquals(traderEntity.getUsername(), traderData.getUsername());
        assertEquals(traderEntity.getEmail(), traderData.getEmail());
        assertEquals(traderEntity.getPassword(), traderData.getPassword());
        assertEquals(traderEntity.getStatus(), traderData.getStatus());
    }

    @Test
    public void testConvertToTraderData_WithEmptyOptional() {
        TraderConverter converter = new TraderConverter();
        Optional<TraderEntity> emptyOptional = Optional.empty();

        assertThrows(TraderExceptions.class, () -> converter.convertToTraderData(emptyOptional));
    }

    @Test
    public void testConvertToTraderData_List() {
        List<TraderEntity> entities = new ArrayList<>();
        TraderEntity traderEntity1 = new TraderEntity();
        traderEntity1.setId(1L);
        traderEntity1.setUsername("user1");
        traderEntity1.setEmail("user1@example.com");
        traderEntity1.setPassword("pass123");
        traderEntity1.setStatus(TraderStatus.TRADER_BASIC);

        TraderEntity traderEntity2 = new TraderEntity();
        traderEntity2.setId(2L);
        traderEntity2.setUsername("user2");
        traderEntity2.setEmail("user2@example.com");
        traderEntity2.setPassword("pass456");
        traderEntity2.setStatus(TraderStatus.TRADER_PLUS);

        entities.add(traderEntity1);
        entities.add(traderEntity2);

        TraderConverter converter = new TraderConverter();
        List<TraderData> tradersData = converter.convertToTraderData(entities);

        assertEquals(2, tradersData.size());

        assertEquals(traderEntity1.getId(), tradersData.get(0).getId());
        assertEquals(traderEntity1.getUsername(), tradersData.get(0).getUsername());
        assertEquals(traderEntity1.getEmail(), tradersData.get(0).getEmail());
        assertEquals(traderEntity1.getPassword(), tradersData.get(0).getPassword());
        assertEquals(traderEntity1.getStatus(), tradersData.get(0).getStatus());

        assertEquals(traderEntity2.getId(), tradersData.get(1).getId());
        assertEquals(traderEntity2.getUsername(), tradersData.get(1).getUsername());
        assertEquals(traderEntity2.getEmail(), tradersData.get(1).getEmail());
        assertEquals(traderEntity2.getPassword(), tradersData.get(1).getPassword());
        assertEquals(traderEntity2.getStatus(), tradersData.get(1).getStatus());
    }
}