package Eco.TradeX.business.utils.StatisticsUtils;

import Eco.TradeX.domain.Statistics.Pages;
import Eco.TradeX.persistence.Entities.PagesEntity;

import java.util.ArrayList;
import java.util.List;

public class PagesConverter {
    public static List<Pages> covertToPages(List<PagesEntity> pagesEntities) {
        List<Pages> pages = new ArrayList<>();

        for (PagesEntity pagesEntity : pagesEntities) {
            pages.add(convert(pagesEntity));
        }

        return pages;
    }

    private static Pages convert(PagesEntity pagesEntity) {
        return pagesEntity.getPageName();
    }
}
