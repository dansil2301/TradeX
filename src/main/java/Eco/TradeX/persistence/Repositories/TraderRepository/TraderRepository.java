package Eco.TradeX.persistence.Repositories.TraderRepository;

import Eco.TradeX.persistence.Entities.TraderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepository extends JpaRepository<TraderEntity, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    TraderEntity findByEmail(String email);

    Page<TraderEntity> findAllByOrderByUsernameAsc(Pageable pageable);

    @Query("SELECT t FROM Eco.TradeX.persistence.Entities.TraderEntity t " +
            "WHERE LOWER(t.username) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(t.email) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    Page<TraderEntity> searchTraderEntityBy(
            @Param("searchString") String searchString,
            Pageable pageable
    );
}
