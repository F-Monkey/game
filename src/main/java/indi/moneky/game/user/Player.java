package indi.moneky.game.user;

import javax.websocket.Session;

public interface Player {
	String getUserName();
	
	Session getSession();
	
	void write(String message);

	void offline();

	boolean isOffline();
	
}
