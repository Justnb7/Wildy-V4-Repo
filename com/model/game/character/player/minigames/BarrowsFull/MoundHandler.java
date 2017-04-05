package com.model.game.character.player.minigames.BarrowsFull;

import java.util.Arrays;
import java.util.Optional;

import com.model.Server;
import com.model.game.character.Animation;
import com.model.game.character.npc.NPC;
import com.model.game.character.npc.NPCHandler;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;



public class MoundHandler {

	private final Player client;

	private ScheduledTask digTask;

	public MoundHandler(Player client) {
		this.client = client;
	}

	public boolean dig(int itemID) {
		if (itemID == BarrowsConstants.SPADE_ID) {
			Optional<MoundData> findMound = getMound(client);

			if (digTask != null) {
				digTask.stop();
			}
			
			client.playAnimation(Animation.create(BarrowsConstants.SPADE_ANIMATION_ID));
			client.getActionSender().sendMessage(BarrowsConstants.BARROWS_DIG_MESSAGE);
			Server.getTaskScheduler().schedule(new ScheduledTask(2) {
				
				@Override
				public void execute() {
					findMound.ifPresent(m -> m.teleportBelow(client));
					stop();
					}

			@Override
			public void onStop() {
				client.playAnimation(Animation.create(BarrowsConstants.STOP_ANIMATION_ID));
			}
		});
			
	}
		return false;
}

	private Optional<MoundData> getMound(Player client) {
		Optional<MoundData> mound = Arrays.stream(MoundData.MOUNDS).filter(m -> m.checkBounds(client)).findFirst();
		return mound;
	}

}