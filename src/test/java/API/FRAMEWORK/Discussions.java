package API.FRAMEWORK;

import API.Methods;
import io.restassured.response.Response;

import java.util.Map;

import static API.ApiCore.*;

/**
 * Класс для работы с сообществом (группой, мероприятием, публичной страницей)
 */
public class Discussions {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String TYPE = "type";
    private static final String GROUP_ID = "group_id";
    private static final String TEXT = "text";
    private static final String TOPIC_ID = "topic_id";
    private static final String MESSAGE = "message";
    private static final String COMMENT_ID = "comment_id";

    /**
     * Метод для создания нового сообщества
     *
     * @param title       - название сообщества
     * @param description - описание сообщества
     * @param type        - тип создаваемого сообщества (group — группа;
     *                    event — мероприятие;
     *                    public — публичная страница)
     * @return возвращает String параметра group_id (идентификатор сообщества)
     */
    public String createGroup(String title, String description, String type) {
        Map<String, Object> discussions = Map.of(
                TITLE, title,
                DESCRIPTION, description,
                TYPE, type);
        Response responseGroupCreate = createResponds(Methods.GROUPS_CREATE.getNameMethod(), discussions);
        parameterNotNull(responseGroupCreate, "response.id");
        return getValue(responseGroupCreate, "response.id");
    }

    /**
     * Метод создает новую тему в списке обсуждений группы
     *
     * @param group_id - идентификатор сообщества
     * @param title    - название обсуждения
     * @param text     - текст первого сообщения в обсуждении
     * @return возвращает String параметра topic_id (идентификатор созданной темы)
     */
    public String addTopicInGroup(String group_id, String title, String text) {
        Map<String, Object> discussions = Map.of(
                GROUP_ID, group_id,
                TITLE, title,
                TEXT, text);
        Response responseAddTopic = createResponds(Methods.BOARD_ADD_TOPIC.getNameMethod(), discussions);
        return getValue(responseAddTopic);
    }

    /**
     * Метод закрепляет тему в списке обсуждений группы
     *
     * @param group_id - идентификатор сообщества, в котором размещено обсуждение
     * @param topic_id - идентификатор обсуждения
     */
    public void fixTopicInGroup(String group_id, String topic_id) {
        Map<String, Object> discussions = Map.of(
                GROUP_ID, group_id,
                TOPIC_ID, topic_id);
        Response responseFixTopic = createResponds(Methods.BOARD_FIX_TOPIC.getNameMethod(), discussions);
        changesAreSuccessful(responseFixTopic, "response", "1");
    }

    /**
     * Метод добавляет новый комментарий в обсуждении
     *
     * @param group_id - идентификатор сообщества, в котором размещено обсуждение
     * @param topic_id - идентификатор обсуждения
     * @param message  - текст комментария
     * @return возвращает String параметра comment_id (идентификатор созданного комментария)
     */
    public String addCommentInTopicInGroup(String group_id, String topic_id, String message) {
        Map<String, Object> discussions = Map.of(
                GROUP_ID, group_id,
                TOPIC_ID, topic_id,
                MESSAGE, message);
        Response response = createResponds(Methods.BOARD_CREATE_COMMENT.getNameMethod(), discussions);
        return getValue(response);
    }

    /**
     * Метод редактирует одно из сообщений в обсуждении сообщества
     *
     * @param group_id   - идентификатор сообщества, в котором размещено обсуждение
     * @param topic_id   - идентификатор обсуждения
     * @param comment_id - идентификатор комментария в обсуждении
     * @param message    - новый текст комментария
     */
    public void editComment(String group_id, String topic_id, String comment_id, String message) {
        Map<String, Object> discussions = Map.of(
                GROUP_ID, group_id,
                TOPIC_ID, topic_id,
                COMMENT_ID, comment_id,
                MESSAGE, message);
        Response responseEditComment = createResponds(Methods.BOARD_EDIT_COMMENT.getNameMethod(), discussions);
        changesAreSuccessful(responseEditComment, "response", "1");
    }

    /**
     * Метод удаляет сообщение темы в обсуждениях сообщества
     *
     * @param group_id   - идентификатор сообщества
     * @param topic_id   - идентификатор обсуждения
     * @param comment_id - идентификатор комментария в обсуждении
     */
    public void deleteComment(String group_id, String topic_id, String comment_id) {
        Map<String, Object> discussions = Map.of(
                GROUP_ID, group_id,
                TOPIC_ID, topic_id,
                COMMENT_ID, comment_id);
        Response responseDeleteComment = createResponds(Methods.BOARD_DELETE_COMMENT.getNameMethod(), discussions);
        changesAreSuccessful(responseDeleteComment, "response", "1");
    }

    /**
     * Метод позволяет покинуть сообщество
     *
     * @param group_id - идентификатор сообщества
     */
    public void leaveGroup(String group_id) {
        Map<String, Object> discussions = Map.of(
                GROUP_ID, group_id);
        Response responseLeave = createResponds(Methods.GROUPS_LEAVE.getNameMethod(), discussions);
        changesAreSuccessful(responseLeave, "response", "1");
    }
}
