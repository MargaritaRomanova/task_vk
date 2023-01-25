package API.FRAMEWORK;

import API.Methods;
import API.VkApi;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

import static API.VkApi.*;

public class ProfileInfo {

    private Map<String, Object> profileInfo;

    public ProfileInfo(String home_town, String bDate, String bDate_visibility, String relation, String sex){
        profileInfo = new HashMap<>();
        profileInfo.put("home_town", home_town);
        profileInfo.put("bdate", bDate);
        profileInfo.put("bdate_visibility", bDate_visibility);
        profileInfo.put("relation", relation);
        profileInfo.put("sex", sex);
    }

    public Object getInfo(String property) {
        return profileInfo.get(property);
    }

    public static ProfileInfo getProfileInfoForCurrentUser() {
        ValidatableResponse responseGetProfileInfo = createResponds(Methods.ACCOUNT_GET_PROFILE_INFO.getNameMethod());
        String homeTown = getValue(responseGetProfileInfo, "response.home_town");
        String bdate = getValue(responseGetProfileInfo, "response.bdate");
        String bdateVisibility = getValue(responseGetProfileInfo, "response.bdate_visibility");
        String relation = getValue(responseGetProfileInfo, "response.relation");
        String sex = getValue(responseGetProfileInfo, "response.sex");

        return new ProfileInfo(homeTown, bdate, bdateVisibility, relation, sex);
    }

    public void saveCurrentProfileInfoOnSite() {
        ValidatableResponse responseSaveProfileInfo = createResponds(Methods.ACCOUNT_SAVE_PROFILE_INFO.getNameMethod(), profileInfo);
        changesAreSuccessful(responseSaveProfileInfo, "response.changed", "1");
    }


}
