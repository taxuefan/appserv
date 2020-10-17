package com.hs.edu.app.api.servlet;

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.api.entity.Result;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.app.service.DictionaryService;
import com.hs.edu.app.service.impl.DictionaryServiceImp;
import lombok.extern.slf4j.Slf4j; 

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * @ClassName RandomDictionaryServlet
 * @Description 词典Servlet
 * @Author taxuefan
 * @Date 2020/10/12 23:53
 * @Version 1.0
 **/
@WebServlet("/api/app/v1/dictionarys")
@Slf4j
public class DictionaryServlet extends HttpServlet {
    DictionaryService service=new DictionaryServiceImp();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String typeId=req.getParameter("typeId");
        String pingmin=req.getParameter("pingmin");
        String wodi=req.getParameter("wodi");
        Dictionary dict=new Dictionary();
        dict.setCreateTime(System.currentTimeMillis());
        dict.setPingmin(pingmin);
        dict.setWodi(wodi);
        dict.setTypeId(typeId);
        Result result=service.addDictionary(dict);
        PrintWriter out = resp.getWriter();
        String jsonData = JSONObject.toJSONString(result);
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(jsonData));
        }
        out.write(jsonData);
    }
}
