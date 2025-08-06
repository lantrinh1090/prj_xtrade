package Testcase;

import Base.BaseSetup;
import Page.Loginpage;
import Page.Logoutpage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest extends BaseSetup {
    private WebDriver driver;
    public Loginpage loginpage;
    public Logoutpage logoutpage;

    @BeforeClass
    public void setUp() {
        driver = getDriver();
    }

    @Test(priority = 1)
    public void Loginsucces() throws InterruptedException {
        loginpage = new Loginpage(driver);
        loginpage.login("admin@gmail.com", "Admin@123");
        System.out.println("Login Success");
        // super(driver);
        String pageTitle = driver.getTitle();
        System.out.println("Title sau khi đăng nhập: " + pageTitle);
    }

    @Test(priority = 2)
    public void Logout() throws InterruptedException {
        logoutpage = new Logoutpage(driver);
        logoutpage.Logout();
        Thread.sleep(2000);
        boolean contains = logoutpage.gettext().contains("Bạn đã đăng xuất khỏi hệ thống");
    }
    @Test(priority = 3)
    public void backlogin() throws InterruptedException {
        //logoutpage = new Logoutpage(driver);
        logoutpage.Clickbacklogin();
        Thread.sleep(2000);
        //trở về màn hình login

    }

    @Test(priority = 4)
    public void LoginFail() throws InterruptedException {
       // driver = getDriver();
       // loginpage = new Loginpage(driver);
        loginpage.login("admin2@gmail.com", "Admin@123");
        System.out.println(loginpage.getErrormsg());

        // super(driver);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }


}

