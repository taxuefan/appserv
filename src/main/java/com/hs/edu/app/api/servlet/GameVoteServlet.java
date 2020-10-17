package com.hs.edu.app.api.servlet;

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.api.entity.Result;
import com.hs.edu.app.cache.WebSocketManager;
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
 * @Description TODO:投票Servlet API
 * @Author taxuefan
 * @Date 2020/10/13 17:29
 * @Version 1.0
 **/
@WebServlet("/api/app/v1/rooms/game/vote")
@Slf4j
public class GameVoteServlet extends HttpServlet {
    GameService service=new GameServiceImpl();
    WebSocketManager webSocketManager=WebSocketManager.getInstance();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roomId=req.getParameter("roomId");
        String seqNo=req.getParameter("seqNo");
        String userId=req.getParameter("userId");
        String playUserId=req.getParameter("playUserId");
        PrintWriter out = resp.getWriter();
        Result result=service.weedUpGamer(roomId,seqNo,userId,playUserId);
        String jsonData = JSONObject.toJSONString(result);
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(jsonData));
        }
        //发送投票信息
        if(result.getCode()==0){
            webSocketManager.sendGameVotePacket(roomId);
        }
        out.write(jsonData);
    }
}
