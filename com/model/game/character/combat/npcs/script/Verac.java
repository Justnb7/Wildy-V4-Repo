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
import com.model.utility.Utility;

public class Verac extends AbstractBossCombat {

	public Verac(int npcId) {
		super(npcId);
	}

	@Override
	public void execute(Entity attacker, Entity victim) {
		if(!attacker.isNPC()) {
			return;
		}
		int randomHit = Utility.random(25);
		attacker.setAttribute("prayer hitthrough", true);
		 Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MELEE, false);
         Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
         ((NPC)attacker).attackTimer = 5;
         attacker.setAttribute("prayer hitthrough", false);
	}

	@Override
	public int distance(Entity attacker) {
		return 2;
	}

}
