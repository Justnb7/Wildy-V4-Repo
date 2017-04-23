package com.model.game.character.combat.nvp;

import java.util.Arrays;
import java.util.List;

import com.model.game.World;
import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Graphic;
import com.model.game.character.Hit;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.Projectile;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.npcs.BossScripts;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.utility.Utility;

/**
 * Holds all of the combat data for npc combat
 * 
 * @author Mobster
 * @author Patrick van Elderen
 *
 */
public class NPCCombatData {

	/**
	 * A list of unspawnable npcs on death
	 */
	private static List<Integer> unspawnableNpcs = Arrays.asList(5779, 4303, 4304, 1605, 1606, 1607, 1608, 1609, 5054);
	
	public static void executeCombat(Entity attacker, Entity victim){
		if(!attacker.isNPC()) {
			return;
		}
		CombatStyle style;
		NPC npc = (NPC) attacker;
		int maxHit = npc.getDefinition().getMaxHit();
		int randomHit = Utility.random(maxHit);
		switch(npc.getId()) {
		/*
		 * Godwars NPCS
		 */
			case 3169:
			case 3183:
			case 3182:
				npc.ignoreClipping = true;
				style = CombatStyle.RANGE;	
				npc.setCombatType(style);
				npc.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
				randomHit = Utility.random(maxHit);
					attacker.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
					int speedEquation;
					if (attacker.getPosition().isWithinDistance(attacker, victim, 1)) {
						speedEquation = 70;
					} else if (attacker.getPosition().isWithinDistance(attacker, victim, 5)) {
						speedEquation = 75;
					} else if (attacker.getPosition().isWithinDistance(attacker, victim, 8)) {
						speedEquation = 85;
					} else {
						speedEquation = 90;
					}
					attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), victim.getCentreLocation(), npc.getId() == 3169 ? 1193 : 1192, 45, 50, speedEquation, 43, 35, victim.getProjectileLockonIndex(), 10, 48));
					Hit  hitInfo = victim.take_hit(attacker, randomHit, style, false);
					Combat.hitEvent(attacker, victim, 2, hitInfo, style);
				break;
			case 2211: //spiritual ranger
			case 2242:
				npc.ignoreClipping = true;
				style = CombatStyle.RANGE;	
				npc.setCombatType(style);
				victim.playGraphics(Graphic.create(76, 0, 0));
				attacker.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));	
				hitInfo = victim.take_hit(attacker, randomHit, style, false);
				Combat.hitEvent(attacker, victim, 2, hitInfo, style);
				break;
				
			case 2209:
			case 2212: // spirit mages and priest
				npc.ignoreClipping = true;
				style = CombatStyle.MAGIC;	
				npc.setCombatType(style);
				victim.playGraphics(Graphic.create(76, 0, 0));
				attacker.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));	
				hitInfo = victim.take_hit(attacker, randomHit, style, false);
				Combat.hitEvent(attacker, victim, 1, hitInfo, style);
			break;
				default:
				attacker.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));	
				hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MELEE, false);
				Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
				break;
	
		}
		
	}
	
	
	/**
	 * Gets the projectile speed for the npc
	 * 
	 * @param npc
	 *            The {@link NPC} to fetch the projectile speed for
	 * @return The projectile speed for the npc
	 */
	public static int getProjectileSpeed(NPC npc) {
		switch (npc.npcId) {
		
		case 5535:
			return 100;
		
		case 6581:
		case 6580:
			return 85;
			
		case 7497:
			return 85;

		case 2265:
		case 2266:
		case 2054:
		case 2837:
			return 85;
		case 4303:
		case 4304:
			return 90;
		case 3127:
			return 130;
		case 742:
		case 3590:
		case 5779:
			return 90;

		case 1672:
			return 85;

		case 1675:
			return 80;

		default:
			return 85;
		}
	}
	
	public static int getProjectileStartHeight(int npcType, int projectileId) {
		switch (npcType) {
		case 3127:
			return 110;
		case 2044:
			return 60;
		case 3163:
		case 3164:
		case 3165:
			return 60;
		case 492:
			return 30;
		case 6610:
			switch (projectileId) {
			case 165:
				return 20;
			}
			break;
		}
		return 43;
	}

	public static int getProjectileEndHeight(int npcType, int projectileId) {
		switch (npcType) {
		case 6610:
			switch (projectileId) {
			case 165:
				return 30;
			}
			break;
		}
		return 31;
	}

	/**
	 * Gets the list of unspawnwable npcs
	 * @return The list of unspawnable npcs
	 */
	public static List<Integer> getUnspawnableNpcs() {
		return unspawnableNpcs;
	}

	/**
	* Distanced required to attack
	**/	
	public static int distanceRequired(NPC npc) {
		if (BossScripts.isBoss(npc.npcId)) {
			return BossScripts.get(npc.npcId).distance(npc);
		}
		switch (npc.npcId) {
		/*
		 * Aviances
		 */
		case 3169:
		case 3183:
		case 3182:
			return 7;
			/*
			 * Spiritual mages
			 */
		case 2209:
		case 2212: // spirit mages and priest
		case 2211: //spiritual ranger
		case 2242:
			return 6;
		case 1672: // Ahrim the Blighted
		case 1675: // Karil the Tainted
			return 6;
		case 2044: // Zulrah
		case 2043: // Zulrah
		case 2042: // Zulrah
			return 20;
		case 494: // Kraken
		case 492: // Cave Kraken
		case 5535: // Enormous tentacle
			return 20;
		case 3130: // Tstanon Karlak
		case 2206: // Starlight
			return 2;
		case 3121: // Tok-Xil
		case 3125: // Ket-Zek
		case 2167: // TzHaar-Xil
		case 3127: // TzTok-Jad
		case 2218:
		case 2217:
		case 6618:
		case 3164:
		case 3163:
		case 3162:
		case 319:
		case 2207:
			return 4;
		case 6616:
			return 4;

		case 6615:
			return 5;

		case 6766:
			return 3;

		case 6611:
		case 6612:
		case 2265:
		case 3428:
		case 5961:
		case 5947:
			return 12;

		case 2054:
			return 6;

		case 3165:
			return 2;
		default:
			return 1;
		}
	}
	public static boolean appendBandosKC(NPC npc) {
		if (npc.killedBy == -1) {
			return false;
		}
		Player c = World.getWorld().getPlayers().get(npc.killedBy);
		int[] bandosGodKC = { 6271, 6272, 6273, 6274, 6275, 6268, 122, 6279, 6280, 6281, 6282, 6283, 6269, 6270, 6276,
				6277, 6278 };
		for (int j : bandosGodKC) {
			if (npc.npcId == j) {
				if (c.bandosKillCount < 20) {
					c.bandosKillCount++;
					c.getActionSender().sendString("" + c.bandosKillCount, 16217);
					return true;
				}
				break;
			}
		}
		return false;
	}
	public static boolean appendArmadylKC(NPC npc) {
		if (npc.killedBy == -1) {
			return false;
		}
		Player c = World.getWorld().getPlayers().get(npc.killedBy);
		int[] armaGodKC = { 6229, 6230, 6231, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240, 6241, 6242,
				6243, 6244, 6245, 6246, 275, 274 };
		for (int j : armaGodKC) {
			if (npc.npcId == j) {
				if (c.armadylKillCount < 20) {
					c.armadylKillCount++;
					c.getActionSender().sendString("" + c.armadylKillCount, 16216);
					return true;
				}
				break;
			}
		}
		return false;
	}
	public static boolean appendSaradominKC(NPC npc) {
		if (npc.killedBy == -1) {
			return false;
		}
		Player c = World.getWorld().getPlayers().get(npc.killedBy);
		int[] saraGodKC = { 6254, 6255, 6256, 6257, 6258, 6259, 96, 97, 111, 125, 913 };
		for (int j : saraGodKC) {
			if (npc.npcId == j) {
				if (c.saradominKillCount < 20) {
					c.saradominKillCount++;
					c.getActionSender().sendString("" + c.saradominKillCount, 16218);
					return true;
				}
				break;
			}
		}
		return false;
	}

	public static boolean appendZamorakKC(NPC npc) {
		if (npc.killedBy == -1) {
			return false;
		}
		Player c = World.getWorld().getPlayers().get(npc.killedBy);
		int[] zamGodKC = { 6210, 6211, 6212, 6214, 6218, 49, 82, 83, 84, 94, 92, 75, 78, 912 };
		for (int j : zamGodKC) {
			if (npc.npcId == j) {
				if (c.zamorakKillCount < 20) {
					c.zamorakKillCount++;
					c.getActionSender().sendString("" + c.zamorakKillCount, 16219);
					return true;
				}
				break;
			}
		}
		return false;
	}
}
