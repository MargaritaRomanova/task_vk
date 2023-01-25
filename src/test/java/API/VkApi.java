package API;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import utils.PropertyReader;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class VkApi {
//apiCore
    public static String getUrlApi(String method) {
        return PropertyReader.getUrlApi1() + method + "?access_token=" + PropertyReader.getAccessToken() + PropertyReader.getUrlApi2();
    }

    public static ValidatableResponse createResponds(String method, Map<String, Object> params) {
        return given().when().queryParams(params).post(getUrlApi(method)).then().log().body().and().assertThat().statusCode(200);
    }

    public static ValidatableResponse createResponds(String method) {
        return given().when().post(getUrlApi(method)).then().log().body().and().assertThat().statusCode(200);
    }

    public static String getValue(ValidatableResponse response, String path) {
        return response.extract().path(path).toString().replaceAll("[\\[\\]\"]", "");
    }

    public static String getValue(ValidatableResponse response) {
        return response.extract().path("response").toString();
    }

    public static void changesAreSuccessful(ValidatableResponse response, String path, String value) {
        Assert.assertEquals(response.extract().path(path).toString(), value, "изменения не сохранены");
    }

    public static void parameterNotNull(ValidatableResponse response, String value) {
        Assert.assertNotNull(response.extract().path(value), "изменения не сохранены/значение отсутствует");
    }

    public static void deleteOwnerPhoto() {
        ValidatableResponse response = createResponds("photos.getProfile");
        String id = getValue(response, "response.items.id");

        Map<String, Object> map = Map.of(
       "photo_id", id);
        ValidatableResponse responseDelete = createResponds("photos.delete", map);
        changesAreSuccessful(responseDelete, "response", "1");
    }

    public static void addOwnerPhoto(String namePhoto) {
        String filePath = Paths.get(Paths.get("").toAbsolutePath().toString(),
                "src", "test", "resources", "files", namePhoto).toString();
        ValidatableResponse resp = given().multiPart("photo", new File(filePath))
                .when().post(getValue(createResponds(Methods.PHOTOS_GET_OWNER_PHOTO_UPLOAD_SERVER.getNameMethod()), "response.upload_url")).then().log().body().and().assertThat().statusCode(200);

        // сохранение фото
        Map<String, Object> map = Map.of(
        "server", resp.extract().path("server").toString(),
        "hash", resp.extract().path("hash").toString(),
        "photo", resp.extract().path("photo").toString());

        ValidatableResponse responseSave = createResponds(Methods.PHOTOS_SAVE_OWNER_PHOTO.getNameMethod(), map);

        parameterNotNull(responseSave, "response.photo_src");
        changesAreSuccessful(responseSave, "response.saved", "1");
    }

    public  static ValidatableResponse addAlbumPhoto(String numAlbum, String namePhoto) {
        String filePath = Paths.get(Paths.get("").toAbsolutePath().toString(),
                "src", "test", "resources", "files", namePhoto).toString();
        Map<String, Object> map = Map.of(
        "album_id", numAlbum);
        ValidatableResponse responseServer = createResponds(Methods.PHOTOS_GET_UPLOAD_SERVER.getNameMethod(), map);
        String url = getValue(responseServer,"response.upload_url");

        Map<String, Object> response =
                given().multiPart("file1", new File(filePath))
                        .when().post(url).
                        then().contentType(ContentType.HTML).
                        extract().jsonPath().getJsonObject("");
        response.putAll(map);
        return createResponds(Methods.PHOTOS_SAVE.getNameMethod(), response);
    }
}
