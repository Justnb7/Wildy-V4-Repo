package com.model.game.sync;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;
import com.model.Server;
import com.model.game.World;
import com.model.game.character.player.Player;
import com.model.game.item.ground.GroundItemHandler;
import com.model.task.events.CycleEventHandler;

/**
 * A service dedicated to handling all game logic. This service executes packets
 * every {@code 300}ms instead of every {@code 600}ms to improve the speed of
 * packet processing.
 *
 * @author Mobster
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class GameLogicService implements Runnable {
	
	/**
	 * A logger for the {@link GameLogicService} class
	 */
	private static final Logger logger = Logger.getLogger(GameLogicService.class.getName());

	private static final AtomicBoolean LOCK = new AtomicBoolean();
	private static final ScheduledExecutorService GAME_SERVICE = Executors.newSingleThreadScheduledExecutor();
	private static final GameLogicService engine = new GameLogicService();

	private static final Queue<Player> loginQueue = new ConcurrentLinkedQueue<>();

	public static void queueLogin(Player player) {
		Preconditions.checkArgument(!loginQueue.contains(player), "Login queue already contains " + player);
		loginQueue.add(player);
	}

	public static Collection<Player> getLoginQueue() {
		return Collections.unmodifiableCollection(loginQueue);
	}

	private static final int LOGIN_THRESHOLD = 25;

	public static GameLogicService getEngine() {
		return engine;
	}

	public static void initialize() {
		if (LOCK.compareAndSet(false, true)) {
			GameLogicService gameEngine = new GameLogicService();
			GAME_SERVICE.scheduleAtFixedRate(gameEngine, 0, 300, TimeUnit.MILLISECONDS);
			logger.info("Game Engine initialized");
		}
	}

	private static void subcycle() {
		for (Player player : World.getWorld().getUnorderedPlayers()) {
			if (player != null) {
				player.getSession().processSubQueuedPackets();
				player.getSession().processQueuedPackets();
				player.refresh_inventory();
			}
		}
	}

	private boolean cycle;

	@Override
	public void run() {
		try {
			if (cycle) {
				cycle();
				cycle = false;
			} else {
				subcycle();
				cycle = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void cycle() {
		for (int count = 0; count < LOGIN_THRESHOLD; count++) {
			Player p = loginQueue.poll();
			if (p == null)
				break;
			World.getWorld().register(p);
		}
		Server.getGlobalObjects().pulse();
		Server.getTaskScheduler().pulse();
		World.getWorld().pulse();
		GroundItemHandler.pulse();
		CycleEventHandler.getSingleton().process();
	}

	private GameLogicService() {
	}
}