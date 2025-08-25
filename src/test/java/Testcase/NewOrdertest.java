package Testcase;

import Base.BaseSetup;
import Model.HistoryDP;
import Model.OrderDataModel;
import Page.Loginpage;
import Page.NewOrderPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import org.apache.commons.lang3.StringUtils;

public class NewOrdertest extends BaseSetup {
    private WebDriver driver;
    private Loginpage loginpage;
    private NewOrderPage newOrderpage;

    @BeforeClass
    @Parameters({"browserType", "appURL"})
    public void setUp(String browserType, String appURL) {
        createDriver(browserType, appURL);
        driver = getDriver();

    }

    @Test(priority = 1)
    public void LoginSuccess() {
        loginpage = new Loginpage(driver);
        loginpage.login("50000200", "8uJbAf-p", "Axi-US50-Demo");
        String pageTitle = driver.getTitle();
        System.out.println("Title sau khi đăng nhập: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("XTrade"), "Đăng nhập thành công vào Dashboard");
    }

    @Test(dataProvider = "orderData", dataProviderClass = HistoryDP.class, priority = 2)
    public void testCreateNewOrder(String testCaseName, OrderDataModel data) throws InterruptedException {
        NewOrderPage orderPage = new NewOrderPage(driver);

        // Reset form trước khi nhập
        orderPage.openmenuOrder();
        orderPage.clickResetFormButton();

        // ----- Symbol -----
        if (isNotBlank(data.getSymbol())) {
            orderPage.selectDropdownByLabel(driver, "Symbol", data.getSymbol());
            orderPage.waitForDropdownSettled("Symbol");
        }

        // ----- Order Mode -----


// ----- Order Type -----
        if (StringUtils.isBlank(data.getOrderType())) {
            throw new IllegalArgumentException("❌ OrderType là required nhưng chưa được cung cấp!");
        }

        orderPage.selectDropdownByLabel(driver, "Order Type", data.getOrderType());
        orderPage.waitForDropdownSettled("Order Type");
        System.out.println("✅ Đã chọn Order Type: " + data.getOrderType());



        // ----- Volume -----
        if (data.getVolume() != null) {
            orderPage.waitForInputReady("Volume");
            orderPage.enterInputByLabel(driver, "Volume", data.getVolume());
            System.out.println("✅ Đã nhập Volume: " + data.getVolume());
        }

        // ====== Logic theo Order Mode ======
        if ("Market Execution".equalsIgnoreCase(data.getOrderMode())) {
            handleMarketExecution(orderPage, data);
        } else if ("Pending Order".equalsIgnoreCase(data.getOrderMode())) {
            handlePendingOrder(orderPage, data);
        }

        // ----- Submit + Verify -----
        orderPage.submitOrder();
        verifyAlertMessage(orderPage, data.isExpectSuccess());
    }

    /* ================== Helpers ================== */

    private void handleMarketExecution(NewOrderPage orderPage, OrderDataModel data) throws InterruptedException {
        if (isNotBlank(data.getFillPolicy())) {
            orderPage.selectDropdownByLabel(driver, "Fill Policy", data.getFillPolicy());
            orderPage.waitForDropdownSettled("Fill Policy");
            System.out.println("✅ Đã chọn Fill Policy: " + data.getFillPolicy());
        }
        System.out.println("ℹ️ Market Execution → Bỏ qua nhập Price.");
    }

    private void handlePendingOrder(NewOrderPage orderPage, OrderDataModel data) throws InterruptedException {
        Float priceToUse = data.getPrice();

        if (priceToUse == null) {
            System.out.println("⚠️ Price không được cung cấp — bỏ qua nhập giá Price.");
            return;
        }
        orderPage.waitForInputReady("Price");
        if (priceToUse > 0) {
            orderPage.enterInputByLabel(driver, "Price", String.valueOf(priceToUse));
            return;
        }
        // priceToUse == 0 → lấy từ UI
        float uiPriceAsk = orderPage.getAskPrice();
        float uiPriceBid = orderPage.getBidPrice();
        switch (data.getOrderType()) {
            case "Buy Limit":
                priceToUse = uiPriceAsk - 100;
                System.out.println("✅ Buy Limit → Ask: " + uiPriceAsk + " → Price nhập: " + priceToUse);
                break;
            case "Sell Limit":
                priceToUse = uiPriceBid + 100;
                System.out.println("✅ Sell Limit → Bid: " + uiPriceBid + " → Price nhập: " + priceToUse);
                break;
            case "Buy Stop":
            case "Buy Stop Limit":
                priceToUse = uiPriceAsk + 100;
                System.out.println("✅ " + data.getOrderType() + " → Ask: " + uiPriceAsk + " → Price nhập: " + priceToUse);
                break;
            case "Sell Stop":
            case "Sell Stop Limit":
                priceToUse = uiPriceBid - 100;
                System.out.println("✅ " + data.getOrderType() + " → Bid: " + uiPriceBid + " → Price nhập: " + priceToUse);
                break;
            default:
                System.out.println("⚠️ OrderType không xác định → giữ nguyên Bid: " + uiPriceBid);
                priceToUse = uiPriceBid;
        }

        orderPage.enterInputByLabel(driver, "Price", String.valueOf(priceToUse));
    }

    private void verifyAlertMessage(NewOrderPage orderPage, boolean expectSuccess) {
        String alert = orderPage.getAlertMessage();
        System.out.println("🧾 Alert Message: " + alert);

        Assert.assertNotNull(alert, "Không có thông báo từ hệ thống.");

        if (expectSuccess) {
            Assert.assertTrue(alert.contains("created successfully"),
                    "Kỳ vọng thành công nhưng thông báo là: " + alert);
        } else {
            boolean isFormError = alert.contains("Please enter") || alert.contains("is required") || alert.contains("Please select");
            boolean isServerError = alert.contains("Order send failed") || alert.contains("retcode");

            Assert.assertTrue(isFormError || isServerError,
                    "Kỳ vọng thất bại nhưng nhận được thông báo không xác định hoặc thành công: " + alert);
        }
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }



   /* @Test(dataProvider = "orderData")
    public void testCreateOrder(OrderDataModel data) throws InterruptedException {
        newOrderPage.selectDropdownByLabel("Symbol", data.getSymbol());
        newOrderPage.selectDropdownByLabel("Order Mode", data.getOrderMode());
        newOrderPage.selectDropdownByLabel("Order Type", data.getOrderType());
        newOrderPage.enterInputByLabel("Volume", data.getVolume());
        newOrderPage.enterInputByLabel("Price", data.getPrice());
        newOrderPage.selectDropdownByLabel("Fill Policy", data.getFillPolicy());

        newOrderPage.submitOrder();

        String alert = newOrderPage.getAlertMessage();
        if (data.isExpectSuccess()) {
            Assert.assertTrue(alert.contains("thành công"), "Kỳ vọng thành công nhưng nhận: " + alert);
        } else {
            Assert.assertTrue(alert != null && !alert.isEmpty(), "Kỳ vọng có thông báo lỗi nhưng không thấy.");
        }
    }*/

/*
    @Test( priority = 1)
    public void NewOrder() throws InterruptedException {
        newOrderpage = new NewOrderpage(driver);
        newOrderpage.Order();
        System.out.println("create Success");
    }*/

    @AfterClass
    public void tearDown() {
        quitDriver();  // quit driver an toàn như trên
    }
}
