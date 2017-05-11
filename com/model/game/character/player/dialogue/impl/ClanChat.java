
/**
 *
 * @author Antony
 */
package com.model.game.character.player.dialogue.impl;

import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.Expression;
import com.model.game.character.player.dialogue.Type;

public class ClanChat extends Dialogue {

	@Override
	protected void start(Object... parameters) {
		send(Type.NPC, 1, Expression.SLEEPY, "Hello there Wildy Reborn player! I am contacting you regarding...",
				"Some current changes with our Clanchat system ", "This is purely a FYI statement.");

		send(Type.NPC, 1, Expression.SLEEPY, "Pick a clanchat", "Join the help clanchat ",
				"Join the community clanchat");
	}

	@Override
	public void next() {
		if (getPhase() == 0) {
			send(Type.NPC, 1, Expression.HAPPY, "To prevent Clanchat's becoming overloaded with too",
					"many players we are now segmenting players into", "Different clanchats, these can be found by",
					" 'Matthew' 'Patrick'");
			setPhase(getPhase() + 1);
		} else if (getPhase() == 1)

		{
			send(Type.NPC, 1, Expression.HAPPY, "We expect most players to head into the community clanchat",
					"New players will be directed into the Help clanchat, while",
					"Older players are directed into the Community clanchat");
			setPhase(getPhase() + 1);
		} else if (getPhase() == 2)

		{
			send(Type.NPC, 1, Expression.HAPPY, "If you do decide to join the Help clanchat ",
					"and begin spamming, flaming, baiting anything ", "which isn't help-related you will be removed.",
					"Please pick which CC to join now.");
			setPhase(getPhase() + 1);
		} else if (getPhase() == 3)

		{
			send(Type.CHOICE, "Pick a clanchat", "Join the help clanchat ", "Join the community clanchat");
		}

	}

	@Override
	public void select(int index) {
		if (index == 1) {
			player.lastClanChat = "mopar";
			stop();
		} else {
			player.lastClanChat = "help";
			stop();
		}
	}

}
