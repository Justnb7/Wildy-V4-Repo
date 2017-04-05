package com.model.game.character.player.dialogue.impl.minigames;

import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.Type;
import com.model.game.location.Position;

public class BarrowsTunnel extends Dialogue {
	
	@Override
	protected void start(Object... parameters) {
		//send(Type.CHOICE, "Select Option", "Enter Tomb", "Nevermind");
		//setPhase(0);
		send(Type.STATEMENT, "You've found a hidden tunnel, do you want to enter?");
	}
	@Override
	public void next() {
	if (isPhase(0)) {
		send(Type.CHOICE, DEFAULT_OPTION_TITLE, "Yeah I'm fearless!", "No way, that looks scary!");
		}
	}
	@Override
	public void select(int index) {
		if (getPhase() == 0) {
			switch(index) {
			case 1:
				player.getActionSender().sendRemoveInterfacePacket();
				player.getBarrows().getMaze().teleportToMaze();
				break;
			case 2:
				player.getActionSender().sendRemoveInterfacePacket();
				break;
			}
		}
	}

}
