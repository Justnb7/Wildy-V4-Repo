package com.model.game.character.player.content.clicking.magic;

import com.model.game.character.Animation;
import com.model.game.character.Graphic;
import com.model.game.character.combat.magic.MagicData;
import com.model.game.character.player.Player;
import com.model.game.item.Item;
import com.model.game.item.ItemAssistant;


/**
 * Handles the action of using spells with items.
 * 
 * @author SeVen
 */
public class MagicOnItems {

    /**
     * Handles the action of using spells with {@link Item}s for a player.
     * 
     * @param player
     *            The player performing this action.
     * 
     * @param itemId
     *            The id of the item.
     * 
     * @param slot
     *            The slot of the item.
     * 
     * @param childId
     *            Not really sure
     * 
     * @param spellId
     *            The id of the spell.
     */
	public static void handleAction(Player player, int itemId, int slot, int childId, int spellId) {

		if (player.isDead()) {
			return;
		}

		if (player.isTeleporting()) {
			return;
		}

		if (player.inDebugMode()) {
			System.out.println(String.format("[MagicOnItem] - ItemId: %d Slot: %d ChildId: %d SpellId: %d", itemId, slot, childId, spellId));
		}

		final Item item = player.getItems().getItemFromSlot(slot);

		if (item == null || item.getId() != itemId) {
			return;
		}

		switch (spellId) {
		
		case 1173:
			if (System.currentTimeMillis() - player.alchDelay > 700) {
	
				int[][] data = { { 436, 1, 438, 1, 2349, 6, 1 }, // TIN
						{ 438, 1, 436, 1, 2349, 6, 15 }, // COPPER
						{ 440, 1, -1, -1, 2351, 13, 20 }, // IRON ORE
						{ 442, 1, -1, -1, 2355, 18, 30 }, // SILVER ORE
						{ 444, 1, -1, -1, 2357, 23, 40 }, // GOLD BAR
						{ 447, 1, 453, 1, 2359, 30, 50 }, // MITHRIL ORE
						{ 449, 1, 453, 1, 2361, 38, 70 }, // ADDY ORE
						{ 451, 1, 453, 1, 2363, 50, 85 }, // RUNE ORE
				};
				for (int i = 0; i < data.length; i++) {
					if (itemId == data[i][0]) {
						if (!player.getItems().playerHasItem(data[i][2], data[i][3])) {
							player.getActionSender().sendMessage("You haven't got enough " + ItemAssistant.getItemName(data[i][2]).toLowerCase() + " to cast this spell!");
							return;
						}
						//if (player.playerLevel[Player.playerSmithing] < data[i][6]) {
						//	player.getActionSender().sendMessage("You need a smithing level of " + data[i][6] + " to heat this ore.");
						//	return;
						//}
						player.getItems().deleteItem(itemId, player.getItems().getItemSlot(itemId), 1);
						for (int lol = 0; lol < data[i][3]; lol++) {
							player.getItems().deleteItem(data[i][2], player.getItems().getItemSlot(data[i][2]), 1);
						}
						player.getItems().addItem(data[i][4], 1);
						player.alchDelay = System.currentTimeMillis();
						player.getSkills().addExperience(13, data[i][5]);
						player.getSkills().addExperience(6, 6);
						player.playAnimation(Animation.create(722));
						player.playGraphics(Graphic.create(148));
						
						return;
					}
				}
				player.getActionSender().sendMessage("You can only cast superheat item on ores!");
				return;
			}
			break;

		/* Low Alchemy */
		case 1162:
			break;

		/* High Alchemy */
		case 1178:
			break;

		}
	}

}