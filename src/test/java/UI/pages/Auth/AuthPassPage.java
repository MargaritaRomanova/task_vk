package UI.pages.Auth;

import UI.pages.BasePage;
import UI.pages.Profile.NewsPage;
import org.openqa.selenium.By;
import org.testng.Assert;

public class AuthPassPage extends BasePage {

    private final static By PASSWORD_FIELD = By.xpath("//input[@name='password' and @placeholder='Введите пароль']");
    private final static By CONTINUE_BUTTON = By.xpath("//button[contains(@class,'vkuiButton')]");

    public AuthPassPage() {
        super();
    }

    public AuthPassPage inputPass(String text) {
        driver.findElement(PASSWORD_FIELD).sendKeys(text);
        return this;
    }

    public NewsPage pressContinueButton() {
        driver.findElement(CONTINUE_BUTTON).click();
        return new NewsPage();
    }

    public void checkInputPasswordContainsText() {
        String actualText = driver.findElement(PASSWORD_FIELD).getAttribute("value");
        Assert.assertFalse(actualText.isEmpty(), "Поле ввода логина не заполнено");
    }
}
