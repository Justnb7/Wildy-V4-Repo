package com.model.game.item.itemCombination;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.model.game.character.player.Player;
import com.model.game.character.player.dialogue.SimpleDialogues;
import com.model.game.item.Item;
import com.model.utility.json.definitions.ItemDefinition;



public abstract class ItemCombination {
	/**
	 * List of items that are required in the combination
	 */
	protected List<Item> items;
	
	/**
	 * The item received when the items are combined
	 */
	protected Item outcome;
	
	/**
	 * The item that can be reverted from the combination, if possible.
	 */
	protected Optional<List<Item>> revertedItems;
	
	/**
	 * The respective level and requirement associated with the combination.
	 */
	protected Optional<int[]> levelRequirements;
	
	/**
	 * Creates a new item combination
	 * @param items		the game items required
	 */
	public ItemCombination(Optional<int[]> levelRequirements, Item outcome, Optional<List<Item>> revertedItems, Item...items) {
		this.levelRequirements = levelRequirements;
		this.items = Arrays.asList(items);
		this.outcome = outcome;
		this.revertedItems = revertedItems;
	}
	
	/**
	 * Combines all of the items to create the outcome
	 * @param player	the player combining the items
	 */
	public abstract void combine(Player player);
	
	/**
	 * Shows the initial dialogue with basic information about the combination
	 * @param player	the player
	 */
	public abstract void showDialogue(Player player);
	
	/**
	 * Reverts the outcome, if possible, back to the items used to combine it
	 * @param player	the player requesting reversion
	 */
	public void revert(Player player) {
		if (!revertedItems.isPresent()) {
			return;
		}
		if (player.getItems().freeSlots() < revertedItems.get().size()) {
			SimpleDialogues.sendStatement(player,"You need atleast "+revertedItems.get().size()+""
					+ " inventory slots to do this.");
			player.nextChat = -1;
			return;
		}
		player.getItems().deleteItem2(outcome.getId(), outcome.getAmount());
		revertedItems.get().forEach(item -> player.getItems().addItem(item.getId(), item.getAmount()));
		SimpleDialogues.sendStatement(player,"The "+ItemDefinition.forId(outcome.getId()).getName()+" has been split up.",
				"You have received some of the item(s) used in the making of", "this item.");
		player.nextChat = -1;
	}
	
	/**
	 * Notifies the player that they do not meet the specified requirements.
	 * @param player	the player
	 */
	public void insufficientRequirements(Player player) {
		if (revertedItems.isPresent()) {
			return;
		}
		SimpleDialogues.sendStatement(player,"You must have a " + /*Skill.has() +*/
									 " level of at least " + getLevelRequired() + " to combine these items.");
		player.nextChat = -1;
	}
	
	/**
	 * Sends the confirmation page to the player so they can choose to cancel it
	 * @param player	the player  
	 */
	public void sendCombineConfirmation(Player player) {
		player.dialogue().start("COMBINE_ITEMS");
	}
	
	/**
	 * Sends the confirmation page to the player so thay revert the item, or cancel the decision.
	 * @param player	the player
	 */
	public void sendRevertConfirmation(Player player) {
		player.dialogue().start("DISMANTLE");
	}
	
	/**
	 * Determines if the player has all of the items required for the combination.
	 * @param player	the player making the combination	
	 * @return			true if they have all of the items, otherwise false
	 */
	public boolean isCombinable(Player player) {
		Optional<Item> unavailableItem = items.stream().filter(i -> !player.getItems().playerHasItem(i.getId(),
				i.getAmount())).findFirst();
		return !unavailableItem.isPresent();
	}
	
	/**
	 * Determines if the items used match all of that required in the combination
	 * @param item1	the first item
	 * @param item2	the second item
	 * @return		true if all items match, otherwise false
	 */
	public boolean allItemsMatch(Item item1, Item item2) {
		return items.stream().allMatch(item -> item.getId() == item1.getId() && item.getAmount() >=
				item1.getAmount() || item.getId() == item2.getId() && item.getAmount() >= item2.getAmount());
	}
	
	/**
	 * Determines whether the player meets the skill requirements for the combination.
	 * @param player
	 * @return	true if the player meets the requirements
	 */
	public boolean hasRequirements(Player player) {
			return player.playerLevel[getSkillRequirement()] >= getLevelRequired();
	}
	
	/**
	 * List of the required items
	 * @return	the items
	 */
	public List<Item> getItems() {
		return items;
	}

	/**
	 * Gets the level required to initiate the combination.
	 * @return	the corresponding level requirement
	 */
	public int getLevelRequired() {
		return levelRequirements.get()[1];
	}
	
	/**
	 * Gets the skill required for the specified item combination.
	 * @return the skill required
	 */
	public int getSkillRequirement() {
		return levelRequirements.get()[0];
	}
	
	/**
	 * Determines if the item is revertable or not
	 * @return
	 */
	public boolean isRevertable() {
		return revertedItems.isPresent() ? true : false;
	}
	
	/**
	 * Determines if the item has requirements.
	 * @return	true if requirements are present in the enum element constructor
	 */
	public boolean isRequirable() { 
		return levelRequirements.isPresent() ? true : false;
	}
	
	/**
	 * Attempts to retrieve the revertable item, if it exists
	 * @return	the revertable item
	 */
	public Optional<List<Item>> getRevertItems() {
		return revertedItems.isPresent() ? revertedItems : Optional.empty();
	}
	
	/**
	 * The item that we receive for combining the items
	 * @return	the item
	 */
	public Item getOutcome() {
		return outcome;
	}

}
