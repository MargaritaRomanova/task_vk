package tests;

import org.testng.annotations.DataProvider;

public class TestsData {
    @DataProvider(name = "searchDataProvider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"selenium java"}};
    }
}
