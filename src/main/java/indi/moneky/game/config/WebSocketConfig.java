package indi.moneky.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import indi.moneky.game.manager.PlayerManager;
import indi.moneky.game.manager.PlayerManagerImpl;
import indi.moneky.game.server.WebSocketServer;

@Configuration
public class WebSocketConfig {

	@Bean
	public ServerEndpointExporter endpointExporter() {
		ServerEndpointExporter serverEndpointExporter = new ServerEndpointExporter();
		return serverEndpointExporter;
	}

	@Bean
	public PlayerManager playerManager() {
		PlayerManagerImpl playerManagerImpl = new PlayerManagerImpl();
		WebSocketServer.setPlayerManager(playerManagerImpl);
		return playerManagerImpl;
	}

}
