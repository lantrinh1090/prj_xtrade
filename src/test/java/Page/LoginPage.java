package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Loginpage {

    private WebDriver driver;
    private By emailinput =By.id("Username");
    private By passwordinput= By.id("IdPassword");
    private By Loginbtn =By.name("button");
    private  By errormsgText=By.xpath(  "/html/body/div[1]/div[2]/div[1]/form/div[5]");

public Loginpage(WebDriver driver)
{
    this.driver=driver;
}
public boolean verifyLogin(){
    enterEmail("email");
    enterPassword("pass");
    clickLogin();
    return getErrormsg().contains("Sai");
    }

    public void enterEmail(String email){
        WebElement emailTxtBox=driver.findElement(emailinput);
        if( emailTxtBox.isDisplayed()) {
            emailTxtBox.sendKeys(email);
        }
    }
public void enterPassword(String password){
    WebElement passwordTxtBox =driver.findElement(passwordinput);
            if (passwordTxtBox.isDisplayed())
                passwordTxtBox.sendKeys(password);
}
public void clickLogin(){
    WebElement login=driver.findElement(Loginbtn);
    if (login.isDisplayed())
        login.click();
}
public void login(String email,String password) throws InterruptedException {
    enterEmail(email);
    Thread.sleep(1000);
    enterPassword(password);
    Thread.sleep(1000);
    clickLogin();
    Thread.sleep(5000);
}

public String getErrormsg(){
    String strErrormsg=null;
    WebElement errormsg=driver.findElement(errormsgText);
    if  (errormsg.isDisplayed())
        strErrormsg=errormsg.getText();
    return strErrormsg;
}
}
