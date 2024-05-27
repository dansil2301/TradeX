package Eco.TradeX.persistence.Repositories;

import Eco.TradeX.domain.Statistics.StatisticsItem;
import Eco.TradeX.persistence.Entities.PagesVisitedEntity;
import Eco.TradeX.persistence.Entities.TraderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface PagesVisitedRepository extends JpaRepository<PagesVisitedEntity, Long> {
    @Query(value = "SELECT COUNT(pv.id) as tradersVisited, DATE(pv.visited_at) as visitedAt " +
            "FROM pages_visited pv " +
            "WHERE pv.visited_at BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(pv.visited_at)", nativeQuery = true)
    List<Object[]> findStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT COUNT(pv.id) as tradersVisited, DATE(pv.visited_at) as visitedAt " +
            "FROM pages_visited pv " +
            "JOIN pages p ON pv.page_id = p.id " +
            "WHERE pv.visited_at BETWEEN :startDate AND :endDate " +
            "AND p.page_name = :pageName " +
            "GROUP BY DATE(pv.visited_at)", nativeQuery = true)
    List<Object[]> findStatistics(@Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate,
                                  @Param("pageName") String pageName);

    @Query(value = "SELECT COUNT(pv.id) " +
            "FROM pages_visited pv " +
            "JOIN pages p ON pv.page_id = p.id " +
            "WHERE pv.user_id = :userId " +
            "AND pv.page_id = :pageId " +
            "AND DATE(pv.visited_at) = :visitedAt", nativeQuery = true)
    Long countPageVisitsByUserAndPageAndDate(@Param("userId") Long userId,
                                             @Param("pageId") Long pageId,
                                             @Param("visitedAt") LocalDate visitedAt);
}
