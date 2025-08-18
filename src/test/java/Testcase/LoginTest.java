package Testcase;

import Base.BaseSetup;
import Page.Loginpage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTest extends BaseSetup {
    private WebDriver driver;
    private Loginpage loginpage;

    @BeforeMethod
    @Parameters({"browserType", "appURL"})
    public void setUp(String browserType, String appURL) {
        createDriver(browserType, appURL);
        driver = getDriver();
        loginpage = new Loginpage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    // Đăng nhập thành công
    @Test(priority = 1)
    public void LoginSuccess() {
        loginpage.login("50000200", "8uJbAf-p", "Axi-US50-Demo");
        String pageTitle = driver.getTitle();
        System.out.println("Title sau khi đăng nhập: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("XTrade"), "Đăng nhập thành công vào Dashboard");
    }

    // Sai Username
    @Test(priority = 2)
    public void LoginWrongUsername() {
        loginpage.login("5000020001", "8uJbAf-p", "Axi-US50-Demo");
        String error = loginpage.getErrorMessage();
        System.out.println("Error: " + error);
        Assert.assertTrue(error.contains("MT5 initialization failed: MT5 initialization failed: (-6, 'Terminal: Authorization failed')"), "Sai username nhưng không hiện thông báo lỗi");

    }

    // Sai Password
    @Test(priority = 3)
    public void LoginWrongPassword() {
        loginpage.login("50000200", "wrongpass", "Axi-US50-Demo");
        String error = loginpage.getErrorMessage();
        System.out.println("Error: " + error);
        Assert.assertTrue(error.contains("Invalid"), "Sai password nhưng thông báo lỗi không đúng");
    }

    // Sai Server
    @Test(priority = 4)
    public void LoginWrongServer() {
        loginpage.login("50000200", "8uJbAf-p", "Axi-US50-Demoabc");
        String error = loginpage.getErrorMessage();
        System.out.println("Error: " + error);
        Assert.assertTrue(error.contains("Invalid"), "Sai server nhưng không hiện thông báo lỗi");
    }

    // Username trống
    @Test(priority = 5)
    public void LoginUsernameNotEntered() {
        loginpage.login("", "8uJbAf-p", "Axi-US50-Demo");
        String error = loginpage.getErrormsgusername();
        System.out.println("Error: " + error);
        Assert.assertTrue(error.contains("Please enter your username"), "Username trống nhưng không báo lỗi");
    }

    // Password trống
    @Test(priority = 6)
    public void LoginPasswordNotEntered() {
        loginpage.login("50000200", "", "Axi-US50-Demo");
        String error = loginpage.getErrormsgpassword();
        System.out.println("Error: " + error);
        Assert.assertTrue(error.contains("Please enter your password"), "Password trống nhưng không báo lỗi");
    }

    // Server trống
    @Test(priority = 7)
    public void LoginServerNotEntered() {
        loginpage.login("50000200", "8uJbAf-p", "");
        String error = loginpage.getErrormsgserver();
        System.out.println("Error: " + error);
        Assert.assertTrue(error.contains("Please enter server address"), "Server trống nhưng không báo lỗi");
    }

    // Tất cả trống
    @Test(priority = 8)
    public void LoginAllNotEntered() {
        loginpage.login("", "", "");
        String error = loginpage.getErrormsgusername();
        System.out.println("Error: " + error);
        String error2 = loginpage.getErrormsgpassword();
        System.out.println("Error: " + error2);
        String error3 = loginpage.getErrormsgserver();
        System.out.println("Error: " + error3);
        Assert.assertTrue(error.contains("Please enter your username"), "username trống nhưng không báo lỗi");
        Assert.assertTrue(error2.contains("Please enter your password"), "password trống nhưng không báo lỗi");
        Assert.assertTrue(error3.contains("Please enter server address"), "address trống nhưng không báo lỗi");
    }
}
