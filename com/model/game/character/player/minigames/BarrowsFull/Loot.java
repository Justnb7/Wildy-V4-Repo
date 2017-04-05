package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.player.Player;

public interface Loot {

	public int getItemID();

	public default int getAmount() {
		return 1;
	}

	public double getChance(Player client);
}