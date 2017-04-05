package com.model.game.character.combat.npcs.script;

import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Graphic;
import com.model.game.character.Hit;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.Projectile;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.npcs.AbstractBossCombat;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Skills;
import com.model.utility.Utility;

public class Dharoks extends AbstractBossCombat {

	public Dharoks(int npcId) {
		super(npcId);
	}

	@Override
	public void execute(Entity attacker, Entity victim) {
		if(!attacker.isNPC()) {
			return;
		}
		
		NPC npc = (NPC) attacker;
		int health = npc.currentHealth;
		final int origHealth = 100;
		double dharokMultiplier =  (1 + (100 - npc.currentHealth)/10);
		int randomHit = Utility.random((int) (npc.getDefinition().getMaxHit()));
		npc.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
		//victim.getActionSender().sendMessage("Dharok multiplier: "+dharokMultiplier+ " Max: "+npc.getDefinition().getMaxHit()* dharokMultiplier);
		 Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MELEE, false);
         Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
         ((NPC)attacker).attackTimer = 5;
	}

	@Override
	public int distance(Entity attacker) {
		return 2;
	}

}
