package tests;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.DriverFactory;
import utils.PropertyReader;
import utils.SQL.JDBC;

public abstract class BaseTest {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setUp() {
        JDBC.connectDB("users");
        driver = DriverFactory.getDriver();
        driver.get(PropertyReader.getUrl());
    }

    @AfterMethod
    public void close() {
        driver.quit();
    }
}
