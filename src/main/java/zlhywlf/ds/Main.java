package zlhywlf.ds;

import org.json.JSONObject;
import zlhywlf.ds.conf.ClientConf;
import zlhywlf.ds.utils.HttpClientHelper;
import zlhywlf.ds.utils.ResponseHandle;

import java.net.HttpURLConnection;
import java.time.LocalTime;
import java.util.HashMap;


/**
 * @author zlhywlf
 */
public class Main {
    public static void main(String[] args) {
        if (ClientConf.accessToken == null || ClientConf.accessToken.length() == 0) {
            JSONObject jsonObject = HttpClientHelper.httpPost(ClientConf.getTokenUrl(), ClientConf.getTokenBody(), ResponseHandle::getResponseJsonFromConn);
            int responseCode = (int) jsonObject.get("responseCode");
            JSONObject responseMsg = new JSONObject(jsonObject.get("responseMsg").toString());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                ClientConf.accessToken=responseMsg.get("access_token").toString();
                ClientConf.writeConf("ACCESS", responseMsg.get("access_token").toString());
                ClientConf.writeConf("REFRESH", responseMsg.get("refresh_token").toString());
            } else {
                ClientConf.writeConf("LOG", LocalTime.now() + "\t\t授权错误,尝试重新授权并将授权码写入CODE:\t" + ClientConf.getAuthorityUrl() + "\n", true);
                ClientConf.writeConf("ACCESS", "");
                ClientConf.writeConf("REFRESH", "");
                return;
            }
        }
        HashMap<String, String> header = new HashMap<>(2);
        header.put("Authorization", "Bearer " + ClientConf.accessToken);
        header.put("Accept", "application/json");
        JSONObject jsonObject = HttpClientHelper.httpGet("https://graph.microsoft.com/v1.0/me/messages?$select=sender,subject", header, ResponseHandle::getResponseJsonFromConn);
        int responseCode = (int) jsonObject.get("responseCode");
        if (responseCode != HttpURLConnection.HTTP_OK) {
            jsonObject = HttpClientHelper.httpPost(ClientConf.getTokenUrl(), ClientConf.getTokenRefreshBody(), ResponseHandle::getResponseJsonFromConn);
            responseCode = (int) jsonObject.get("responseCode");
            JSONObject responseMsg = new JSONObject(jsonObject.get("responseMsg").toString());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                ClientConf.writeConf("ACCESS", responseMsg.get("access_token").toString());
                header.remove("Authorization");
                header.put("Authorization", "Bearer " + responseMsg.get("access_token").toString());
                HttpClientHelper.httpGet("https://graph.microsoft.com/v1.0/me/messages?$select=sender,subject", header, ResponseHandle::getResponseJsonFromConn);
            } else {
                ClientConf.writeConf("LOG", LocalTime.now() + "\t\t刷新token错误,尝试重新授权并将授权码写入CODE:\t" + ClientConf.getAuthorityUrl() + "\n", true);
                ClientConf.writeConf("ACCESS", "");
                ClientConf.writeConf("REFRESH", "");
            }
        }
    }


}
