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
        WebElement elLoginField = driver.findElement(LOGIN_FIELD);
        return elLoginField.getAttribute("value");
    }

    public void verifyLoginFieldIsEmpty() {
        WebElement elLoginField = driver.findElement(LOGIN_FIELD);
        if (!elLoginField.getAttribute("value").isEmpty()) {
            elLoginField.clear();
        }
    }

    public AuthLoginPage inputLogin(String text) {
        WebElement elLoginField = driver.findElement(LOGIN_FIELD);
        elLoginField.sendKeys(text);
        return this;
    }

    public AuthPassPage pressSignInButton() {
        driver.findElement(SIGN_IN_BUTTON).click();
        return new AuthPassPage();
    }

    public AuthLoginPage checkInputLoginContainsText(String text) {
        WebElement elLoginField = driver.findElement(LOGIN_FIELD);
        String actualText = elLoginField.getAttribute("value");
        Assert.assertFalse(actualText.isEmpty(), "Поле ввода логина не заполнено");
        Assert.assertEquals(actualText, text,
                "Поле ввода логина содержит текст:" + actualText + " ожидалось: " + text);
        return this;
    }
}
