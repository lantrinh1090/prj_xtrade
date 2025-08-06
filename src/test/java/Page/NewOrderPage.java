package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    private WebDriverWait wait;

    public NewOrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private String buildOptionXpath(String optionText) {
        return "//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + optionText + "']";
    }

    public void selectDropdownByLabel(WebDriver driver, String labelText, String optionText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1. Tìm label
            WebElement label = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(normalize-space(.), '" + labelText + "')]")
            ));

            // 2. Tìm dropdown phía dưới label
            WebElement dropdown = label.findElement(By.xpath("following-sibling::div[contains(@class,'ant-select')]"));

            // 3. Click để mở dropdown
            dropdown.click();

            // 4. Tìm input (nếu có thể nhập được)
            List<WebElement> inputList = dropdown.findElements(By.xpath(".//input[contains(@class,'ant-select-selection-search-input')]"));

            if (!inputList.isEmpty()) {
                WebElement inputBox = inputList.get(0);
                String readonly = inputBox.getAttribute("readonly");

                if (readonly == null || readonly.isEmpty()) {
                    inputBox.sendKeys(optionText);  // chỉ nhập nếu không readonly
                }
            }

            // 5. Chờ và click vào option
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                 //   By.xpath("//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + optionText + "']")
                    By.xpath("//div[contains(@class,'ant-select-item-option-content') and starts-with(normalize-space(text()), '" + optionText + "')]")

            ));
            option.click();

        } catch (Exception e) {
            System.out.println("❌ Không thể chọn dropdown với label: " + labelText + ". Lỗi: " + e.getMessage());
            throw e;
        }
    }
    public void enterInputByLabel(WebDriver driver, String labelText, String value) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(normalize-space(.),'" + labelText + "')]/following-sibling::div//input")
            ));

            input.clear();
            input.sendKeys(value);
        } catch (Exception e) {
            System.out.println("❌ Không thể nhập giá trị cho label: " + labelText + ". Lỗi: " + e.getMessage());
            throw e;
        }
    }



    public void enterVolume(String volume) {
        WebElement volumeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".ant-input-number input")
        ));
        volumeInput.clear();
        volumeInput.sendKeys(volume);
    }
    public void Enterprice(float pice) {
        WebElement element = driver.findElement(pricetxt);
        element.clear();
        element.sendKeys(Float.toString(pice));
    }

    public void EnterSL(float SL) {
        WebElement element = driver.findElement(SLtxt);
        element.clear();
        element.sendKeys(Float.toString(SL));
    }

    public void EnterTP(float TP) {
        WebElement element = driver.findElement(TPtxt);
        element.clear();
        element.sendKeys(Float.toString(TP));
    }

    public void CheckMarket() {
        WebElement market = driver.findElement(marketchk);
        market.click();
    }

    public void submitOrder() throws InterruptedException {
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
             PlaceOrderbtn
        ));
        submitBtn.click();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".ant-message-success .ant-message-notice-content")
            ));
            System.out.println("Thông báo thành công: " + successMsg.getText());
            return successMsg.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Không thấy thông báo thành công.");
            return false;
        }
    }


    public boolean isValidationErrorDisplayed(String fieldName) {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[contains(normalize-space(),'" + fieldName + "')]/following-sibling::div//div[contains(@class,'ant-form-item-explain-error')]")
            ));
            return error.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isSuccessAlertDisplayed() {
        try {
            WebElement alert = driver.findElement(By.cssSelector(".ant-alert-success, .ant-empty-description"));
            return alert.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
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
