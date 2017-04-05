package com.model.game.character.player.content.clicking.items;

import com.model.game.character.player.Player;
import com.model.game.character.player.Rights;
import com.model.game.character.player.content.PotionCombinating;
import com.model.game.character.player.content.rewards.CrystalChest;
import com.model.game.character.player.skill.crafting.GemCutting;
import com.model.game.character.player.skill.crafting.Gems;
import com.model.game.character.player.skill.firemaking.Firemaking;
import com.model.game.character.player.skill.fletching.Fletching;
import com.model.game.character.player.skill.fletching.FletchingHandler;
import com.model.game.character.player.skill.fletching.Bolts;
import com.model.game.item.Item;
import com.model.utility.json.definitions.ItemDefinition;

public class ItemOnItem {
	
	/**
	 * Handles the action of using an item with another item.
	 * 
	 * @param player
	 *            The player performing this action.
	 * 
	 * @param usedItem
	 *            The {@link Item} that is being initially selected.
	 * 
	 * @param withItem
	 *            The {@link Item} that is being selected on.
	 */
	public static void handleAction(Player player, Item usedItem, Item withItem) {
		if (player.inDebugMode()) {
			System.out.println(String.format("[ItemOnItem] - itemUsed: %d usedWith: %d ", usedItem.getId(), withItem.getId()));
		}
		
		Firemaking.startFire(player, usedItem.getId(), withItem.getId(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
		
		if (player.rights == Rights.ADMINISTRATOR) {
			if(usedItem.getId() == 5733 || withItem.getId() == 5733) {
				int amount = player.getItems().checkAmount(withItem.getId());
				player.getItems().remove(new Item(withItem.getId(), amount));
				player.getActionSender().sendMessage("Whee... "+ItemDefinition.forId(withItem.getId()).getName()+" All gone!");
			}
		}
		
		if (PotionCombinating.get().isPotion(usedItem) && PotionCombinating.get().isPotion(withItem)) {
			if (PotionCombinating.get().matches(usedItem, withItem)) {
				PotionCombinating.get().mix(player, usedItem, withItem);
			} else {
				player.getActionSender().sendMessage("You cannot combine two potions of different types.");
			}
			return;
		}
		
		if (usedItem.getId() == 1755 || withItem.getId() == 1755) {
			Item uncut = null;
			if (usedItem.getId() == 1755) {
				uncut = withItem;
			} else {
				uncut = usedItem;
			}
			Gems gem = Gems.forId(uncut.getId());
			
			if (gem != null) {
				GemCutting.attemptGemCutting(player, usedItem.getId(), withItem.getId());
			}
		}
		
		if (CrystalChest.createKey(player)) {
			return;
		}
		
		for (int ref : Fletching.refItems) {
			if (usedItem.getId() == ref || withItem.getId() == ref) {
				FletchingHandler.appendType(player, usedItem.getId(), withItem.getId());
			}
		}
		
		for (final Bolts bolt : Bolts.values()) {
			if (usedItem.getId() == bolt.getUnfBolts() || withItem.getId() == bolt.getUnfBolts()) {
				FletchingHandler.appendType(player, usedItem.getId(), withItem.getId());
				return;
			}
		}
		
		if (usedItem.getId() == 227 || withItem.getId() == 227) {
			int primary = usedItem.getId() == 227 ? withItem.getId() : usedItem.getId();
			player.getHerblore().mix(primary);
			return;
		}

		if (usedItem.getId() == 227 || withItem.getId() == 227) {
			int primary = usedItem.getId() == 227 ? withItem.getId() : usedItem.getId();
			player.getHerblore().mix(primary);
			return;
		}
		
		if (usedItem.getId() == 11810 && withItem.getId() == 11798 || usedItem.getId() == 11798 && withItem.getId() == 11810) {
			player.getItems().remove(new Item(11810, 1));
			player.getItems().remove(new Item(11798, 1));
			player.getItems().addItem(new Item(11802, 1));
			return;
		}
		
		if (usedItem.getId() == 11812 && withItem.getId() == 11798 || usedItem.getId() == 11798 && withItem.getId() == 11812) {
			player.getItems().remove(new Item(11812, 1));
			player.getItems().remove(new Item(11798, 1));
			player.getItems().addItem(new Item(11804, 1));
			return;
		}
		
		if (usedItem.getId() == 11814 && withItem.getId() == 11798 || usedItem.getId() == 11798 && withItem.getId() == 11814) {
			player.getItems().remove(new Item(11814, 1));
			player.getItems().remove(new Item(11798, 1));
			player.getItems().addItem(new Item(11806, 1));
			return;
		}
		
		if (usedItem.getId() == 11816 && withItem.getId() == 11798 || usedItem.getId() == 11798 && withItem.getId() == 11816) {
			player.getItems().remove(new Item(11816, 1));
			player.getItems().remove(new Item(11798, 1));
			player.getItems().addItem(new Item(11808, 1));
			return;
		}
		
		if (usedItem.getId() == 11800 && withItem.getId() == 11818 || usedItem.getId() == 11818 && withItem.getId() == 11800) {
			player.getItems().remove(new Item(11818, 1));
			player.getItems().remove(new Item(11800, 1));
			player.getItems().addItem(new Item(11798, 1));
			return;
		}
		
		if (usedItem.getId() == 11820 && withItem.getId() == 11796 || usedItem.getId() == 11796 && withItem.getId() == 11820) {
			player.getItems().remove(new Item(11796, 1));
			player.getItems().remove(new Item(11820, 1));
			player.getItems().addItem(new Item(11798, 1));
			return;
		}
		
		if (usedItem.getId() == 11822 && withItem.getId() == 11794 || usedItem.getId() == 11794 && withItem.getId() == 11822) {
			player.getItems().remove(new Item(11794, 1));
			player.getItems().remove(new Item(11822, 1));
			player.getItems().addItem(new Item(11798, 1));
			return;
		}
		
		if (usedItem.getId() == 11818 && withItem.getId() == 11820 || usedItem.getId() == 11820 && withItem.getId() == 11818) {
			player.getItems().remove(new Item(11818, 1));
			player.getItems().remove(new Item(11820, 1));
			player.getItems().addItem(new Item(11794, 1));
			return;
		}
		
		if (usedItem.getId() == 11822 && withItem.getId() == 11818 || usedItem.getId() == 11818 && withItem.getId() == 11822) {
			player.getItems().remove(new Item(11818, 1));
			player.getItems().remove(new Item(11822, 1));
			player.getItems().addItem(new Item(11796, 1));
			return;
		}
		
		if (usedItem.getId() == 11820 && withItem.getId() == 11822 || usedItem.getId() == 11822 && withItem.getId() == 11820) {
			player.getItems().remove(new Item(11820, 1));
			player.getItems().remove(new Item(11822, 1));
			player.getItems().addItem(new Item(11800, 1));
			return;
		}
		
	}

}
