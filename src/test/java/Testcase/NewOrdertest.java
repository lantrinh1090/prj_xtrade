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

        if (!data.getSymbol().isEmpty()) {
            orderPage.selectDropdownByLabel(driver, "Symbol", data.getSymbol());
            waitForDropdownSettled("Symbol");
        }

        orderPage.selectMarketExecutionOption(driver, data.getOrderMode());

        if (!data.getOrderType().isEmpty()) {
            orderPage.selectDropdownByLabel(driver, "Order Type", data.getOrderType());
            waitForDropdownSettled("Order Type");
        }

        if (data.getVolume() != null) {
            waitForInputReady("Volume");
            orderPage.enterInputByLabel(driver, "Volume", data.getVolume());
        }

        // ✅ Chỉ xử lý giá khi là "Market Execution"
        if ("Pending Order".equalsIgnoreCase(data.getOrderMode())) {
            if (data.getPrice() != null) {
                waitForInputReady("Price");
                orderPage.enterInputByLabel(driver, "Price", String.valueOf(data.getPrice()));
            } else {
                waitForInputReady("Price");
                orderPage.increasePriceByArrowUp(driver);
            }
        }
        if ("Market Execution".equalsIgnoreCase(data.getOrderMode())) {
            if (data.getFillPolicy() == null) {
                orderPage.selectDropdownByLabel(driver, "Fill Policy", data.getFillPolicy());
                waitForDropdownSettled("Fill Policy");
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
            Assert.assertTrue(
                    alert.contains("Order send failed") || alert.contains("retcode"),
                    "Kỳ vọng thất bại nhưng nhận được thông báo thành công: " + alert
            );
        }

    }

    public void waitForInputReady(String labelText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        By inputLocator = null;

        // Tùy theo label để dùng XPath chính xác
        switch (labelText) {
            case "Volume":
                inputLocator = By.xpath("//label[@for='volume']/ancestor::div[contains(@class,'ant-form-item')]//input");
                break;
            case "Price":
                inputLocator = By.xpath("//label[@for='price']/ancestor::div[contains(@class,'ant-form-item')]//input");
                break;
            default:
                inputLocator = By.xpath("//label[contains(normalize-space(.),'" + labelText + "')]/following-sibling::div//input");
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));
        wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
    }


    public void waitForDropdownSettled(String labelText) {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector(".ant-select-dropdown-hidden") // dropdown đã đóng
                ));
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
