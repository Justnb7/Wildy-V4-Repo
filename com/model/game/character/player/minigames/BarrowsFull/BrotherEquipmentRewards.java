package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.player.Player;

public enum BrotherEquipmentRewards implements Loot {

	AHRIMS_HOOD(4708),
	AHRIMS_STAFF(4710),
	AHRIMS_ROBE_TOP(4712),
	AHRIMS_ROBE_SKIRT(4714),
	DHAROKS_HELM(4716),
	DHAROKS_GREATAXE(4718),
	DHAROKS_PLATEBODY(4720),
	DHAROKS_PLATELEGS(4722),
	GUTHANS_HELM(4724),
	GUTHANS_WARSPEAR(4726),
	GUTHANS_PLATEBODY(4728),
	GUTHANS_CHAINSKIRT(4730),
	KARILS_COIF(4732),
	KARILS_CROSSBOW(4734),
	KARILS_TOP(4736),
	KARILS_SKIRT(4738),
	TORAGS_HELM(4745),
	TORAGS_HAMMERS(4747),
	TORAGS_PLATEBODY(4749),
	TORAGS_PLATELEGS(4751),
	VERACS_HELM(4753),
	VERACS_FLAIL(4755),
	VERACS_BRASSARD(4757),
	VERACS_PLATESKIRT(4759);

	private final int itemID;

	private BrotherEquipmentRewards(int itemID) {
		this.itemID = itemID;
	}

	@Override public int getItemID() {
		return itemID;
	}

	@Override public double getChance(Player client) {
		BarrowsNpcController npcController = client.getBarrows().getNpcController();

		return 1.0 / (120.0 + (7.0 * (BarrowsConstants.NUMBER_OF_BROTHERS - npcController.countBrothersKilled())));
	}
}