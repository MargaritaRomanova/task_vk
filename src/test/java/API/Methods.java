package API;

/**
 * enam, содержащий возможные типы методов, для работы с vk_api
 */
public enum Methods {
    //account
    ACCOUNT_GET_PROFILE_INFO("account.getProfileInfo"),
    ACCOUNT_SAVE_PROFILE_INFO("account.saveProfileInfo"),

    //photos
    PHOTOS_GET_OWNER_PHOTO_UPLOAD_SERVER("photos.getOwnerPhotoUploadServer"),
    PHOTOS_SAVE_OWNER_PHOTO("photos.saveOwnerPhoto"),
    PHOTOS_CREATE_ALBUM("photos.createAlbum"),
    PHOTOS_GET_UPLOAD_SERVER("photos.getUploadServer"),
    PHOTOS_SAVE("photos.save"),
    PHOTOS_MAKE_COVER("photos.makeCover"),
    PHOTOS_CREATE_COMMENT("photos.createComment"),
    PHOTOS_MOVE("photos.move"),
    PHOTOS_DELETE_ALBUM("photos.deleteAlbum"),
    PHOTOS_PUT_TAG("photos.putTag"),
    PHOTOS_CONFIRM_TAG("photos.confirmTag"),
    PHOTOS_GET_PROFILE("photos.getProfile"),
    PHOTOS_DELETE("photos.delete"),

    //messages
    MESSAGES_SEND("messages.send"),
    MESSAGES_EDIT("messages.edit"),
    MESSAGES_MARK_AS_IMPORTANT("messages.markAsImportant"),
    MESSAGES_DELETE_CONVERSATION("messages.deleteConversation"),

    //groups
    GROUPS_CREATE("groups.create"),
    GROUPS_LEAVE("groups.leave"),

    //board
    BOARD_ADD_TOPIC("board.addTopic"),
    BOARD_FIX_TOPIC("board.fixTopic"),
    BOARD_CREATE_COMMENT("board.createComment"),
    BOARD_EDIT_COMMENT("board.editComment"),
    BOARD_DELETE_COMMENT("board.deleteComment");

    public final String nameMethod;

    Methods(String nameMethod) {
        this.nameMethod = nameMethod;
    }

    public String getNameMethod() {
        return nameMethod;
    }
}
