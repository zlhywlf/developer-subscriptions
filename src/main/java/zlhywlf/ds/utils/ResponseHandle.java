package zlhywlf.ds.utils;

import org.json.JSONException;
import org.json.JSONObject;
import zlhywlf.ds.conf.ClientConf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.LocalTime;

/**
 * @author zlhywlf
 */
public class ResponseHandle {
    public static String getResponseStringFromConn(HttpURLConnection conn) {
        if (conn == null) {
            return "conn==null";
        }
        BufferedReader reader;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            stringBuilder.append(e.getMessage());
        }
        return stringBuilder.toString();
    }

    public static JSONObject getResponseJsonFromConn(HttpURLConnection conn) {
        JSONObject responseJson = new JSONObject();
        if (conn == null) {
            responseJson.put("responseMsg", "conn==null");
            return responseJson;
        }
        try {
            responseJson.put("responseCode", conn.getResponseCode());
            String response = getResponseStringFromConn(conn);
            if ("".equalsIgnoreCase(response)) {
                responseJson.put("responseMsg", "");
            } else {
                responseJson.put("responseMsg", new JSONObject(response));
            }
        } catch (JSONException | IOException e) {
            System.out.println(e.getMessage());
        }
        ClientConf.writeConf("LOG", LocalTime.now() + "\t\t" + responseJson.toString() + "\n", true);
        return responseJson;
    }
}
