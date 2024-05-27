package Eco.TradeX.business.Impl.StatisticsService;

import Eco.TradeX.business.Interfaces.StatisticsServiceInterfaces.CreateStatisticsUseCase;
import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.GetTradersMethodsUseCase;
import Eco.TradeX.domain.Requests.CreatePageVisitRequest;
import Eco.TradeX.domain.Requests.CreateTraderRequest;
import Eco.TradeX.persistence.Entities.PagesEntity;
import Eco.TradeX.persistence.Entities.PagesVisitedEntity;
import Eco.TradeX.persistence.Entities.TraderEntity;
import Eco.TradeX.persistence.Repositories.PagesRepository;
import Eco.TradeX.persistence.Repositories.PagesVisitedRepository;
import Eco.TradeX.persistence.Repositories.TraderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateStatisticsUseCaseImpl implements CreateStatisticsUseCase {
    private final PagesVisitedRepository pagesVisitedRepository;
    private final TraderRepository traderRepository;
    private final PagesRepository pagesRepository;
    @Override
    public void createPageVisit(CreatePageVisitRequest request) {
        validateRequest(request);
        savePageVisited(request);
    }

    private void validateRequest(CreatePageVisitRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (request.getPageName() == null) {
            throw new IllegalArgumentException("Page name cannot be null");
        }
    }

    private void savePageVisited(CreatePageVisitRequest request) {
        Optional<TraderEntity> userOptional = traderRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + request.getUserId());
        }

        Optional<PagesEntity> pageOptional = pagesRepository.findByPageName(request.getPageName());
        if (pageOptional.isEmpty()) {
            throw new IllegalArgumentException("Page not found with name: " + request.getPageName());
        }

        PagesVisitedEntity pagesVisited = PagesVisitedEntity.builder()
                .user(userOptional.get())
                .page(pageOptional.get())
                .visitedAt(new Date())
                .build();

        LocalDate visitedAtDate = pagesVisited.getVisitedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (pagesVisitedRepository.countPageVisitsByUserAndPageAndDate(pagesVisited.getUser().getId(), pagesVisited.getPage().getId(), visitedAtDate) == 0)
        { pagesVisitedRepository.save(pagesVisited); }
    }
}
