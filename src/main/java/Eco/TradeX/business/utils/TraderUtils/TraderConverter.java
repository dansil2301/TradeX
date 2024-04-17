package Eco.TradeX.business.utils.TraderUtils;

import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.persistence.Entities.TraderEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TraderConverter {
    private TraderData convert(TraderEntity traderEntity) {
        return TraderData.builder()
                .id(traderEntity.getId())
                .username(traderEntity.getUsername())
                .email(traderEntity.getEmail())
                .password(traderEntity.getPassword())
                .status(traderEntity.getStatus())
                .createdAt(traderEntity.getRegisteredAt())
                .build();
    }

    public TraderData convertToTraderData(Optional<TraderEntity> entity) {
        if (entity.isPresent()) {
            var traderEntity = entity.get();
            TraderData traderData = convert(traderEntity);
            return traderData;
        } else {
            throw new TraderExceptions("couldn't find user with this id");
        }
    }

    public List<TraderData> convertToTraderData(List<TraderEntity> entities) {
        List<TraderData> tradersData = new ArrayList<>();
        for (var entity : entities) {
            tradersData.add(convert(entity));
        }
        return tradersData;
    }
}
