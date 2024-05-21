package Eco.TradeX.persistence.Repositories.PagesVisitedRepository;

import Eco.TradeX.domain.Statistics.StatisticsItem;
import Eco.TradeX.persistence.Entities.PagesVisitedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PagesVisitedRepository extends JpaRepository<PagesVisitedEntity, Long> {
    @Query(value = "SELECT COUNT(pv.id) as tradersVisited, DATE(pv.visited_at) as visitedAt " +
            "FROM pages_visited pv " +
            "WHERE pv.visited_at BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(pv.visited_at)", nativeQuery = true)
    List<Object[]> findStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
