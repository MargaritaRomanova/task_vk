package API;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.PropertyReader;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Класс, содержащий методы, для работы с vk_api
 */
public class ApiCore {

    public static String getUrlApi(String method) {
        return PropertyReader.getUrlApi1() + method + "?access_token=" + PropertyReader.getAccessToken() + PropertyReader.getUrlApi2();
    }

    public static Response createResponds(String method, Map<String, Object> params) {
        return given()
                .when()
                .queryParams(params)
                .post(getUrlApi(method))
                .then().log().all()
                .extract().response();
    }

    public static Response createResponds(String method) {
        return given()
                .when()
                .post(getUrlApi(method))
                .then().log().all()
                .extract().response();
    }

    public static String getValue(Response response, String path) {
        return response.path(path).toString().replaceAll("[\\[\\]\"]", "");
    }

    public static String getValue(Response response) {
        return response.path("response").toString();
    }

    public static void changesAreSuccessful(Response response, String path, String value) {
        Assert.assertEquals(response.path(path).toString(), value, "изменения не сохранены");
    }

    public static void parameterNotNull(Response response, String value) {
        Assert.assertNotNull(response.path(value), "изменения не сохранены/значение отсутствует");
    }
}
