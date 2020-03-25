package indi.moneky.game.user;

import java.io.IOException;

import javax.websocket.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GamePlayer implements Player {

	private String username;
	private Session session;
	private boolean isOnline;

	public GamePlayer(Session session, String username, int messageQueueSize) {
		this.session = session;
		this.username = username;
		this.isOnline = true;
		// 用户量太大。使用线程维护消息发送过于消耗资源
		// this.new MessageThead().start();
	}

	public GamePlayer(Session session, String username) {
		this(session, username, -1);
	}

	@Override
	public String getUserName() {
		return username;
	}

	@Override
	public void write(String message) {
		// 有可能会单独发送消息
		synchronized (session) {
			try {
				log.info("send message:{} to player:{}", message, username);
				this.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				log.error("{} message send error:{}", username, e);
			}
		}
	}

	@Override
	public boolean isOffline() {
		return !isOnline;
	}

	@Override
	public void offline() {
		this.isOnline = false;
	}

	@Override
	public Session getSession() {
		return this.session;
	}
}
