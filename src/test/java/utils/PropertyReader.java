package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public static String getUrl() {
        return getProperty("url");
    }

    public static Browser getBrowser() {
        return Browser.valueOf(getProperty("Browser"));
    }

    public static String getLogin() {
        return getProperty("login");
    }

    public static String getAccessToken() {
        return getProperty("access_token");
    }

    public static String getUser_id_friend() {
        return getProperty("user_id_friend");
    }

    public static String getUrlApi1() {
        return getProperty("url_api1");
    }

    public static String getUrlApi2() {
        return getProperty("url_api2");
    }

    public static String getJDBC_DRIVER() {
        return getProperty("JDBC_DRIVER");
    }

    public static String getJDBC_DB_URL() {
        return getProperty("JDBC_DB_URL");
    }

    public static String getJDBC_USER() {
        return getProperty("JDBC_USER");
    }

    public static String getJDBC_PASS() {
        return getProperty("JDBC_PASS");
    }

    private static String getProperty(String propertyName) {
        if (System.getProperty(propertyName) == null) {
            return getPropertyFromFile(propertyName);
        } else {
            return System.getProperty(propertyName);
        }
    }

    private static String getPropertyFromFile(String propertyName) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            if (propertyName.contains("JDBC_")) {
                input = new FileInputStream("src/test/resources/db.properties");
            } else {
                input = new FileInputStream("src/test/resources/framework.properties");
            }
            prop.load(input);
        } catch (IOException ex) {
            System.out.println("Cannot read property value for " + propertyName);
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop.getProperty(propertyName);
    }
}
