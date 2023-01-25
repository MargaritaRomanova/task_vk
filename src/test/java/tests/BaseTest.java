package tests;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import utils.DriverFactory;
import utils.PropertyReader;

public abstract class BaseTest extends TestsData {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.getDriver(PropertyReader.getBrowser());
        driver.get(PropertyReader.getUrl());
    }

//    @AfterClass
//    public void tearDown() {
//        driver.quit();
//    }

    @AfterTest
    public void close() {
        driver.quit();
    }

//    @AfterMethod
//    public void goBack() {
//        driver.navigate().back();
//    }
}
