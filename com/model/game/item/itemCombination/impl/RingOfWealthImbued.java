package com.model.game.item.itemCombination.impl;

import java.util.List;
import java.util.Optional;

import com.model.game.character.player.Player;
import com.model.game.character.player.dialogue.SimpleDialogues;
import com.model.game.item.Item;
import com.model.game.item.itemCombination.ItemCombination;


public class RingOfWealthImbued extends ItemCombination {

	public RingOfWealthImbued(Optional<int[]> skillRequirements, Item outcome, Optional<List<Item>> revertedItems, Item[] items) {
		super(skillRequirements, outcome, revertedItems, items);
	}

	@Override
	public void combine(Player player) {
		items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(outcome.getId(), outcome.getAmount());
		SimpleDialogues.sendStatement(player,"You combined the items and created a ring of wealth (i).");
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}
	
	@Override
	public void showDialogue(Player player) {
		SimpleDialogues.sendStatement(player, "The ring of wealth imbued is untradable.",
				"You cannot revert this.");
	}

}
