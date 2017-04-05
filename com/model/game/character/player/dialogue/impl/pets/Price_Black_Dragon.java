package com.model.game.character.player.dialogue.impl.pets;

import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.Expression;
import com.model.game.character.player.dialogue.Type;

/**
 * The Prince black dragon pet chat dialogue
 * 
 * @author Patrick van Elderen
 *
 */
public class Price_Black_Dragon extends Dialogue {
	
	private final int PET = 6636;

	@Override
	protected void start(Object... parameters) {
		send(Type.PLAYER, Expression.DEFAULT, "Shouldn't a prince only have two heads?");
		setPhase(0);
	}
	
	@Override
	protected void next() {
		switch (getPhase()) {
		case 0:
			send(Type.NPC, Expression.DEFAULT, PET, "Why is that?");
			setPhase(1);
			break;
		case 1:
			send(Type.PLAYER, Expression.DEFAULT, "Well,", "a standard Black dragon has one,", "the King has three so inbetween must have two?");
			setPhase(2);
			break;
		case 2:
			send(Type.NPC, Expression.DEFAULT, PET, "You're overthinking this.");
			setPhase(3);
			break;
		case 3:
			stop();
			break;
		}
	}
	
	@Override
	protected void select(int index) {
		
	}

}