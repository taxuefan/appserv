package com.hs.edu.app.api.servlet;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.hs.edu.app.cache.RoomCacheManager;
import com.hs.edu.app.cache.WebSocketManager;
import com.hs.edu.app.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName ShowRoomInfoServlet
 * @Description TODO 显示房间信息api接口
 * @Author taxuefan
 * @Date 2020/10/13 16:34
 * @Version 1.0
 **/
@WebServlet("/api/room/info")
@Slf4j
public class MonitorRoomInfoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RoomCacheManager roomCacheManager= RoomCacheManager.getInstance();
        String roomId=req.getParameter("roomId");
        String clearFlag= req.getParameter("clear");
        String jsonData=null;
        if(clearFlag!=null&& clearFlag.equals("clear")){
            roomCacheManager.getAllRoomInfo();
        }
        if(!StrUtil.isEmpty(roomId)){
            jsonData = JSONObject.toJSONString(roomCacheManager.get(roomId));
        }else{
            jsonData=JSONObject.toJSONString(roomCacheManager.getAllRoomInfo());
        }
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(jsonData));
        }
        PrintWriter out = resp.getWriter();
        out.write(jsonData);
    }
}
