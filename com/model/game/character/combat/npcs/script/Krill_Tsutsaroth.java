package com.model.game.character.combat.npcs.script;

import java.util.List;
import java.util.Random;

import com.model.Server;
import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Graphic;
import com.model.game.character.Hit;
import com.model.game.character.PoisonType;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.PrayerHandler.Prayers;
import com.model.game.character.combat.Projectile;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.combat_data.CombatType;
import com.model.game.character.combat.npcs.AbstractBossCombat;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.game.character.player.Skills;
import com.model.game.character.player.packets.out.SendMessagePacket;
import com.model.task.ScheduledTask;
import com.model.utility.Utility;

public class Krill_Tsutsaroth extends AbstractBossCombat {
	
	private static Random r = new Random();
	
	private final String[] MESSAGES = { "Attack them, you dogs!", "Forward!", "Death to Saradomin's dogs!",
			"Kill them, you cowards!",
			"The Dark One will have their souls!",
			"Zamorak curse them!",
			"Rend them limb from limb!", "No retreat!", "Flay them all!"};

	
	public Krill_Tsutsaroth(int npcId) {
		super(npcId);
	}
	
	private void sendSpecialAttack(Entity player, Entity npc) {
		npc.forceChat("YARRRRRRR!");
		int prayerReduction = player.asPlayer().getSkills().getLevel(Skills.PRAYER) / 2;
		if (prayerReduction < 1) {
			return;
		}
		player.asPlayer().getSkills().setLevel(Skills.PRAYER, prayerReduction);
		
		if (player.asPlayer().getSkills().getLevel(Skills.PRAYER) < 0) {
			player.asPlayer().getSkills().setLevel(Skills.PRAYER, 0);
		}
		player.asPlayer().write(new SendMessagePacket("K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained."));
	}
	@Override
	public void execute(Entity attacker, Entity victim) {
		
		if(!attacker.isNPC()) {
			return;
		}
		
		int randomMessage = r.nextInt(5);
		if (randomMessage == 1) {
			attacker.forceChat(MESSAGES[(int) (Math.random() * MESSAGES.length)]);
		}
		CombatStyle style = Utility.random(0) == 0 ? CombatStyle.MAGIC : CombatStyle.MELEE;
		NPC npc = (NPC) attacker;
		int maxHit = style == CombatStyle.MELEE ? 47 : 30;
		int specMax = 49;
		
		switch (style) {
		
		case MELEE:
			npc.setCombatType(style);
			int randomHit = Utility.random(maxHit);
			attacker.playAnimation(Animation.create(attacker.asNpc().getDefinition().getAttackAnimation()));
			if (victim.asPlayer().isActivePrayer(Prayers.PROTECT_FROM_MELEE) && r.nextInt(10) == 0) {
				sendSpecialAttack(victim, attacker);
				attacker.setAttribute("prayer hitthrough", true);
				Hit hitInfo = victim.take_hit(attacker,  Utility.random(specMax), CombatStyle.MELEE, false);
	            Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
	            attacker.setAttribute("prayer hitthrough", false);
			} else {
            Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MELEE, false);
            Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
			}
		break;
			
		/*
		 * Utilizes getSurroundingPlayers to perform multi attack
		 */
		case MAGIC:
			npc.setCombatType(style);
			npc.playAnimation(Animation.create(6950));
			randomHit = Utility.random(maxHit);
			int randomHit2 = Utility.random(maxHit);
            List<Player> localPlayers = NPC.getSurroundingPlayers(npc, 16);
			for (Player players : localPlayers) {
				int speedEquation;
				if (attacker.getPosition().isWithinDistance(attacker, players, 1)) {
					speedEquation = 70;
				} else if (attacker.getPosition().isWithinDistance(attacker, players, 5)) {
					speedEquation = 90;
				} else if (attacker.getPosition().isWithinDistance(attacker, players, 8)) {
					speedEquation = 110;
				} else {
					speedEquation = 130;
				}
				attacker.playGraphics(Graphic.create(1210));
				players.playGraphics(Graphic.create(1225, 0, 0));
				attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), players.getCentreLocation(), 1224, 45, 50, speedEquation, 43, 35, players.getProjectileLockonIndex(), 10, 48));
				Hit hitInfo = players.take_hit(attacker, randomHit, style, false);
				 Combat.hitEvent(attacker, players, 2, hitInfo, style);
				//Poison
			if(r.nextInt(8) == 0) {
				players.setPoisonType(PoisonType.SUPER_NPC);
			}
		}
			break;
			
		default:
			break;
		
		}
		((NPC)attacker).attackTimer = style == CombatStyle.MAGIC ? 4 : 4;
		npc.setCombatType(style);
	}

	@Override
	public int distance(Entity attacker) {
		return attacker.getCombatType() == CombatStyle.MELEE ? 1: 8;
	}

}
