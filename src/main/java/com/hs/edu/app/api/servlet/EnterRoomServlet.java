package com.hs.edu.app.api.servlet;

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.api.entity.Result;
import com.hs.edu.app.cache.RoomCacheManager;
import com.hs.edu.app.cache.WebSocketManager;
import com.hs.edu.app.entity.Dictionary;
import com.hs.edu.app.entity.Gamer;
import com.hs.edu.app.service.GameService;
import com.hs.edu.app.service.impl.GameServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName EnterRoomServlet
 * @Description TODO:获取游戏玩家的API接口
 * @Author taxuefan
 * @Date 2020/10/13 17:29
 * @Version 1.0
 **/
@WebServlet("/api/app/v1/rooms/player")
@Slf4j
public class EnterRoomServlet extends HttpServlet {
    GameService service=new GameServiceImpl();
    WebSocketManager webSocketManager=WebSocketManager.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roomId=req.getParameter("roomId");
        String nickName=req.getParameter("nickName");
        String avatarUrl=req.getParameter("avatarUrl");
        String userId=req.getParameter("userId");
        Gamer gamer=new Gamer();
        gamer.setNickName(nickName);
        gamer.setAvatarUrl(avatarUrl);
        gamer.setUserId(userId);
        PrintWriter out = resp.getWriter();
        Result result=service.addGamer(roomId,gamer);
        if(result.getCode()==0){
            webSocketManager.sendEnterRoomPacket(roomId);
        }
        String jsonData = JSONObject.toJSONString(result);
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(jsonData));
        }
        out.write(jsonData);
    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roomId=req.getParameter("roomId");
        String userId=req.getParameter("userId");
        PrintWriter out = resp.getWriter();
        Result result=service.exitRoom(roomId,userId);
        String jsonData = JSONObject.toJSONString(result);
        out.write(jsonData);
    }
}
