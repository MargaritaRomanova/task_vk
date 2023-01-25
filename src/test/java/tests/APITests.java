package tests;

import API.Methods;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import API.VkApi;
import utils.PropertyReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class APITests {

    @Test()
    public void api_vk_edit_profile() {
        VkApi vkApi = new VkApi();

        //•	Получить всю информацию о текущем профиле
        ValidatableResponse responseGetProfileInfo = vkApi.createResponds(Methods.ACCOUNT_GET_PROFILE_INFO.getNameMethod());
        String homeTown = vkApi.getValue(responseGetProfileInfo, "response.home_town");
        String bdate = vkApi.getValue(responseGetProfileInfo, "response.bdate");
        String bdateVisibility = vkApi.getValue(responseGetProfileInfo, "response.bdate_visibility");
        String relation = vkApi.getValue(responseGetProfileInfo, "response.relation");
        String sex = vkApi.getValue(responseGetProfileInfo, "response.sex");

        //•	Заполнить недостающую информацию
        Map<String, Object> map = new HashMap<>();
        map.put("home_town", "Москва");
        map.put("bdate", "2.2.2000");
        map.put("bdate_visibility", "2");
        map.put("relation", "2");
        map.put("sex", "1");
        ValidatableResponse responseSaveProfileInfo = vkApi.createResponds(Methods.ACCOUNT_SAVE_PROFILE_INFO.getNameMethod(), map);
        vkApi.changesAreSuccessful(responseSaveProfileInfo, "response.changed", "1");

        //•	Сменить фото профиля на любую другую
        vkApi.deleteOwnerPhoto();
        vkApi.addOwnerPhoto("кот.jpg");

        //ПОСТУСЛОВИЕ  • Удалить измененные данные профиля
        map = new HashMap<>();
        map.put("home_town", homeTown);
        map.put("bdate", bdate);
        map.put("bdate_visibility", bdateVisibility);
        map.put("relation", relation);
        map.put("sex", sex);
        ValidatableResponse respSaveProfileInfo = vkApi.createResponds(Methods.ACCOUNT_SAVE_PROFILE_INFO.getNameMethod(), map);
        vkApi.changesAreSuccessful(respSaveProfileInfo, "response.changed", "1");
        vkApi.deleteOwnerPhoto();
        vkApi.addOwnerPhoto("dwoilp3BVjlE.jpg");
    }

    @Test()
    public void api_vk_conversation() {
        VkApi apiSteps = new VkApi();
        String random_id = String.valueOf(new Random().nextInt());
        String user_id_friend = PropertyReader.getUser_id_friend();

        //•	Написать сообщение - "Важное сообщение"
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id_friend);
        map.put("message", "Важное сообщение");
        map.put("random_id", random_id);
        ValidatableResponse responseSend = apiSteps.createResponds(Methods.MESSAGES_SEND.getNameMethod(), map);
        String message_id = apiSteps.getValue(responseSend);

        //•	Редактировать сообщение - "Ну очень важное сообщение"
        map = new HashMap<>();
        map.put("peer_id", user_id_friend);
        map.put("message", "Ну очень важное сообщение");
        map.put("random_id", random_id);
        map.put("message_id", message_id);
        ValidatableResponse responseEdit = apiSteps.createResponds(Methods.MESSAGES_EDIT.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseEdit, "response", "1");

        //•	Отметить сообщение как важное
        map = new HashMap<>();
        map.put("important", "1");
        map.put("message_ids", message_id);
        ValidatableResponse responseMarkAsImportant = apiSteps.createResponds(Methods.MESSAGES_MARK_AS_IMPORTANT.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseMarkAsImportant, "response", "[" + message_id + "]");

        //•	Снять отметку с сообщения как важное
        map = new HashMap<>();
        map.put("important", "0");
        map.put("message_ids", message_id);
        ValidatableResponse responseMarkAsNotImportant = apiSteps.createResponds(Methods.MESSAGES_MARK_AS_IMPORTANT.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseMarkAsNotImportant, "response", "[" + message_id + "]");

        // ПОСТУСЛОВИЕ •	Удалить диалог с пользователем
        map = new HashMap<>();
        map.put("peer_id", user_id_friend);
        ValidatableResponse responseDeleteConversation = apiSteps.createResponds(Methods.MESSAGES_DELETE_CONVERSATION.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseDeleteConversation, "response.last_deleted_id", message_id);
    }

    @Test()
    public void api_vk_discussions() {
        VkApi apiSteps = new VkApi();

        //•	Создать группу
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Новая группа автотест");
        map.put("description", "Новая группа автотест");
        map.put("type", "group");
        ValidatableResponse responseGroupCreate = apiSteps.createResponds(Methods.GROUPS_CREATE.getNameMethod(), map);
        apiSteps.parameterNotNull(responseGroupCreate, "response.id");
        String group_id = apiSteps.getValue(responseGroupCreate, "response.id");

        //•	Добавить новую тему для обсуждений
        map = new HashMap<>();
        map.put("group_id", group_id);
        map.put("title", "Тестовое обсуждение");
        map.put("text", "Текст тестового обсуждения");
        ValidatableResponse responseAddTopic = apiSteps.createResponds(Methods.BOARD_ADD_TOPIC.getNameMethod(), map);
        String topic_id = apiSteps.getValue(responseAddTopic);

        //•	Закрепить тему в списке обсуждений группы
        map = new HashMap<>();
        map.put("group_id", group_id);
        map.put("topic_id", topic_id);
        ValidatableResponse responseFixTopic = apiSteps.createResponds(Methods.BOARD_FIX_TOPIC.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseFixTopic, "response", "1");

        //•	Написать 3 комментария с произвольным текстом
        map = new HashMap<>();
        map.put("group_id", group_id);
        map.put("topic_id", topic_id);
        map.put("message", String.valueOf(new Random().nextInt()));
        ValidatableResponse responseCreateComment1 = apiSteps.createResponds(Methods.BOARD_CREATE_COMMENT.getNameMethod(), map);
        String comment_id1 = apiSteps.getValue(responseCreateComment1);
        ValidatableResponse responseCreateComment2 = apiSteps.createResponds(Methods.BOARD_CREATE_COMMENT.getNameMethod(), map);
        String comment_id2 = apiSteps.getValue(responseCreateComment2);
        apiSteps.createResponds(Methods.BOARD_CREATE_COMMENT.getNameMethod(), map);

        //•	Поменять текст в предпоследнем комментарии
        map = new HashMap<>();
        map.put("group_id", group_id);
        map.put("topic_id", topic_id);
        map.put("comment_id", comment_id2);
        map.put("message", "редактированный тестовый комментарий");
        ValidatableResponse responseEditComment = apiSteps.createResponds(Methods.BOARD_EDIT_COMMENT.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseEditComment, "response", "1");

        //•	Удалить первый комментарий
        map = new HashMap<>();
        map.put("group_id", group_id);
        map.put("topic_id", topic_id);
        map.put("comment_id", comment_id1);
        ValidatableResponse responseDeleteComment = apiSteps.createResponds(Methods.BOARD_DELETE_COMMENT.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseDeleteComment, "response", "1");

        //ПОСТУСЛОВИЕ • Покинуть сообщество
        map = new HashMap<>();
        map.put("group_id", group_id);
        ValidatableResponse responseLeave = apiSteps.createResponds(Methods.GROUPS_LEAVE.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseLeave, "response", "1");
    }

    @Test()
    public void api_vk_photos() {
        VkApi apiSteps = new VkApi();

        // •	Создать приватный альбом
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Приватный альбом автотест");
        map.put("description", "описание Приватный альбом автотест");
        map.put("privacy_view", "3");
        map.put("privacy_comment", "3");
        ValidatableResponse responseCreatePrivateAlbum = apiSteps.createResponds(Methods.PHOTOS_CREATE_ALBUM.getNameMethod(), map);
        String album_id = apiSteps.getValue(responseCreatePrivateAlbum, "response.id");

        //•	Добавить фотографию в альбом
        ValidatableResponse responseAddAlbumPhoto = apiSteps.addAlbumPhoto(album_id,"для_альбома.jpg");
        apiSteps.parameterNotNull(responseAddAlbumPhoto, "response.id");
        String photo_id = apiSteps.getValue(responseAddAlbumPhoto, "response.id");
        String owner_id = apiSteps.getValue(responseAddAlbumPhoto, "response.owner_id");

        //•	Сделать фотографию обложкой альбома
        map = new HashMap<>();
        map.put("album_id", album_id);
        map.put("photo_id", photo_id);
        ValidatableResponse responseMakeCover = apiSteps.createResponds(Methods.PHOTOS_MAKE_COVER.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseMakeCover, "response", "1");

        //•	Прокомментировать фотографию
        map = new HashMap<>();
        map.put("photo_id", photo_id);
        map.put("message", "Тестовый комментарий");
        ValidatableResponse responseCreateComment = apiSteps.createResponds(Methods.PHOTOS_CREATE_COMMENT.getNameMethod(), map);
        apiSteps.parameterNotNull(responseCreateComment, "response");

        //•	Добавить отметку на фотографию
        map = new HashMap<>();
        map.put("photo_id", photo_id);
        map.put("user_id", owner_id);
        map.put("x", "30");
        map.put("y", "30");
        map.put("x2", "50");
        map.put("y2", "50");
        ValidatableResponse responsePutTag = apiSteps.createResponds(Methods.PHOTOS_PUT_TAG.getNameMethod(), map);
        String tag_id = apiSteps.getValue(responsePutTag, "response");

        map = new HashMap<>();
        map.put("tag_id", tag_id);
        map.put("photo_id", photo_id);
        ValidatableResponse responseConfirmTag = apiSteps.createResponds(Methods.PHOTOS_CONFIRM_TAG.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseConfirmTag, "response", "1");

        //•	Создать публичный альбом
        map = new HashMap<>();
        map.put("title", "Публичный альбом автотест");
        map.put("description", "описание Публичный альбом автотест");
        map.put("privacy_view", "0");
        map.put("privacy_comment", "0");
        ValidatableResponse responseCreatePublicAlbum = apiSteps.createResponds(Methods.PHOTOS_CREATE_ALBUM.getNameMethod(), map);
        String album_public_id = apiSteps.getValue(responseCreatePublicAlbum, "response.id");

        //•	Перенести туда фотографию
        map = new HashMap<>();
        map.put("target_album_id", album_public_id);
        map.put("photo_id", photo_id);
        ValidatableResponse responseMove = apiSteps.createResponds(Methods.PHOTOS_MOVE.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseMove, "response", "1");

        //•	Удалить первый альбом
        map = new HashMap<>();
        map.put("album_id", album_id);
        ValidatableResponse responseDeleteAlbum = apiSteps.createResponds(Methods.PHOTOS_DELETE_ALBUM.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseDeleteAlbum, "response", "1");

        //ПОСТУСЛОВИЕ • Удалить второй альбом
        map = new HashMap<>();
        map.put("album_id", album_public_id);
        ValidatableResponse responseDeleteAlbum2 = apiSteps.createResponds(Methods.PHOTOS_DELETE_ALBUM.getNameMethod(), map);
        apiSteps.changesAreSuccessful(responseDeleteAlbum2, "response", "1");
    }
}
