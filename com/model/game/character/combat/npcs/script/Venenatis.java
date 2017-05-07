package com.model.game.character.combat.npcs.script;

import java.util.List;

import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Graphic;
import com.model.game.character.Hit;
import com.model.game.character.PoisonType;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.Projectile;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.npcs.AbstractBossCombat;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.utility.Utility;

public class Venenatis extends AbstractBossCombat {

public Venenatis(int npcId) {
	super(npcId);
}

@Override
public void execute(Entity attacker, Entity victim) {
	
	if(!attacker.isNPC()) {
		return;
	}
	
	//int timer = attacker.isNPC() ? 1 : attacker.isPlayer() ? 2:  5;
	
	CombatStyle style = Utility.random(1) == 0 ? CombatStyle.MAGIC : attacker.getPosition().distanceToEntity(attacker, victim) <= 1 ? CombatStyle.MELEE : CombatStyle.MAGIC;
	NPC npc = (NPC) attacker;
	Player player = (Player) victim;
	int maxHit = style == CombatStyle.MAGIC ? 25 : 50;
	
	switch (style) {
	
	case MELEE:
		npc.setCombatType(style);
		npc.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
		int randomHit = Utility.random(maxHit);
        Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MELEE, false);
        Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
    	npc.setPoisonType(PoisonType.SUPER_NPC);
		break;
		
		
	case MAGIC:
		npc.setCombatType(style);
		npc.playAnimation(Animation.create(npc.getDefinition().getAttackAnimation()));
		npc.playGraphics(Graphic.create(164, 0, 100));
		int clientSpeed;
		int gfxDelay;
		randomHit = Utility.random(maxHit);
        List<Player> localPlayers = NPC.getSurroundingPlayers(npc, 16);
		for (Player players : localPlayers) {
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
			attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), players.getCentreLocation(), 50, 96, 165, speedEquation, 43, 31, players.getProjectileLockonIndex(), 66, 0));
			player.playGraphics(Graphic.create(166, 80, 0));
			  hitInfo = players.take_hit(attacker, randomHit, CombatStyle.MAGIC, false);
			  //attacker.playProjectile(Projectile.create(attacker.getCentreLocation(), victim.getCentreLocation(), 1202, 45, 50, speedEquation, 43, 35, victim.getProjectileLockonIndex(), 10, 48));  System.out.println("Creating projectile at "+npc.getCentreLocation());
	          Combat.hitEvent(attacker, players, 2, hitInfo, CombatStyle.MAGIC);
	          //figure out npc size and location
	        //Poison
		}
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
