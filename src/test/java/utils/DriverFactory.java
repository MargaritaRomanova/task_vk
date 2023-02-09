package utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DriverFactory {

    private final static String DRIVER_PATH = "src/test/resources/";
    private static WebDriver driver;

    public static WebDriver getDriver() {
        File file;
        Browser browser = PropertyReader.getBrowser();
        switch (browser) {
            case CHROME:
                file = new File(DRIVER_PATH + "chromedriver_111.exe");
                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
                driver = new ChromeDriver(chromeOptions);
                break;
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }
}
