package testCases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Date;

public class Login {

    WebDriver driver;

    @BeforeClass
    public void setupSelenium() {
        String browserName = "edge";

        if (browserName.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        } else if (browserName.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("start-maximized");
            options.addArguments("--remote-allow-origins=*");
            driver = new FirefoxDriver(options);
        } else if (browserName.equals("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("start-maximized");
            options.addArguments("--remote-allow-origins=*");
            driver = new EdgeDriver(options);
        }
    }

    @BeforeMethod
    public void setupTest() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.get("https://tutorialsninja.com/demo/");
        driver.findElement(By.xpath("//span[text()=\"My Account\"]")).click();
        driver.findElement(By.linkText("Login")).click();
    }

    @Test(priority = 10)
    public void verifyLoginWithValidCredentials() {
        driver.findElement(By.id("input-email")).sendKeys("laskowska.kadry@gmail.com");
        driver.findElement(By.id("input-password")).sendKeys("testtest123");
        driver.findElement(By.xpath("//input[@value=\"Login\"]")).click();

        Assert.assertTrue(driver.findElement(By.linkText("Edit your account information")).isDisplayed(), "Edit your account information link is not displayed.");
    }

    @Test
    public void verifyLoginWithInvalidCredentials() {
        driver.findElement(By.id("input-email")).sendKeys("lasko" + generateTimeStamp() + "wice@gmail.com");
        driver.findElement(By.id("input-password")).sendKeys("test");
        driver.findElement(By.xpath("//input[@value=\"Login\"]")).click();

        String actualWarningMessage = driver.findElement(By.xpath("//div[contains(@class,\"alert-dismissible\")]")).getText();
        String expectedWarningMessage = "Warning: No match for E-Mail Address and/or Password.";
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage), "Expected warning message is not displayed.");
    }

    @Test
    public void verifyLoginWithoutProvidingCredentials() {
        driver.findElement(By.xpath("//input[@value=\"Login\"]")).click();

        String actualWarningMessage = driver.findElement(By.xpath("//div[contains(@class,\"alert-dismissible\")]")).getText();
        String expectedWarningMessage = "Warning: No match for E-Mail Address and/or Password.";
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage), "Expected warning message is not displayed.");
    }

    @Test
    public void verifyLoginWithInvalidEmailAndValidPassword() {
        driver.findElement(By.id("input-email")).sendKeys("lasko" + generateTimeStamp() + "wice@gmail.com");
        driver.findElement(By.id("input-password")).sendKeys("testtest123");
        driver.findElement(By.xpath("//input[@value=\"Login\"]")).click();

        String actualWarningMessage = driver.findElement(By.xpath("//div[contains(@class,\"alert-dismissible\")]")).getText();
        String expectedWarningMessage = "Warning: No match for E-Mail Address and/or Password.";
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage), "Expected warning message is not displayed.");
    }

    @Test
    public void verifyLoginWithValidEmailAndInvalidPassword() {
        driver.findElement(By.id("input-email")).sendKeys("laskowska.kadry@gmail.com");
        driver.findElement(By.id("input-password")).sendKeys("test" + generateTimeStamp());
        driver.findElement(By.xpath("//input[@value=\"Login\"]")).click();

        String actualWarningMessage = driver.findElement(By.xpath("//div[contains(@class,\"alert-dismissible\")]")).getText();
        String expectedWarningMessage = "Warning: No match for E-Mail Address and/or Password.";
        Assert.assertTrue(actualWarningMessage.contains(expectedWarningMessage), "Expected warning message is not displayed.");
    }


    public String generateTimeStamp() {
        Date date = new Date();
        return date.toString().replace(" ", "_").replace(":", "_");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
