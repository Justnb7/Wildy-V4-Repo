package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.npc.NPC;
import com.model.game.character.npc.NPCHandler;
import com.model.game.character.player.Player;
import com.model.game.character.player.packets.out.SendWalkableInterfacePacket;
import com.model.utility.Utility;

import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.AHRIMS_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.CRYPT_RAT_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.SKELETON_ID_4;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.VERACS_ID;

public class BarrowsNpcController {
	/**
	 * Counter for the crypt NPCs.
	 */
	private int killCount = 0; // Save this.

	/**
	 * Tracker for brothers killed.
	 * Each of the first 6 bits is a brother.
	 * 0 = not killed, 1 = killed.
	 */
	private int killedBrothers = 0x0; // Save this.

	/**
	 * Tracker for brothers spawned.
	 * Each of the first 6 bits is a brother.
	 * 0 = not spawned, 1 = spawned.
	 */
	private int spawnedBrothers = 0x0;

	/**
	 * The brother is the target, aka the mound you search.
	 */
	private Brother targetBrother = Brother.AHRIM; // Save this.

	private final Player client;

	public BarrowsNpcController(Player client) {
		this.client = client;
	}

	public void reset() {
		clearKills();
		despawnBrothers();
		determineBrother();
	}
	
	public void spawnBrother(Brother brother) {
		NPC npc = NPCHandler.spawnNpc(client, brother.getID(), client.getX() + 1, client.getY(), client.getZ(), 0, true, true, false);
		npc.forceChat(Utility.random(1) == 0 ? BarrowsConstants.BROTHER_AWAKEN : BarrowsConstants.BROTHER_THEFT);
		registerSpawn(brother);
	}

	public boolean registerKill(int npcID) {
		if (npcID >= AHRIMS_ID && npcID <= VERACS_ID) {
			registerKill(Brother.lookup(npcID));
			increaseKillCount();
			/*for (Brother brother : Brother.values()) {
				if (client.getBarrows().getNpcController().checkIfKilled(brother)) {
					client.getActionSender().sendMessage("Brother: "+NPC.getName(brother.getID())+" killed");
				}
			}*/
		} else if (npcID >= CRYPT_RAT_ID && npcID <= SKELETON_ID_4) {
			increaseKillCount();
			client.getActionSender().sendMessage("<img=8> Kill Count "+client.getBarrows().getNpcController().getKillCount());
		}
		return npcID >= AHRIMS_ID && npcID <= SKELETON_ID_4;
	}

	public int countBrothersKilled() {
		int num = 0;
		for (int bitPosition = 0; bitPosition < BarrowsConstants.NUMBER_OF_BROTHERS; bitPosition++) {
			// Go through the six bits and add up all the ones.
			num += (killedBrothers >> bitPosition) & 0x1;
		}
		return num;
	}

	private void clearKills() {
		killCount = 0;
		killedBrothers = 0x0;
	}

	public void despawnBrothers() {
		spawnedBrothers = 0x0;
	}

	public void registerSpawn(Brother brother) {
		spawnedBrothers |= brother.getMask();
	}

	private void registerKill(Brother brother) {
		killedBrothers |= brother.getMask();
	}

	public void unregisterSpawn(Brother brother) {
		spawnedBrothers &= ~brother.getMask();
	}

	private void increaseKillCount() {
		killCount++;
	}

	private void determineBrother() {
		targetBrother = Brother.values()[(int) (Math.random() * BarrowsConstants.NUMBER_OF_BROTHERS)];
	}

	public boolean checkIfSpawned(Brother brother) {
		return (spawnedBrothers & brother.getMask()) != 0;
	}

	public boolean checkIfKilled(Brother brother) {
		return (killedBrothers & brother.getMask()) != 0;
	}

	public void setKilledBrothers(int killedBrothers) {
		this.killedBrothers = killedBrothers;
	}

	public void setTargetBrother(Brother targetBrother) {
		this.targetBrother = targetBrother;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public Brother getTargetBrother() {
		return targetBrother;
	}

	public int getKilledBrothers() {
		return killedBrothers;
	}

	public int getKillCount() {
		return killCount;
	}
}