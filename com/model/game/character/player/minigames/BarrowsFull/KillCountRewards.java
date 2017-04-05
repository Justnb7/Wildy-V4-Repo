package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.player.Player;

public enum KillCountRewards implements Loot {

	KEY_TOOTH(985),
	KEY_LOOP(987),
	DMED(1149);

	private final int itemID;

	private KillCountRewards(int itemID) {
		this.itemID = itemID;
	}

	public static final KillCountRewards[] REWARDS = KillCountRewards.values();

	@Override public int getItemID() {
		return itemID;
	}

	@Override public double getChance(Player client) {
		BarrowsNpcController npcController = client.getBarrows().getNpcController();
		int killCountCeiling = Math.min(8, npcController.getKillCount());

		return killCountCeiling / 1000.0;
	}
}