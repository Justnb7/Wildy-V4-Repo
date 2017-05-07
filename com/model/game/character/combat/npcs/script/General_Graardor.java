package com.model.game.character.combat.npcs.script;

import java.util.List;
import java.util.Random;

import com.model.Server;
import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Graphic;
import com.model.game.character.Hit;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.Projectile;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.combat_data.CombatType;
import com.model.game.character.combat.npcs.AbstractBossCombat;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;
import com.model.utility.Utility;

public class General_Graardor extends AbstractBossCombat {
	
	private static Random r = new Random();
	private final String[] MESSAGES = { "CHAAARGE!", "Death to our enemies!",
			"Split their skulls!", "Break their bones!",
			"Crush them underfoot!", "All glory to Bandos!", "GRRRAAAAAR!",
			"FOR THE GLORY OF THE BIG HIGH WAR GOD!",
			"For the glory of Bandos!", "Brargh!",
			"We feast on the bones of our enemies tonight!" };
	
	public General_Graardor(int npcId) {
		super(npcId);
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
				
		CombatStyle style = Utility.random(1) == 0 ? CombatStyle.RANGE : attacker.getPosition().distanceToEntity(attacker, victim) <= 1 ? CombatStyle.MELEE : CombatStyle.RANGE;
		NPC npc = (NPC) attacker;
		Player player = (Player) victim;
		int maxHit = style == CombatStyle.RANGE ? 35 : 60;
		
		switch (style) {
		
		case MELEE:
			npc.setCombatType(style);
			npc.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
			int randomHit = Utility.random(maxHit);
            Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MELEE, false);
            Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
			break;
			
		/*
		 * Utilizes getSurroundingPlayers to perform multi attack
		 */
		case RANGE:
			npc.setCombatType(style);
			npc.playAnimation(Animation.create(7021));
			int clientSpeed;
			int gfxDelay;
			randomHit = Utility.random(maxHit);
            List<Player> localPlayers = NPC.getSurroundingPlayers(npc, 16);
			for (Player players : localPlayers) {
				attacker.playAnimation(Animation.create(7021));
				attacker.playGraphics(Graphic.create(1203, 0, 0));

				// Set the projectile speed based on distance
				int speedEquation;
				if (attacker.getPosition().isWithinDistance(attacker, victim, 1)) {
					speedEquation = 70;
				} else if (attacker.getPosition().isWithinDistance(attacker, victim, 5)) {
					speedEquation = 90;
				} else if (attacker.getPosition().isWithinDistance(attacker, victim, 8)) {
					speedEquation = 110;
				} else {
					speedEquation = 130;
				}
				
				// Send the projectile
				attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), players.getCentreLocation(), 1202, 45, 50, speedEquation, 43, 35, players.getProjectileLockonIndex(), 10, 48));
				  hitInfo = players.take_hit(attacker, randomHit, CombatStyle.RANGE, false);
				  //attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), victim.getCentreLocation(), 1202, 45, 50, speedEquation, 43, 35, victim.getProjectileLockonIndex(), 10, 48));  System.out.println("Creating projectile at "+npc.getCentreLocation());
		          Combat.hitEvent(attacker, players, 2, hitInfo, CombatStyle.RANGE);
		          
		          //figure out npc size and location
			}
			break;
			
		default:
			break;
		
		}
		((NPC)attacker).attackTimer = 6;
		npc.setCombatType(style);
	}

	@Override
	public int distance(Entity attacker) {
		return 8;
	}

}
