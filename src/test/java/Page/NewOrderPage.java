package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
    private By PlaceOrderbtn = By.xpath("//*[@id=\"root\"]/div/div/div[2]/main/div/div[2]/div[1]/div[2]/div/form/div/a");
    private WebDriverWait wait;

    public NewOrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private String buildOptionXpath(String optionText) {
        return "//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + optionText + "']";
    }

    public void selectDropdownByLabel(String labelText, String optionText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(normalize-space(),'" + labelText + "')]/following-sibling::div//div[contains(@class,'ant-select-selector')]")
        ));
        dropdown.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(buildOptionXpath(optionText))
        ));
        option.click();
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
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.cssSelector(".ant-message"), "Order created successfully"
            ));
        } catch (TimeoutException e) {
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





/*
    public void Selectsymbol(String symbol) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // 1. Click để mở dropdown
        WebElement dropdownTrigger = wait.until(ExpectedConditions.elementToBeClickable(symbolcmb));
        dropdownTrigger.click();
        // 2. Đợi dropdown xuất hiện và tìm option
        String xpath = "//div[contains(@class, 'ant-select-item-option-content') and text()='" + symbol + "']";
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(xpath)
        ));
        // 3. Click để chọn
        option.click();
    }

    public void Selectordertype(String type) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // 1. Mở dropdown theo label
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(orderTypecmb));
        dropdown.click();
        // 2. Đợi option và chọn
        String xpath = "//div[contains(@class,'ant-select-item-option-content') and normalize-space(text())='" + type + "']";
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        option.click();
    }

    public void Entervolume(float volume) {
        WebElement element = driver.findElement(volumetxt);
        element.clear();
        element.sendKeys(Float.toString(volume));
    }

    public void Enterpice(float pice) {
        WebElement element = driver.findElement(picetxt);
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

    public void Order() throws InterruptedException {
        Selectsymbol("EURUSD");
        Thread.sleep(1000);
        Selectordertype("BUY_LIMIT");
        Thread.sleep(1000);
        Entervolume(0.01F);
        Thread.sleep(1000);
        Enterpice(0.01F);
        Thread.sleep(1000);
        CheckMarket();
        Thread.sleep(1000);
        EnterSL(999.36F);
        Thread.sleep(1000);
        EnterTP(9999);
        Thread.sleep(1000);
    }*/

}
