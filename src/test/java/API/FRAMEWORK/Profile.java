package API.FRAMEWORK;

import static API.FRAMEWORK.ProfileInfo.*;

/**
 * Класс для работы c профилем
 */
public class Profile {

    private ProfileInfo profileInfo;

    public Profile() {
        profileInfo = getProfileInfoForCurrentUser();
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(Object home_town, Object bDate, Object bDate_visibility, Object relation, Object sex) {
        ProfileInfo profileInfo = new ProfileInfo(home_town, bDate, bDate_visibility, relation, sex);
        this.profileInfo = profileInfo;
        profileInfo.saveCurrentProfileInfoOnSite();
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
        profileInfo.saveCurrentProfileInfoOnSite();
    }

    /**
     * Метод для удаления и загрузки иной фотографии на главную страницу профиля
     */
    public void deleteAndUploadPhoto(String nameNewPhoto) {
        deleteOwnerPhoto();
        addOwnerPhoto(nameNewPhoto);
    }
}
