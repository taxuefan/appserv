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
 * @Description 创建房间API Servlet
 * @Author taxuefan
 * @Date 2020/10/11 11:50
 * @Version 1.0
 **/
@WebServlet("/api/app/v1/rooms")
@Slf4j
public class RoomManagerServlet extends HttpServlet {
    GameService service=new GameServiceImpl();
    WebSocketManager webSocketManager=WebSocketManager.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         Room room=new Room();
         String wodiNum=req.getParameter("wodiNum") ;
         String pingminNum=req.getParameter("pingminNum") ;
         String baibanNum=req.getParameter("baibanNum") ;
         String dictType=req.getParameter("dictType") ;
         String userId=req.getParameter("userId") ;
         room.setDictType(dictType==null?0:Integer.valueOf(dictType));
         room.setPingminNum(pingminNum==null?0:Integer.valueOf(pingminNum));
         room.setWodiNum(wodiNum==null?0:Integer.valueOf(wodiNum));
         room.setBaibanNum(baibanNum==null?0:Integer.valueOf(baibanNum));
         room.setHostUserId(userId);
         Result result=service.createRoom(room);
         PrintWriter out = resp.getWriter();
         String jsonData = JSONObject.toJSONString(result);
        if(log.isInfoEnabled()){
            log.info("respone data:{}",JSONObject.toJSONString(jsonData));
        }
         out.write(jsonData);
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String roomId=req.getParameter("roomId") ;
        String userId=req.getParameter("userId") ;
        Result result=service.exitRoom(roomId,userId);
        PrintWriter out = resp.getWriter();
        String jsonData = JSONObject.toJSONString(result);
        out.write(jsonData);
        //强制退出发消息
        if(result.getCode()==0){
            webSocketManager.sendCloseRoomPacket(roomId,userId);
        }
    }
}
