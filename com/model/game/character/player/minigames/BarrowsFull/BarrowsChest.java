package com.model.game.character.player.minigames.BarrowsFull;

import java.util.ArrayList;
import java.util.List;

import com.model.Server;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;
import com.model.task.TaskScheduler;



public class BarrowsChest {
	/**
	 * Boolean for keeping track of the chest is looted.
	 * This is also an end game check. If you dig in a
	 * mound and the chest is looted, start a new game.
	 */
	private boolean looted; // Save this.
	private boolean interacted = false;

	private ScheduledTask openTask;

	private final Player client;

	public static final int BARROWS_CHEST = 20973;
	public static final int BARROWS_CHEST_OPEN = 20724;
	public BarrowsChest(Player client) {
		this.client = client;
	}

	public boolean openChest(int objectID, int objectX, int objectY) {
		if (objectID != BARROWS_CHEST && objectID != BARROWS_CHEST_OPEN) {
			return false;
		}
		if (objectID == BARROWS_CHEST) {
		client.getPA().checkObjectSpawn(BARROWS_CHEST_OPEN, objectX, objectY, 0, 10);

		if (openTask != null) {
			openTask.stop();
		}
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (client.goodDistance(objectX, objectY, client.absX, client.absY, 2)) {
					client.getBarrows().getChest().spawnNpc();
					this.stop();
				}
			}
		});
	} else {
		if(objectID == BARROWS_CHEST_OPEN) {
			Server.getTaskScheduler().schedule(new ScheduledTask(1) {
				@Override
				public void execute() {
					if (client.goodDistance(objectX, objectY, client.absX, client.absY, 2)) {
						client.getBarrows().getChest().lootChest();
					//	client.getPA().checkObjectSpawn(BARROWS_CHEST, objectX, objectY, 0, 10);
						this.stop();
					}
				}
			});
		}
	}
		return true;
	}

	public void lootChest() {
		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();
		Brother brother = npcController.getTargetBrother();
		// Check if the target brother is killed and spawned a if not, spawn him.
		if (!npcController.checkIfKilled(brother) && !npcController.checkIfSpawned(brother)) {
			npcController.spawnBrother(brother);
		}
		if (!hasLooted()) {
			if (!interacted) {
				interacted = true;
			} else {
				rollRewards();
				client.getActionSender().sendMessage("<img=15>You receive your rewards..<img=15>");
				setLooted(true);
				client.getBarrows().getNpcController().reset();
			}
		} else {
			client.getActionSender().sendMessage(BarrowsConstants.EMPTY_CHEST);
		}
	}
	/*
	 * Spawns the Brother if applicable
	 */
	public void spawnNpc() {
		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();
		Brother brother = npcController.getTargetBrother();
		// Check if the target brother is killed and spawned a if not, spawn him.
		if (!npcController.checkIfKilled(brother) && !npcController.checkIfSpawned(brother)) {
			npcController.spawnBrother(brother);
		}
	}
	private void rollRewards() {
		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();
		for (int roll = 0; roll < npcController.countBrothersKilled(); roll++) {
			rollRegularItems();
			rollBrotherEquipment();
		}
		rollKillCountItems();
	}

	private void rollRegularItems() {
		RegularRewardsData regularItems = RegularRewardsData.REWARDS[(int) (Math.random() * RegularRewardsData.REWARDS.length)];
		rollForLoot(regularItems);
	}

	private void rollKillCountItems() {
		KillCountRewards regularItems = KillCountRewards.REWARDS[(int) (Math.random() * KillCountRewards.REWARDS.length)];
		rollForLoot(regularItems);
	}

	private void rollBrotherEquipment() {
		BrotherEquipmentRewards item = getRandomBarrowsEquipment();
		rollForLoot(item);
	}

	private void rollForLoot(Loot loot) {
		double random = Math.random();

		if (loot.getChance(client) >= random) {
			client.getItems().addItem(loot.getItemID(), loot.getAmount());
		}
	}

	private BrotherEquipmentRewards getRandomBarrowsEquipment() {
		List<BrotherEquipmentRewards> items = new ArrayList<>();

		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();

		// Add killed brothers loot to a list and shuffle it.
		for (Brother brother : Brother.values()) {
			if (npcController.checkIfKilled(brother)) {
				for (BrotherEquipmentRewards item : brother.getGear()) {
					items.add(item);
				}
			}
		}
		return items.get((int) (Math.random() * items.size()));
	}

	public void reset() {
		setLooted(false);
		interacted = false;
	}

	public void setLooted(boolean looted) {
		this.looted = looted;
	}

	public boolean hasLooted() {
		return looted;
	}
}