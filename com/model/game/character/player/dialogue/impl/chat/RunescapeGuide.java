package com.model.game.character.player.dialogue.impl.chat;

import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.Expression;
import com.model.game.character.player.dialogue.Type;

public class RunescapeGuide extends Dialogue {
	
	private static final int NPC_ID = 3308;

	@Override
	protected void start(Object... parameters) {
		send(Type.NPC, NPC_ID, Expression.DEFAULT, "Welcome to Wildy Reborn, "+player.getName(), "First, you need to choose your game mode.");
		setPhase(0);
	}
	
	@Override
	protected void next() {
		if (getPhase() == 0) {
			player.getGameModeSelection().open(player);
		}
	}
}
