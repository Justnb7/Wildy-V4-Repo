package com.model.game.character.player.minigames.BarrowsFull;

import java.util.ArrayList;
import java.util.List;

import com.model.Server;
import com.model.game.character.player.Player;
import com.model.game.location.Position;
import com.model.task.ScheduledTask;


public class BarrowsMaze {
	/**
	 * The tunnel you spawn in after you dig.
	 */
	private TunnelSpawns spawn = TunnelSpawns.SOUTHEAST_SPAWN;

	/**
	 * Keeps track of closed hallways.
	 */
	private final List<TunnelHallwayData> closedHallways = new ArrayList<>();

	private final Player client;

	public BarrowsMaze(Player client) {
		this.client = client;
	}

	/**
	 * Reopen any closed doors.
	 */
	public void resetHallways() {
		for (TunnelHallwayData hallway : closedHallways) {
			hallway.openDoors(client);
		}
		closedHallways.clear();
	}

	/**
	 * Close of hallways.
	 */
	public void randomizeMaze() {
		resetHallways();
		randomizeChestRoom();
		randomizeSpawnRoom();
	}

	/**
	 * Set a random spawn point.
	 */
	public void randomizeSpawnPoint() {
		spawn = TunnelSpawns.SPAWNS[(int) (Math.random() * TunnelSpawns.SPAWNS.length)];
	}

	/**
	 * Teleport inside of the maze after digging.
	 */
	public void teleportToMaze() {
		Position position = spawn.getPosition();
		client.teleportToX = position.getX();
		client.teleportToY = position.getY();
		client.heightLevel = position.getZ();
		// Teleporting too fast, objects spawn on wrong level.
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {
			@Override
			public void execute() {
				//client.getActionSender().disableMap(2);
				HideMiniMap.toggle(client);
				randomizeMaze();
				this.stop();
			}
		});
	}

	private void randomizeRoom(TunnelRoomData room) {
		int openDirection = (int) (Math.random() * 4);

		// Loop through the 4 cardinal directions and close off all but 1 hallway.
		for (int direction = 0; direction < 4; direction++) {
			TunnelHallwayData hallway = room.getHallway(direction);

			if (hallway == null || direction == openDirection) {
				continue;
			}
			hallway.closeDoors(client);
			closedHallways.add(hallway);
		}
	}

	private void randomizeChestRoom() {
		randomizeRoom(TunnelRoomData.CHEST);
	}

	private void randomizeSpawnRoom() {
		randomizeRoom(spawn.getRoom());
	}

	public TunnelSpawns getSpawn() {
		return spawn;
	}
}