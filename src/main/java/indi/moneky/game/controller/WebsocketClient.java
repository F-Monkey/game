package indi.moneky.game.controller;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebsocketClient extends WebSocketClient {

	public WebsocketClient(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("onOpen~ client");
	}

	@Override
	public void onMessage(String message) {
		System.out.println("client:" + message);
		double random = Math.random();
		if (random > 0.5) { //模拟用户退出
			this.close();
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {

	}

	@Override
	public void onError(Exception ex) {

	}

}
