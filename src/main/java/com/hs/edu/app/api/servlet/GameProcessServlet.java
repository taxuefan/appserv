package com.hs.edu.app.api.servlet;

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.api.entity.Result;
import com.hs.edu.app.cache.WebSocketManager;
import com.hs.edu.app.entity.Room;
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
 * @ClassName TestServlet
 * @Description TODO启动游戏的Servlet api
 * @Author taxuefan
 * @Date 2020/10/11 11:50
 * @Version 1.0
 **/
@WebServlet("/api/app/v1/rooms/game")
@Slf4j
public class GameProcessServlet extends HttpServlet {
    GameService service=new GameServiceImpl();
    WebSocketManager webSocketManager=WebSocketManager.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         String roomId=req.getParameter("roomId");
         String userId=req.getParameter("userId");
         Result result=service.startGame(roomId,userId);
         PrintWriter out = resp.getWriter();
         String jsonData = JSONObject.toJSONString(result);
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(jsonData));
        }
        if(result.getCode()==0){
            webSocketManager.sendGamingPacket(roomId);
        }
         out.write(jsonData);
    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roomId=req.getParameter("roomId");
        String seqNo=req.getParameter("seqNo");
        String userId=req.getParameter("userId");
        Result result=service.finishGame(roomId,seqNo,userId,true);
        PrintWriter out = resp.getWriter();
        String jsonData = JSONObject.toJSONString(result);
        out.write(jsonData);
    }
}
