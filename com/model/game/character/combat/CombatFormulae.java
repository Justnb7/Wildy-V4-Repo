package com.model.game.character.combat;

import com.model.game.character.Entity;
import com.model.game.character.combat.PrayerHandler.Prayers;
import com.model.game.character.combat.range.RangeData;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.game.character.player.Skills;
import com.model.game.item.equipment.Equipment;
import com.model.utility.Utility;

/**
 * Handles the combat's accuracy and max hit formulas.
 * 
 * @author Gabriel | Wolfs Darker
 *
 */
public class CombatFormulae {
	
	/**
	 * Default private constructor.
	 */
	private CombatFormulae() {
		
	}

    private static int npcDef(NPC npc, int att_type) {
        if (att_type == 0)
            return npc.melee_defence;
        else if (att_type == 1)
            return npc.range_defence;
        else if (att_type == 2)
            return npc.magic_defence;
        throw new IllegalStateException();
    }

    /**
     * Calculates the accuracy between the attacker and the target.
     * 
     * @param attacker
     * @param target
     * @param att_type
     * @param additionalSpecMulti TODO
     * @return
     */
    public static boolean getAccuracy(Entity attacker, Entity target, int att_type, double additionalSpecMulti) {

        int att_curr_attack = attacker.isNPC() ? attacker.toNPC().getDefinition().getAttackBonus() : attacker.toPlayer().getSkills().getLevel(Skills.ATTACK);
        int att_curr_range = attacker.isNPC() ? attacker.toNPC().getDefinition().getAttackBonus() : attacker.toPlayer().getSkills().getLevel(Skills.RANGE);
        int att_curr_magic = attacker.isNPC() ? attacker.toNPC().getDefinition().getAttackBonus() : attacker.toPlayer().getSkills().getLevel(Skills.MAGIC);

        int tar_curr_magic = target.isNPC() ? target.toNPC().getDefinition().getMagicDefence() : target.toPlayer().getSkills().getLevel(Skills.MAGIC);
        int tar_curr_defence = target.isNPC() ? npcDef(target.toNPC(), att_type) : target.toPlayer().toPlayer().getSkills().getLevel(Skills.DEFENCE);

        int att_base_attack = attacker.isNPC() ? attacker.toNPC().getDefinition().getAttackBonus() : attacker.toPlayer().getSkills().getLevelForExperience(Skills.ATTACK);
        int att_base_range = attacker.isNPC() ? attacker.toNPC().getDefinition().getAttackBonus() : attacker.toPlayer().getSkills().getLevelForExperience(Skills.RANGE);
        int att_base_magic = attacker.isNPC() ? attacker.toNPC().getDefinition().getAttackBonus() : attacker.toPlayer().getSkills().getLevelForExperience(Skills.MAGIC);

        double att_prayer_bonus = 1.0;
        double att_style_bonus = 0;
        double att_spec_bonus = 1;

        double att_spell_bonus = 0;
        double att_weapon_bonus = 0;
        double att_additional_bonus = 1;
        double att_effective_attack = 0;
        double att_equipment_bonus = 1;
        double augmented_attack = 0;
        double att_hit_chance = 0;
        double att_void_bonus = 1;

        double tar_prayer_bonus = 1.0;
        double tar_style_bonus = 0;

        double tar_equipment_bonus = 1;
        double augmented_defence = 0;
        double tar_block_chance = 0;

        double tar_effective_defence = 0;

        if (attacker.isPlayer()) {
            Player p = attacker.toPlayer();

            switch (att_type) {
            
            case 0:
            	if (p.isActivePrayer(Prayers.CLARITY_OF_THOUGHT)) {
                    att_prayer_bonus += 0.05;
                }

                if (p.isActivePrayer(Prayers.IMPROVED_REFLEXES)) {
                    att_prayer_bonus += 0.1;
                }

                if (p.isActivePrayer(Prayers.INCREDIBLE_REFLEXES)) {
                    att_prayer_bonus += 0.15;
                }

                if (p.isActivePrayer(Prayers.CHIVALRY)) {
                    att_prayer_bonus += 0.15;
                }

                if (p.isActivePrayer(Prayers.PIETY)) {
                    att_prayer_bonus += 0.2;
                }

                att_equipment_bonus = p.playerBonus[p.getAttackStyle() <= 1 ? p.getAttackStyle() : 1];
                att_void_bonus = Equipment.wearingFullVoid(p, att_type) ? 1.1 : 1;
                break;
            case 1:
            	 if (p.isActivePrayer(Prayers.SHARP_EYE)) {
                     att_prayer_bonus += 0.05;
                 }

                 if (p.isActivePrayer(Prayers.HAWK_EYE)) {
                     att_prayer_bonus += 0.1;
                 }

                 if (p.isActivePrayer(Prayers.EAGLE_EYE)) {
                     att_prayer_bonus += 0.15;
                 }

                att_equipment_bonus = p.playerBonus[4];
                att_void_bonus = Equipment.wearingFullVoid(p, att_type) ? 1.1 : 1;
                break;
            case 2:
            	 if (p.isActivePrayer(Prayers.MYSTIC_WILL)) {
                     att_prayer_bonus += 0.05;
                 }

                 if (p.isActivePrayer(Prayers.MYSTIC_LORE)) {
                     att_prayer_bonus += 0.1;
                 }

                 if (p.isActivePrayer(Prayers.MYSTIC_MIGHT)) {
                     att_prayer_bonus += 0.15;
                 }

                att_spell_bonus += ((att_base_magic - p.MAGIC_SPELLS[p.oldSpellId][1]) * 0.3);

                att_equipment_bonus = p.playerBonus[3];
                att_void_bonus = Equipment.wearingFullVoid(p, att_type) ? 1.3 : 1;
                break;
                
            default:
                break;
            }

            if (p.playerEquipment[3] > 0 && att_type != 2) {
                att_weapon_bonus += (((att_type == 0 ? att_base_attack : att_base_range) - (att_type = 0)) * 0.3);
            }

            if (att_type != 2) {
                att_style_bonus = p.getAttackStyle() == 2 ? 1 : p.getAttackStyle() == 1 ? 3 : 0;
            }

        }

        switch (att_type) {
        case 0:
            att_effective_attack = Math
                .floor(((att_curr_attack * att_prayer_bonus) * att_additional_bonus) + att_style_bonus + att_weapon_bonus);
            break;
        case 1:
            att_effective_attack = Math
                .floor(((att_curr_range * att_prayer_bonus) * att_additional_bonus) + att_style_bonus + att_weapon_bonus);
            break;
        case 2:
            att_effective_attack = Math.floor(((att_curr_magic * att_prayer_bonus) * att_additional_bonus) + att_spell_bonus);
            break;
        default:
            break;
        }

        if (target.isPlayer()) {

            Player t = target.toPlayer();

            if (t.isActivePrayer(Prayers.THICK_SKIN)) {
                tar_prayer_bonus += 0.05;
            }

            if (t.isActivePrayer(Prayers.ROCK_SKIN)) {
                tar_prayer_bonus += 0.1;
            }

            if (t.isActivePrayer(Prayers.STEEL_SKIN)) {
                tar_prayer_bonus += 0.15;
            }

            if (t.isActivePrayer(Prayers.CHIVALRY)) {
                tar_prayer_bonus += 0.2;
            }

            if (t.isActivePrayer(Prayers.PIETY)) {
                tar_prayer_bonus += 0.25;
            }

            tar_style_bonus = t.getAttackStyle() == 2 ? 1 : t.getAttackStyle() == 3 ? 3 : 0;

            switch (att_type) {
            case 0:
                tar_equipment_bonus = t.playerBonus[attacker.isPlayer() ? attacker.toPlayer().getAttackStyle() <= 1
                    ? attacker.toPlayer().getAttackStyle() + 5 : 6 : 6];
                break;
            case 1:
                tar_equipment_bonus = t.playerBonus[9];
                break;
            case 2:
                tar_equipment_bonus = t.playerBonus[8];
                break;
            default:
                break;
            }
        }

        switch (att_type) {
        case 0:
            tar_effective_defence = Math.floor(((tar_curr_defence * tar_prayer_bonus) + tar_style_bonus));
            break;
        case 1:
            tar_effective_defence = Math.floor(((tar_curr_defence * tar_prayer_bonus) + tar_style_bonus));
            break;
        case 2:
            tar_effective_defence = Math.floor(((tar_curr_defence * tar_prayer_bonus) * 0.3)) + (Math.floor(tar_curr_magic * 0.7));
            break;
        default:
            break;
        }

        augmented_attack = Math.floor((((att_effective_attack + 8) * (att_equipment_bonus + 64)) / 10));

        augmented_defence = Math.floor((((tar_effective_defence + 8) * (tar_equipment_bonus + 64)) / 10));

        double hit_chance = 0;

        if (augmented_attack < augmented_defence) {
            hit_chance = ((augmented_attack - 1) / (augmented_defence * 2));
        } else {
            hit_chance = 1 - ((augmented_defence + 1) / (augmented_attack * 2));
        }

        switch (att_type) {
        case 0:
            if (target.isPlayer() && target.toPlayer().isActivePrayer(Prayers.PROTECT_FROM_MELEE)) {
                att_hit_chance = Math.floor((((hit_chance * att_spec_bonus) * att_void_bonus) * 0.6) * 100);
                tar_block_chance = Math.floor(101 - ((((hit_chance * att_spec_bonus) * att_void_bonus) * 0.6) * 100));
            } else {
                att_hit_chance = Math.floor(((hit_chance * att_spec_bonus) * att_void_bonus) * 100);
                tar_block_chance = Math.floor(101 - (((hit_chance * att_spec_bonus) * att_void_bonus) * 100));
            }
            break;
        case 1:
            if (target.isPlayer() && target.toPlayer().isActivePrayer(Prayers.PROTECT_FROM_MISSILE)) {
                att_hit_chance = Math.floor((((hit_chance * att_spec_bonus) * att_void_bonus) * 0.6) * 100);
                tar_block_chance = Math.floor(101 - ((((hit_chance * att_spec_bonus) * att_void_bonus) * 0.6) * 100));
            } else {
                att_hit_chance = Math.floor(((hit_chance * att_spec_bonus) * att_void_bonus) * 100);
                tar_block_chance = Math.floor(101 - (((hit_chance * att_spec_bonus) * att_void_bonus) * 100));
            }
            break;
        case 2:
            if (target.isPlayer() && target.toPlayer().isActivePrayer(Prayers.PROTECT_FROM_MAGIC)) {
                att_hit_chance = Math.floor(((hit_chance * att_void_bonus) * 0.6) * 100);
                tar_block_chance = Math.floor(101 - ((((hit_chance * att_spec_bonus) * att_void_bonus) * 0.6) * 100));
            } else {
                att_hit_chance = Math.floor((hit_chance * att_void_bonus) * 100);
                tar_block_chance = Math.floor(101 - (((hit_chance * att_spec_bonus) * att_void_bonus) * 100));
            }
            break;
        default:
            break;
        }
        att_hit_chance = Utility.getRandom((int) (att_hit_chance + 20 + (!attacker.isNPC() ? attacker.toPlayer().isUsingSpecial() ? 20 * additionalSpecMulti : 0 : 0)));
        tar_block_chance = Utility.getRandom((int) tar_block_chance);
        /*if (attacker.isPlayer())
        	System.out.println("target "+target+" hit = "+((int)att_hit_chance)+" > "+((int) tar_block_chance * repBonus)+" also additionalSpecMulti "+additionalSpecMulti);*/
        return (int) att_hit_chance > (int) tar_block_chance;
    }
    
    /**
	 * Calculates a mob's melee max hit.
	 */
	public static int calculateMeleeMaxHit(Entity attacker, Entity target) {

		Player player = (Player) attacker;
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double strengthBonus = player.playerBonus[10];
		double otherBonusMultiplier = 1;
		int strengthLevel = player.getSkills().getLevel(Skills.STRENGTH);
		int combatStyleBonus = weaponBonus(player);

		if (player.isActivePrayer(Prayers.BURST_OF_STRENGTH)) {
			prayerMultiplier = 1.05;
		} else if (player.isActivePrayer(Prayers.SUPERHUMAN_STRENGTH)) {
			prayerMultiplier = 1.1;
		} else if (player.isActivePrayer(Prayers.ULTIMATE_STRENGTH)) {
			prayerMultiplier = 1.15;
		} else if (player.isActivePrayer(Prayers.CHIVALRY)) {
			prayerMultiplier = 1.18;
		} else if (player.isActivePrayer(Prayers.PIETY)) {
			prayerMultiplier = 1.23;
		}

		// Apply black mask/slayer helm bonus if the victim is the player's slayer task
		if (target != null)
			if (attacker.isPlayer() && target.isNPC()) {
				final NPC npc = (NPC) target;
				if (player.getSlayerTask() != -1 && player.getSlayerTask() == npc.npcId && hasBlackMaskOrSlayerHelm(player)) {
					otherBonusMultiplier = 1.15;
				}
			}

		if (fullDharok(player)) {
			double dharokMultiplier = ((1 - ((float) player.getSkills().getLevel(Skills.HITPOINTS) / (float) player.getSkills().getLevelForExperience(Skills.HITPOINTS)) * 1.7)) + 1;
			otherBonusMultiplier *= dharokMultiplier;
		}
		
		if(wearingFullVoid(player, 0)) {
			otherBonusMultiplier = 1.1;
		}
		
		if (hasBerserkerNecklaceBonus(player)) {
			otherBonusMultiplier = 1.2;
		}
		
		int effectiveStrengthDamage = (int) ((strengthLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double base = (13 + effectiveStrengthDamage + (strengthBonus / 8) + ((effectiveStrengthDamage * strengthBonus) * 0.016865)) / 10;

		if (player.isUsingSpecial()) {
			switch (player.playerEquipment[player.getEquipment().getWeaponId()]) {
			case 11802:
				specialMultiplier = 1.42375;
				break;
			case 11804:
				specialMultiplier = 1.1825;
				break;
			case 11806:
			case 11808:
				specialMultiplier = 1.075;
				break;
			case 3101:
			case 3204:
			case 1215:
			case 1231:
			case 5680:
			case 5698:
				specialMultiplier = 1.25;
				break;
			case 1305:
				specialMultiplier = 1.15;
				break;
			case 1434:
				specialMultiplier = 1.45;
				break;
			}
			base *= specialMultiplier;
		}

		return (int) Math.floor(base);
	}

	/**
	 * Retrieves the players attackstyle.
	 * 
	 * @param player
	 * @return {@link attackStyle EX: CONTROLLED}
	 */
	public static final int weaponBonus(Player player) {
		switch (player.getAttackStyle()) {
		case 2:
			return 3;
		default:
			break;
		}
		return 0;
	}
	
	/**
	 * Calculates a mob's range max hit.
	 */
	public static int calculateRangeMaxHit(Entity mob, Entity victim) {
		Player player = (Player) mob;
		
		int maxHit = 0;
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1;
		int rangedStrength = RangeData.getRangeStr(player.playerEquipment[player.getEquipment().getQuiverId()]);
		
		if(player.playerEquipment[player.getEquipment().getWeaponId()] == 4222) {
			/**
			 * Crystal Bow does not use arrows, so we don't use the arrows range strength bonus.
			 */
			rangedStrength = 70;
		}
		
		if(player.playerEquipment[player.getEquipment().getWeaponId()] == 12926) {
			rangedStrength = 60;
		}
		
		if (victim != null)
			if (mob.isPlayer() && victim.isNPC()) {
				final NPC npc = (NPC) victim;
				if (player.getSlayerTask() != -1 && player.getSlayerTask() == npc.npcId && hasBlackMaskOrSlayerHelm(player)) {
					otherBonusMultiplier = 1.15;
				}
			}
		
		int rangeLevel = player.getSkills().getLevel(Skills.RANGE);
		int combatStyleBonus = player.getAttackStyle() == 0 ? 3 : 0;
		
		if (player.isActivePrayer(Prayers.SHARP_EYE)) {
			rangeLevel *= 1.05;
        } else if (player.isActivePrayer(Prayers.HAWK_EYE)) {
        	rangeLevel *= 1.10;
        } else if (player.isActivePrayer(Prayers.EAGLE_EYE)) {
        	rangeLevel *= 1.15;
        }
		
		if(wearingFullVoid(player, 2)) {
			otherBonusMultiplier = 1.2;
		}
		
		int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double baseDamage = 1.3 + (effectiveRangeDamage / 10) + (rangedStrength / 80) + ((effectiveRangeDamage * rangedStrength) / 640);
		
		if (player.isUsingSpecial()) {
			switch (player.playerEquipment[player.getEquipment().getQuiverId()]) {
			case 9243:
				specialMultiplier = 1.15;
				break;
			case 9244:
				specialMultiplier = 1.45;
				break;
			case 9245:
				specialMultiplier = 1.15;
				break;
			case 9236:
				specialMultiplier = 1.25;
				break;
			case 882:
			case 884:
			case 886:
			case 888:
			case 890:
			case 892:
			case 11212:
				if (player.playerEquipment[player.getEquipment().getWeaponId()] == 11235) {
					specialMultiplier = 1.3;
					if (player.playerEquipment[player.getEquipment().getQuiverId()] == 11212)
						specialMultiplier += .5;
				}
				if (player.playerEquipment[player.getEquipment().getQuiverId()] == 11212)
					specialMultiplier = 1.5;
				break;
			}
		}
		
		maxHit = (int) (baseDamage * specialMultiplier);
		if(player.playerEquipment[player.getEquipment().getWeaponId()] == -1) {
			maxHit = 0;
		}
		if(player.playerEquipment[player.getEquipment().getWeaponId()] == 12926) {
        	if (player.isUsingSpecial()) {
        		maxHit *= 1.65;
        	} else {
        		maxHit *= 1.13;
        	}
        }
		
		if(player.playerEquipment[player.getEquipment().getWeaponId()] == 20997) {
			maxHit *= 2.22;
        }
		
		if(player.playerEquipment[player.getEquipment().getAmuletId()] == 19547) {
			maxHit += 1;
        }
		return maxHit;
	}

	public static boolean hasAmuletOfTheDamned(Player player) {
		return player.getItems().isWearingItem(12851);
	}

	public static boolean hasBerserkerNecklaceBonus(Player player) {
		return player.getItems().isWearingItem(11128)
				&& (player.getItems().isWearingItem(6523) || player.getItems().isWearingItem(6528)
						|| player.getItems().isWearingItem(6527) || player.getItems().isWearingItem(6525));
	}

	public static boolean hasBlackMaskOrSlayerHelm(Player player) {
		return player.getItems().isWearingItem(8901) || player.getItems().isWearingItem(11864)
				|| player.getItems().isWearingItem(11865);
	}

	public static boolean hasImbuedSlayerHelm(Player player) {
		return player.getItems().isWearingItem(11865);
	}

	public static boolean fullGuthan(Player player) {
		return player.getItems().isWearingItem(4724) && player.getItems().isWearingItem(4726)
				&& player.getItems().isWearingItem(4728) && player.getItems().isWearingItem(4730);
	}

	public static boolean fullTorag(Player player) {
		return player.getItems().isWearingItem(4745) && player.getItems().isWearingItem(4747)
				&& player.getItems().isWearingItem(4749) && player.getItems().isWearingItem(4751);
	}

	public static boolean fullKaril(Player player) {
		return player.getItems().isWearingItem(4732) && player.getItems().isWearingItem(4734)
				&& player.getItems().isWearingItem(4736) && player.getItems().isWearingItem(4738);
	}

	public static boolean fullAhrim(Player player) {
		return player.getItems().isWearingItem(4708) && player.getItems().isWearingItem(4710)
				&& player.getItems().isWearingItem(4712) && player.getItems().isWearingItem(4714);
	}

	public static boolean fullAhrimDamned(Player player) {
		return player.getItems().isWearingItem(4708) && player.getItems().isWearingItem(4710)
				&& player.getItems().isWearingItem(4712) && player.getItems().isWearingItem(4714)
				&& player.getItems().isWearingItem(12851);
	}

	public static boolean fullDharok(Player player) {
		return player.getItems().isWearingItem(4716) && player.getItems().isWearingItem(4718)
				&& player.getItems().isWearingItem(4720) && player.getItems().isWearingItem(4722);
	}

	public static boolean fullVerac(Player player) {
		return player.getItems().isWearingItem(4753) && player.getItems().isWearingItem(4755)
				&& player.getItems().isWearingItem(4757) && player.getItems().isWearingItem(4759);
	}

	/**
	 * Checks if the player is wearing full void.
	 * 
	 * @param player
	 * @return
	 */
	public static boolean wearingFullVoid(Player player, int type) {
		int complete = 0;

		if (type < 0) {
			return false;
		}

		boolean helmet = player.playerEquipment[0] == (type == 0 ? 11665 : type == 1 ? 11663 : 11664);

		boolean hasGloves = player.playerEquipment[9] == 8842;

		boolean hasDeflector = player.playerEquipment[5] == 19712;

		boolean hasLegs = player.playerEquipment[7] == 8840 || player.playerEquipment[7] == 13073;

		boolean hasPlate = player.playerEquipment[4] == 8839 || player.playerEquipment[4] == 13072;

		if (helmet) {
			complete++;
		}

		if (hasGloves) {
			complete++;
		}

		if ((hasDeflector && ((hasPlate && !hasLegs) || (!hasPlate && hasLegs))) || hasPlate && hasLegs) {
			complete++;
		}

		return complete >= 3;
	}
	
	/**
	 * The percentage of the hit reducted by antifire.
	 */
	public static double dragonfireReduction(Entity entity) {
		Player player = (Player) entity;
		boolean dragonfireShield = player.getItems().isWearingItem(1540) || player.getItems().isWearingItem(11283) || player.getItems().isWearingItem(11284) || player.getItems().isWearingItem(11285);
		boolean dragonfirePotion = false;
		if (entity.hasAttribute("antiFire")) {
			dragonfirePotion = System.currentTimeMillis() - (long)entity.getAttribute("antiFire", 0L) < 360000;
		} else if (entity.hasAttribute("extended_antiFire")) {
            dragonfirePotion = System.currentTimeMillis() - (long)entity.getAttribute("extended_antiFire", 0L) < 720000;
        }
		boolean protectPrayer = player.isActivePrayer(Prayers.PROTECT_FROM_MAGIC);
		if (dragonfireShield && dragonfirePotion) {
			player.getActionSender().sendMessage("You shield absorbs most of the dragon fire!");
			player.getActionSender().sendMessage("Your potion protects you from the heat of the dragon's breath!");
			return 1;
		} else if (dragonfireShield) {
			player.getActionSender().sendMessage("You shield absorbs most of the dragon fire!");
			return 0.8; // 80%
		} else if (dragonfirePotion) {
			player.getActionSender().sendMessage("Your potion protects you from the heat of the dragon's breath!");
			return 0.8; // 80%
		} else if (protectPrayer) {
			player.getActionSender().sendMessage("Your prayers resist some of the dragon fire.");
			return 0.6; // 60%
		}
		return 0;		
	}

}