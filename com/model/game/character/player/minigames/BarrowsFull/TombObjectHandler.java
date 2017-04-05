 package com.model.game.character.player.minigames.BarrowsFull;

import com.model.Server;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;

public class TombObjectHandler {

	private final Player client;

	private ScheduledTask openTask;

	public TombObjectHandler(Player client) {
		this.client = client;
	}

	public boolean openTomb(int objectID, int objectX, int objectY) {
		TombData tomb = TombData.lookup(objectID);

		if (tomb == null) {
			return false;
		}

		if (openTask != null) {
			openTask.stop();
		}

		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (client.goodDistance(objectX, objectY, client.absX, client.absY, 3)) {
					client.getActionSender().sendMessage("You search the tomb...");
					this.stop();
				}
			}
			
			@Override
			public void onStop() {
				openTomb(tomb);
			}
		});
		return true;
	}
	
	

	private void openTomb(TombData tomb) {
		Brother brother = tomb.getBrother();
		BarrowsNpcController npcController = client.getBarrows().getNpcController();
		
		if (brother == npcController.getTargetBrother()) {
			client.dialogue().start("BARROWS_TUNNEL", client);
		} else {
			if (npcController.checkIfSpawned(tomb.getBrother()) || npcController.checkIfKilled(tomb.getBrother())) {
				client.getActionSender().sendMessage(BarrowsConstants.NOTHING_FOUND);
			} else {
				npcController.spawnBrother(brother);
			}
		}
	}

}