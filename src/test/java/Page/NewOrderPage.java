package Page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

public class NewOrderPage {
    private WebDriver driver;
    private By title = By.xpath("//*[@id=\"root\"]/div/div/div[2]/main/div/div[2]/div[1]/div[1]/div/div/div/span");
    private By symbolcmb = By.xpath("//label[contains(text(),'" + "Symbol" + "')]/following-sibling::div//div[contains(@class,'ant-select-selector')]");
    private By orderTypecmb = By.xpath("//label[contains(text(),'" + "Order Type" + "')]/following-sibling::div//div[contains(@class,'ant-select-selector')]");
    private By volumetxt = By.xpath("//*[@id=\"root\"]/div/div/div[2]/main/div/div[2]/div[1]/div[2]/div/form/div/div[3]/div/div[2]/input");
    private By pricetxt = By.xpath("//*[@id=\"root\"]/div/div/div[2]/main/div/div[2]/div[1]/div[2]/div/form/div/div[4]/div/div[2]/input");
    private By marketchk = By.xpath("//*[@id=\"root\"]/div/div/div[2]/main/div/div[2]/div[1]/div[2]/div/form/div/label/span[1]/input");
    private By SLtxt = By.xpath("//label[text()='S/L']/following-sibling::div//input");
    private By TPtxt = By.xpath("//label[text()='T/P']/following-sibling::div//input");
    private By PlaceOrderbtn = By.xpath("//button[@type='submit' and contains(., 'Place Order')]");
    private By Ordermenu = By.xpath("//li[.//a[text()='Order']]");
    private WebDriverWait wait;
    private By priceSpan = By.cssSelector("span.font-bold.text-xl.text-red-600");

    public NewOrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private String buildOptionXpath(String optionText) {
        return "//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + optionText + "']";
    }
    public void openmenuOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));

        WebElement order = wait.until(ExpectedConditions.elementToBeClickable(Ordermenu));

        // Scroll phần tử vào giữa màn hình (tránh bị header che)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", order);

        try {
            order.click(); // Thử click bình thường
        } catch (ElementClickInterceptedException e) {
            System.out.println("⚠️ Element bị che khuất — fallback dùng JavaScript để click.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", order);
        }
    }


    public void selectDropdownByLabel(WebDriver driver, String labelText, String optionText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(300));

        try {
            // 1. Tìm label theo text
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[normalize-space(text())='" + labelText + "']")
            ));

            // 2. Đi từ label → lên 3 cấp (ant-col > ant-row > ant-form-item)
            // rồi tìm div.ant-select nằm dưới cùng cây đó
            WebElement formItem = label.findElement(By.xpath("ancestor::div[contains(@class,'ant-form-item')]"));
            WebElement dropdown = formItem.findElement(By.xpath(".//div[contains(@class,'ant-select')]"));
            // 3. Click để mở dropdown
            dropdown.click();
            try {
                Thread.sleep(3000);  // Tạm dừng 3 giây cho dropdown load xong
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 4. Chờ option xuất hiện và click
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + optionText + "']")
            ));
            option.click();

        } catch (Exception e) {
            System.out.println("❌ Không thể chọn dropdown với label: " + labelText + ". Lỗi: " + e.getMessage());
            throw e;
        }
    }


    public void enterInputByLabel(WebDriver driver, String labelText, Object value) {
        WebElement input = waitForInputReady(labelText);

        // Xóa giá trị cũ bằng CTRL+A + DELETE (hoạt động cả với type=number)
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);

        // Nhập giá trị mới (chuyển sang chuỗi)
        if (value != null) {
            input.sendKeys(String.valueOf(value));
        }
    }

    public void selectMarketExecutionOption(WebDriver driver, String optionText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Tìm radio label chứa text
        WebElement radioLabel = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='marketExecution']//label[.//span[contains(normalize-space(.),'" + optionText + "')]]")
        ));

        radioLabel.click();
    }

    public void submitOrder() throws InterruptedException {
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
             PlaceOrderbtn
        ));
        submitBtn.click();
    }
    public void clickResetFormButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        By resetButtonLocator = By.xpath("//button[.//span[text()='Reset Form']]");

        // Đợi nút hiển thị và có thể click
        wait.until(ExpectedConditions.presenceOfElementLocated(resetButtonLocator));
        wait.until(ExpectedConditions.elementToBeClickable(resetButtonLocator));

        // Click vào nút
        driver.findElement(resetButtonLocator).click();
    }

    public float getCurrentPrice() {
        String priceText = driver.findElement(priceSpan).getText().trim();
        // loại bỏ ký tự không phải số nếu có (VD: dấu , hoặc $)
        priceText = priceText.replaceAll("[^0-9.]", "");
        return Float.parseFloat(priceText);
    }
    public WebElement waitForInputReady(String labelText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        By inputLocator;
        switch (labelText) {
            case "Volume":
                inputLocator = By.xpath("//label[normalize-space(text())='Volume']/ancestor::div[contains(@class,'ant-form-item')]//input");
                break;
            case "Price":
                inputLocator = By.xpath("//input[@placeholder='Enter price (e.g. 0.01)']");
                break;
            default:
                inputLocator = By.xpath("//label[contains(normalize-space(.),'" + labelText + "')]/following-sibling::div//input");
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));
        wait.until(ExpectedConditions.elementToBeClickable(inputLocator));

        return driver.findElement(inputLocator); // ← Trả về input để tái sử dụng
    }



    public void waitForDropdownSettled(String labelText) {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector(".ant-select-dropdown-hidden") // dropdown đã đóng
                ));
    }

     public boolean isResultDisplayedCorrectlyByColumns(String symbol, String OrderType, String Volume,
                                                       String Price, String SLTP) {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));

            if (rows.isEmpty()) {
                System.out.println("❌ Không tìm thấy hàng nào trong bảng.");
                return false;
            }

            WebElement firstRow = rows.get(0);
            List<WebElement> columns = firstRow.findElements(By.tagName("td"));

            String actualSymbol = columns.get(1).getText();
            String actualOrderType = columns.get(2).getText();
            String actualVolume = columns.get(3).getText();
            String actualPrice = columns.get(4).getText();
            String actualSLTP = columns.get(5).getText();
            // Bỏ lấy actualProfitLoss và actualDate vì không dùng

            System.out.println("🔍 actualSymbol: " + actualSymbol);
            System.out.println("🔍 actualEntryType: " + actualOrderType);
            System.out.println("🔍 actualTradeType: " + actualPrice);

            // So sánh lowercase để tránh lệch hoa thường
            boolean symbolMatch = actualSymbol.toLowerCase().contains(symbol.toLowerCase());
            boolean VolumeMatch = actualVolume.toLowerCase().contains(Volume.toLowerCase());
            boolean OderTypeMatch = actualOrderType.toLowerCase().contains(OrderType.toLowerCase());

            System.out.println("✅ Symbol khớp: " + symbolMatch);
            System.out.println("✅ OderType khớp: " + OderTypeMatch);
            System.out.println("✅ Volume khớp: " + VolumeMatch);

            return symbolMatch && OderTypeMatch && VolumeMatch;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAlertMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // 1. Thử bắt message popup
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".ant-message-notice-content")
            ));
            return msg.getText();
        } catch (TimeoutException ignored) {}

        // 2. Thử bắt alert component
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".ant-alert")
            ));
            return alert.getText();
        } catch (TimeoutException ignored) {}

        // 3. Thử bắt lỗi inline từ form (ant-form-item-explain-error)
        try {
            List<WebElement> errors = driver.findElements(By.cssSelector(".ant-form-item-explain-error"));
            if (!errors.isEmpty()) {
                StringBuilder errorMessages = new StringBuilder();
                for (WebElement error : errors) {
                    String text = error.getText().trim();
                    if (!text.isEmpty()) {
                        errorMessages.append(text).append(" | ");
                    }
                }
                // Loại bỏ dấu | cuối
                if (errorMessages.length() > 0) {
                    return errorMessages.substring(0, errorMessages.length() - 3);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("ℹ Không tìm được thông báo dạng popup, alert hay inline form.");
        return null;
    }


    public void increasePriceByArrowUp(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Nút "+"
            By plusBtn = By.xpath("//button[.//span[@aria-label='plus']]");
            WebElement plusButton = wait.until(ExpectedConditions.elementToBeClickable(plusBtn));
            plusButton.click();

            // Đợi input có value mới
            By priceInputLocator = By.xpath("//input[@placeholder='Enter price (e.g. 0.01)']");
            WebElement priceInput = wait.until(ExpectedConditions.visibilityOfElementLocated(priceInputLocator));

            // Wait cho đến khi value khác rỗng
            wait.until(driver1 -> !priceInput.getDomProperty("value").isEmpty());

            String newValue = priceInput.getDomProperty("value");
            System.out.println("✅ Giá sau khi tăng: " + newValue);
        } catch (TimeoutException e) {
            System.out.println("❌ Không tìm thấy nút tăng Price hoặc input Price.");
        } catch (Exception e) {
            System.out.println("❌ Lỗi khác: " + e.getMessage());
        }
    }


    public void increasePriceByArrowDown(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Nút "-"
            By minusBtn = By.xpath("//button[.//span[@aria-label='minus']]");
            WebElement minusButton = wait.until(ExpectedConditions.elementToBeClickable(minusBtn));
            minusButton.click();

            // Input Price
            By priceInputLocator = By.xpath("//input[@placeholder='Enter price (e.g. 0.01)']");
            WebElement priceInput = wait.until(ExpectedConditions.visibilityOfElementLocated(priceInputLocator));

            // Chờ value thay đổi (hoặc ít nhất không rỗng)
            wait.until(d -> !priceInput.getDomProperty("value").isEmpty());

            String newValue = priceInput.getDomProperty("value");
            System.out.println("✅ Giá sau khi giảm: " + newValue);

        } catch (TimeoutException e) {
            System.out.println("❌ Không tìm thấy nút giảm Price hoặc input Price.");
        } catch (Exception e) {
            System.out.println("❌ Lỗi khác: " + e.getMessage());
        }
    }


    private boolean isWithinDateRange(String actualTime, String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime actual = LocalDateTime.parse(actualTime, formatter);
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return !actual.toLocalDate().isBefore(start) && !actual.toLocalDate().isAfter(end);
        } catch (Exception e) {
            return false;
        }
    }

}
