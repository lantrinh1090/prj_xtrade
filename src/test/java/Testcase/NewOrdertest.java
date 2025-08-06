package Testcase;

import Base.BaseSetup;
import Model.HistoryDP;
import Model.OrderDataModel;
import Page.NewOrderPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class NewOrdertest extends BaseSetup {
 //   private WebDriver driver;
  //  public NewOrderPage newOrderpage;

    @BeforeMethod
    @Parameters({"browserType", "appURL"})
    public void setUp(String browserType, String appURL) {
        createDriver(browserType, appURL);
    }
    @Test(dataProvider = "orderData", dataProviderClass = HistoryDP.class)
    public void testCreateNewOrder(OrderDataModel data) throws InterruptedException {

        NewOrderPage orderPage = new NewOrderPage(driver);

        if (!data.symbol.isEmpty()) {
            orderPage.selectDropdownByLabel(driver,"Symbol", data.symbol);
            Thread.sleep(1000);
        }

        if (!data.orderType.isEmpty()) {
            orderPage.selectDropdownByLabel(driver,"Order Type", data.orderType);
            Thread.sleep(1000);
        }

        if (!data.volume.isEmpty()) {
            orderPage.enterInputByLabel(driver,"Volume",data.volume);
            Thread.sleep(1000);
        }
        if (!(data.price==0)) {
            orderPage.enterInputByLabel(driver,"Price", String.valueOf(data.price));
            Thread.sleep(1000);
        }
        if (!(data.SL==0)) {
            orderPage.enterInputByLabel(driver,"S/L", String.valueOf(data.SL));
            Thread.sleep(1000);
        }
        if (!(data.TP==0)) {
            orderPage.enterInputByLabel(driver,"T/P", String.valueOf(data.TP));
            Thread.sleep(1000);
        }

        orderPage.submitOrder();
        //Thread.sleep(5000);
        Thread.sleep(2000);
        boolean actualResult = orderPage.isResultDisplayedCorrectlyByColumns(data.symbol, data.orderType, data.volume, String.valueOf(data.price), String.valueOf(data.SL));
        Assert.assertTrue(actualResult, "Kết quả ko đúng");

    }

/*
    @Test( priority = 1)
    public void NewOrder() throws InterruptedException {
        newOrderpage = new NewOrderpage(driver);
        newOrderpage.Order();
        System.out.println("create Success");
    }*/

    @AfterMethod
    public void tearDown() {
        quitDriver();  // quit driver an toàn như trên
    }
}
