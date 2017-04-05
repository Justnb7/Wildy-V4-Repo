package com.model.game.character.player.dialogue.impl.pets;

import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.Expression;
import com.model.game.character.player.dialogue.Type;

/**
 * The Dagannoth rex pet chat dialogue
 * 
 * @author Patrick van Elderen
 *
 */
public class Dagannoth_Rex extends Dialogue {
	
	private final int PET = 6630;

	@Override
	protected void start(Object... parameters) {
		send(Type.PLAYER, Expression.DEFAULT, "Do you have any berserker rings?");
		setPhase(0);
	}
	
	@Override
	protected void next() {
		switch (getPhase()) {
		case 0:
			send(Type.NPC, Expression.DEFAULT, PET, "Nope.");
			setPhase(1);
			break;
		case 1:
			send(Type.PLAYER, Expression.DEFAULT, "You sure?");
			setPhase(2);
			break;
		case 2:
			send(Type.NPC, Expression.DEFAULT, PET, "Yes.");
			setPhase(3);
			break;
		case 3:
			send(Type.PLAYER, Expression.DEFAULT, "So, if I tipped you upside down and shook you,", "you'd not drop any berserker rings?");
			setPhase(4);
			break;
		case 4:
			send(Type.NPC, Expression.DEFAULT, PET, "Nope.");
			setPhase(5);
			break;
		case 5:
			send(Type.PLAYER, Expression.DEFAULT, "What if I endlessly killed your father for weeks on end,", "would I get one then.");
			setPhase(6);
			break;
		case 6:
			send(Type.NPC, Expression.DEFAULT, PET, "Been done by someone, nope.");
			setPhase(7);
			break;
		case 7:
			stop();
			break;
		}
	}
	
	@Override
	protected void select(int index) {
		
	}

}
