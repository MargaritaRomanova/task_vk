package API.FRAMEWORK;

import API.Methods;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static API.ApiCore.*;
import static io.restassured.RestAssured.given;

/**
 * Класс для работы с альбомом для хранения фотографий пользователя
 */
public class PhotoAlbum {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PRIVACY_VIEW = "privacy_view";
    private static final String PRIVACY_COMMENT = "privacy_comment";
    private static final String AlBUM_ID = "album_id";
    private static final String PHOTO_ID = "photo_id";
    private static final String MESSAGE = "message";
    private static final String TAG_ID = "tag_id";
    private static final String TARGET_ALBUM_ID = "target_album_id";

    /**
     * Метод для создания пустого альбома для фотографий
     *
     * @param privateAlbum - создание приватного альбома, значение - true
     *                     - создание публичного альбома, значение - false
     * @param title        - название альбома
     * @param description  - описание альбома
     * @return возвращает String параметра album_id (идентификатор созданного альбома)
     */
    public String createAlbum(boolean privateAlbum, String title, String description) {
        String value;
        if (privateAlbum) value = "3";
        else value = "0";

        Map<String, Object> photos = Map.of(
                TITLE, title,
                DESCRIPTION, description,
                PRIVACY_VIEW, value,
                PRIVACY_COMMENT, value);
        Response responseCreatePrivateAlbum = createResponds(Methods.PHOTOS_CREATE_ALBUM.getNameMethod(), photos);
        return getValue(responseCreatePrivateAlbum, "response.id");
    }

    /**
     * Метод для добавления фотографии в альбом
     *
     * @param album_id  - идентификатор альбома
     * @param photoName - название фотографии
     * @return возвращает массив String параметров photo_id (идентификатор фотографии),
     * owner_id (Идентификатор пользователя, которому принадлежит фотография)
     */
    public String[] addPhotoInAlbum(String album_id, String photoName) {
        Response responseAddAlbumPhoto = getAddressServer(album_id, photoName);
        parameterNotNull(responseAddAlbumPhoto, "response.id");
        String photo_id = getValue(responseAddAlbumPhoto, "response.id");
        String owner_id = getValue(responseAddAlbumPhoto, "response.owner_id");
        return new String[]{photo_id, owner_id};
    }

    /**
     * Метод возвращает адрес сервера для загрузки фотографий
     *
     * @param album_id  - идентификатор альбома
     * @param photoName - название фотографии
     * @return возвращает массив Response (используется в методе addPhotoInAlbum)
     */
    private static Response getAddressServer(String album_id, String photoName) {
        String filePath = Paths.get(Paths.get("").toAbsolutePath().toString(),
                "src", "test", "resources", "files", photoName).toString();
        Map<String, Object> params = Map.of(
                "album_id", album_id);
        Response responseServer = createResponds(Methods.PHOTOS_GET_UPLOAD_SERVER.getNameMethod(), params);
        String url = getValue(responseServer, "response.upload_url");

        Map<String, Object> response = given()
                .multiPart("file1", new File(filePath))
                .when()
                .post(url)
                .then().contentType(ContentType.HTML)
                .extract().jsonPath().getJsonObject("");
        response.putAll(params);
        return createResponds(Methods.PHOTOS_SAVE.getNameMethod(), response);
    }

    /**
     * Метод делает фотографию обложкой альбома
     *
     * @param album_id - идентификатор альбома
     * @param photo_id - идентификатор фотографии
     */
    public void makeCoverPhoto(String album_id, String photo_id) {
        Map<String, Object> photos = Map.of(
                AlBUM_ID, album_id,
                PHOTO_ID, photo_id);
        Response responseMakeCover = createResponds(Methods.PHOTOS_MAKE_COVER.getNameMethod(), photos);
        changesAreSuccessful(responseMakeCover, "response", "1");
    }

    /**
     * Метод для создания нового комментария к фотографии
     *
     * @param photo_id - идентификатор фотографии
     * @param message  - текст комментария
     */
    public void createCommentForPhoto(String photo_id, String message) {
        Map<String, Object> photos = Map.of(
                PHOTO_ID, photo_id,
                MESSAGE, message);
        Response responseCreateComment = createResponds(Methods.PHOTOS_CREATE_COMMENT.getNameMethod(), photos);
        parameterNotNull(responseCreateComment, "response");
    }

    /**
     * Метод добавляет отметку на фотографию
     *
     * @param photo_id - идентификатор фотографии
     * @param owner_id - идентификатор пользователя, которому принадлежит фотография
     * @return возвращает String параметра tag_id (идентификатор отметки на фотографии.)
     */
    public String putTagForPhoto(String photo_id, String owner_id) {
        Map<String, Object> photos = Map.of(
                "photo_id", photo_id,
                "user_id", owner_id,
                "x", "30",
                "y", "30",
                "x2", "50",
                "y2", "50");
        Response responsePutTag = createResponds(Methods.PHOTOS_PUT_TAG.getNameMethod(), photos);
        return getValue(responsePutTag, "response");
    }

    /**
     * Метод подтверждает отметку на фотографии
     *
     * @param tag_id   - идентификатор отметки на фотографии
     * @param photo_id - идентификатор фотографии
     */
    public void confirmTagForPhoto(String tag_id, String photo_id) {
        Map<String, Object> photos = Map.of(
                TAG_ID, tag_id,
                PHOTO_ID, photo_id);
        Response responseConfirmTag = createResponds(Methods.PHOTOS_CONFIRM_TAG.getNameMethod(), photos);
        changesAreSuccessful(responseConfirmTag, "response", "1");
    }

    /**
     * Метод переносит фотографию из одного альбома в другой
     *
     * @param album_id - идентификатор альбома, в который нужно переместить фотографию
     * @param photo_id - идентификатор фотографии
     */
    public void movePhoto(String album_id, String photo_id) {
        Map<String, Object> photos = Map.of(
                TARGET_ALBUM_ID, album_id,
                PHOTO_ID, photo_id);
        Response responseMove = createResponds(Methods.PHOTOS_MOVE.getNameMethod(), photos);
        changesAreSuccessful(responseMove, "response", "1");
    }

    /**
     * Метод удаляет указанный альбом для фотографий у текущего пользователя
     *
     * @param album_id - идентификатор альбома
     */
    public void deleteAlbum(String album_id) {
        Map<String, Object> photos = Map.of(
                AlBUM_ID, album_id);
        Response responseDeleteAlbum = createResponds(Methods.PHOTOS_DELETE_ALBUM.getNameMethod(), photos);
        changesAreSuccessful(responseDeleteAlbum, "response", "1");
    }
}
