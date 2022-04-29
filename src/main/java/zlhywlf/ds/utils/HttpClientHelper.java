package zlhywlf.ds.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

/**
 * @author zlhywlf
 */
public class HttpClientHelper {

    public static HttpURLConnection http(String urlStr, String method) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(10 * 1000);
        con.setReadTimeout(15 * 1000);
        con.setRequestMethod(method);
        return con;
    }

    public static void setHeader(HttpURLConnection con, Map<String, String> headerMap) {
        if (headerMap != null) {
            for (String key : headerMap.keySet()) {
                con.setRequestProperty(key, headerMap.get(key));
            }
        }
    }


    public static <R> R httpGet(String urlStr, Map<String, String> headerMap, Function<HttpURLConnection, R> handle) {
        HttpURLConnection conn = null;
        try {
            conn = http(urlStr, "GET");
            conn.setDoInput(true);
            if (headerMap != null) {
                setHeader(conn, headerMap);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return handle.apply(conn);
    }


    public static <R> R httpGet(String urlStr, Function<HttpURLConnection, R> handle) {
        return httpGet(urlStr, null, handle);
    }

    public static <R> R httpPost(String urlStr, String body, String bodyType, Map<String, String> headerMap, Function<HttpURLConnection, R> handle) {
        HttpURLConnection conn = null;
        try {
            conn = http(urlStr, "POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            if (bodyType != null && bodyType.length() != 0) {
                conn.setRequestProperty("Content-Type", bodyType);
            }
            if (headerMap != null) {
                setHeader(conn, headerMap);
            }

            if (body != null && body.length() != 0) {
                OutputStream out = conn.getOutputStream();
                out.write(body.getBytes(StandardCharsets.UTF_8));
                out.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return handle.apply(conn);
    }

    public static <R> R httpPost(String urlStr, String body, String bodyType, Function<HttpURLConnection, R> handle) {
        return httpPost(urlStr, body, bodyType, null, handle);
    }

    public static <R> R httpPost(String urlStr, String body, Function<HttpURLConnection, R> handle) {
        return httpPost(urlStr, body, "application/x-www-form-urlencoded", null, handle);
    }

    public static <R> R httpPost(String urlStr, Function<HttpURLConnection, R> handle) {
        return httpPost(urlStr, null, null, null, handle);
    }

}
