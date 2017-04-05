package com.model.game.character.combat.magic;

import com.model.game.character.combat.PrayerHandler.Prayers;
import com.model.game.character.player.Player;
import com.model.game.character.player.Skills;
import com.model.game.item.equipment.Equipment;
import com.model.utility.Utility;

public class MagicCalculations {

	/**
     * Returns the entity's magic hit
     *
     * @return the max hit
     */
	public static int magicMaxHitModifier(Player player) {
        int maxhit = player.MAGIC_SPELLS[player.oldSpellId][6];

        if (player.getCombat().godSpells()) {
            if (System.currentTimeMillis() - player.godSpellDelay < 300000) {
                maxhit += 10;
            }
        }
        // Random
        double damage = Utility.getRandom(maxhit);

        // Multipliers
        double damageMultiplier = 1;

        if (player.getSkills().getLevel(Skills.MAGIC) > player.getSkills().getLevelForExperience(Skills.MAGIC)) {
            damageMultiplier += .03 * (player.getSkills().getLevel(Skills.MAGIC) - player.getSkills().getLevelForExperience(Skills.MAGIC));
        }

        switch (player.playerEquipment[player.getEquipment().getWeaponId()]) {
        case 4675: // Ancient Staff
        case 4710: // Ahrim's Staff
        case 4862: // Ahrim's Staff
        case 4864: // Ahrim's Staff
        case 4865: // Ahrim's Staff
        case 6914: // Master Wand
        case 8841: // Void Knight Mace
            damageMultiplier += 0.10;
            break;
        }

        if (player.playerEquipment[player.getEquipment().getAmuletId()] > 0) {
            switch (player.playerEquipment[player.getEquipment().getAmuletId()]) {
            case 12002: // Occult
                damageMultiplier += 0.10;
                break;
            }
        }

        damage *= damageMultiplier;
        return (int) damage;
	}
	
	/**
	 * Returns the players magic attack bonus
	 * 
	 * @param player
	 * @return
	 */
	public static int calculateMagicAttack(Player player) {
		int attack = player.getSkills().getLevel(Skills.MAGIC);
		// TODO: void bonus
		int magicBonus = player.playerBonus[3];
		if (player.isActivePrayer(Prayers.MYSTIC_WILL)) {
			attack *= player.getSkills().getLevelForExperience(0) * 1.05;
		} else if (player.isActivePrayer(Prayers.MYSTIC_LORE)) {
			attack *= player.getSkills().getLevelForExperience(0) * 1.10;
		} else if (player.isActivePrayer(Prayers.MYSTIC_MIGHT)) {
			attack *= player.getSkills().getLevelForExperience(0) * 1.15;
		}
		if (Equipment.wearingFullVoid(player, 2)) {
			attack *= 1.1;
		}
		return (int) (attack + (magicBonus * 2));
	}
	
}
