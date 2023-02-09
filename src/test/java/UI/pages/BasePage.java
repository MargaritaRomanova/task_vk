package UI.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

import static tests.BaseTest.getDriver;

public abstract class BasePage {
    public WebDriver driver;
    WebDriverWait wait;
    public Actions builder;
    JavascriptExecutor executor;
    protected SoftAssert softAssert = new SoftAssert();

    protected BasePage() {
        driver = getDriver();
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        builder = new Actions(driver);
        executor = (JavascriptExecutor) driver;
    }

    public boolean isElementFoundAndDisplayed(By by, int timeout) {
        boolean isDisplayed = false;
        try {
            WebElement element = driver.findElement(by);
            int time = 0;
            while (!isDisplayed && time < timeout) {
                isDisplayed = element.isDisplayed();
                Thread.sleep(1000);
                time++;
            }
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isDisplayed;
    }

    public void clickWithJavascript(WebElement element) {
        executor.executeScript("arguments[0].click();", element);
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void back() {
        driver.navigate().back();
    }

    public WebElement findElement(By by) {
        if (!isElementFoundAndDisplayed(by, 7)) {
            Assert.fail("элемент не найден");
        }
        return driver.findElement(by);
    }
}
