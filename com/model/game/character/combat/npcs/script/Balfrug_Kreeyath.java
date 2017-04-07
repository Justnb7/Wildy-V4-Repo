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

public class Balfrug_Kreeyath extends AbstractBossCombat {
	

	public Balfrug_Kreeyath(int npcId) {
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
		
		switch (style) {

		case RANGE:
			int randomHit = Utility.random(maxHit);
			attacker.setCombatType(style);
			attacker.playAnimation(Animation.create(attacker.asNpc().getDefinition().getAttackAnimation()));
			attacker.playGraphics(Graphic.create(1222));
			randomHit = Utility.random(maxHit);
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
				attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), victim.getCentreLocation(), 1223, 45, 50, speedEquation, 43, 35, victim.getProjectileLockonIndex(), 10, 48));
				Hit  hitInfo = victim.take_hit(attacker, randomHit, style, false);
				Combat.hitEvent(attacker, victim, 2, hitInfo, style);
			break;
			
	
		default:
			break;
		
		}
		((NPC)attacker).attackTimer = npc.getDefinition().getAttackSpeed();
		npc.setCombatType(style);
	}

	@Override
	public int distance(Entity attacker) {
		return 8;
	}

}
