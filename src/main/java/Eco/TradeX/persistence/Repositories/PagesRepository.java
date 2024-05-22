package Eco.TradeX.persistence.Repositories;

import Eco.TradeX.domain.Statistics.Pages;
import Eco.TradeX.persistence.Entities.PagesEntity;
import Eco.TradeX.persistence.Entities.PagesVisitedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagesRepository extends JpaRepository<PagesEntity, Long> {
    PagesEntity findAllById(Long id);
    Optional<PagesEntity> findByPageName(Pages pageName);
}
