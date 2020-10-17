package com.hs.edu.app.api.servlet;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.hs.edu.app.cache.RoomCacheManager;
import com.hs.edu.app.cache.WebSocketManager;
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
@WebServlet("/api/websocket/info")
@Slf4j
public class MonitorWebSocketServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebSocketManager webSocketManager= WebSocketManager.getInstance();
        String roomId=req.getParameter("roomId");
        String jsonData=null;
        if(!StrUtil.isEmpty(roomId)){
            jsonData = JSONObject.toJSONString(webSocketManager.getRoomWebSockets(roomId));
        }else{
            jsonData=JSONObject.toJSONString(webSocketManager.getAllWebSocket());
        }
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(jsonData));
        }
        PrintWriter out = resp.getWriter();
        out.write(jsonData);
    }
}
