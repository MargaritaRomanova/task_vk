package UI.pages.Profile;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class DiscussionsPage extends MainPage {

    public DiscussionsPage() {
        super();
    }

    private final static By CREATE_GROUP_BUTTON = By.xpath("//a[.//span[text()='Создать сообщество']]");
    private final static By TYPE_MODAL_WINDOW = By.xpath("//div[@class='groups_create_type']");
    private final static By CREATE_GROUP_MODAL_WINDOW = By.xpath("//div[@class='box_layout']");
    private final static By NAME_GROUP_FIELD = By.xpath(".//div[text()='Название:']/following-sibling::input");
    private final static By TOPIC_DROPDOWN = By.xpath(".//div[text()='Тематика:']/following::input[@class='selector_input']");

    private final static By CREATE_BUTTON = By.xpath(".//span[.//span[text()='Создать сообщество']]");

    private final static By BLOCK_GALLERY_ITEM = By.xpath("//div[@class='ui_gallery__inner_cont']");
    private final static By ARROW_RIGHT = By.xpath("//div[contains(@class,'arrow_right')]");
    private final static By ADD_DISCUSSION_BUTTON = By.xpath("//a[.//span[text()='Добавить обсуждение']]");

    private final static By TITLE_FIELD = By.xpath("//input[@id='bnt_title']");
    private final static By TEXT_FIELD = By.xpath("//textarea[@id='bnt_text']");
    private final static By CREATE_TOPIC_BUTTON = By.xpath("//button[.//span[text()='Создать тему']]");

    private final static By EDIT_TOPIC_BUTTON = By.xpath("//button[.//span[text()='Редактировать тему']]");
    private final static By EDIT_TOPIC_MODAL_WINDOW = By.xpath("//div[@class='box_layout'][.//div[text()='Редактирование темы']]");
    private final static By PIN_TOPIC_CHECKBOX = By.xpath(".//div[contains(@class,'checkbox') and text()='Закрепить тему']");
    private final static By SAVE_BUTTON = By.xpath(".//button[.//span[text()='Сохранить']]");
    private final static By MESSAGE = By.xpath("//div[@id='bt_msg'] | //div[@class='msg info_msg']");

    private final static By CRUMB = By.xpath("//a[@class='ui_crumb' and text()='Обсуждения']");
    private final static By TOPIC = By.xpath("//div[@class='blst_info']");
    private final static By PIN = By.xpath("./following-sibling::div[@class='blst_fixed']");

    private final static By INPUT_FIELD = By.xpath("//div[@class='reply_field submit_post_field']");
    private final static By EDIT_BUTTON = By.xpath(".//a[contains(@class,'edit_button')]");
    private final static By DELETE_TOPIC_BUTTON = By.xpath(".//a[contains(@class,'delete_button')]");
    private final static By DELETE_BUTTON = By.xpath("//button[.//span[text()='Удалить']]");

    private final static By MORE_ACTIONS_BUTTON = By.xpath(".//div[@class='ui_actions_menu_icons']");

    public DiscussionsPage pressCreateGroupButton() {
        driver.findElement(CREATE_GROUP_BUTTON).click();
        return this;
    }

    public DiscussionsPage selectTypeOfGroup(String text) {
        driver.findElement(TYPE_MODAL_WINDOW).findElement(By.xpath(".//div[text()='" + text + "']")).click();
        return this;
    }

    public void inputFieldsAndPressCreateButton(String title, String topic) {
        WebElement modalWindow = driver.findElement(CREATE_GROUP_MODAL_WINDOW);
        modalWindow.findElement(NAME_GROUP_FIELD).sendKeys(title);
        WebElement dropdown = modalWindow.findElement(TOPIC_DROPDOWN);
        dropdown.sendKeys(topic);
        dropdown.findElement(By.xpath(".//following::*[self::em or self::li][text()='" + topic + "']")).click();
        modalWindow.findElement(CREATE_BUTTON).click();
    }

    public DiscussionsPage selectDiscussions() {
        WebElement itemList = driver.findElement(BLOCK_GALLERY_ITEM);
        builder.moveToElement(itemList);
        driver.findElement(ARROW_RIGHT).click();
        itemList.findElement(By.xpath(".//span[text()='Обсуждения']")).click();
        driver.findElement(ADD_DISCUSSION_BUTTON).click();
        return this;
    }

    public void createTopic(String title, String text) {
        driver.findElement(TITLE_FIELD).sendKeys(title);
        driver.findElement(TEXT_FIELD).sendKeys(text);
        driver.findElement(CREATE_TOPIC_BUTTON).click();
    }

    public DiscussionsPage pressEditTopicButton() {
        driver.findElement(EDIT_TOPIC_BUTTON).click();
        return this;
    }

    public DiscussionsPage activatePinTopicCheckbox() throws InterruptedException {
        WebElement checkbox = driver.findElement(EDIT_TOPIC_MODAL_WINDOW).findElement(PIN_TOPIC_CHECKBOX);
        if (isElementFoundAndDisplayed(PIN_TOPIC_CHECKBOX, 5)) {
            Thread.sleep(500);
            if (checkbox.getAttribute("aria-checked").equals("false")) {
                checkbox.click();
            }
            Assert.assertEquals(checkbox.getAttribute("aria-checked"), "true",
                    "чекбокс не удалось активировать");
        } else {
            Assert.fail("элемент не отображается");
        }
        return this;
    }

    public DiscussionsPage pressSaveButton() {
        driver.findElement(EDIT_TOPIC_MODAL_WINDOW).findElement(SAVE_BUTTON).click();
        return this;
    }

    public void verifyInfoWasSaved() {
        Assert.assertTrue(driver.findElement(MESSAGE).getText().contains("Изменения сохранены\n" +
                "Название и настройки темы сохранены."), "сообщение о сохранении изменений не отображается");
    }

    public void goToCrumb() {
        driver.findElement(CRUMB).click();
    }

    public void verifyPinTopic(String title) {
        Assert.assertTrue(driver.findElement(TOPIC).findElement(By.xpath(".//a[text()='" + title + "']")).findElement(PIN).isDisplayed(),
                "тема не закреплена");
    }

    public void selectTopic(String title) {
        driver.findElement(TOPIC).findElement(By.xpath(".//a[text()='" + title + "']")).click();
    }

    public String writeMessage(String text) throws InterruptedException {
        WebElement field = driver.findElement(INPUT_FIELD);
        field.sendKeys(text);
        field.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='bp_post clear_fix ']"));
        //добавленный комментарий в списке всегда последний
        WebElement newComment = list.get(list.size() - 1);
        return newComment.getAttribute("id");
    }

    public DiscussionsPage editComment(String comment_id, String newComment) {
        WebElement element = driver.findElement(By.xpath("//div[contains(@class,'bp_post clear_fix') and @id='" + comment_id + "']"));
        builder.moveToElement(element);
        element.findElement(EDIT_BUTTON).click();
        WebElement field = element.findElement(By.xpath(".//textarea"));
        field.clear();
        field.sendKeys(newComment);
        element.findElement(SAVE_BUTTON).click();
        return this;
    }

    public void verifyCommentWasEdited(String comment_id, String newComment) throws InterruptedException {
        Thread.sleep(1000);
        WebElement element = driver.findElement(By.xpath("//div[contains(@class,'bp_post clear_fix') and @id='" + comment_id + "']"));
        Assert.assertEquals(element.findElement(By.xpath(".//div[@class='bp_text']")).getText(), newComment,
                "комментарий не изменен");
    }

    public DiscussionsPage deleteComment(String comment_id) {
        WebElement element = driver.findElement(By.xpath("//div[contains(@class,'bp_post clear_fix') and @id='" + comment_id + "']"));
        builder.moveToElement(element);
        clickWithJavascript(element.findElement(DELETE_TOPIC_BUTTON));
        return this;
    }

    public void verifyCommentWasDeleted(String comment_id) throws InterruptedException {
        Thread.sleep(1000);
        WebElement element = driver.findElement(By.xpath("//div[contains(@class,'bp_post clear_fix') and @id='" + comment_id + "']"));
        Assert.assertTrue(element.findElement(By.xpath(".//div[@class='bp_deleted_text']"))
                .getText().contains("Комментарий удалён."), "Комментарий не был удален");
    }

    public DiscussionsPage deleteTopic() {
        driver.findElement(EDIT_TOPIC_MODAL_WINDOW)
                .findElement(By.xpath(".//a[text()='Удалить тему']")).click();
        driver.findElement(EDIT_TOPIC_MODAL_WINDOW).findElement(DELETE_BUTTON).click();
        return this;
    }

    public void verifyTopicWasDeleted() throws InterruptedException {
        if (isElementFoundAndDisplayed(MESSAGE, 5)) {
            Assert.assertTrue(driver.findElement(MESSAGE).getText().contains("Тема удалена.\n" +
                            "Тема удалена из списка обсуждений группы."),
                    "сообщение об удалинии темы не отображается");
        } else {
            Assert.fail("элемент не отображается");
        }
    }

    public void deleteGroup() throws InterruptedException {
        List<WebElement> list = driver.findElements(By.xpath("//div[@id='groups_list_groups']//div[contains(@class,'group_list_row')]"));
        int size = list.size();
        Assert.assertEquals(size, 1, "ожидалось групп: 1, отображается: " + size);
        //ожидаемое количество созданных групп 1, для работы с этой группой берем .get(0)
        clickWithJavascript(list.get(0).findElement(MORE_ACTIONS_BUTTON));
        if (isElementFoundAndDisplayed(By.xpath("//a[text()='Отписаться']"), 5)) {
            list.get(0).findElement(MORE_ACTIONS_BUTTON).findElement(By.xpath("./following::a[text()='Отписаться']")).click();
            Thread.sleep(1000);
            Assert.assertTrue(list.get(0).getAttribute("class").contains("deleted"),
                    "пользователь не отписался от группы");
        } else {
            Assert.fail("элемент не отображается");
        }
    }
}
