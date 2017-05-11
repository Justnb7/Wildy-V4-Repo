package com.model.game.character.player.dialogue.ItemCombinationDialogue;

import java.util.Optional;

import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.SimpleDialogues;
import com.model.game.character.player.dialogue.Type;
import com.model.game.item.itemCombination.ItemCombination;

public class Combine extends Dialogue {

	@Override
	protected void start(Object... parameters) {
		send(Type.CHOICE, "Select Option", "Combine Items?", "Yes", "Cancel");
		setPhase(0);
	}
	
	@Override
	public void select(int index) {
		if (getPhase() == 0) {
			switch(index) {
			case 1:
				if (player.getCurrentCombination().isPresent()) {
					ItemCombination combination = player.getCurrentCombination().get();
					if (combination.isCombinable(player)) {
						combination.combine(player);
					} else {
						SimpleDialogues.sendStatement(player, "You don't have all the items you need for this combination.");
						player.setCurrentCombination(Optional.empty());
					}
					return;
				}
				break;
			case 2:
				finish();
				break;
			
	}
}

	}}