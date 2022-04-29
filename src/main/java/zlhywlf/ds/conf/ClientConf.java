package zlhywlf.ds.conf;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

/**
 * @author zlhywlf
 */
public class ClientConf {
    public static String authorityUrl;
    public static String clientId;
    public static String secret;
    public static String redirectUrl;
    public static String tokenUrl;
    public static String scope;
    public static String code;
    public static String refreshToken;
    public static String accessToken;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        authorityUrl = properties.getProperty("AUTHORITY_URL");
        clientId = properties.getProperty("CLIENT_ID");
        secret = properties.getProperty("SECRET");
        redirectUrl = properties.getProperty("REDIRECT_URL");
        tokenUrl = properties.getProperty("TOKEN_URL");
        scope = properties.getProperty("SCOPE");
        code = readConf("CODE");
        refreshToken = readConf("REFRESH");
        accessToken = readConf("ACCESS");
    }

    public static String getAuthorityUrl() {

        return authorityUrl + "?" + "client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + redirectUrl +
                "&response_mode=query" +
                "&scope=" + scope +
                "&state=" + UUID.randomUUID().toString();
    }

    public static String getTokenUrl() {

        return tokenUrl;
    }

    public static String getTokenBody() {

        return "client_id=" + clientId +
                "&grant_type=authorization_code" +
                "&client_secret=" + secret +
                "&redirect_uri=" + redirectUrl +
                "&code=" + code;
    }

    public static String getTokenRefreshBody() {

        return "client_id=" + clientId +
                "&scope=" + scope +
                "&client_secret=" + secret +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;
    }

    public static String readConf(String fileName) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void writeConf(String fileName, String content, boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, append))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeConf(String fileName, String content) {
        writeConf(fileName, content, false);
    }
}
