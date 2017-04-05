package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.player.Player;

public enum RegularRewardsData implements Loot {

	MIND_RUNE(558, 1, 2889, 0.40),
	CHAOS_RUNE(562, 1, 885, 0.40),
	DEATH_RUNE(560, 1, 392, 0.40),
	BLOOD_RUNE(565, 1, 199, 0.30),
	COINS(995, 1, 530, 0.70),
	BOLT_RACK(4740, 1, 199, 0.40);

	private final int itemID;
	private final int amountStart;
	private final int amountEnd;
	private final double baseChance;

	private RegularRewardsData(int itemID, int amountStart, int amountEnd, double baseChance) {
		this.itemID = itemID;
		this.amountStart = amountStart;
		this.amountEnd = amountEnd;
		this.baseChance = baseChance;
	}

	private RegularRewardsData(int itemID, int amount, double baseChance) {
		this(itemID, amount, amount, baseChance);
	}

	public static final RegularRewardsData[] REWARDS = RegularRewardsData.values();

	@Override public int getItemID() {
		return itemID;
	}

	@Override public int getAmount() {
		return amountStart + (int) (Math.random() * (amountEnd - amountStart + 1));
	}

	@Override public double getChance(Player client) {
		BarrowsNpcController npcController = client.getBarrows().getNpcController();
		int killCountCeiling = Math.min(8, npcController.getKillCount());

		return baseChance + (killCountCeiling / 100.0);
	}
}