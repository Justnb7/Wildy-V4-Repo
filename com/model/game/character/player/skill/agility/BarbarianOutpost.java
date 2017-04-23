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

public class BarbarianOutpost extends SkillHandler {
	
	public static void handleObject(int objectId, Player player) {
		if (!isObstacle(objectId))
			return;
		switch (objectId) {

		case 2282:
			handleRope(player);
			break;

		case 2294:
			handleLog(player);
			break;

		case 20211:
			handleNet(player);
			break;

		case 2302:
			handleLedge(player);
			break;

		case 3205:
			handleStairs(player);
			break;

		case 1948:
			handleWall(player);
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
	}

	/**
	 * Moves the player to a coordinate with a assigned animation
	 */

	private static void specialMove(final Player player, final int walkAnimation, final int x, final int y) {
		player.setRunning(false);
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
		case 2282: // rope
		case 2294: // log
		case 20211: // net
		case 2302: // ledge
		case 3205: // stairs
		case 1948: // wall
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player has passed all obstacles
	 */

	public static boolean isFinished(Player player) {
		if (player.finishedBarbRope && player.finishedBarbLog && player.finishedBarbNet && player.finishedBarbStairs && player.finishedBarbWall1 && player.finishedBarbWall2 && player.finishedBarbWall3) {
			return true;
		}
		return false;
	}

	/**
	 * Obstacle methods
	 */

	public static void handleRope(final Player player) {
		if (player.absY >= 3554) {
			player.doingAgility = true;
			resetAgilityWalk(player);
			player.getSkills().addExperience(16, Skills.AGILITY);
			player.finishedBarbRope = true;
			player.doingAgility = false;
		}
	}

	public static void handleLog(final Player player) {
		player.doingAgility = true;
		specialMove(player, 762, -10, 0);
		player.isSkilling = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			public void execute() {
				if(!player.isSkilling) {
					this.stop();
					return;
				}
				if (player.absX == 2541) {
					this.stop();
				}
			}

			public void onStop() {
				player.isSkilling = false;
				resetAgilityWalk(player);
				player.getSkills().addExperience(16, Skills.AGILITY);
				player.finishedBarbLog = true;
				player.doingAgility = false;
			}
		}.attach(player));
	}

	public static void handleNet(final Player player) {
		player.playAnimation(Animation.create(828));
		player.getPA().movePlayer(2538, player.absY, 1);
		player.getSkills().addExperience(16, Skills.AGILITY);
		player.finishedBarbNet = true;
	}

	public static void handleLedge(final Player player) {
		if (player.absX >= 2536) {
			player.doingAgility = true;
			specialMove(player, 756, -4, 0);
			Server.getTaskScheduler().schedule(new ScheduledTask(1) {
				public void execute() {
					if (player.absX == 2532 && player.absY == 3547) {
						this.stop();
					}
				}

				@Override
				public void onStop() {
					resetAgilityWalk(player);
					player.getSkills().addExperience(16, Skills.AGILITY);
					player.finishedBarbLedge = true;
					player.doingAgility = false;
				}
			}.attach(player));
		}
	}

	public static void handleStairs(final Player player) {
		player.playAnimation(Animation.create(828));
		player.getPA().movePlayer(2532, 3546, 0);
		player.getSkills().addExperience(16, Skills.AGILITY);
		player.finishedBarbStairs = true;
	}

	public static void handleWall(final Player player) {
		if (player.absX != 2535 && player.absY != 3553) {
			player.getPA().walkTo(2535 - player.absX, 3553 - player.absY);
			return;
		}
		if (player.objectX == 2536) {
			player.doingAgility = true;
			specialMove(player, 839, +2, 0);
			player.isSkilling =true;
			Server.getTaskScheduler().schedule(new ScheduledTask(1) {
				public void execute() {
					if (!player.isSkilling) {
						this.stop();
						return;
					}
					if (player.absX == 2537 && player.absY == 3553) {
						this.stop();
					}
				}

				@Override
				public void onStop() {
					player.isSkilling = false;
					resetAgilityWalk(player);
					player.getSkills().addExperience(16, Skills.AGILITY);
					player.finishedBarbWall1 = true;
					player.doingAgility = false;
				}
			}.attach(player));

		} else if (player.objectX == 2539) {
			if (player.absX == 2539) {
				player.getPA().walkTo(2538 - player.absX, 3553 - player.absY);
				return;
			}
			if (player.absX != 2538 && player.absY != 3553) {
				player.getPA().walkTo(2538 - player.absX, 3553 - player.absY);
				return;
			}
			if (player.absX == 2538 && player.absY == 3553) {
				player.doingAgility = true;
				specialMove(player, 839, +2, 0);
				player.isSkilling = true;
				Server.getTaskScheduler().schedule(new ScheduledTask(1) {
					public void execute() {
						if (!player.isSkilling) {
							this.stop();
							return;
						}
						if (player.absX == 2540 && player.absY == 3553) {
							this.stop();
						}
					}

					@Override
					public void onStop() {
						player.isSkilling = false;
						resetAgilityWalk(player);
						player.getSkills().addExperience(16, Skills.AGILITY);
						player.finishedBarbWall2 = true;
						player.doingAgility = false;
					}
				}.attach(player));
			}
		} else if (player.objectX == 2542) {
			if (player.absX == 2542) {
				player.getPA().walkTo(2541 - player.absX, 3553 - player.absY);
				return;
			}
			if (player.absX != 2541 && player.absY != 3553) {
				player.getPA().walkTo(2541 - player.absX, 3553 - player.absY);
				return;
			}
			if (player.absX == 2541 && player.absY == 3553) {
				player.doingAgility = true;
				specialMove(player, 839, +2, 0);
				player.isSkilling = true;
				Server.getTaskScheduler().schedule(new ScheduledTask(1) {
					public void execute() {
						if (!player.isSkilling) {
							this.stop();
							return;
						}
						if (player.absX == 2543 && player.absY == 3553) {
							this.stop();
						}
					}

					@Override
					public void onStop() {
						player.isSkilling = false;
						player.finishedBarbWall3 = true;
						resetAgilityWalk(player);
						if (isFinished(player)) {
							player.getSkills().addExperience(50000, Skills.AGILITY);
							player.getActionSender().sendMessage("You have completed the full barbarian agility course.");
							player.getActionSender().sendMessage("You have been rewarded with 50k Agility XP!");
						} else {
							player.getSkills().addExperience(16, Skills.AGILITY);
						}
						player.finishedBarbRope = false;
						player.finishedBarbLog = false;
						player.finishedBarbNet = false;
						player.finishedBarbLedge = false;
						player.finishedBarbStairs = false;
						player.finishedBarbWall1 = false;
						player.finishedBarbWall2 = false;
						player.finishedBarbWall3 = false;
					}
				}.attach(player));
			}
		}
	}

}
