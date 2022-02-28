package sx;

import lombok.Data;

import java.util.TreeMap;

/**
 * @Description
 * @Author shimmer
 * @Date 2022-02-23 16:46
 */
@Data
public class TemplateMessage {
    private String touser; // 接收者openid

    private String template_id; // 模板ID

    private String url; // 模板跳转链接

    private Miniprogram miniprogram;

    private TreeMap<String, TreeMap<String, String>> data; // data数据

    /**
     * 参数
     *
     * @param value
     * @param color
     *            可不填
     * @return
     */
    public static TreeMap<String, String> item(String value, String color) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("value", value);
        params.put("color", color);
        return params;
    }
}
