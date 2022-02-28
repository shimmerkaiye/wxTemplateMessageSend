package sx;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @Description
 * @Author shimmer
 * @Date 2022-02-23 16:59
 */

@Slf4j
@Controller
@RequestMapping("/wxgzh")
public class TestController {
    // 与接口配置信息中的Token要一致
    private static String WECHAT_TOKEN = "你在服务器配置中填写的token";

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(){
        return "123";
    }

    @RequestMapping(value = "/checkToken")
    @ResponseBody
    public void wxCallBack(HttpServletRequest request){
        log.info("------------进入wxCallBack------------");
        Map<String, String> data = parseRequest(request);
        String event = data.get("Event");
        if("subscribe".equals(event)){
            log.info("==========用户关注了^_^=========");
            //用户关注时，根据openId获取unionId
            try {
                UserInfoWx userInfo = WeChatUtil.getUserInfo(data.get("FromUserName"));
                log.info("unionId为：{}",userInfo.getUnionid());
                //TODO 根据unionId，将openId更新进account表中

            } catch (IOException e) {
                e.printStackTrace();
                log.error("调用微信接口，根据openId获取用户信息失败");
            }
        }else{
            log.info("==========用户取关了-.-=========");
            //TODO 用户取消关注时，要删除数据库中的openId和unionId
        }
        log.info(JSONUtil.toJsonPrettyStr(data));
    }

    public static Map<String,String> parseRequest(HttpServletRequest request){
        Map<String,String> map=new HashMap<String,String>();
        SAXReader reader=new SAXReader();
        Document document= null;
        try {
            //读取输入流获取文档对象
            document = reader.read(request.getInputStream());
            //根据文档对象获取根节点
            Element rootElement=document.getRootElement();
            //获取根所有子节点
            List<Element> elements = rootElement.elements();
            for (Element e:elements) {
                map.put(e.getName(),e.getStringValue());
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 该方法用于验证自己是开发者，验证完之后就没用了，但如果修改了url则需要再次验证。
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    public String verifyWXConfig(@RequestParam(value="signature",required=false) String signature,
                                 @RequestParam(value="timestamp",required=false) String timestamp,
                                 @RequestParam(value="nonce",required=false) String nonce,
                                 @RequestParam(value="echostr",required=false) String echostr) {
        log.info("开始签名验证："+" PARAM VAL: >>>" + signature + "\t" + timestamp + "\t" + nonce + "\t" + echostr);
        if (StringUtils.isNotEmpty(signature) && StringUtils.isNotEmpty(timestamp)
                &&StringUtils.isNotEmpty(nonce) && StringUtils.isNotEmpty(echostr)) {
            String sTempStr = "";
            List<String> list = new ArrayList<>();
            list.add(WECHAT_TOKEN);
            list.add(timestamp);
            list.add(nonce);

            Collections.sort(list);
            sTempStr = list.get(0)+list.get(1)+list.get(2);
            try {
                sTempStr = DigestUtils.sha1Hex(sTempStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotEmpty(sTempStr) && StringUtils.equals(signature, sTempStr)) {
                log.info("验证成功：-----------："+sTempStr);
                return echostr;
            } else {
                log.info("验证失败：-----------：00000");
                return "-1";
            }
        } else {
            log.info("验证失败：-----------：11111");
            return "-1";
        }
    }

    @RequestMapping(value = "/templateMessage")
    public void sendTemplateMessage() throws IOException {
        log.info("开始封装模板消息。。。。。。。。。。");
        // 将信息内容封装到实体类
        TemplateMessage temp = new TemplateMessage();
        // 设置接收方的openid		user.getOpenid()是从数据库中得到的对应的openid
        temp.setTouser("微信用户的openid");
        // 设置模板id（从页面上复制）
        temp.setTemplate_id("模板消息id");
        // 设置回调地址（点击模板消息所跳转的地址，设置为空即无跳转）
        temp.setUrl("");
        Miniprogram miniprogram = new Miniprogram();
        miniprogram.setAppid("需要跳转的小程序appid");
        temp.setMiniprogram(miniprogram);
        // 设置模板消息内容和对应的字体颜色
        TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
        params.put("first", TemplateMessage.item("呼叫通知", "#173177"));
        //date为当前时间，new出来的在这里省略
        params.put("keyword1", TemplateMessage.item(System.currentTimeMillis()+"", "#173177"));
        params.put("keyword2", TemplateMessage.item("南大门", "#173177"));
        params.put("remark", TemplateMessage.item("点击接听", "#173177"));

        temp.setData(params);
        // 将实体类转为jsonObject
        JSONObject json = JSONUtil.parseObj(temp);
        log.info(json.toString());
        // 通过工具类调用微信接口发送模板消息
        String result = WeChatUtil.sendTemplate(json.toString());
        log.info("返回结果集：" + result);
    }

}
