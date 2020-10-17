package com.hs.edu.app.service.impl;

import cn.hutool.http.HttpUtil;
import com.hs.edu.app.api.config.AppConfig;
import com.hs.edu.app.service.WxService;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName WxServiceImpl
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/15 17:11
 * @Version 1.0
 **/
@Slf4j
public class WxServiceImpl implements WxService {
    private final String WX_AUTH_SESSON_URL="https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    @Override
    public String getOpenId(String jsCode) {
        String wxAppID= AppConfig.getInstance().getConfig("wx_app_id");
        String wxAppsecret=AppConfig.getInstance().getConfig("wx_app_secret");
        String url= String.format(WX_AUTH_SESSON_URL, wxAppID,wxAppsecret,jsCode);
        String result=HttpUtil
                .createGet(url).header("Content-Type", "application/json")
                .execute()
                .charset("utf-8")
                .body();
        log.info("访问的url为：{}",url);
        return result;
    }
    public static void main(String args[]){
        WxService service=new WxServiceImpl();
        service.getOpenId("061k7aGa1HYuOz0TVZHa1CsEK72k7aGu");

    }
}
