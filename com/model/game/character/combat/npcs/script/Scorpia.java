package com.model.game.character.combat.npcs.script;

import com.model.Server;
import com.model.game.World;
import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Hit;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.Projectile;
import com.model.game.character.combat.PrayerHandler.Prayers;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.npcs.AbstractBossCombat;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;
import com.model.task.impl.NPCDeathTask;
import com.model.utility.Utility;

public class Scorpia extends AbstractBossCombat {

	public Scorpia(int npcId) {
		super(npcId);
	}
	
	@Override
	public void execute(Entity attacker, Entity victim) {
		if(!attacker.isNPC()) {
			return;
		}
		CombatStyle style = CombatStyle.MELEE;
			NPC npc = (NPC) attacker;
			int maxHit = 16;
			
			switch (style) {
			case MELEE:
				npc.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
				int clientSpeed;
				int gfxDelay;
				npc.setCombatType(style);
				int randomHit = Utility.random(maxHit);
				randomHit = Utility.random(maxHit);
						if(attacker.getPosition().isWithinDistance(attacker, victim, 1)) {
							clientSpeed = 70;
							gfxDelay = 80;
						} else if(attacker.getPosition().isWithinDistance(attacker, victim, 5)) {
							clientSpeed = 90;
							gfxDelay = 100;
						} else if(attacker.getPosition().isWithinDistance(attacker, victim, 8)) {
							clientSpeed = 110;
							gfxDelay = 120;
						} else {
							clientSpeed = 130;
							gfxDelay = 140;
						}
					  int delay = (gfxDelay / 20) - 1;
					  attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), victim.getCentreLocation(), 1217, 40, 50, clientSpeed-10, 43, 35, victim.getProjectileLockonIndex(), 10, 48));
							  Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MAGIC, false);
			          Combat.hitEvent(attacker, victim, delay, hitInfo, CombatStyle.MAGIC);
			
			default:
				break;
			
			}
			((NPC)attacker).attackTimer = 6;
	}

	@Override
	public int distance(Entity attacker) {
		// TODO Auto-generated method stub
		return 2;
	}
		
	}

		