package com.model.game.character.player.skill.agility;

import com.model.Server;
import com.model.game.character.Animation;
import com.model.game.character.player.Player;
import com.model.game.character.player.Skills;
import com.model.game.character.player.packets.out.SendConfigPacket;
import com.model.game.character.player.skill.SkillHandler;
import com.model.task.ScheduledTask;
import com.model.task.Stackable;
import com.model.task.Walkable;

public class GnomeStrongholdAgilityCourse extends SkillHandler {  
	
	public static void handleObject(int objectId, Player player) {
		if (!isObstacle(objectId))
			return;
		switch (objectId) {

		case 23145:
			handleLog(player);
			break;

		case 23134:
			handleNet1(player);
			break;

		case 23559:
			handleBranch1(player);
			break;

		case 23557:
			handleRope(player);
			break;

		case 23560:
		case 2315:
			handleBranch2(player);
			break;

		case 23135:
			handleNet2(player);
			break;

		case 23138:
		case 23139:
			handlePipe(player);
			break;

		}
	}

	/**
	 * Restores the player details after doing the obstacle
	 */

	public static void resetAgilityWalk(final Player player) {
		player.setRunning(true);
		player.write(new SendConfigPacket(173, 1));
		player.walkAnimation = 0x333;
		player.getPA().requestUpdates();
		player.doingAgility = false;
	}

	/**
	 * Moves the player to a coordinate with a asigned animation
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
	 * Checks if its a obstacle
	 */

	public static boolean isObstacle(int i) {
		switch (i) {
		case 23145: // log
		case 23134: // net1
		case 23559: // branch1
		case 23557: // rope
		case 23560: // branch2
		case 2315: // branch2
		case 23135: // net2
		case 23138: // pipe left
		case 23139: // pipe right
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player has passed all obstacles
	 */

	public static boolean isFinished(Player player) {
		if (player.finishedLog && player.finishedNet1 && player.finishedBranch1 && player.finishedRope && player.finishedBranch2
				&& player.finishedNet2 && player.finishedPipe) {
			return true;
		}
		return false;
	}

	/**
	 * Obstacle methods
	 */

	public static void handleLog(final Player player) {
		if (player.doingAgility) {
			return;
		}
		final boolean running = player.isRunning();
		player.doingAgility = true;
		specialMove(player, 762, 0, -7);
		player.isSkilling = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 7, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				if (!player.isActive()) {
					this.stop();
					return;
				}
				if (player.absY == 3429) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				player.isSkilling = false;
				resetAgilityWalk(player);
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.finishedLog = true;
				player.getMovementHandler().setForcedMovement(false);
				player.doingAgility = false;
				player.setRunning(running);
			}
		}.attach(player));
	}

	public static void handleNet1(final Player player) {
		if (player.doingAgility) {
			return;
		}
		player.doingAgility = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 1, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				this.stop();
			}

			public void onStop() {
				player.playAnimation(Animation.create(828));
				player.getPA().movePlayer(player.absX, 3424, 1);
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.doingAgility = false;
				player.finishedNet1 = true;
			}
		}.attach(player));
	}

	public static void handleBranch1(final Player player) {
		player.playAnimation(Animation.create(828));
		player.getPA().movePlayer(2473, 3420, 2);
		player.getSkills().addExperience(16, Skills.AGILITY);
		player.finishedBranch1 = true;
	}

	public static void handleRope(final Player player) {
		if (player.doingAgility) {
			return;
		}
		final boolean running = player.isRunning();
		player.doingAgility = true;
		specialMove(player, 762, 6, 0);
		player.isSkilling = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 6, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				if (!player.isActive()) {
					this.stop();
					return;
				}
				if (player.absX == 2483) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				player.isSkilling = false;
				resetAgilityWalk(player);
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.finishedRope = true;
				player.doingAgility = false;
				player.setRunning(running);
				player.getMovementHandler().setForcedMovement(false);
			}
		}.attach(player));
	}

	public static void handleBranch2(final Player player) {
		player.playAnimation(Animation.create(828));
		player.getPA().movePlayer(player.absX, player.absY, 0);
		player.getSkills().addExperience(16, Skills.AGILITY);
		player.finishedBranch2 = true;
	}

	public static void handleNet2(final Player player) {
		if (player.doingAgility) {
			return;
		}
		if (player.isSkilling) {
			return;
		}
		if (player.absY == 3425) {
			player.doingAgility = true;
			player.playAnimation(Animation.create(828));
			player.isSkilling = true;
			Server.getTaskScheduler().schedule(new ScheduledTask(player, 2, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
				public void execute() {
					this.stop();
				}

				@Override
				public void onStop() {
					player.isSkilling = false;
					player.getPA().movePlayer(2484, 3427, 0);
					player.getSkills().addExperience(16, Skills.AGILITY);
					player.finishedNet2 = true;
					player.doingAgility = false;
				}
			});
		}
	}

	public static void handlePipe(final Player player) {
		if (player.doingAgility) {
			return;
		}
		player.doingAgility = true;
		final boolean running = player.isRunning();
		specialMove(player, 844, 0, 7);
		player.isSkilling = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(player, 4, Walkable.WALKABLE, Stackable.NON_STACKABLE) {
			public void execute() {
				if (!player.isActive()) {
					this.stop();
					return;
				}
				if (player.absY == 3438) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				player.isSkilling = false;
				player.playAnimation(Animation.create(844));
				player.finishedPipe = true;
				resetAgilityWalk(player);
				if (isFinished(player)) {
					player.getSkills().addExperience(25000, Skills.AGILITY);
					player.getActionSender().sendMessage("You have completed the full gnome agility course.");
				} else {
					player.getSkills().addExperience(16, Skills.AGILITY);
				}
				player.finishedLog = false;
				player.finishedNet1 = false;
				player.getMovementHandler().setForcedMovement(false);
				player.finishedBranch1 = false;
				player.finishedRope = false;
				player.finishedBranch2 = false;
				player.finishedNet2 = false;
				player.finishedPipe = false;
				player.setRunning(running);
			}
		});
	}

}
