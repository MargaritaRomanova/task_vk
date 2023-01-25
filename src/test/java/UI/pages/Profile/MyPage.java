package UI.pages.Profile;

import org.openqa.selenium.By;

public class MyPage extends MainPage {

    public MyPage() {
        super();
    }

    private final static By EDIT_PROFILE_BUTTON = By.xpath("//div[@class='ProfileHeaderButton'][.//span[text()='Редактировать профиль']]");

    public EditPage pressEditProfileButton() {
        driver.findElement(EDIT_PROFILE_BUTTON).click();
        return new EditPage();
    }
}
