package Eco.TradeX.persistence.Interfaces.TraderRepositoryInterfaces;

import Eco.TradeX.persistence.impl.TraderRepository.TraderEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraderRepository extends JpaRepository<TraderEntity, Long> {
}
