package UI.pages.Auth;

import UI.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AuthLoginPage extends BasePage {

    private final static By LOGIN_FIELD = By.xpath("//input[@name='login']");
    private final static By SIGN_IN_BUTTON = By.xpath("//button[contains(@class,'signInButton')]");

    public AuthLoginPage() {
        super();
    }

    public String getLoginField() {
        return findElement(LOGIN_FIELD).getAttribute("value");
    }

    public void verifyLoginFieldIsEmpty() {
        WebElement elLoginField = findElement(LOGIN_FIELD);
        if (!elLoginField.getAttribute("value").isEmpty()) {
            elLoginField.clear();
        }
    }

    public AuthLoginPage inputLogin(String text) {
        findElement(LOGIN_FIELD).sendKeys(text);
        return this;
    }

    public AuthPassPage pressSignInButton() {
        findElement(SIGN_IN_BUTTON).click();
        return new AuthPassPage();
    }

    public AuthLoginPage checkInputLoginContainsText(String text) {
        String actualText = findElement(LOGIN_FIELD).getAttribute("value");
        Assert.assertFalse(actualText.isEmpty(), "Поле ввода логина не заполнено");
        Assert.assertEquals(actualText, text,
                "Поле ввода логина содержит текст:" + actualText + " ожидалось: " + text);
        return this;
    }
}
