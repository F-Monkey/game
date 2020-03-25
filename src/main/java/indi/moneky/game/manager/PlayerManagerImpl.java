package indi.moneky.game.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.websocket.Session;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import indi.moneky.game.user.Player;
import indi.moneky.game.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerManagerImpl implements PlayerManager {

	// users
	private final ConcurrentHashMap<String, Player> users;

	private final ConcurrentHashMap<String, String> sessions;

	// thread poll
	private ExecutorService executorService;

	private int TIME_OUT = 2 << 9;

	private static Object REMOVE_LOCK = new Object();

	public PlayerManagerImpl() {
		this.users = new ConcurrentHashMap<>();
		this.sessions = new ConcurrentHashMap<>();
		this.executorService = new ThreadPoolExecutor(2, ThreadPoolUtil.ioIntesivePoolSize(), TIME_OUT,
				TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1000));
		new Thread(this.new RemoveUserThread()).start();
	}

	@Override
	public void addPlayer(Player player) {
		users.put(player.getUserName(), player);
		String id = player.getSession().getId();
		sessions.put(id, player.getUserName());
	}

	@Override
	public Player getPlayer(String username) {
		return users.get(username);
	}

	private class RemoveUserThread implements Runnable {

		@Override
		public void run() {
			for (;;) {
				synchronized (REMOVE_LOCK) {
					try {
						REMOVE_LOCK.wait();
					} catch (InterruptedException e) {
					}
				}
				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e1) {
				}
				for (Iterator<Entry<String, Player>> it = users.entrySet().iterator(); it.hasNext();) {
					Entry<String, Player> e = it.next();
					Player player;
					if ((player = e.getValue()) != null && player.isOffline()) {
						log.info("player:{} remove", player.getUserName());
						it.remove();
					}
				}
			}
		}
	}

	@Override
	public synchronized void broadcast(final String message) {
		if (!users.isEmpty()) {
			List<Runnable> tasks = users.values().parallelStream().filter(user -> !user.isOffline()).map(user -> {
				Runnable runnable = () -> {
					user.write(message);
				};
				return runnable;
			}).collect(Collectors.toList());
			tasks.forEach(executorService::execute);
		}
	}

	public void sendMessage(String username, String message) {
		Player player = getPlayer(username);
		if (null != player) {
			player.write(message);
		}
	}

	@Override
	public void offline(Session session) {
		//
		Player player = getPlayer(session);
		if (null != player) {
			log.info("{} player offline...", player.getUserName());
			player.offline();
			synchronized (REMOVE_LOCK) {
				REMOVE_LOCK.notifyAll();
			}
		}
	}

	private Player getPlayer(Session session) {
		String username = sessions.get(session.getId());
		if (null != username) {
			return getPlayer(username);
		}
		return null;
	}

	@Override
	public void onMessage(Session session, String message) {
		Player player = getPlayer(session);
		if (null != player) {
			log.info("message:{} from player:{}", message, player.getUserName());
		}
	}

	@Override
	public Set<String> getPlayers() {
		return users.keySet();
	}

}
