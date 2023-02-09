package tests;

import UI.pages.Profile.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import UI.pages.Auth.AuthLoginPage;
import UI.pages.Auth.AuthPassPage;
import utils.PropertyReader;

public class UITests extends BaseTest {

    /**
     * Метод авторизации в системе vk
     *
     * @return возвращает страницу NewsPage
     */
    private NewsPage login() {
        AuthLoginPage authLoginPage = new AuthLoginPage();
        authLoginPage.verifyLoginFieldIsEmpty();
        Assert.assertTrue(authLoginPage.getLoginField().isEmpty(),
                "поле ввода логина не удалось очистить");
        AuthPassPage authPassPage = authLoginPage.inputLogin(PropertyReader.getLogin())
                .checkInputLoginContainsText(PropertyReader.getLogin())
                .pressSignInButton();
        authPassPage.inputPass(PropertyReader.getPassword())
                .checkInputPasswordContainsText();
        return authPassPage.pressContinueButton();
    }

    @Test(description = "редактирование личного профиля")
    public void vk_edit_profile() {
        NewsPage newsPage = login();
        newsPage.choosePageInSideBar("Моя страница");
        EditPage editPage = new MyPage()
                .pressEditProfileButton();

        //•	Получить всю информацию о текущем профиле
        editPage.getProfileName();

        //•	Заполнить недостающую информацию
        String infoAboutMyself = editPage.inputInfoAboutMyselfField("поле заполнено автотестом");
        String status = editPage.inputStatusDropdown("В активном поиске");
        String town = editPage.inputHomeTownField("Москва");

        //•	Сменить фото профиля
        editPage.deleteAvatar();
        editPage.uploadAvatar("avatar.jpg")
                .pressSaveButton()
                .verifyInfoWasSaved();

        //ПОСТУСЛОВИЕ  • Удалить измененные данные профиля
        editPage.deleteAvatar();
        editPage.uploadAvatar("avatar2.jpg");

        editPage.inputInfoAboutMyselfField(infoAboutMyself);
        editPage.inputStatusDropdown(status);
        editPage.inputHomeTownField(town);
        editPage.pressSaveButton()
                .verifyInfoWasSaved();
    }

    @Test(description = "работа с личными сообщениями")
    public void vk_conversation() throws InterruptedException {
        NewsPage newsPage = login();
        newsPage.choosePageInSideBar("Мессенджер");

        MessagePage messengerPage = new MessagePage();
        int countBeforeCreate = messengerPage.getCountDialogues();

        //•	Создать беседу
        messengerPage.pressAddNewChatButton();
        messengerPage.fillInfoAndPressCreateButton("Просто беседа");
        Thread.sleep(1000);
        messengerPage.verifyDialogueWasCreated("Просто беседа");

        int countAfterCreate = messengerPage.getCountDialogues();
        Assert.assertEquals(countBeforeCreate + 1, countAfterCreate, "диалог не был создан");

        //•	Пометить беседу как "важно"
        messengerPage.actionWithDialogue("Закрепить в списке чатов")
                .verifyTheDialogueWasPinned("Просто беседа");

        //•	Переименовать беседу в "Ну очень важная беседа"
        messengerPage.pressTitleForEdit()
                .changeTitleChatAndPressEnter("Ну очень важная беседа")
                .closeEditModalWindow();
        Thread.sleep(1000);
        messengerPage.verifyChangeTitle("Просто беседа", "Ну очень важная беседа");

        messengerPage.pressTitleForEdit();
        int countPersonBeforeAddDialogue = messengerPage.getCountPeople();

        //•	Добавить нового участника
        messengerPage.pressAddNewPersonInDialogue()
                .addNewPersonInDialogue();

        int countPersonAfterAddDialogue = messengerPage.getCountPeople();
        Assert.assertEquals(countPersonBeforeAddDialogue + 1, countPersonAfterAddDialogue,
                "новый участник не был добавлен в диалог");

        messengerPage.closeInformationModalWindow();

        //•	Написать в группу сообщение - "Это очень важная беседа, выходить!"
        messengerPage.writeAndSendMessage("Это очень важная беседа, выходить!");

        //•	Отредактировать сообщение - "Это очень важная беседа, НЕ выходить!"
        messengerPage.editMessage("Это очень важная беседа, выходить!", "Это очень важная беседа, НЕ выходить!");

        //•	Закрепить сообщение
        messengerPage.pinAMessageAtTheTop("Это очень важная беседа, НЕ выходить!")
                .verifyPinMessage("Это очень важная беседа, НЕ выходить!");

        //•	Написать в группу сообщение - "А нет, лучше в группу"
        messengerPage.writeAndSendMessage("А нет, лучше в группу");

        //ПОСТУСЛОВИЕ  • Удалить беседу
        messengerPage.actionWithDialogue("Очистить историю сообщений")
                .pressLeaveTheChatButton();
        messengerPage.refresh();
        int countAfterDelete = messengerPage.getCountDialogues();
        Assert.assertEquals(countAfterCreate - 1, countAfterDelete, "диалог не был удален");
    }

    @Test(description = "работа с группами")
    public void vk_discussions() throws InterruptedException {
        NewsPage newsPage = login();
        newsPage.choosePageInSideBar("Сообщества");
        DiscussionsPage discussionsPage = new DiscussionsPage();

        //•	Создать группу
        discussionsPage.pressCreateGroupButton()
                .selectTypeOfGroup("Группа по интересам")
                .inputFieldsAndPressCreateButton("Мототехника", "Мототехника");

        //•	Добавить новую тему для обсуждений
        discussionsPage.selectDiscussions()
                .createTopic("Новая тема", "Описание");

        //•	Закрепить тему в списке обсуждений группы
        discussionsPage.pressEditTopicButton()
                .activatePinTopicCheckbox()
                .pressSaveButton()
                .verifyInfoWasSaved();
        discussionsPage.goToCrumb();
        discussionsPage.verifyPinTopic("Новая тема");
        discussionsPage.selectTopic("Новая тема");

        //•	Написать 3 комментария с произвольным текстом
        String message_id1 = discussionsPage.writeMessage("первое сообщение");
        String message_id2 = discussionsPage.writeMessage("второе сообщение");
        discussionsPage.writeMessage("третье сообщение");

        //•	Поменять текст в предпоследнем комментарии
        discussionsPage.editComment(message_id2, "измененное второе сообщение")
                .verifyCommentWasEdited(message_id2, "измененное второе сообщение");

        //•	Удалить первый комментарий
        discussionsPage.deleteComment(message_id1)
                .verifyCommentWasDeleted(message_id1);

        //•	ПОСТУСЛОВИЕ удалить тему\ покинуть группу
        discussionsPage.pressEditTopicButton()
                .deleteTopic()
                .verifyTopicWasDeleted();
        discussionsPage.choosePageInSideBar("Сообщества");
        discussionsPage.deleteGroup();
    }

    @Test(description = "работа с фотографиями в альбомах")
    public void vk_photos() throws InterruptedException {
        NewsPage newsPage = login();
        newsPage.choosePageInSideBar("Фотографии");
        PhotoPage photoPage = new PhotoPage();

        //•	Создать приватный альбом
        photoPage.pressCreateAlbumButton()
                .inputFieldAndCreateAlbum("private album",
                        "description", "Только я", "Только я");

        //•	Добавить фотографию в альбом
        photoPage.loadPhoto()
                .verifyCountPhotoInAlbum(1);

        //•	Сделать фотографию обложкой альбома
        photoPage.clickOnPhoto()
                .moreActionsWithPhoto("Сделать обложкой альбома")
                .verifyPhotoAsAlbumCover();

        //•	Прокомментировать фотографию
        photoPage.addCommentForPhoto("Тестовый комментарий для фотографии");

        //•	Добавить отметку на фотографию
        photoPage.tagAPersonOnPhoto("Отметить человека", "Тест Автотест");
        photoPage.pressEscape();
        photoPage.choosePageInSideBar("Фотографии");
        photoPage.verifyAlbumIsPrivate("private album");

        //•	Создать публичный альбом
        photoPage.pressCreateAlbumButton()
                .inputFieldAndCreateAlbum("public album",
                        "description", "Все пользователи", "Все пользователи");

        photoPage.choosePageInSideBar("Фотографии");
        photoPage.openAlbum("public album")
                .verifyCountPhotoInAlbum(0);
        photoPage.back();
        photoPage.openAlbum("private album")
                .verifyCountPhotoInAlbum(1);

        //•	Перенести туда фотографию
        photoPage.clickOnPhoto()
                .moreActionsWithPhoto("Перенести в альбом")
                .importPhotoInAlbum("public album")
                .verifyNotificationImportPhoto();
        photoPage.pressEscape();
        photoPage.refresh();
        photoPage.verifyCountPhotoInAlbum(0);
        photoPage.back();
        photoPage.openAlbum("public album")
                .verifyCountPhotoInAlbum(1);
        photoPage.back();

        //•	Удалить первый альбом
        photoPage.openAlbum("private album")
                .deleteAlbum();

        //ПОСТУСЛОВИЕ  • Удалить публичный альбом
        photoPage.openAlbum("public album");
        photoPage.deleteAlbum()
                .verifyAllAlbumsWereDeleted();
    }
}
