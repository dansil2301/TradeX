package Eco.TradeX.persistence.Repositories.TraderRepository;

import Eco.TradeX.persistence.Entities.TraderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepository extends JpaRepository<TraderEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    TraderEntity findByEmail(String email);
    Page<TraderEntity> findAllByOrderByUsernameAsc(Pageable pageable);
}
