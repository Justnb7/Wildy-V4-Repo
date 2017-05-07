package com.model.game.character.player.dialogue.impl.slayer;

import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.Expression;
import com.model.game.character.player.dialogue.Type;
import com.model.game.shop.Shop;

public class BlockTask extends Dialogue {

	@Override
	protected void start(Object... parameters) {
		//send(Type.CHOICE, "Select Option", "Enter Tomb", "Nevermind");
		//setPhase(0);
		send(Type.STATEMENT, "You are about to cancel your task. Are you sure?");
	}
	@Override
	public void next() {
	if (isPhase(0)) {
		send(Type.CHOICE, DEFAULT_OPTION_TITLE, "Yes, Pay @red@100 Points", "Nevermind");
		}
	}
	@Override
	public void select(int index) {
		if (getPhase() == 0) {
			switch(index) {
			case 1:
				
				break;
			case 2:
				player.getActionSender().sendRemoveInterfacePacket();
				break;
			
			}
		}
	}

}
