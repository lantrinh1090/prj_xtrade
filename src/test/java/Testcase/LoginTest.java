package Testcase;

import BaseSetup.BaseSetup;
import Page.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest extends BaseSetup{
    private WebDriver driver;
    public LoginPage loginPage;
    @BeforeClass
    public void setUp(){
        driver = getDriver();
    }
@Test
  public  void signIn() throws Exception{

    System.out.println(driver);
    loginPage = new LoginPage(driver);
    Assert.assertTrue(loginPage.verifySignInPageTitle(), "Sign In page title doesn't match");
    //Assert.assertTrue(loginPage.verifySignInPageText(), "Header page text not matching");
    loginPage.login("thaian@mailinator.com", "Demo@123", "123456");
    driver.quit();
}
}
