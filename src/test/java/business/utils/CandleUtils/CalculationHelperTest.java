package business.utils.CandleUtils;

import Eco.TradeX.TradeXApplication;
import TestConfigs.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static Eco.TradeX.business.utils.CandleUtils.CalculationHelper.calculateAverage;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TradeXApplication.class)
class CalculationHelperTest extends BaseTest {

    @Test
    void calculateAverageTestNormalSituationOnly1() {
        List<BigDecimal> avrTestLst = new ArrayList<>();
        avrTestLst.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));
        avrTestLst.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));
        avrTestLst.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));

        var actualResult = calculateAverage(avrTestLst, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP), actualResult);
    }

    @Test
    void calculateAverageTestNormalSituation123() {
        List<BigDecimal> avrTestLst = new ArrayList<>();
        avrTestLst.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));
        avrTestLst.add(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP));
        avrTestLst.add(new BigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP));

        var actualResult = calculateAverage(avrTestLst, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP), actualResult);
    }

    @Test
    void calculateAverageTestNormalSituationPointNumber179() {
        List<BigDecimal> avrTestLst = new ArrayList<>();
        avrTestLst.add(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP));
        avrTestLst.add(new BigDecimal(7).setScale(2, BigDecimal.ROUND_HALF_UP));
        avrTestLst.add(new BigDecimal(9).setScale(2, BigDecimal.ROUND_HALF_UP));

        var actualResult = calculateAverage(avrTestLst, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal(5.67).setScale(2, BigDecimal.ROUND_HALF_UP), actualResult);
    }
}