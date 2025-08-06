package Testcase;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.time.Duration;

public class Login {
WebDriver driver;

@Test(priority = 1)
public void case1()
{
    WebDriverManager.chromedriver().setup();
    driver= new ChromeDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    driver.get("https://isodev.idasonline.com/daotao/khoadaotao/index");
    driver.quit();
}
@Test(priority = 2)
    public void case2() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.get("https://isodev.idasonline.com/daotao/khoadaotao/index");
        Thread.sleep(1000);
        WebElement email=driver.findElement(By.xpath("//*[@id=\"Username\"]"));
         email.click();
        email.sendKeys("admin@gmail.com");
        Thread.sleep(1000);
        WebElement pass=driver.findElement(By.xpath("//*[@id=\"IdPassword\"]"));
        pass.click();
        pass.sendKeys("Admin@123");Thread.sleep(1000);
        WebElement buttonlogin= driver.findElement(By.name("button"));
       buttonlogin.click();
       Thread.sleep(5000);

    driver.quit();
    }
    @Test(priority = 3)
    public void case3() throws InterruptedException {



    }

/*public static void main(String[] args)
{
    System.out.println("hello");

    //Khởi tạo browser với Chrome
    WebDriver driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    WebDriverManager chromedriver = WebDriverManager.chromedriver();

    chromedriver.setup();
    //Mở trang
    driver.get("https://isodev.idasonline.com/daotao/khoadaotao/index");

    //Click nút Login
    driver.findElement(By.xpath("//*[@id=\"formlogin\"]/button")).click();

    //Tắt browser
    driver.quit();
}*/
}

