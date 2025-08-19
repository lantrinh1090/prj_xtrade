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
    public void testCreateNewOrder(OrderDataModel data) throws InterruptedException {
        NewOrderPage orderPage = new NewOrderPage(driver);
        orderPage.openmenuOrder();
        orderPage.clickResetButton();
        if (!data.getSymbol().isEmpty()) {
            orderPage.selectDropdownByLabel(driver, "Symbol", data.getSymbol());
            orderPage.waitForDropdownSettled("Symbol");
        }

        orderPage.selectMarketExecutionOption(driver, data.getOrderMode());
        if (data.getOrderType() != null) {
            orderPage.selectDropdownByLabel(driver, "Order Type", data.getOrderType());
            orderPage.waitForDropdownSettled("Order Type");
        } else {
            System.out.println("⚠️ Order Type không được cung cấp — bỏ qua chọn dropdown.");
        }

        if (data.getVolume() != null)
        {
        orderPage.waitForInputReady("Volume");
        orderPage.enterInputByLabel(driver, "Volume", data.getVolume());
        }
        else
        {
                System.out.println("⚠️ Volume không được cung cấp — bỏ qua chọn Volume.");
        }

        // ✅ Chỉ xử lý giá khi là "Market Execution"
        if ("Pending Order".equalsIgnoreCase(data.getOrderMode())) {
            if (data.getPrice() != null) {
                orderPage.waitForInputReady("Price");

                if (data.getPrice() != 0) {
                    // ✅ Nhập giá trực tiếp
                    orderPage.enterInputByLabel(driver, "Price", String.valueOf(data.getPrice()));
                } else {
                    // ✅ Price = 0, chọn tăng hoặc giảm tùy theo OrderMode
                    if (data.getOrderMode() != null && data.getOrderMode().contains("By")) {
                        orderPage.increasePriceByArrowDown(driver);
                    } else {
                        orderPage.increasePriceByArrowUp(driver);
                    }
                }
            } else {
                System.out.println("⚠️ Price không được cung cấp — bỏ qua nhập giá Price.");
            }

        }

        if ("Market Execution".equalsIgnoreCase(data.getOrderMode())) {
            if (data.getFillPolicy() != null) {
                orderPage.selectDropdownByLabel(driver, "Fill Policy", data.getFillPolicy());
                orderPage.waitForDropdownSettled("Fill Policy");
            }
            else {
                System.out.println("⚠️ Policy không được cung cấp — bỏ qua nhập giá Policy.");
            }
        }

    /*
    if (data.getFillPolicy() != null) {
        orderPage.selectDropdownByLabel(driver, "Fill Policy", data.getFillPolicy());
        waitForDropdownSettled("Fill Policy");
    }
    */

        orderPage.submitOrder();
        String alert = orderPage.getAlertMessage();

        System.out.println("🧾 Alert Message: " + alert);

        if (data.isExpectSuccess()) {
            Assert.assertNotNull(alert, "Kỳ vọng thành công nhưng không có thông báo.");
            Assert.assertTrue(alert.contains("Order created successfully"), "Kỳ vọng thành công nhưng thông báo là: " + alert);
        } else {
            Assert.assertNotNull(alert, "Kỳ vọng thất bại nhưng không có thông báo lỗi.");

            boolean isFormError = alert.contains("Please enter")
                    || alert.contains("is required")
                    || alert.contains("Please select");

            boolean isServerError = alert.contains("Order send failed")
                    || alert.contains("retcode");

            Assert.assertTrue(
                    isFormError || isServerError,
                    "Kỳ vọng thất bại nhưng nhận được thông báo không xác định hoặc thành công: " + alert
            );

    }
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
