package com.hs.edu.app.websocket;/*
 /**
 * @Author taxuefan
 * @Description websocket消息类型
 * @Date 9:13 2020/10/10
 * @Param
 * @return
 **/

import com.hs.edu.app.enums.Role;

import java.util.HashMap;
import java.util.Map;

public enum MsgType {
	/** 网家连接服务器消息 */
	CONNECT,
	/**进入房间**/
	ENTER_ROOM,
	/**游戏结束**/
	GAME_OVER,
	/**游戏中**/
	GAMING,
	/**游戏被迫关闭掉**/
	GAME_OVER_FORCE,
	/** 关闭连接消息 */
	CLOSE,

	/**  获取 **/
	WEED_OUT_PLAYER,
	/**
	 * @Author 网家退出房间
	 * @Description //TODO 
	 * @Date 17:13 2020/10/10
	 * @Param 
	 * @return 
	 **/
	PLAYER_EXIT_ROOM,
	/**
	 * @Author 关闭网络
	 * @Description //TODO 
	 * @Date 23:18 2020/10/10
	 * @Param 
	 * @return 
	 **/
	CLOSE_ROOM,
	/** 错误 */
	ERROR,


}
