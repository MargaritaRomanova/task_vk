package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DriverFactory {

    private final static String DRIVER_PATH = "src/test/resources/";
    private static WebDriver driver;

    public static WebDriver getDriver(Browser browser) {
        File file;

        switch (browser) {
            case CHROME:
                file = new File(DRIVER_PATH + "chromedriver_108.exe");
                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");

                driver = new ChromeDriver(chromeOptions);
                break;
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        return driver;
    }
}
