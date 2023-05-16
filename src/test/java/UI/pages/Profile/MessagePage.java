package UI.pages.Profile;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class MessagePage extends MainPage {

    public MessagePage() {
        super();
    }

    private final static By ADD_NEW_CHAT_BUTTON = By.xpath("//div[@class='im-page--center-empty']//a[text()='создайте новый']");
    private final static By CREATE_CHAT = By.xpath("//div[@class='im-create--table'][.//div[text()='Создание чата']]");
    private final static By INPUT_CHAT_TITLE = By.xpath(".//input[@placeholder='Введите название чата']");
    private final static By CREATE_CHAT_BUTTON = By.xpath(".//button[.//span[text()='Создать чат']]");

    private final static By CHAT_BODY = By.xpath("//div[@class='im-page--chat-body']");
    private final static By CHAT_HEADER = By.xpath("//div[contains(@class,'chat-header_chat')]");

    private final static By DIALOG = By.xpath("//li[./div[@class='nim-dialog--photo']]");
    private final static By DIALOG_PINNED = By.xpath("//li[contains(@class,'dialog_pinned')][./div[@class='nim-dialog--photo']]");

    private final static By CHAT_EDIT_MODAL_WINDOW = By.xpath("//div[contains(@class,'box_body')]");
    private final static By EDIT_TITLE_FIELD = By.xpath(".//div[@class='EditableLabel']");

    private final static By INFORMATION_MODAL_WINDOW = By.xpath("//div[@id='ChatSettings'][.//h2[text()='Информация']]");
    private final static By ADD_NEW_PERSON_BUTTON = By.xpath(".//div[@class='ListItem__main'][.//span[text()='Добавить участников']]");
    private final static By ADD_NEW_PERSON_MODAL_WINDOW = By.xpath("//div[@id='ChatSettings'][.//h2[text()='Добавить участников']]");
    private final static By INPUT_NAME = By.xpath(".//div[@class='MultiSelect__search']");
    private final static By ADD_PERSON_BUTTON = By.xpath(".//button[.//span[text()='Добавить собеседника']]");

    private final static By MESSAGE_FIELD = By.xpath("//div[contains(@class,'editable im-chat-input--text')]");
    private final static By EDIT_BUTTON = By.xpath("//span[@aria-label='Редактировать']");
    private final static By PIN_MESSAGE_BUTTON = By.xpath("//button[@aria-label='Закрепить сообщение']");
    private final static By PIN_MESSAGE = By.xpath(".//div[contains(@class,'pinned_message')]");

    private final static By ACTIONS_ICON = By.xpath(".//div[@class='ui_actions_menu_icons'][.//span[text()='Действия']]");
    private final static By MENU_ACTIONS_ICON = By.xpath("./following-sibling::div[contains(@class,'ui_actions_menu')]");

    private final static By LEAVE_THE_CHAT_BUTTON = By.xpath("//button[.//span[text()='Удалить']]");

    public int getCountDialogues() {
        if (!isElementFoundAndDisplayed(By.xpath("//ul[@id='im_dialogs']"), 10)) {
            Assert.fail("элемент не отображается");
        }
        return driver.findElements(DIALOG).size();
    }

    public void pressAddNewChatButton() {
        driver.findElement(ADD_NEW_CHAT_BUTTON).click();
    }

    public void fillInfoAndPressCreateButton(String title) {
        WebElement we = driver.findElement(CREATE_CHAT);
        we.findElement(INPUT_CHAT_TITLE).sendKeys(title);
        we.findElement(CREATE_CHAT_BUTTON).click();
    }

    public void verifyDialogueWasCreated(String text) {
        Assert.assertTrue(findElement(CHAT_BODY).getText()
                .contains("сегодня\n" + "Тест Автотест создала чат «" + text + "»"), "");
    }

    public MessagePage pressTitleForEdit() {
        driver.findElement(CHAT_HEADER).findElement(By.xpath(".//img")).click();
        return this;
    }

    public void verifyTheDialogueWasPinned(String title) {
        if (isElementFoundAndDisplayed(DIALOG_PINNED, 7)) {
            List<WebElement> chat = driver.findElements(DIALOG);
            //закрепленный диалог всегда 1 в списке .get(0)
            WebElement el = chat.get(0);
            Assert.assertTrue(el.getAttribute("class").contains("dialog_pinned"),
                    "диалог: " + title + " не был закреплен");
        } else {
            Assert.fail("элемент не отображается");
        }
    }

    public MessagePage changeTitleChatAndPressEnter(String newText) {
        WebElement element = driver.findElement(CHAT_EDIT_MODAL_WINDOW).findElement(EDIT_TITLE_FIELD);
        element.click();
        WebElement title = element.findElement(By.xpath(".//textarea"));
        title.sendKeys(Keys.DELETE);
        title.sendKeys(newText);
        title.sendKeys(Keys.ENTER);
        return this;
    }

    public void closeEditModalWindow() {
        findElement(By.xpath("//button[@class='PopupHeader__closeBtn']")).click();
    }

    public void verifyChangeTitle(String oldText, String newText) {
        Assert.assertTrue(findElement(CHAT_BODY).getText()
                .contains("Тест Автотест изменила название чата: «" + oldText + "» → «" + newText + "»"), "");
    }

    public int getCountPeople() {
        return findElement(INFORMATION_MODAL_WINDOW).findElements(By.xpath(".//li[.//div[@class='Entity']]")).size();
    }

    public MessagePage pressAddNewPersonInDialogue() {
        findElement(INFORMATION_MODAL_WINDOW).findElement(ADD_NEW_PERSON_BUTTON).click();
        return this;
    }

    public void addNewPersonInDialogue() {
        WebElement modalWindow = driver.findElement(ADD_NEW_PERSON_MODAL_WINDOW);
        WebElement we = modalWindow.findElement(By.xpath(".//li"));
        we.click();
        Assert.assertTrue(modalWindow.findElement(INPUT_NAME).getText().contains(we.getText()),
                "фио добавленного участника не отображается в строке");
        modalWindow.findElement(ADD_PERSON_BUTTON).click();
    }

    public void closeInformationModalWindow() {
        WebElement modalWindow = driver.findElement(INFORMATION_MODAL_WINDOW);
        modalWindow.findElement(By.xpath(".//button[@class='PopupHeader__closeBtn']")).click();
        Assert.assertTrue(driver.findElement(CHAT_BODY).getText()
                .contains("Тест Автотест пригласила"), "");
    }

    public void writeAndSendMessage(String text) {
        WebElement we = findElement(MESSAGE_FIELD);
        we.sendKeys(text);
        we.sendKeys(Keys.ENTER);
    }

    public void editMessage(String text, String newText) throws InterruptedException {
        findElement(CHAT_BODY).findElement(By.xpath(".//li[.//div[text()='" + text + "']]")).click();
        Thread.sleep(1000);
        findElement(EDIT_BUTTON).click();
        WebElement field = findElement(MESSAGE_FIELD);
        Assert.assertEquals(field.getText(), text, "сообщение не находится на редактировании");
        field.clear();
        field.sendKeys(newText);
        field.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
        Assert.assertTrue(findElement(CHAT_BODY)
                        .findElement(By.xpath(".//li[.//div[contains(text(),'" + newText + "')]]")).isDisplayed(),
                "сообщение не изменено");
    }

    public MessagePage pinAMessageAtTheTop(String text) throws InterruptedException {
        driver.findElement(CHAT_BODY).findElement(By.xpath(".//li[.//div[contains(text(),'" + text + "')]]")).click();
        driver.findElement(PIN_MESSAGE_BUTTON).click();
        Thread.sleep(1000);
        return this;
    }

    public void verifyPinMessage(String text) {
        Assert.assertTrue(driver.findElement(CHAT_BODY).getText()
                        .contains("Тест Автотест закрепила сообщение «" + text + "»"),
                "отсутствует сообщение о закреплении сообщения:" + text);
        Assert.assertTrue(driver.findElement(CHAT_HEADER).findElement(PIN_MESSAGE).getText().contains(text),
                "сообщение: " + text + " не закреплено:");
    }

    public MessagePage actionWithDialogue(String text) {
        WebElement button = driver.findElement(CHAT_HEADER).findElement(ACTIONS_ICON);
        clickWithJavascript(button);
        button.findElement(MENU_ACTIONS_ICON).findElement(By.xpath("./a[normalize-space()='" + text + "']")).click();
        return this;
    }

    public void pressLeaveTheChatButton() {
        findElement(LEAVE_THE_CHAT_BUTTON).click();
    }
}
