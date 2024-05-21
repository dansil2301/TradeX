package Eco.TradeX.persistence.Repositories.PagesRepository;

import Eco.TradeX.domain.Statistics.Pages;
import Eco.TradeX.persistence.Entities.PagesEntity;
import Eco.TradeX.persistence.Entities.PagesVisitedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagesRepository extends JpaRepository<PagesEntity, Long> {
}
