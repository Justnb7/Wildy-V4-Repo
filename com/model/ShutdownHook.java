package com.model;

import com.model.game.World;
import com.model.game.character.player.Player;
import com.model.game.character.player.serialize.PlayerSerialization;

/**
 * A thread which will be started when the server is being shut down. Although in most cases the Thread will be started, it cannot be guaranteed.
 * 
 * @author Emiel
 *
 */
public class ShutdownHook extends Thread {

	public void run() {
		for (Player players : World.getWorld().getPlayers()) {
			if (players != null && players.isActive()) {
				PlayerSerialization.saveGame(players);
			}
		}
		System.out.println("Successfully executed ShutdownHook");
	}
}
