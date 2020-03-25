package indi.moneky.game.manager;

import java.util.Set;

import javax.websocket.Session;

import indi.moneky.game.user.Player;

public interface PlayerManager {
	void addPlayer(Player player);
	
	Set<String> getPlayers();

	Player getPlayer(String username);

	void broadcast(String message);

	void onMessage(Session session, String message);

	public void sendMessage(String username, String message);

	void offline(Session session);
}
