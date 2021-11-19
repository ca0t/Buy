package com.ms;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ms.common.HttpClientHelp;
import com.ms.common.QRHelp;
import com.ms.model.Account;
import com.ms.model.HttpRes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Login {

    private HttpClientHelp client;
    private QRHelp qr = new QRHelp();
    private String state;

    public Login(HttpClientHelp c) {
        client = c;
    }

    public Account login() {
        referer();
        load();
        String qrConnectUrl = wechat();
        String authBackUrl = auth(qrConnectUrl);
        authBack(authBackUrl);
        return getAccount();
    }

    private void referer() {
        System.out.println("登录请求");
        client.get("https://www.microsoftstore.com.cn/customer/account/login/referer/");
    }

    private void load() {
        int unixTimeStamp = (int) (System.currentTimeMillis() / 1000);
        client.get("https://www.microsoftstore.com.cn/customer/section/load/?sections=customer&force_new_section_timestamp=false&_=" + unixTimeStamp);
    }

    private String wechat() {
        System.out.println("切换微信登录,扫描二维码登录");
        HttpRes httpRes = client.get("https://www.microsoftstore.com.cn/sociallogin/wechat/auth/");
        //解析qr地址
        String qrPath = getQRPath(httpRes.Content);

        //解析qr的uuid
        int uuidIndex = qrPath.lastIndexOf("/");
        String uuid = qrPath.substring(uuidIndex + 1);

        //解析state
        state = getState(httpRes.Content);

        qr.start(qrPath);

        //解析qr结果地址
        int unixTimeStamp = (int) (System.currentTimeMillis() / 1000);
        return "https://lp.open.weixin.qq.com/connect/l/qrconnect?uuid=" + uuid + "&_=" + unixTimeStamp;
    }

    private String auth(String url) {
        try {
            String wx_code;
            while (true) {
                HttpRes httpRes = client.get(url);
                if (httpRes.Content.length() > 40) {
                    String[] textView = httpRes.Content.split(";");
                    wx_code = textView[1].split("=")[1].replace("'", "");
                    break;
                }
            }
            return "https://www.microsoftstore.com.cn/sociallogin/wechat/authback/?code=" + wx_code + "&state=" + state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void authBack(String url){
        qr.setVisible(false);
        client.get(url);
    }

    private Account getAccount(){
        String getAccountUrl = "https://www.microsoftstore.com.cn/customer/section/load/?sections=customer&force_new_section_timestamp=false&_=" + (int) (System.currentTimeMillis() / 1000);
        HttpRes httpRes = client.get(getAccountUrl);
        JSONObject jsonObject = JSONUtil.parseObj(httpRes.Content);
        return jsonObject.toBean(Account.class);
    }

    private String getQRPath(String html) {
        Document document = Jsoup.parse(html);
        Elements element = document.getElementsByClass("qrcode lightBorder");
        String qrPath = element.attr("src");
        return "https://open.weixin.qq.com" + qrPath;
    }

    private  String getState(String html){
        int stateIndex = html.lastIndexOf("state");
        String lastHtml = html.substring(stateIndex);
        return lastHtml.substring(0,lastHtml.indexOf("\";")).replace("state=","");
    }
}
