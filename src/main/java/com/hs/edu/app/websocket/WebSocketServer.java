package com.hs.edu.app.websocket;/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import com.alibaba.fastjson.JSONObject;
import com.hs.edu.app.cache.WebSocketManager;
import com.hs.edu.app.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
/**
 * @Author taxuefan
 * @Description //TODO
 * @Date 9:24 2020/10/9
 * @Param
 * @return
 **/
@ServerEndpoint("/webSocket/{roomId}/{userId}")
@Slf4j
public class WebSocketServer {
	WebSocketManager webSocketManager= WebSocketManager.getInstance();
 	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;

	/**
	 * 接收roomId
	 */
	private String roomId="";
	/**
	 * 接收userId
	 */
	private String userId="";
	/**
	 * 连接建立成功调用的方法
	 * */
	@OnOpen
	public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("userId") String userId) {
		this.session = session;
		this.roomId=roomId;
		this.userId=userId;
		log.info("websocket is opened the roomId is {} {}",roomId,userId);
		//添加websocket
		webSocketManager.addWebSocket(roomId,userId,this);
	}
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
	   log.info("the user exit room,roomId:{},userId:{}",roomId,userId);
       webSocketManager.sendCloseRoomPacket(roomId,this.userId);
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息*/
	@OnMessage
	public void onMessage(String message, Session session) {
		//目前暂时不处理客户端发来的信息
		log.info("从房间号{},收到用户{}的信息 ",this.roomId,this.userId);
		//群发消息
		/**for (WebSocketServer item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}**/
	}
	@OnError
	public void onError(Session session, Throwable error) {
		log.info("从房间号{},收到用户{}的websocket发生错误",this.roomId,this.userId);
		error.printStackTrace();
	}
	/**
	 * 实现服务器主动推送
	 */
	private void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 * */
	public  void sendPacket(SocketMsg socketMsg) throws IOException {
		SocketMsg sendSocketPacket= AppUtils.filterMsgPacket(this.userId,socketMsg);
		String message = JSONObject.toJSONString(sendSocketPacket);
		log.info("推送消息到:{} userId{},roomId{}",message,userId,roomId);
		this.sendMessage(message);
		/**for (WebSocketServer item : webSocketSet) {
			try {
				//这里可以设定只推送给这个sid的，为null则全部推送
				if(sid==null) {
					item.sendMessage(message);
				}else if(item.sid.equals(sid)){
					item.sendMessage(message);
				}
			} catch (IOException ignored) { }
		}**/
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		WebSocketServer that = (WebSocketServer) o;
		return Objects.equals(session, that.session) &&
				Objects.equals(roomId, that.roomId)&&Objects.equals(userId,that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(session, roomId,userId);
	}
}
