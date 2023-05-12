package utils.SQL;

import org.testng.Assert;

import java.sql.*;
import java.util.*;

public class JDBC {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/accounts";

    static final String USER = "postgres";
    static final String PASS = "Steklova12";
    private static Connection conn = null;
    private static Statement stmt = null;

    private static final Map<String, User> USERS = new HashMap<>();

    public static synchronized void connectDB(String table) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            executeQuery(table);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    private static void executeQuery(String table) throws SQLException {
        Statement st = conn.createStatement();
        String sql = " SELECT * FROM " + table;
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            String log = rs.getString("login");
            String pass = rs.getString("password");
            USERS.put(log, new User(log, pass));
        }
    }

    public static User getUserByLogin(String login) {
        Assert.assertTrue(USERS.containsKey(login), String.format("Пользователь с логином '%s' не найден", login));
        return USERS.get(login);
    }
}
