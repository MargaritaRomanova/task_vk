package UI.pages.Auth;

import UI.pages.BasePage;
import UI.pages.Profile.NewsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AuthPassPage extends BasePage {

    private final static By PASSWORD_FIELD = By.xpath("//input[@name='password']");
    private final static By CONTINUE_BUTTON = By.xpath("//button[contains(@class,'vkuiButton')]");
    private final static By CAPTCHA = By.xpath("//form[@class='vkc__Captcha__container']");

    public AuthPassPage() {
        super();
    }

    public AuthPassPage inputPass(String text) {
        if (isElementFoundAndDisplayed(CAPTCHA, 0)) {
            Assert.fail("на странице отображается каптча. Дальнейший вход в систему не возможен");
        }
        if (isElementFoundAndDisplayed(PASSWORD_FIELD, 10)) {
            WebElement field = driver.findElement(PASSWORD_FIELD);
            field.sendKeys(text);
        } else {
            Assert.fail("элемент не отображается");
        }
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
