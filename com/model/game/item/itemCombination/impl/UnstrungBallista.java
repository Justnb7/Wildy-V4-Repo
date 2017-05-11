package com.model.game.item.itemCombination.impl;

import java.util.List;
import java.util.Optional;

import com.model.game.character.player.Player;
import com.model.game.character.player.dialogue.SimpleDialogues;
import com.model.game.item.Item;
import com.model.game.item.itemCombination.ItemCombination;



public class UnstrungBallista extends ItemCombination {

	public UnstrungBallista(Optional<int[]> skillRequirements, Item outcome, Optional<List<Item>> revertedItems, Item[] items) {
		super(skillRequirements, outcome, revertedItems, items);
	}

	@Override
	public void combine(Player player) {
		super.items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(super.outcome.getId(), super.outcome.getAmount());
		SimpleDialogues.sendStatement(player, "Your items created an unstrung heavy Ballista. Last piece: Monkey's Tail");
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}
	
	@Override
	public void showDialogue(Player player) {
		SimpleDialogues.sendStatement(player, "Once Combines ",
				"There is no going back",
				"Choose wisely.");
	}

}
