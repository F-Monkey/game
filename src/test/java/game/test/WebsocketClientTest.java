package game.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket.READYSTATE;

public class WebsocketClientTest {
	public static void main(String[] args) throws URISyntaxException {
		for (int i = 0; i < 20; i++) {
			String wsUri = "ws://localhost:8080/user/player" + i;
			URI uri = new URI(wsUri);
			new Thread(() -> {
				WebsocketClient client = new WebsocketClient(uri);
				System.out.println("new client...");
				client.connect();
				while (!client.getReadyState().equals(READYSTATE.OPEN)) {

				}
				client.send("111");
			}).start();
		}
	}
}
