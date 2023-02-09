package API.FRAMEWORK;

import API.Methods;
import io.restassured.response.Response;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static API.ApiCore.*;
import static io.restassured.RestAssured.given;

/**
 * Класс, содержащий основную информацию о профиле
 */
public class ProfileInfo {

    private final Map<String, Object> profileInfo;
    private static final String HOME_TOWN = "home_town";
    private static final String BDATE = "bdate";
    private static final String BDATE_VISIBILITY = "bdate_visibility";
    private static final String RELATION = "relation";
    private static final String SEX = "sex";

    ProfileInfo(Object home_town, Object bDate, Object bDate_visibility, Object relation, Object sex) {
        profileInfo = new HashMap<>();
        profileInfo.put(HOME_TOWN, home_town);
        profileInfo.put(BDATE, bDate);
        profileInfo.put(BDATE_VISIBILITY, bDate_visibility);
        profileInfo.put(RELATION, relation);
        profileInfo.put(SEX, sex);
    }

    /**
     * Метод получает основную информацию текущего профиля
     */
    static ProfileInfo getProfileInfoForCurrentUser() {
        Response responseGetProfileInfo = createResponds(Methods.ACCOUNT_GET_PROFILE_INFO.getNameMethod());
        String homeTown = getValue(responseGetProfileInfo, "response.home_town");
        String bdate = getValue(responseGetProfileInfo, "response.bdate");
        String bdateVisibility = getValue(responseGetProfileInfo, "response.bdate_visibility");
        String relation = getValue(responseGetProfileInfo, "response.relation");
        String sex = getValue(responseGetProfileInfo, "response.sex");
        return new ProfileInfo(homeTown, bdate, bdateVisibility, relation, sex);
    }

    /**
     * Метод сохраняет измененную информацию профиля
     */
    void saveCurrentProfileInfoOnSite() {
        Response responseSaveProfileInfo = createResponds(Methods.ACCOUNT_SAVE_PROFILE_INFO.getNameMethod(), profileInfo);
        changesAreSuccessful(responseSaveProfileInfo, "response.changed", "1");
    }

    /**
     * Метод удаляет главную фотографию профиля
     */
    static void deleteOwnerPhoto() {
        Response response = createResponds(Methods.PHOTOS_GET_PROFILE.getNameMethod());
        String id = getValue(response, "response.items.id");

        Map<String, Object> map = Map.of(
                "photo_id", id);
        Response responseDelete = createResponds(Methods.PHOTOS_DELETE.getNameMethod(), map);
        changesAreSuccessful(responseDelete, "response", "1");
    }

    /**
     * Метод позволяет сохранить главную фотографию пользователя или сообщества
     *
     * @param photoName - название фотографии
     */
    static void addOwnerPhoto(String photoName) {
        String filePath = Paths.get(Paths.get("").toAbsolutePath().toString(),
                "src", "test", "resources", "files", photoName).toString();
        Response resp = given()
                .multiPart("photo", new File(filePath))
                .when()
                .post(getValue(createResponds(Methods.PHOTOS_GET_OWNER_PHOTO_UPLOAD_SERVER.getNameMethod()), "response.upload_url"))
                .then().log().all()
                .extract().response();

        // сохранение фото
        Map<String, Object> params = Map.of(
                "server", resp.path("server").toString(),
                "hash", resp.path("hash").toString(),
                "photo", resp.path("photo").toString());
        Response responseSave = createResponds(Methods.PHOTOS_SAVE_OWNER_PHOTO.getNameMethod(), params);

        parameterNotNull(responseSave, "response.photo_src");
        changesAreSuccessful(responseSave, "response.saved", "1");
    }
}
