package Testcase;

import BaseSetup.BaseSetup;
import Dataprovider.HistoryDP;
import Page.Historypage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class HistoryTest extends BaseSetup {

    @BeforeMethod
    @Parameters({"browserType", "appURL"})
    public void setUp(String browserType, String appURL) {
        createDriver(browserType, appURL);
    }

    @Test(dataProvider = "filterData", dataProviderClass = HistoryDP.class)
    public void testFilterWithMultipleData(String symbol, String tradeType, String entryType,
                                           String profitLoss, String startDate, String endDate, boolean expectedResult) throws InterruptedException {
        Historypage filterPage = new Historypage(driver);

        if (!symbol.isEmpty()) filterPage.selectDropdownByLabel(driver, "Symbol", symbol);
        if (!tradeType.isEmpty()) filterPage.selectDropdownByLabel(driver, "Trade Type", tradeType);
        if (!entryType.isEmpty()) filterPage.selectDropdownByLabel(driver, "Entry Type", entryType);
        if (!profitLoss.isEmpty()) filterPage.selectDropdownByLabel(driver, "Profit / Loss", profitLoss);
        if (!startDate.isEmpty()) filterPage.enterStartDate(startDate);
        if (!endDate.isEmpty()) filterPage.enterEndDate(endDate);

        filterPage.clickApplyFilters();
        Thread.sleep(2000);
        boolean actualResult = filterPage.isResultDisplayedCorrectlyByColumns(symbol, tradeType, entryType, profitLoss, startDate, endDate);
        Assert.assertEquals(actualResult, expectedResult, "Kết quả lọc không đúng");
        filterPage.clickClearFilters();
    }

    @AfterMethod
    public void tearDown() {
        quitDriver();  // quit driver an toàn như trên
    }
}
