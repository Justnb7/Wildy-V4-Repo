package com.model.game.character.player.skill.agility.rooftop;

import com.model.Server;
import com.model.game.character.Animation;
import com.model.game.character.player.Player;
import com.model.game.character.player.Skills;
import com.model.game.character.player.packets.out.SendConfigPacket;
import com.model.game.character.player.skill.SkillHandler;
import com.model.task.ScheduledTask;
import com.model.task.Stackable;
import com.model.task.Walkable;

/**
 * 
 * @author Patrick van Elderen
 *
 */
public class Draynor_Village {
	
	public static void usingObstacle(Player player, int object) {
		if (!obstacle(object)) {
			return;
		}
		
		switch (object) {
		case 10073:
			if (!player.doingAgility)
				roughWall(player);
			break;
		case 10074:
			if (!player.doingAgility)
				tightRopeWest(player);
			break;
		case 10075:
			if (!player.doingAgility)
				tightRopeSouth(player);
			break;
		case 10077:
			if (!player.doingAgility)
				narrowWall(player);
			break;
		}
	}
	
	/**
	 * Checks if the object is an valid obstacle.
	 * @return {@code true} if the object is, {@code false} otherwise.
	 */
	public static boolean obstacle(int object) {
		switch(object) {
		case 10073:
		case 10074:
		case 10075:
		case 10077:
			return true;
		}
		return false;
	}
	
	public static void roughWall(Player player) {
		if (player.absY != 3279)
			return;
		player.turnPlayerTo(player.objectX, player.objectY);
		player.playAnimation(Animation.create(828));
		player.doingAgility = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 2, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				if (!player.isActive()) {
					this.stop();
					return;
				}
				this.stop();
			}

			@Override
			public void onStop() {
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.getMovementHandler().setForcedMovement(false);
				player.getPA().movePlayer(3103, 3279, 3);
				player.doingAgility = false;
			}
		}.attach(player));
	}
	
	public static void tightRopeWest(Player player) {
		final boolean running = player.isRunning();
		player.doingAgility = true;
		specialMove(player, 762, -8, 0);
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 10, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				if (!player.isActive()) {
					this.stop();
					return;
				}
				if (player.absX == 3090) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				restore(player);
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.setRunning(running);
				player.getMovementHandler().setForcedMovement(false);
				player.doingAgility = false;
				player.getPA().movePlayer(3092, 3276, 3);
				player.doingAgility = false;
			}
		}.attach(player));
	}
	
	public static void tightRopeSouth(Player player) {
		final boolean running = player.isRunning();
		player.doingAgility = true;
		specialMove(player, 762, 0, -10);
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 10, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				if (!player.isActive()) {
					this.stop();
					return;
				}
				if (player.absY == 3268) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				restore(player);
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.setRunning(running);
				player.getMovementHandler().setForcedMovement(false);
				player.doingAgility = false;
			}
		}.attach(player));
	}
	
	public static void narrowWall(Player player) {
		final boolean running = player.isRunning();
		player.doingAgility = true;
		specialMove(player, 753, 0, -4);
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 4, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				if (!player.isActive()) {
					this.stop();
					return;
				}
				if (player.absY == 3261) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				restore(player);
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.setRunning(running);
				player.getMovementHandler().setForcedMovement(false);
				player.doingAgility = false;
			}
		}.attach(player));
	}
	
	/**
	 * Moves the player to a coordinate with a asigned animation.
	 * @param player
	 * @param walkAnimation
	 * @param x
	 * @param y
	 */
	private static void specialMove(final Player player, final int walkAnimation, final int x, final int y) {
		player.setRunning(false);
		player.getMovementHandler().setForcedMovement(true);
		player.write(new SendConfigPacket(173, 0));
		player.walkAnimation = walkAnimation;
		player.getPA().requestUpdates();
		player.getPA().walkTo(x, y);
	}
	
	/**
	 *  Restores the player details after doing the obstacle.
	 * @param player
	 */
	private static void restore(Player player) {
		player.setRunning(true);
		player.write(new SendConfigPacket(173, 1));
		player.walkAnimation = 0x333;
		player.getPA().requestUpdates();
		player.doingAgility = false;
	}

}
