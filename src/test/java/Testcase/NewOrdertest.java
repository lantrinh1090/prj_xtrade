package Testcase;

import Base.BaseSetup;
import Model.OrderDataModel;
import Page.NewOrderPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NewOrdertest extends BaseSetup {
    private WebDriver driver;
    public NewOrderPage newOrderpage;
    @DataProvider(name = "orderData")
    public Object[][] orderData() {
        return new Object[][]{
                // symbol, orderType, volume, expectedSuccess
                { new OrderDataModel("EURUSD", "BUY_LIMIT", "10", 111.01F, 111.03F, 112.3F,true) },
                { new OrderDataModel("", "BUY", "5",  111.01F, 111.03F, 112.3F,false) },
                { new OrderDataModel("GBPUSD", "", "2",  111.01F, 111.03F, 112.3F,false) },
                { new OrderDataModel("USDJPY", "SELL", "", 111.01F, 111.03F, 112.3F, false) },
                { new OrderDataModel("EURUSD", "BUY", "0",  111.01F, 111.03F, 112.3F,false) }
        };
    }


    @BeforeClass
    public void setUp() {
        driver = getDriver();
    }


    @Test(dataProvider = "orderData")
    public void testCreateNewOrder(OrderDataModel data) throws InterruptedException {

        NewOrderPage orderPage = new NewOrderPage(driver);

        if (!data.symbol.isEmpty()) {
            orderPage.selectDropdownByLabel("Symbol", data.symbol);
            Thread.sleep(1000);
        }

        if (!data.orderType.isEmpty()) {
            orderPage.selectDropdownByLabel("Order Type", data.orderType);
            Thread.sleep(1000);
        }

        if (!data.volume.isEmpty()) {
            orderPage.enterVolume(data.volume);
            Thread.sleep(1000);
        }
        if (!(data.price==0)) {
            orderPage.Enterprice(data.price);
            Thread.sleep(1000);
        }
        if (!(data.SL==0)) {
            orderPage.EnterSL(data.SL);
            Thread.sleep(1000);
        }
        if (!(data.TP==0)) {
            orderPage.EnterTP(data.TP);
            Thread.sleep(1000);
        }

        orderPage.submitOrder();
        Thread.sleep(5000);
        if (data.expectedSuccess) {
            Assert.assertTrue(orderPage.isSuccessMessageDisplayed(), "✅ Order should be created but wasn't");
        } else {
            Assert.assertFalse(orderPage.isSuccessMessageDisplayed(), "❌ Order shouldn't be created, but it was");
        }
    }

/*
    @Test( priority = 1)
    public void NewOrder() throws InterruptedException {
        newOrderpage = new NewOrderpage(driver);
        newOrderpage.Order();
        System.out.println("create Success");
    }*/

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
