package API.FRAMEWORK;

import API.Methods;
import io.restassured.response.Response;

import java.util.Map;
import java.util.Random;

import static API.ApiCore.*;

/**
 * Класс для работы с личными сообщениями
 */
public class Dialog {

    private String random_id = String.valueOf(new Random().nextInt());
    private static final String USER_ID = "user_id";
    private static final String MESSAGE = "message";
    private static final String RANDOM_ID = "random_id";
    private static final String PEER_ID = "peer_id";
    private static final String MESSAGE_ID = "message_id";
    private static final String MESSAGE_IMPORTANT = "important";
    private static final String MESSAGE_IDS = "message_ids";
    private String user_id_friend;

    public Dialog(String user_id_friend) {
        this.user_id_friend = user_id_friend;
    }

    /**
     * Метод отправляет сообщение
     *
     * @param message - текст личного сообщения
     * @return возвращает String параметра message_id (идентификатор сообщения)
     */
    public String messageSend(String message) {
        Map<String, Object> messages = Map.of(
                USER_ID, user_id_friend,
                MESSAGE, message,
                RANDOM_ID, random_id);
        Response responseSend = createResponds(Methods.MESSAGES_SEND.getNameMethod(), messages);
        return getValue(responseSend);
    }

    /**
     * Метод редактирует сообщение
     *
     * @param message    - текст сообщения
     * @param message_id - идентификатор сообщения
     */
    public void messageEdit(String message, String message_id) {
        Map<String, Object> messages = Map.of(
                PEER_ID, user_id_friend,
                MESSAGE, message,
                RANDOM_ID, random_id,
                MESSAGE_ID, message_id);
        Response responseEdit = createResponds(Methods.MESSAGES_EDIT.getNameMethod(), messages);
        changesAreSuccessful(responseEdit, "response", "1");
    }

    /**
     * Метод помечает сообщения как важные либо снимает отметку
     *
     * @param important  - отметить сообщение как важное - true
     *                   - снять отметку - false
     * @param message_id - идентификатор сообщения
     */
    public void messageMark(boolean important, String message_id) {
        String value = "0";
        if (important)
            value = "1";

        Map<String, Object> messages = Map.of(
                MESSAGE_IMPORTANT, value,
                MESSAGE_IDS, message_id);
        Response responseMark = createResponds(Methods.MESSAGES_MARK_AS_IMPORTANT.getNameMethod(), messages);
        changesAreSuccessful(responseMark, "response", "[" + message_id + "]");
    }

    /**
     * Метод удаляет диалог
     *
     * @param message_id - идентификатор последнего сообщения в диалоге
     */
    public void deleteConversation(String message_id) {
        Map<String, Object> messages = Map.of(
                PEER_ID, user_id_friend);
        Response responseDeleteConversation = createResponds(Methods.MESSAGES_DELETE_CONVERSATION.getNameMethod(), messages);
        changesAreSuccessful(responseDeleteConversation, "response.last_deleted_id", message_id);
    }
}
