package com.wjs.takeout.utils;

/**
 * @author wjs
 * @createTime 2022-11-15 15:32
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * 短信发送工具类
 */
public class SMSUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 发送验证码短信
     *
     * @param phone
     */
    public static void sendPhone(String phone, String code) {

        String host = "https://cxkjsms.market.alicloudapi.com";
        String path = "/chuangxinsms/dxjk";
        String method = "POST";
        String appcode = "加上自己AppCode";//开通服务后 买家中心-查看AppCode
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();

        querys.put("code", code);
        querys.put("content", "【宿友】你的验证码是：" + code + "，" + "3分钟内有效。");
        querys.put("mobile", phone);

        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            String body = EntityUtils.toString(response.getEntity());

            JsonNode jsonNode = MAPPER.readTree(body);
            System.out.println(jsonNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
