package com.model.game.character.player.skill;

import com.model.game.character.player.Player;

public class SkillHandler {

	public static boolean noInventorySpace(Player player, String skill) {
		if (player.getItems().freeSlots() == 0) {
			player.getActionSender().sendMessage("You don't have enough inventory space.");
			return false;
		}
		return true;
	}

	public static boolean hasRequiredLevel(final Player player, int id, int lvlReq, String skill, String event) {
		if (player.getLevel[id] < lvlReq) {
			player.getActionSender().sendMessage("You at least need a " + skill + " level of " + lvlReq + " to " + event + ".");
			return false;
		}
		return true;
	}

	public static void deleteTime(Player c) {
		c.doAmount--;
	}

	/**
	 * Returns if a skill is currently active
	 * 
	 * @param player
	 *            The {@link Player} To check if the skill is active for
	 * @param skill
	 *            The id of the skill to check for
	 * @return If the skill is currently active
	 */
	public static boolean isSkillActive(Player player, int skill) {
		return player.getAttribute("skill_" + skill + "_active", false);
	}

	/**
	 * Sets a skill active/inactive
	 * 
	 * @param player
	 *            The {@link Player} to set the skill active/inactive for
	 * @param skill
	 *            The id of the skill
	 * @param active
	 *            The active status of the skill
	 */
	public static void setSkillActive(Player player, int skill, boolean active) {
		player.setAttribute("skill_" + skill + "_active", active);
	}
}