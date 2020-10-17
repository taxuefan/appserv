package com.hs.edu.app.api.servlet;

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.api.entity.Result;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.app.service.DictionaryService;
import com.hs.edu.app.service.WxService;
import com.hs.edu.app.service.impl.DictionaryServiceImp;
import com.hs.edu.app.service.impl.WxServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName WxServiceServlet
 * @Description 获取微信的api接口
 * @Author taxuefan
 * @Date 2020/10/12 23:53
 * @Version 1.0
 **/
@WebServlet("/api/app/v1/wx/openid")
@Slf4j
public class WxServiceServlet extends HttpServlet {
    private WxService service=new WxServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsCode=req.getParameter("js_code");
        String result= service.getOpenId(jsCode);
        PrintWriter out = resp.getWriter();
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(result));
        }
        out.write(result);
    }
}
