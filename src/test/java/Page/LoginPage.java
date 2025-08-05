package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    private By headerPageText = By.xpath("//a[normalize-space()='Forgot Username/Password?']");
    private By emailInput = By.name("email");
    private By passwordInput = By.name("password");
    private By recaptchaFrame = By.cssSelector("iframe[title='reCAPTCHA']");
    private By captchabtn = By.cssSelector("div.recaptcha-checkbox-border");
    private By signinBtn = By.xpath("//button[contains(text(),'login')]");
    private By errorMsgText = By.xpath("//*[@id=\"style-7\"]/section[2]/div/div/div/div[1]");
    private By pinInput = By.id("Pin");
    private By submitBtn = By.id("RequestPinForm_SubmitButton");
    private By backBtn = By.id("RequestPinForm_Back");
    private By resetPintBtn = By.id("RequestPinForm_ResetPin");

    // Khởi tạo class khi được gọi và truyền driver vào để các thành phần trong
    // class này đọc
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getSignInPageTitle() {
        String pageTitle = driver.getTitle();
        return pageTitle;
    }

    public boolean verifySignInPageTitle() {
        String expectedTitle = "Login | Anh Tester Blog";
        return getSignInPageTitle().equals(expectedTitle);
    }

    public boolean verifySignInPageText() {
        WebElement element = driver.findElement(headerPageText);
        String pageText = element.getText();
        String expectedPageText = "Forgot Username/Password?";
        return pageText.contains(expectedPageText);
    }

    // Sau khi thực hiện click Submit thì khởi tạo trang DashboardPage
    public void login(String username, String password, String Pin) throws Exception {
        enterEmail(username);
        Thread.sleep(1000);
        enterPassword(password);
        Thread.sleep(1000);
        clickcaptcha();
        System.out.println("Chờ bạn xử lý CAPTCHA...");
        System.in.read();
        Thread.sleep(30000);
        clickSignIn();
        Thread.sleep(1000);
        enterPin(Pin);
        clickSubmit();
    }

    public boolean verifySignIn() {
        enterEmail("test");
        enterPassword("pass");
        clickSignIn();
        return getErrorMessage().contains("incorrect");
    }

    public void enterEmail(String email)  {
        WebElement emailTxtBox = driver.findElement(emailInput);
        if (emailTxtBox.isDisplayed())
            emailTxtBox.sendKeys(email);

    }

    public void enterPassword(String password) {
        WebElement passwordTxtBox = driver.findElement(passwordInput);
        if (passwordTxtBox.isDisplayed())
            passwordTxtBox.sendKeys(password);


    }


    public void clickcaptcha() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Chuyển vào iframe chứa reCAPTCHA
        driver.switchTo().frame(driver.findElement(recaptchaFrame));

        // Chờ checkbox có thể click và click vào
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(captchabtn));
        checkbox.click(); // bắt buộc phải click để CAPTCHA hiển thị

        // Chờ CAPTCHA được tick
        wait.until(driver1 -> {
            WebElement freshCheckbox = driver1.findElement(captchabtn); // cập nhật lại
            String checked = freshCheckbox.getAttribute("aria-checked");
            System.out.println("CAPTCHA aria-checked = " + checked); // debug
            return "true".equals(checked);
        });

        // Trở lại frame chính
        driver.switchTo().defaultContent();
    }

    public void clickSignIn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        loginButton.click();
        System.out.println("Đã nhấn login.");

    }
    public void clickSubmit() {
      //  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      //  WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
     //   submit.click();
    }

    public void enterPin(String PIN) {
        driver.findElement(pinInput).sendKeys(PIN);
    }

    public void clickBack() {
        driver.findElement(backBtn).click();
    }

    public void clickResetPin() {
        driver.findElement(resetPintBtn).click();
    }

    public String getErrorMessage() {
        String strErrorMsg = null;
        WebElement errorMsg = driver.findElement(errorMsgText);
        if (errorMsg.isDisplayed() && errorMsg.isEnabled())
            strErrorMsg = errorMsg.getText();
        return strErrorMsg;
    }

    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                        .equals("complete");
            }
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait( driver, Duration.ofSeconds(30));
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }
}
