package com.model.game.item.itemCombination.impl;

import java.util.List;
import java.util.Optional;

import com.model.game.character.player.Player;
import com.model.game.character.player.dialogue.SimpleDialogues;
import com.model.game.item.Item;
import com.model.game.item.itemCombination.ItemCombination;



public class AmuletOfFury extends ItemCombination {

	public AmuletOfFury(Optional<int[]> skillRequirements, Item outcome, Optional<List<Item>> revertedItems, Item[] items) {
		super(skillRequirements, outcome, revertedItems, items);
	}

	@Override
	public void combine(Player player) {
		super.items.forEach(item -> player.getItems().deleteItem(item.getId(), item.getAmount()));
		player.getItems().addItem(super.outcome.getId(), super.outcome.getAmount());
		SimpleDialogues.sendStatement(player, "You combined the items and created the Amulet of Fury (or).");
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}
	
	@Override
	public void showDialogue(Player player) {
		SimpleDialogues.sendStatement(player, "The Amulet of fury (or) is untradeable.",
				"You can dismantle this item at any time and receive",
				"both the kit and the fury back.");
	}

}
