package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Logoutpage {
    private WebDriver driver;
    private By acountimg = By.xpath("/html/body/div[4]/div/div/header/div[2]/div[6]/a/img");
    private By logoutbtn = By.xpath("/html/body/div[4]/div/div/header/div[2]/div[6]/div/a[4]/span[1]");
    private By backloginbtn= By.xpath("/html/body/div[1]/div[2]/div[1]/div[2]/a");
    private By gettext= By.xpath("/html/body/div[1]/div[2]/div[1]/div[2]/h1");

    public Logoutpage(WebDriver driver) {
        this.driver = driver;
    }

    public void Clickacount() {
        WebElement acount = driver.findElement(acountimg);
        if (acount.isDisplayed())
            acount.click();
    }

    public void Clicklogout() {
        WebElement logout = driver.findElement(logoutbtn);
        if (logout.isDisplayed())
            logout.click();
    }
    public void Clickbacklogin() {
        WebElement backlogin = driver.findElement(backloginbtn);
        if (backlogin.isDisplayed())
            backlogin.click();
    }

    public void Logout() throws InterruptedException {
        Clickacount();
        Thread.sleep(2000);
        Clicklogout();
        Thread.sleep(1000);
    }
    public String gettext(){
        String text=null;
        WebElement errormsg=driver.findElement(gettext);
        if  (errormsg.isDisplayed())
            text=errormsg.getText();
        return text;
    }

}
