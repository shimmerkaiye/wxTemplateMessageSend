package sx;

import lombok.Data;

/**
 * @Description
 * @Author shimmer
 * @Date 2022-02-25 15:05
 */
@Data
public class UserInfoWx {

    //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    private Integer subscribe;

    private String openid;

    private String language;

    //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    private Long subscribe_time;

    //只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
    private String unionid;

    private String remark;

    //用户所在的分组ID（兼容旧的用户分组接口）
    private Integer groupid;

    //用户被打上的标签ID列表
    private int[] tagid_list;

    //返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载 ,ADD_SCENE_LIVESTREAM 视频号直播，ADD_SCENE_CHANNELS 视频号 , ADD_SCENE_OTHERS 其他
    private String subscribe_scene;

}
