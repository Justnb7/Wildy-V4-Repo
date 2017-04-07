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

public class Commander_Zilyana extends AbstractBossCombat {
	
	private static Random r = new Random();
	
	private final String[] MESSAGES = { "eath to the enemies of the light!", "Slay the evil ones!", "Saradomin lend me strength!",
			"By the power of Saradomin!",
			"May Saradomin be my sword!",
			"Good will always triumph!",
			"Forward! Our allies are with us!", "Saradomin is with us!", "In the name of Saradomin!",
			"Attack! Find the Godsword!"};

	
	public Commander_Zilyana(int npcId) {
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
		
		CombatStyle style = Utility.random(5) == 0 ? CombatStyle.MAGIC : CombatStyle.MELEE;
		NPC npc = (NPC) attacker;
		Player player = (Player) victim;
		int maxHit = style == CombatStyle.MELEE ? 35 : 35;
		
		switch (style) {
		
		case MELEE:
			npc.setCombatType(style);
			npc.playAnimation(Animation.create(6967));
			int randomHit = Utility.random(maxHit);
            Hit hitInfo = victim.take_hit(attacker, randomHit, CombatStyle.MELEE, false);
            Combat.hitEvent(attacker, victim, 1, hitInfo, CombatStyle.MELEE);
			break;
			
		/*
		 * Utilizes getSurroundingPlayers to perform multi attack
		 */
		case MAGIC:
			npc.setCombatType(style);
			npc.playAnimation(Animation.create(6970));
			randomHit = Utility.random(maxHit);
			int randomHit2 = Utility.random(maxHit);
            List<Player> localPlayers = NPC.getSurroundingPlayers(npc, 16);
			for (Player players : localPlayers) {
				hitInfo = players.take_hit(attacker, randomHit, CombatStyle.MAGIC, false);
				Hit hitInfo2 = players.take_hit(attacker, randomHit2, CombatStyle.MAGIC, false);
				Combat.hitEvent(attacker, players, 2, hitInfo, CombatStyle.MAGIC);
				Server.getTaskScheduler().schedule(new ScheduledTask(2) {
					@Override
					public void execute() {
						Combat.hitEvent(attacker, players, 2, hitInfo2, CombatStyle.MAGIC);
						this.stop();
					}
				});
				players.playGraphics(Graphic.create(1221));
			}
			break;
			
		default:
			break;
		
		}
		((NPC)attacker).attackTimer = style == CombatStyle.MAGIC ? 6 : 4;
		npc.setCombatType(style);
	}

	@Override
	public int distance(Entity attacker) {
		return attacker.getCombatType() == CombatStyle.MELEE ? 1: 8;
	}

}
