package Page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Historypage {

    WebDriver driver;

    public Historypage(WebDriver driver) {
        this.driver = driver;
    }

    // Select dropdowns

        public void selectDropdownByLabel(WebDriver driver, String labelText, String optionText) {
            // Tìm label có text đúng labelText
            WebElement label = driver.findElement(By.xpath("//label[text()='" + labelText + "']"));

            // Tìm div dropdown là em/anh/em út của label, ví dụ following-sibling
            WebElement dropdown = label.findElement(By.xpath("following-sibling::div[contains(@class,'ant-select')]"));

            // Click mở dropdown
            dropdown.click();

            // Chờ dropdown options hiện ra, chọn option theo optionText
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'ant-select-item-option-content') and text()='" + optionText + "']")
            ));

            option.click();
    }



    public void enterStartDate(String startDate) {
        driver.findElement(By.xpath("//input[@placeholder='Select date']")).sendKeys(startDate);
    }

    public void enterEndDate(String endDate) {
        driver.findElements(By.xpath("//input[@placeholder='Select date']")).get(1).sendKeys(endDate);
    }

    public void clickApplyFilters() {
        driver.findElement(By.xpath("//span[text()='Apply Filters']")).click();
    }

    public void clickClearFilters() {
        driver.findElement(By.xpath("//span[text()='Clear Filters']")).click();

    }
    public boolean isResultDisplayedCorrectlyByColumns(String symbol, String tradeType, String entryType,
                                                       String profitLoss, String startDate, String endDate) {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));

            if (rows.isEmpty()) {
                System.out.println("❌ Không tìm thấy hàng nào trong bảng.");
                return false;
            }

            WebElement firstRow = rows.get(0);
            List<WebElement> columns = firstRow.findElements(By.tagName("td"));

            String actualSymbol = columns.get(1).getText();
            String actualEntryType = columns.get(2).getText();
            String actualTradeType = columns.get(3).getText();
            // Bỏ lấy actualProfitLoss và actualDate vì không dùng

            System.out.println("🔍 actualSymbol: " + actualSymbol);
            System.out.println("🔍 actualEntryType: " + actualEntryType);
            System.out.println("🔍 actualTradeType: " + actualTradeType);

            // So sánh lowercase để tránh lệch hoa thường
            boolean symbolMatch = actualSymbol.toLowerCase().contains(symbol.toLowerCase());
            boolean tradeTypeMatch = actualTradeType.toLowerCase().contains(tradeType.toLowerCase());
            boolean entryTypeMatch = actualEntryType.toLowerCase().contains(entryType.toLowerCase());

            System.out.println("✅ Symbol khớp: " + symbolMatch);
            System.out.println("✅ TradeType khớp: " + tradeTypeMatch);
            System.out.println("✅ EntryType khớp: " + entryTypeMatch);

            return symbolMatch && tradeTypeMatch && entryTypeMatch;

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
