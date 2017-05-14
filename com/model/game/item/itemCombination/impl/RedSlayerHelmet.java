package com.model.game.item.itemCombination.impl;

import java.util.List;
import java.util.Optional;

import com.model.game.character.player.Player;
import com.model.game.character.player.Skills;
import com.model.game.character.player.Skills.SkillData;
import com.model.game.character.player.dialogue.SimpleDialogues;
import com.model.game.item.Item;
import com.model.game.item.itemCombination.ItemCombination;



public class RedSlayerHelmet extends ItemCombination {

	public RedSlayerHelmet(Optional<int[]> skillRequirements, Item outcome, Optional<List<Item>> revertedItems, Item[] items) {
		super(skillRequirements, outcome, revertedItems, items);
	}

	@Override
	public void combine(Player player) {
		items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(outcome.getId(), outcome.getAmount());
		SimpleDialogues.sendStatement(player, "You combined the items and created "+player.getItems().getItemName(outcome.getId()));
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}
	
	@Override
	public void showDialogue(Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) <= 55) {
			player.getActionSender().sendMessage("55 crafting is required to make this helmet");
			player.setCurrentCombination(Optional.empty());
			return;
		}
		if(player.getSlayerInterface().getUnlocks().entrySet().toString().contains("UNHOLY_HELMET")) {
		SimpleDialogues.sendStatement(player, ""+player.getItems().getItemName(outcome.getId())+" is untradeable.",
				"You can revert this item.");
		} else {
			player.getActionSender().sendMessage("You don't have the slayer unlock for this action.");
			player.setCurrentCombination(Optional.empty());
		}
	}
}
