package Page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Loginpage {
    private WebDriver driver;
    private By usernameinput =By.id("login_username");
    private By passwordinput= By.id("login_password");
    private By Loginbtn =By.xpath("//button[normalize-space()='Sign in']");
    private By serverinput =By.xpath("//input[@id='login_server']");
    private By errormsgText=By.xpath( "/html/body/div[1]/div[2]/div[1]/form/div[5]");
    private WebDriverWait wait;


    // Constructor
    public Loginpage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void enterUsername(String username) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameinput));

        // Cách 1: clear() thông thường
        usernameInput.clear();

        // Cách 2: đảm bảo sạch bằng Ctrl + A và Delete
        usernameInput.sendKeys(Keys.CONTROL, "a");
        usernameInput.sendKeys(Keys.DELETE);

        // Nhập giá trị mới
        usernameInput.sendKeys(username);
    }


    public void enterPassword(String password) {
        WebElement passInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordinput));
        passInput.clear();
        passInput.sendKeys(Keys.CONTROL, "a");
        passInput.sendKeys(Keys.DELETE);
        passInput.sendKeys(password);
    }

    public void enterServer(String server) {
        WebElement serverInput = wait.until(ExpectedConditions.visibilityOfElementLocated(serverinput));
        serverInput.clear();
        serverInput.sendKeys(Keys.CONTROL, "a");
        serverInput.sendKeys(Keys.DELETE);
        serverInput.sendKeys(server);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(Loginbtn)).click();
    }

    public void login(String username, String password, String server) {
        enterUsername(username);
        enterPassword(password);
        enterServer(server);
        clickLogin();
    }

    public String getErrorMessage() {
        // 1. lấy alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept(); // đóng alert
        return alertText.trim();
    }

    // Lấy message lỗi cho một field bất kỳ
    public String getFieldErrorMsg(String fieldId) {
        try {
            WebElement errormsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@id='" + fieldId + "']//div[contains(@class,'ant-form-item-explain-error')]")
            ));
            return errormsg.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }


    // Hàm riêng cho username (nếu vẫn muốn dùng)
    public String getErrormsgusername() {
        return getFieldErrorMsg("login_username_help");
    }
    public String getErrormsgpassword() {
        return getFieldErrorMsg("login_password_help");
    }
    public String getErrormsgserver() {
        return getFieldErrorMsg("login_server_help");
    }

}
