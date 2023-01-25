package tests;

import UI.pages.Profile.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import UI.pages.Auth.AuthLoginPage;
import UI.pages.Auth.AuthPassPage;
import utils.PropertyReader;

public class UITests extends BaseTest {

    @Test()
    public void vk_edit_profile() {
        AuthLoginPage authLoginPage = new AuthLoginPage();
        authLoginPage.verifyLoginFieldIsEmpty();
        Assert.assertTrue(authLoginPage.getLoginField().isEmpty(),
                "поле ввода логина не удалось очистить");
        authLoginPage.inputLogin(PropertyReader.getLogin());
        authLoginPage.checkInputLoginContainsText(PropertyReader.getLogin());
        authLoginPage.verifyCheckboxDeActive();

        AuthPassPage authPassPage = authLoginPage.pressSignInButton();
        authPassPage.inputPass(PropertyReader.getPassword());
        authPassPage.checkInputPasswordContainsText();
        NewsPage newsPage = authPassPage.pressContinueButton();
        newsPage.choosePageInSideBar("Моя страница");

        MyPage myPage = new MyPage();
        EditPage editPage = myPage.pressEditProfileButton();

        //•	Получить всю информацию о текущем профиле
        editPage.getProfileName();

        //•	Заполнить недостающую информацию
        String infoAboutMyself = editPage.inputInfoAboutMyselfField("поле заполнено автотестом");
        String status = editPage.inputStatusDropdown("В активном поиске");
        String day = editPage.inputDay("2");
        String month = editPage.inputMonth("Марта");
        String year = editPage.inputYear("2000");
        String town = editPage.inputHomeTownField("Москва");

        //•	Сменить фото профиля на любую другую
        editPage.avatarAction("Загрузить фотографию");
        editPage.pressSaveButton();
        editPage.verifyInfoWasSaved();

        //ПОСТУСЛОВИЕ  • Удалить измененные данные профиля
        editPage.avatarAction("Удалить");

        editPage.inputInfoAboutMyselfField(infoAboutMyself);
        editPage.inputStatusDropdown(status);
        editPage.inputDay(day);
        editPage.inputMonth(month);
        editPage.inputYear(year);
        editPage.inputHomeTownField(town);
        editPage.pressSaveButton();
        editPage.verifyInfoWasSaved();
    }

    @Test()
    public void vk_conversation() throws InterruptedException {
        AuthLoginPage authLoginPage = new AuthLoginPage();
        authLoginPage.verifyLoginFieldIsEmpty();
        Assert.assertTrue(authLoginPage.getLoginField().isEmpty(),
                "поле ввода логина не удалось очистить");
        authLoginPage.inputLogin(PropertyReader.getLogin());
        authLoginPage.checkInputLoginContainsText(PropertyReader.getLogin());
        authLoginPage.verifyCheckboxDeActive();

        AuthPassPage authPassPage = authLoginPage.pressSignInButton();
        authPassPage.inputPass(PropertyReader.getPassword());
        authPassPage.checkInputPasswordContainsText();
        NewsPage newsPage = authPassPage.pressContinueButton();
        newsPage.choosePageInSideBar("Мессенджер");

        MessengerPage messengerPage = new MessengerPage();
        int countBeforeCreate = messengerPage.getCountDialogues();

        //•	Создать беседу
        messengerPage.pressAddNewChatButton();
        messengerPage.fillInfoAndPressCreateButton("Просто беседа");
        Thread.sleep(1000);
        messengerPage.verifyDialogueWasCreated("Просто беседа");

        int countAfterCreate = messengerPage.getCountDialogues();
        Assert.assertEquals(countBeforeCreate + 1, countAfterCreate, "диалог не был создан");

        //•	Пометить беседу как "важно"
        messengerPage.actionWithDialogue("Закрепить в списке чатов");
        messengerPage.verifyTheDialogueWasPinned("Просто беседа");

        //•	Переименовать беседу в "Ну очень важная беседа"
        messengerPage.pressTitleForEdit();
        messengerPage.changeTitleChatAndPressEnter("Ну очень важная беседа");
        messengerPage.closeEditModalWindow();
        Thread.sleep(1000);
        messengerPage.verifyChangeTitle("Просто беседа", "Ну очень важная беседа");

        messengerPage.pressTitleForEdit();
        int countPersonBeforeAddDialogue = messengerPage.getCountPeople();

        //•	Добавить нового участника
        messengerPage.pressAddNewPersonInDialogue();
        messengerPage.addNewPersonInDialogue();

        int countPersonAfterAddDialogue = messengerPage.getCountPeople();
        Assert.assertEquals(countPersonBeforeAddDialogue + 1, countPersonAfterAddDialogue,
                "новый участник не был добавлен в диалог");

        messengerPage.closeInformationModalWindow();

        //•	Написать в группу сообщение - "Это очень важная беседа, выходить!"
        messengerPage.writeAndSendMessage("Это очень важная беседа, выходить!");

        //•	Отредактировать сообщение - "Это очень важная беседа, НЕ выходить!"
        messengerPage.editMessage("Это очень важная беседа, выходить!", "Это очень важная беседа, НЕ выходить!");

        //•	Закрепить сообщение
        messengerPage.pinAMessageAtTheTop("Это очень важная беседа, НЕ выходить!");
        Thread.sleep(1000);
        messengerPage.verifyPinMessage("Это очень важная беседа, НЕ выходить!");

        //•	Написать в группу сообщение - "А нет, лучше в группу"
        messengerPage.writeAndSendMessage("А нет, лучше в группу");

        //ПОСТУСЛОВИЕ  • Удалить беседу
        messengerPage.actionWithDialogue("Выйти из чата");
        messengerPage.activateAllMessagesCheckbox();
        messengerPage.pressLeaveTheChatButton();
        Thread.sleep(1000);
        int countAfterDelete = messengerPage.getCountDialogues();
        Assert.assertEquals(countAfterCreate - 1, countAfterDelete, "диалог не был удален");
    }

//    @Test()
//    public void vk_discussions() {
//        AuthLoginPage authLoginPage = new AuthLoginPage();
//        authLoginPage.verifyLoginFieldIsEmpty();
//        Assert.assertTrue(authLoginPage.getLoginField().isEmpty(),
//                "поле ввода логина не удалось очистить");
//        authLoginPage.inputLogin(PropertyReader.getLogin());
//        authLoginPage.checkInputLoginContainsText(PropertyReader.getLogin());
//        authLoginPage.verifyCheckboxDeActive();
//
//        AuthPassPage authPassPage = authLoginPage.pressSignInButton();
//        authPassPage.inputPass(PropertyReader.getPassword());
//        authPassPage.checkInputPasswordContainsText();
//        NewsPage newsPage = authPassPage.pressContinueButton();
//        newsPage.choosePageInSideBar("Мессенджер");
//    }

    @Test()
    public void vk_photos() throws InterruptedException {
        AuthLoginPage authLoginPage = new AuthLoginPage();
        authLoginPage.verifyLoginFieldIsEmpty();
        Assert.assertTrue(authLoginPage.getLoginField().isEmpty(),
                "поле ввода логина не удалось очистить");
        authLoginPage.inputLogin(PropertyReader.getLogin());
        authLoginPage.checkInputLoginContainsText(PropertyReader.getLogin());
        authLoginPage.verifyCheckboxDeActive();

        AuthPassPage authPassPage = authLoginPage.pressSignInButton();
        authPassPage.inputPass(PropertyReader.getPassword());
        authPassPage.checkInputPasswordContainsText();
        NewsPage newsPage = authPassPage.pressContinueButton();
        newsPage.choosePageInSideBar("Фотографии");

        PhotoPage photoPage = new PhotoPage();

        //•	Создать приватный альбом
        photoPage.pressCreateAlbumButton();
        photoPage.inputFieldAndCreateAlbum("Tест Приватный",
                "Автотест Приватный Альбом Описание", "Только я", "Только я");

        //•	Добавить фотографию в альбом
        photoPage.loadPhoto();
        photoPage.verifyCountPhotoInAlbum(1);

        photoPage.clickOnPhoto();

        //•	Сделать фотографию обложкой альбома
        photoPage.moreActionsWithPhoto("Сделать обложкой альбома");
        photoPage.verifyPhotoAsAlbumCover();

        //•	Прокомментировать фотографию
        photoPage.addCommentForPhoto("Тестовый комментарий для фотографии");

        //•	Добавить отметку на фотографию
        photoPage.tagAPersonOnPhoto("Отметить человека", "Тест Автотест");
        photoPage.pressEscape();

        photoPage.choosePageInSideBar("Фотографии");
        photoPage.verifyAlbumIsPrivate("Tест Приватный");

        //•	Создать публичный альбом
        photoPage.pressCreateAlbumButton();
        photoPage.inputFieldAndCreateAlbum("Tест публичный",
                "Автотест публичный Альбом Описание", "Все пользователи", "Все пользователи");

        photoPage.choosePageInSideBar("Фотографии");
        photoPage.openAlbum("Tест публичный");
        photoPage.verifyCountPhotoInAlbum(0);

        photoPage.back();
        photoPage.openAlbum("Tест Приватный");
        photoPage.verifyCountPhotoInAlbum(1);
        photoPage.clickOnPhoto();

        //•	Перенести туда фотографию
        photoPage.moreActionsWithPhoto("Перенести в альбом");
        photoPage.importPhotoInAlbum("Tест публичный");
        photoPage.verifyNotificationImportPhoto();
        photoPage.pressEscape();
        photoPage.refresh();
        photoPage.verifyCountPhotoInAlbum(0);

        photoPage.back();
        photoPage.openAlbum("Tест публичный");
        photoPage.verifyCountPhotoInAlbum(1);

        photoPage.back();
        photoPage.openAlbum("Tест Приватный");

        //•	Удалить первый альбом
        photoPage.deleteAlbum("Tест Приватный");

        //ПОСТУСЛОВИЕ  • Удалить публичный альбом
        photoPage.openAlbum("Tест публичный");
        photoPage.deleteAlbum("Tест публичный");
        photoPage.verifyAllAlbumsWereDeleted();
    }
}
