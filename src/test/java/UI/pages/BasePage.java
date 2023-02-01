package UI.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static tests.BaseTest.getDriver;

public abstract class BasePage {
    public WebDriver driver;
    WebDriverWait wait;
    public Actions builder;
    JavascriptExecutor executor;

    protected BasePage() {
        driver = getDriver();
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        builder = new Actions(driver);
        executor = (JavascriptExecutor) driver;
    }

    public boolean isElementFoundAndDisplayed(By by, int timeout) throws InterruptedException {
        WebElement element = driver.findElement(by);
        for (int i = 0; (i < timeout) && (!element.isDisplayed()); i++) {
            Thread.sleep(1000);
            element = driver.findElement(by);
        }
        return element.isDisplayed();
    }

    public void clickWithJavascript(WebElement element) {
        executor.executeScript("arguments[0].click()", element);
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void back() {
        driver.navigate().back();
    }
}
