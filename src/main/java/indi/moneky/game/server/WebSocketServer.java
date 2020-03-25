package indi.moneky.game.server;

import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import indi.moneky.game.manager.PlayerManager;
import indi.moneky.game.user.GamePlayer;
import lombok.extern.slf4j.Slf4j;

@ServerEndpoint("/user/{username}")
@Component
@Slf4j
public class WebSocketServer {

	static PlayerManager playerManager;

	public WebSocketServer() {
	}

	public static void setPlayerManager(PlayerManager playerManager) {
		WebSocketServer.playerManager = playerManager;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) {
		log.info("player:{} login...", username);
		playerManager.addPlayer(new GamePlayer(session, username));
		session.addMessageHandler(new MessageHandler.Whole<String>() {

			@Override
			public void onMessage(String message) {
				log.info("onmessage:{}", message);
			}
		});

	}

	@OnClose
	public void onClose(Session session) {
		playerManager.offline(session);
	}

	//	@OnMessage	
	@Deprecated
	public void onMessage(Session session, String message) {
	}

	@OnError
	public void onError(Session session, Throwable e) {
		log.error("error:{}", e);
		// 客户端中断
		playerManager.offline(session);
	}

}
