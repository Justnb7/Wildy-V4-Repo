package com.model.game.character.player.minigames.BarrowsFull;

import com.model.Server;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;
import com.model.task.TaskScheduler;

public class TombStairsHandler {

	private final Player client;
	
	private ScheduledTask stairTask;

	public TombStairsHandler(Player client) {
		this.client = client;
	}

	public boolean useStairs(int objectID) {
		TombStairData stair = TombStairData.lookup(objectID);
		if (stair != null) {
			
			if(stairTask != null) {
				stairTask.stop();
			}
			
			Server.getTaskScheduler().schedule(new ScheduledTask(2) {
				@Override
				public void execute() {
					if (client.goodDistance(stair.getPosition().getX(), stair.getPosition().getY(), client.absX,
							client.absY, 1)) {
						this.stop();
					}
				}
				
				@Override
				public void onStop() {
					client.getBarrows().getNpcController().despawnBrothers();
					stair.teleportAbove(client);
				}
			});
			
			return true;
		}
		return false;
	}
}