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

public class Tstanon_Karlak extends AbstractBossCombat {
	

	public Tstanon_Karlak(int npcId) {
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
			int randomHit = Utility.random(maxHit);
			npc.setCombatType(style);
			npc.playAnimation(Animation.create(attacker.asNpc().getDefinition().getAttackAnimation()));
			Hit hitInfo = victim.take_hit(attacker, randomHit, style, false);
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
		return 1;
	}

}
