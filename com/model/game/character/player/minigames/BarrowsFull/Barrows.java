package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.player.Boundary;
import com.model.game.character.player.Player;
import com.model.game.character.player.packets.out.SendWalkableInterfacePacket;

public class Barrows {
	/**
	 * Controls the mazes tunnels and rooms.
	 */
	private final BarrowsMaze maze;

	/**
	 * Controls the loot.
	 */
	private final BarrowsChest chest;

	/**
	 * Randomizes the puzzle on doors.
	 */
	private final BarrowsPuzzleDisplay puzzle;

	/**
	 * Tracks kill count, brothers killed, and NPC spawns.
	 */
	private final BarrowsNpcController npcController;

	private final OpenTunnelDoorHandler doorHandler;

	private final MoundHandler moundHandler;

	private final TombObjectHandler tombObjectHandler;

	private final TombStairsHandler tombStairsHandler;

	public Barrows(Player client) {
		this.maze = new BarrowsMaze(client);
		this.chest = new BarrowsChest(client);
		this.npcController = new BarrowsNpcController(client);
		this.puzzle = new BarrowsPuzzleDisplay();

		this.doorHandler = new OpenTunnelDoorHandler(client);
		this.moundHandler = new MoundHandler(client);
		this.tombObjectHandler = new TombObjectHandler(client);
		this.tombStairsHandler = new TombStairsHandler(client);

		this.resetGame();
	}
	public static boolean isBarrowsNpc(int npc){
		switch (npc){
			case 1677://verac
			case 1676: // torag
			case 1675://karil
			case 1674://guthan
			case 1673://dharok
			case 1672://ahrim
		return true;
		
		}
		return false;
	}
	
	public void setBarrowsInterface(Player player){
		if(Boundary.isIn(player, Boundary.BARROWS_HILLS)) {
			Barrows barrows = player.getBarrows();
			player.getActionSender().sendString("" + barrows.getNpcController().getKillCount(), 16137);
			if (barrows.getNpcController().checkIfKilled(Brother.KARIL)) {
				player.getActionSender().sendString("@red@Karils", 16135);
			}
			if (barrows.getNpcController().checkIfKilled(Brother.GUTHAN)) {
				player.getActionSender().sendString("@red@Guthans", 16134);
			}
			if (barrows.getNpcController().checkIfKilled(Brother.TORAG)) {
				player.getActionSender().sendString("@red@Torags", 16133);
			}
			if (barrows.getNpcController().checkIfKilled(Brother.AHRIM)) {
				player.getActionSender().sendString("@red@Ahrims", 16132);
			}
			if (barrows.getNpcController().checkIfKilled(Brother.VERAC)) {
				player.getActionSender().sendString("@red@Veracs", 16131);
			}
			if (barrows.getNpcController().checkIfKilled(Brother.DHAROK)) {
				player.getActionSender().sendString("@red@Dharoks", 16130);
			}
		}
	}
	public void resetBarrowsInterface(Player player){
		if(Boundary.isIn(player, Boundary.BARROWS_HILLS)) {
			Barrows barrows = player.getBarrows();
			player.getActionSender().sendString("" + barrows.getNpcController().getKillCount(), 16137);
			if (barrows.getNpcController().checkIfKilled(Brother.KARIL)) {
				player.getActionSender().sendString("@red@Karils", 16135);
			} else player.getActionSender().sendString("@gre@Karils", 16135);
			if (barrows.getNpcController().checkIfKilled(Brother.GUTHAN)) {
				player.getActionSender().sendString("@red@Guthans", 16134);
			} else player.getActionSender().sendString("@gre@Guthans", 16134);
			if (barrows.getNpcController().checkIfKilled(Brother.TORAG)) {
				player.getActionSender().sendString("@red@Torags", 16133);
			} else player.getActionSender().sendString("@gre@Torags", 16133);
			if (barrows.getNpcController().checkIfKilled(Brother.AHRIM)) {
				player.getActionSender().sendString("@red@Ahrims", 16132);
			} else player.getActionSender().sendString("@gre@Ahrims", 16132);
			if (barrows.getNpcController().checkIfKilled(Brother.VERAC)) {
				player.getActionSender().sendString("@red@Veracs", 16131);
			} else player.getActionSender().sendString("@gre@Veracs", 16131);
			if (barrows.getNpcController().checkIfKilled(Brother.DHAROK)) {
				player.getActionSender().sendString("@red@Dharoks", 16130);
			} else player.getActionSender().sendString("@gre@Dharoks", 16130);
		}
	}
	public void resetGame() {
		// Maze is reset on teleporting into tunnels.
		maze.randomizeSpawnPoint();
		chest.reset();
		npcController.reset();
		puzzle.reset();
	}

	public BarrowsNpcController getNpcController() {
		return npcController;
	}

	public BarrowsMaze getMaze() {
		return maze;
	}

	public BarrowsPuzzleDisplay getPuzzle() {
		return puzzle;
	}

	public BarrowsChest getChest() {
		return chest;
	}

	public OpenTunnelDoorHandler getDoorHandler() {
		return doorHandler;
	}

	public MoundHandler getMoundHandler() {
		return moundHandler;
	}

	public TombObjectHandler getTombObjectHandler() {
		return tombObjectHandler;
	}

	public TombStairsHandler getTombStairsHandler() {
		return tombStairsHandler;
	}
}