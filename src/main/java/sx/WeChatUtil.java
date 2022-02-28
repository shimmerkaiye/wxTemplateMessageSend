package sx;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description
 * @Author shimmer
 * @Date 2022-02-23 16:10
 */
@Slf4j
public class WeChatUtil {
    // // URL验证时使用的token
    // public static final String TOKEN = "qq";
    // appid
    public static String APPID = "";
    // secret
    public static String SECRET = "";
    // 创建菜单接口地址
    // public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // 发送模板消息的接口
    public static final String SEND_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    // 获取access_token的接口地址
    public static final String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    // 缓存的access_token
    private static String accessToken;
    // access_token的失效时间
    private static long expiresTime;

    private static Properties properties = new Properties();


    static{
        try {
            properties.load(WeChatUtil.class.getClassLoader().getResourceAsStream("wx.properties"));
            APPID = properties.getProperty("APPID");
            SECRET = properties.getProperty("SECRET");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取accessToken
     *
     * @return
     * @throws IOException
     */
    public static String getAccessToken() throws IOException {
        // 判断accessToken是否已经过期，如果过期需要重新获取
        log.info("APPID为，{}；SECRET为：{}",APPID,SECRET);
        if (accessToken == null || expiresTime < System.currentTimeMillis()) {
            // 发起请求获取accessToken
            String result = HttpUtil.get(GET_ACCESSTOKEN_URL.replace("APPID",APPID).replace("APPSECRET", SECRET));
            log.info("获取accessToken得结果为：{}",result);
            // 把json字符串转换为json对象
            JSONObject json = JSONUtil.parseObj(result);
            // 缓存accessToken
            accessToken = (String)json.get("access_token");
            // 设置accessToken的失效时间
            long expires_in = json.getLong("expires_in");
            // 失效时间 = 当前时间 + 有效期(提前一分钟)
            expiresTime = System.currentTimeMillis() + (expires_in - 60) * 1000;
        }
        return accessToken;
    }

    /**
     * 发送模板
     *
     * @return 结果集（{"errcode":0,"errmsg":"ok","msgid":528986890112614400}）、
     * 				（{"errcode":40003,"errmsg":"invalid openid hint: [tv6YMa03463946]"}）
     * @throws IOException
     */
    public static String sendTemplate(String data) throws IOException {
        // 通过调用HttpUtil工具类发送post请求
        // 调用微信发送模板消息的接口
        String result = HttpUtil.post(SEND_TEMPLATE_URL.replace("ACCESS_TOKEN",
                getAccessToken()), data);
        System.out.println(result);
        return result;
    }

    /**
     * 返回用户基本信息
     * @return
     */
    public static UserInfoWx getUserInfo(String openId) throws IOException{
        // 调用微信发送模板消息的接口
        String result = HttpUtil.get(getUserInfoUrl.replace("ACCESS_TOKEN",
                getAccessToken()).replace("OPENID",openId));
        UserInfoWx userInfoWx = JSONUtil.toBean(result, UserInfoWx.class);
        return userInfoWx;
    }

}
