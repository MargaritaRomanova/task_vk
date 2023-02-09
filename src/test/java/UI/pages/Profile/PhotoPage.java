package UI.pages.Profile;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.nio.file.Paths;
import java.util.List;

public class PhotoPage extends MainPage {

    public PhotoPage() {
        super();
    }

    private final static By CREATE_ALBUM_BUTTON = By.xpath("//button[.//span[text()='Создать альбом']]");

    private final static By CREATE_ALBUM_MODAL_WINDOW = By.xpath("//div[@class='box_layout'][.//text()='Создать альбом']");
    private final static By TITLE_ALBUM = By.xpath(".//input[@id='new_album_title']");
    private final static By DESCRIPTION_ALBUM = By.xpath(".//textarea[@id='new_album_description']");
    private final static By WHO_CAN_VIEW_BUTTON = By.xpath(".//div[text()='Кто может просматривать этот альбом? ']//button");
    private final static By WHO_CAN_COMMENT_BUTTON = By.xpath(".//div[text()='Кто может комментировать фотографии? ']//button");
    private final static By LIST = By.xpath("./following::div[@class='body body__flex']");

    private final static By PHOTO = By.xpath("//div[@class='photos_row ']/a | //div[@class='photos_photo_edit_row']");
    private final static By PHOTO_MODAL_WINDOW = By.xpath("//div[@id='pv_box']");
    private final static By CREATE_ALBUM_BUTTON_IN_MODAL_WINDOW = By.xpath(".//button[.//span[text()='Создать альбом']]");

    private final static By MORE_ACTIONS_BUTTON = By.xpath(".//a[@class='pv_actions_more']");

    private final static By NOTIFICATION = By.xpath("//div[contains(@class,'notifier_baloon_body')]" +
            "[.//div[text()='Фотография установлена обложкой альбома']]");

    private final static By COMMENT_FIELD = By.xpath(".//div[@class='reply_field submit_post_field']");

    private final static By READY_BUTTON = By.xpath(".//button[.//span[text()='Готово']]");

    private final static By ALBUM = By.xpath("//div[contains(@class,'photos_album_thumb crisp')]");

    private final static By IMPORT_PHOTO_IN_ALBUM_MODAL_WINDOW = By.xpath("//div[@class='box_layout'][.//text()='Перенос фотографии в альбом']");
    private final static By NOTIFICATION_IMPORT_PHOTO = By.xpath("//div[contains(@class,'notifier_baloon')" +
            " and text()='1 фотография успешно перенесена в альбом ']");
    private final static By DELETE_ALBUM_BUTTON = By.xpath("//button[.//span[text()='Удалить альбом']]");
    private final static By DELETE_BUTTON = By.xpath("//button[.//span[text()='Удалить']]");

    public PhotoPage pressCreateAlbumButton() {
        driver.findElement(CREATE_ALBUM_BUTTON).click();
        return this;
    }

    public void inputFieldAndCreateAlbum(String title, String description, String whoCanView, String whoCanComment) {
        WebElement modalWindow = driver.findElement(CREATE_ALBUM_MODAL_WINDOW);
        modalWindow.findElement(TITLE_ALBUM).sendKeys(title);
        modalWindow.findElement(DESCRIPTION_ALBUM).sendKeys(description);

        WebElement view = modalWindow.findElement(WHO_CAN_VIEW_BUTTON);
        view.click();
        view.findElement(LIST).findElement(By.xpath("./button[text()='" + whoCanView + "']")).click();

        WebElement comment = modalWindow.findElement(WHO_CAN_COMMENT_BUTTON);
        comment.click();
        comment.findElement(LIST).findElement(By.xpath("./button[text()='" + whoCanComment + "']")).click();
        modalWindow.findElement(CREATE_ALBUM_BUTTON_IN_MODAL_WINDOW).click();
        driver.findElement(By.xpath("//div[contains(@class,'page_block_header')][./div[text()='" + title + "']]"));
    }

    public PhotoPage loadPhoto() throws InterruptedException {
        By fileInput = By.cssSelector("input[type=file]");
        String filePath = Paths.get(Paths.get("").toAbsolutePath().toString(),
                "src", "test", "resources", "files", "avatar2.jpg").toString();
        driver.findElement(fileInput).sendKeys(filePath);
        Thread.sleep(5000);
        Assert.assertTrue(driver.findElement(PHOTO).isDisplayed(), "фотография в альбом не добавлена");
        return this;
    }

    public void verifyCountPhotoInAlbum(int count) {
        int countPhoto = driver.findElements(PHOTO).size();
        Assert.assertEquals(countPhoto, count,
                "количество фотографий не совпадает, ожидалось: " + count + ", найдено :" + countPhoto);
    }

    public PhotoPage clickOnPhoto() {
        clickWithJavascript(findElement(PHOTO));
        return this;
    }

    public PhotoPage moreActionsWithPhoto(String text) throws InterruptedException {
        Thread.sleep(2000);
        if (isElementFoundAndDisplayed(By.xpath("//a[@class='pv_actions_more']"), 10)) {
            WebElement action = driver.findElement(PHOTO_MODAL_WINDOW).findElement(MORE_ACTIONS_BUTTON);
            action.click();
            action.findElement(By.xpath("//*[self::div or self::a][normalize-space()='" + text + "']")).click();
        } else {
            Assert.fail("элемент не отображается");
        }
        return this;
    }

    public void verifyPhotoAsAlbumCover() {
        Assert.assertTrue(driver.findElement(NOTIFICATION).isDisplayed(), "уведомление отсутствует");
    }

    public void addCommentForPhoto(String text) {
        WebElement field = driver.findElement(PHOTO_MODAL_WINDOW).findElement(COMMENT_FIELD);
        field.sendKeys(text);
        field.sendKeys(Keys.ENTER);
        Assert.assertTrue(driver.findElement(By.xpath("//div[@id='pv_comments_list']/div")).getText().contains(text),
                "комментарий для фотографии не опубликован");
    }

    public void tagAPersonOnPhoto(String text, String name) throws InterruptedException {
        WebElement modalWindow = driver.findElement(PHOTO_MODAL_WINDOW);
        modalWindow.findElement(By.xpath(".//a[text()='" + text + "']")).click();
        WebElement photo = modalWindow.findElement(By.xpath(".//div[@id='pv_photo']"));
        Thread.sleep(2000);
        builder.moveToElement(photo).perform();
        photo.click();
        Thread.sleep(2000);
        driver.findElement(By.xpath(".//div[@class='photo_tooltip'][2]//following::div[@data-name='" + name + "']")).click();
        Thread.sleep(2000);
        Assert.assertTrue(modalWindow.findElement(By.xpath(".//div[@id='pv_tags']"))
                        .getText().contains("На этой фотографии: " + name),
                "не удалось отметить человека на фотографии");
        modalWindow.findElement(READY_BUTTON).click();
    }

    public void pressEscape() {
        driver.findElement(PHOTO_MODAL_WINDOW).sendKeys(Keys.ESCAPE);
    }

    public void verifyAlbumIsPrivate(String title) {
        WebElement element = driver.findElement(By.xpath("//div[@title='" + title + "']"));
        Assert.assertTrue(element.findElement(By.xpath(".//div[contains(@class,'photos_album_privacy')]")).isDisplayed(),
                "созданный альбом: " + title + " не приватный");
    }

    public PhotoPage openAlbum(String title) {
        if (isElementFoundAndDisplayed(By.xpath("//div[@id='ui_albums_load_more']"), 10)) {
            findElement(By.xpath("//div[@id='ui_albums_load_more']")).click();
        }
        WebElement element = findElement(By.xpath("//div[@title='" + title + "']"));
        clickWithJavascript(element);
        return this;
    }

    public PhotoPage importPhotoInAlbum(String title) throws InterruptedException {
        WebElement modalWindow = driver.findElement(IMPORT_PHOTO_IN_ALBUM_MODAL_WINDOW);
        WebElement photo = modalWindow.findElement(By.xpath(".//div[@title='" + title + "']"));
        photo.click();
        Thread.sleep(2000);
        return this;
    }

    public void verifyNotificationImportPhoto() {
        Assert.assertTrue(driver.findElement(NOTIFICATION_IMPORT_PHOTO).isDisplayed(), "уведомление отсутствует");
    }

    public PhotoPage deleteAlbum() {
        findElement(By.xpath("//a[text()='Редактировать альбом']")).click();
        findElement(DELETE_ALBUM_BUTTON).click();
        findElement(By.xpath("//div[@aria-label='Закрыть']")).click();
        findElement(DELETE_ALBUM_BUTTON).click();
        findElement(DELETE_BUTTON).click();
        return this;
    }

    public void verifyAllAlbumsWereDeleted() {
        List<WebElement> albums = driver.findElements(ALBUM);
        Assert.assertTrue(albums.size() <= 2
                        && albums.stream().allMatch(r -> r.getText().contains("Отметки на фото") ||
                        r.getText().contains("Фото профиля")) || (albums.size() == 0),
                "Удалены не все альбомы");
    }
}
