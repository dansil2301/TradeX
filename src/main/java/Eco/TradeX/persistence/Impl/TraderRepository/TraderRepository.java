package Eco.TradeX.persistence.Impl.TraderRepository;

import Eco.TradeX.persistence.Entities.TraderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepository extends JpaRepository<TraderEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    TraderEntity findByEmail(String email);
}
