package indi.moneky.game.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocket.READYSTATE;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import indi.moneky.game.controller.request.dto.MessageData;
import indi.moneky.game.manager.PlayerManager;

@RestController
@RequestMapping(value = "/message")
public class MessageController {
	@Resource
	PlayerManager playerManager;

	public static ConcurrentHashMap<String, WebsocketClient> clients = new ConcurrentHashMap<>();

	public MessageController() throws URISyntaxException {
		for (int i = 0; i < 20; i++) {
			String wsUri = "ws://localhost:8080/user/player" + i;
			URI uri = new URI(wsUri);
			WebsocketClient client = new WebsocketClient(uri);
			clients.put("player" + i, client);
		}
	}

	@RequestMapping(value = { "/sendToClient/{username}", "/sendToClient" }, method = RequestMethod.POST)
	public void sendToClient(@PathVariable(required = false) String username, @RequestBody MessageData data) {
		String message = data.getMessage();
		if (message == null) {
			message = "1234567";
		}
		if (StringUtils.isEmpty(username)) {
			playerManager.broadcast(message);
		} else {
			playerManager.sendMessage(username, message);
		}
	}

	@RequestMapping(value = { "/sendToServer/{username}", "/sendToServer" }, method = RequestMethod.POST)
	public void sendToServer(@PathVariable(required = false) String username, @RequestBody MessageData data)
			throws URISyntaxException {
		String message = data.getMessage() == null ? "1234567" : data.getMessage();
		if (username == null) {
			clients.values().forEach(client -> {
				if (!client.isOpen()) {
					if (client.getReadyState().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED)) {
						try {
							client.connect();
						} catch (IllegalStateException e) {
						}
					} else if (client.getReadyState().equals(WebSocket.READYSTATE.CLOSING)
							|| client.getReadyState().equals(WebSocket.READYSTATE.CLOSED)) {
						client.reconnect();
					}
				}
				Runnable t = () -> {
					while (!client.getReadyState().equals(READYSTATE.OPEN)) {

					}
					client.send(message);
				};
				new Thread(t).start();
			});
		}
	}
}
