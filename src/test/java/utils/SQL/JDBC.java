package utils.SQL;

import org.apache.commons.codec.binary.Hex;
import org.testng.Assert;
import utils.PropertyReader;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class JDBC {

    private static Connection conn = null;
    private static Statement stmt = null;

    private static final Map<String, User> USERS = new HashMap<>();

    public static void loadUsersFromDB() throws Exception {
        connectDB();
        loadUsers();
    }
    public static synchronized void connectDB() {
        try {
            Class.forName(PropertyReader.getJDBC_DRIVER());
            conn = DriverManager
                    .getConnection(PropertyReader.getJDBC_DB_URL(), PropertyReader.getJDBC_USER(), PropertyReader.getJDBC_PASS());
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

    private static void loadUsers() throws Exception {
        Statement st = conn.createStatement();
        String sql = " SELECT * FROM users";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            String log = rs.getString("login");
            String pass = decrypt(rs.getString("password"));
            USERS.put(log, new User(log, pass));
        }
    }

    public static User getUserByLogin(String login) {
        Assert.assertTrue(USERS.containsKey(login), String.format("Пользователь с логином '%s' не найден", login));
        return USERS.get(login);
    }

    public static SecretKeySpec generatePasswordKey(final String key) {
        final byte[] finalKey = new byte[16];
        int i = 0;
        for (byte b : key.getBytes(StandardCharsets.UTF_8))
            finalKey[i++ % 16] ^= b;
        return new SecretKeySpec(finalKey, "AES");
    }

    public static String decrypt(String text) throws Exception {
        final Cipher decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, generatePasswordKey("task vk"));
        return new String(decryptCipher.doFinal(Hex.decodeHex(text.toCharArray())));

    }

    public static String encrypt(String text) throws Exception {
        final Cipher encryptCipher = Cipher.getInstance("AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, generatePasswordKey("task vk"));
        return new String(Hex.encodeHex(encryptCipher.doFinal(text.getBytes(StandardCharsets.UTF_8))));
    }
}
