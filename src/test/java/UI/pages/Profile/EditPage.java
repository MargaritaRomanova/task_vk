package UI.pages.Profile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.nio.file.Paths;

public class EditPage extends MainPage {

    public EditPage() {
        super();
    }

    private SoftAssert softAssert = new SoftAssert();

    private final static By PROFILE_NAME = By.xpath("//div[@class='ProfileEditName']");

    //------------поля------------//
    private final static By ABOUT_MYSELF_FIELD = By.xpath("//textarea[contains(@class,'general_short_info')]");
    private final static By STATUS_DROPDOWN = By.xpath("//div[text()='Семейное положение:']" +
            "/following::div[contains(@class,'dropdown')]");

    private final static By OPTION = By.xpath(".//ul[contains(@id,'list_options_container')]");

    private final static By DAY = By.xpath("//div[contains(@class,'day')]/div[contains(@class,'dropdown')]");
    private final static By MONTH = By.xpath("//div[contains(@class,'month')]/div[contains(@class,'dropdown')]");
    private final static By YEAR = By.xpath("//div[contains(@class,'year')]/div[contains(@class,'dropdown')]");

    private final static By HOME_TOWN_FIELD = By.xpath("//input[@id='pedit_home_town']");

    //-------------работа с фотографией профиля-----------//
    private final static By START_IMAGE = By.xpath("//img[@src='/images/camera_100.png']");
    private final static By IMAGE = By.xpath("//img[not(@src='/images/camera_100.png') and @class='vkuiAvatar__img']");
    private final static By PROFILE_EDIT_AVATAR = By.xpath("//div[contains(@class,'ProfileEditAvatar__image')]");
    private final static By OPTION_PROFILE_EDIT_AVATAR = By.xpath("//div[@class='DropdownActionSheetContent']");
    private final static By LOAD_MODAL_WINDOW = By.xpath("//div[@class='BaseModal__content'][.//h1[text()='Загрузка новой фотографии']]");

    private final static By SAVE_BUTTON = By.xpath("//button[.//span[text()='Сохранить']]");

    private final static By PHOTO_IS_LOAD_MODAL_WINDOW = By.xpath("//div[@class='BaseModal__content'][.//h1[text()='Фотография на вашей странице']]");
    private final static By SAVE_AND_CONTINUE_BUTTON = By.xpath(".//button[text()='Сохранить и продолжить']");

    private final static By CHOOSE_MINIATURE_MODAL_WINDOW = By.xpath("//div[@class='BaseModal__content'][.//h1[text()='Выбор миниатюры']]");
    private final static By SAVE_CHANGES_BUTTON = By.xpath(".//button[text()='Сохранить изменения']");

    private final static By COMPLETION_MODAL_WINDOW = By.xpath("//div[@class='BaseModal__content'][.//h1[text()='Завершение']]");
    private final static By CONTINUE_BUTTON = By.xpath(".//button[text()='Продолжить']");

    private final static By DELETE_PHOTO_MODAL_WINDOW = By.xpath("//div[@class='box_layout'][.//div[text()='Предупреждениe']]");
    private final static By DELETE_BUTTON = By.xpath(".//button[.//span[text()='Удалить']]");

    //----------уведомление----------//
    private final static By NOTIFICATION = By.xpath(".//div[contains(@class,'notifier_type_done_box')]" +
            "[.//div[contains(text(),'Фотография профиля обновлена, запись опубликована')]]");
    private final static By MESSAGE_ABOUT_SAVE_ALL = By.xpath("//div[@class='msg ok_msg']");

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

    public String inputStatusDropdown(String text) {
        WebElement el = driver.findElement(STATUS_DROPDOWN).findElement(By.xpath(".//input[@type='text']"));
        WebElement el2 = driver.findElement(STATUS_DROPDOWN).findElement(OPTION);
        String value = el.getAttribute("value");
        el.click();
        el2.findElement(By.xpath(".//li[text()='" + text + "']")).click();
        el.click();
        return value;
    }

    public String inputDay(String dayNew) {
        WebElement dayField = driver.findElement(DAY).findElement(By.xpath(".//input[@type='text']"));
        WebElement dayOption = driver.findElement(DAY).findElement(OPTION);
        String value = dayField.getAttribute("value");
        dayField.click();
        dayOption.findElement(By.xpath(".//li[text()='" + dayNew + "']")).click();
        dayField.click();
        return value;
    }

    public String inputMonth(String monthNew) {
        WebElement monthField = driver.findElement(MONTH).findElement(By.xpath(".//input[@type='text']"));
        WebElement monthOption = driver.findElement(MONTH).findElement(OPTION);
        String value = monthField.getAttribute("value");
        monthField.click();
        monthOption.findElement(By.xpath(".//li[text()='" + monthNew + "']")).click();
        monthField.click();
        return value;
    }

    public String inputYear(String yearNew) {
        WebElement yearField = driver.findElement(YEAR).findElement(By.xpath(".//input[@type='text']"));
        WebElement yearOption = driver.findElement(YEAR).findElement(OPTION);
        String value = yearField.getAttribute("value");
        yearField.click();
        yearOption.findElement(By.xpath(".//li[text()='" + yearNew + "']")).click();
        yearField.click();
        return value;
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

    public void avatarAction(String action) {
        WebElement avatar = driver.findElement(PROFILE_EDIT_AVATAR);
        avatar.click();
        WebElement opt = driver.findElement(OPTION_PROFILE_EDIT_AVATAR);
        Assert.assertTrue(opt.getText().contains(action), action + " - действие отсутствует");
        WebElement optAction = opt.findElement(By.xpath(".//span[text()='" + action + "']"));
        if (action.equals("Загрузить фотографию")) {
            optAction.click();
            driver.findElement(LOAD_MODAL_WINDOW);
            By fileInput = By.cssSelector("input[type=file]");
            String filePath = Paths.get(Paths.get("").toAbsolutePath().toString(),
                    "src", "test", "resources", "files", "dwoilp3BVjlE.jpg").toString();
            driver.findElement(fileInput).sendKeys(filePath);
            driver.findElement(PHOTO_IS_LOAD_MODAL_WINDOW).findElement(SAVE_AND_CONTINUE_BUTTON).click();
            driver.findElement(CHOOSE_MINIATURE_MODAL_WINDOW).findElement(SAVE_CHANGES_BUTTON).click();
            driver.findElement(COMPLETION_MODAL_WINDOW).findElement(CONTINUE_BUTTON).click();
            softAssert.assertTrue(driver.findElement(NOTIFICATION).isDisplayed(), "уведомление отсутствует");
            Assert.assertTrue(driver.findElement(IMAGE).isDisplayed(), "фотография не прикреплена");
        }
        if (action.equals("Удалить")) {
            optAction.click();
            driver.findElement(DELETE_PHOTO_MODAL_WINDOW).findElement(DELETE_BUTTON).click();
            Assert.assertTrue(driver.findElement(START_IMAGE).isDisplayed(), "фотография не удалена");
        }
    }

    public void pressSaveButton() {
        driver.findElement(SAVE_BUTTON).click();
    }

    public void verifyInfoWasSaved() {
        Assert.assertTrue(driver.findElement(MESSAGE_ABOUT_SAVE_ALL).getText().contains("Изменения сохранены\n" +
                "Новые данные будут отражены на вашей странице."), "сообщение о сохранении изменений не отображается");
    }
}
