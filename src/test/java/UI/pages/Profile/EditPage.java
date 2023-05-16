package UI.pages.Profile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.nio.file.Paths;

public class EditPage extends MainPage {

    public EditPage() {
        super();
    }

    private final static By PROFILE_NAME = By.xpath("//div[@class='ProfileEditName']");

    //------------поля------------//
    private final static By ABOUT_MYSELF_FIELD = By.xpath("//textarea[contains(@class,'general_short_info')]");
    private final static By STATUS_DROPDOWN = By.xpath("//div[text()='Семейное положение:']" +
            "/following::div[contains(@class,'dropdown')]");

    private final static By OPTION = By.xpath(".//ul[contains(@id,'list_options_container')]");

    private final static By HOME_TOWN_FIELD = By.xpath("//input[@id='pedit_home_town']");

    //-------------работа с фотографией профиля-----------//
    private final static By START_IMAGE = By.xpath("//img[@src='/images/camera_100.png']");
    private final static By IMAGE = By.xpath("//img[not(@src='/images/camera_100.png') and @class='vkuiAvatar__img']");
    private final static By PROFILE_EDIT_AVATAR = By.xpath("//div[contains(@class,'ProfileEditAvatar__image')]");
    private final static By OPTION_PROFILE_EDIT_AVATAR = By.xpath("//div[@class='DropdownActionSheetContent']");
    private final static By LOAD_MODAL_WINDOW = By.xpath("//div[@class='BaseModal__content'][.//*[contains(text(),'Загрузка новой фотографии')]]");

    private final static By SAVE_BUTTON = By.xpath("//button[.//span[text()='Сохранить']]");
    private final static By SAVE_AND_CONTINUE_BUTTON = By.xpath("//button[text()='Сохранить и продолжить']");
    private final static By SAVE_CHANGES_BUTTON = By.xpath("//button[text()='Сохранить изменения']");
    private final static By CONTINUE_BUTTON = By.xpath("//button[text()='Продолжить']");

    private final static By DELETE_PHOTO_MODAL_WINDOW = By.xpath("//div[@class='box_layout'][.//div[text()='Предупреждениe']]");
    private final static By DELETE_BUTTON = By.xpath(".//button[.//span[text()='Удалить']]");

    //----------уведомление----------//
    private final static By NOTIFICATION = By.xpath(".//div[contains(@class,'notifier_type_done_box')]" +
            "[.//div[contains(text(),'Фотография профиля обновлена, запись опубликована')]]");
    private final static By MESSAGE_ABOUT_SAVE_ALL = By.xpath("//div[@class='msg ok_msg']//b");

    public String getProfileName() {
        return driver.findElement(PROFILE_NAME).getText();
    }

    public String inputInfoAboutMyselfField(String text) {
        WebElement el = driver.findElement(ABOUT_MYSELF_FIELD);
        String value = el.getAttribute("value");
        if (!value.isEmpty()) {
            el.clear();
        }
        el.sendKeys(text);
        return value;
    }

    private void selectDropdown(WebElement element, String value) {
        element.click();
        WebElement option = element.findElement(OPTION);
        clickWithJavascript(option.findElement(By.xpath(".//li[text()='" + value + "']")));
    }

    public String inputStatusDropdown(String text) {
        WebElement element = driver.findElement(STATUS_DROPDOWN);
        String oldText = element.findElement(By.xpath(".//input[@type='text']")).getAttribute("value");
        selectDropdown(element, text);
        return oldText;
    }

    public String inputHomeTownField(String text) {
        WebElement el = driver.findElement(HOME_TOWN_FIELD);
        String value = el.getAttribute("value");
        if (!value.isEmpty()) {
            el.clear();
        }
        el.sendKeys(text);
        return value;
    }

    public EditPage uploadAvatar(String photoName) {
        clickWithJavascript(findElement(PROFILE_EDIT_AVATAR));
        driver.findElement(By.xpath("//div[@role='button'][.//span[text()='Загрузить фотографию']]")).click();
        findElement(LOAD_MODAL_WINDOW);
        By fileInput = By.cssSelector("input[type=file]");
        String filePath = Paths.get(Paths.get("").toAbsolutePath().toString(),
                "src", "test", "resources", "files", photoName).toString();
        driver.findElement(fileInput).sendKeys(filePath);
        findElement(SAVE_AND_CONTINUE_BUTTON).click();
        findElement(SAVE_CHANGES_BUTTON).click();
        findElement(CONTINUE_BUTTON).click();
        softAssert.assertTrue(driver.findElement(NOTIFICATION).isDisplayed(), "уведомление отсутствует");
        Assert.assertTrue(driver.findElement(IMAGE).isDisplayed(), "фотография не прикреплена");
        return this;
    }

    public void deleteAvatar() {
        clickWithJavascript(findElement(PROFILE_EDIT_AVATAR));
        WebElement opt = findElement(OPTION_PROFILE_EDIT_AVATAR);
        Assert.assertTrue(opt.getText().contains("Удалить") || opt.getText().contains("Удалить фотографию"),
                "Удалить - действие отсутствует");
        WebElement deleteButton = opt.findElement(By.xpath(".//div[@role='button'][.//span[text()='Удалить']] | " +
                ".//div[@role='button'][.//span[text()='Удалить фотографию']]"));
        deleteButton.click();
        driver.findElement(DELETE_PHOTO_MODAL_WINDOW).findElement(DELETE_BUTTON).click();
        Assert.assertTrue(findElement(START_IMAGE).isDisplayed(), "фотография не удалена");
    }

    public EditPage pressSaveButton() {
        clickWithJavascript(findElement(SAVE_BUTTON));
        return this;
    }

    public void verifyInfoWasSaved() {
        Assert.assertEquals(findElement(MESSAGE_ABOUT_SAVE_ALL).getText(), "Изменения сохранены",
                "сообщение о сохранении изменений не отображается");
    }
}
