package test;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author shimmer
 * @Date 2022-02-25 10:37
 */
public class Test {
    public static void main(String[] args) {

        String token = "shimmer";

        String signature = "c37bf93d9b7abd22c0cdaf288213801806d53765";
        String timestamp = "1645756531";
        String nonce = "1375783871";
        String echostr = "3192631394358163187";

        String sTempStr = "";
        List<String> list = new ArrayList<>();
        list.add(token);
        list.add(timestamp);
        list.add(nonce);

        Collections.sort(list);
        sTempStr = list.get(0)+list.get(1)+list.get(2);
        System.out.println(sTempStr);
        sTempStr = DigestUtils.sha1Hex(sTempStr);
        System.out.println(sTempStr);
        System.out.println(signature);
    }
}
