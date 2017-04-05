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
import com.model.game.character.combat.npcs.AbstractBossCombat;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;
import com.model.utility.Utility;

public class Sergeant_Grimspike extends AbstractBossCombat {
	
	public Sergeant_Grimspike(int npcId) {
		super(npcId);
	}

	@Override
	public void execute(Entity attacker, Entity victim) {
		if(!attacker.isNPC()) {
			return;
		}
		CombatStyle style = CombatStyle.RANGE;
		NPC npc = (NPC) attacker;
		int maxHit = 21;
		npc.aggressive = true;
		switch (style) {
		case RANGE:
			npc.setCombatType(style);
			npc.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
			int clientSpeed;
			int gfxDelay;
			int randomHit = Utility.random(maxHit);
			randomHit = Utility.random(maxHit);
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
				
				  attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), victim.getCentreLocation(), 1220, 45, 50, speedEquation-10, 43, 35, victim.getProjectileLockonIndex(), 10, 48));
					  victim.getActionSender().sendMessage("Start Projectile: "+attacker.getCentreLocation()+" End projectile "+victim.getPosition());
				  Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.RANGE, false);
		          Combat.hitEvent(attacker, victim, 2, hitInfo, CombatStyle.RANGE);
		
		default:
			break;
		
		}
		((NPC)attacker).attackTimer = 6;
	}

	@Override
	public int distance(Entity attacker) {
		return 8;
	}

}
