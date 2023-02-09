package tests;

import API.FRAMEWORK.*;
import org.testng.annotations.Test;
import utils.PropertyReader;

public class APITests {

    @Test()
    public void api_vk_edit_profile() {
        //•  Получить всю информацию о текущем профиле
        Profile profile = new Profile();
        ProfileInfo currentProfileInfoOrigin = profile.getProfileInfo();

        //•  Заполнить недостающую информацию
        profile.setProfileInfo("Москва", "2.2.2000", "2", "2", "1");

        //•	Сменить фото профиля на любую другую
        profile.deleteAndUploadPhoto("avatar.jpg");

        //ПОСТУСЛОВИЕ  • Удалить измененные данные профиля
        profile.setProfileInfo(currentProfileInfoOrigin);
        profile.deleteAndUploadPhoto("avatar2.jpg");
    }

    @Test()
    public void api_vk_conversation() {
        //•	Написать сообщение - "Важное сообщение"
        Dialog dialog = new Dialog(PropertyReader.getUser_id_friend());
        String message_id = dialog.messageSend("Важное сообщение");

        //•	Редактировать сообщение - "Ну очень важное сообщение"
        dialog.messageEdit("Ну очень важное сообщение", message_id);

        //•	Отметить сообщение как важное
        dialog.messageMark(true, message_id);

        //•	Снять отметку с сообщения как важное
        dialog.messageMark(false, message_id);

        // ПОСТУСЛОВИЕ •	Удалить диалог с пользователем
        dialog.deleteConversation(message_id);
    }

    @Test()
    public void api_vk_discussions() {
        //•	Создать группу
        Discussions discussions = new Discussions();
        String group_id = discussions.createGroup("Новая группа автотест", "Новая группа автотест", "group");

        //•	Добавить новую тему для обсуждений
        String topic_id = discussions.addTopicInGroup(group_id, "Тестовое обсуждение", "Текст тестового обсуждения");

        //•	Закрепить тему в списке обсуждений группы
        discussions.fixTopicInGroup(group_id, topic_id);

        //•	Написать 3 комментария с произвольным текстом
        String comment_id1 = discussions.addCommentInTopicInGroup(group_id, topic_id, "Тестовый");
        String comment_id2 = discussions.addCommentInTopicInGroup(group_id, topic_id, "Тестовый комментарий");
        discussions.addCommentInTopicInGroup(group_id, topic_id, "АвтоТестовый комментарий");

        //•	Поменять текст в предпоследнем комментарии
        discussions.editComment(group_id, topic_id, comment_id2, "редактированный тестовый комментарий");

        //•	Удалить первый комментарий
        discussions.deleteComment(group_id, topic_id, comment_id1);

        //ПОСТУСЛОВИЕ • Покинуть сообщество
        discussions.leaveGroup(group_id);
    }

    @Test()
    public void api_vk_photos() {
        // •	Создать приватный альбом
        PhotoAlbum photos = new PhotoAlbum();
        String album_id = photos.createAlbum(true, "Приватный альбом автотест",
                "описание Приватный альбом автотест");

        //•	Добавить фотографию в альбом
        String[] value = photos.addPhotoInAlbum(album_id, "album.jpg");
        String photo_id = value[0];
        String owner_id = value[1];

        //•	Сделать фотографию обложкой альбома
        photos.makeCoverPhoto(album_id, photo_id);

        //•	Прокомментировать фотографию
        photos.createCommentForPhoto(photo_id, "Тестовый комментарий");

        //•	Добавить отметку на фотографию
        String tag_id = photos.putTagForPhoto(photo_id, owner_id);
        photos.confirmTagForPhoto(tag_id, photo_id);

        //•	Создать публичный альбом
        String album_id_public = photos.createAlbum(false, "Публичный альбом автотест",
                "описание Публичный альбом автотест");

        //•	Перенести туда фотографию
        photos.movePhoto(album_id_public, photo_id);

        //•	Удалить первый альбом
        photos.deleteAlbum(album_id);

        //ПОСТУСЛОВИЕ • Удалить второй альбом
        photos.deleteAlbum(album_id_public);
    }
}
