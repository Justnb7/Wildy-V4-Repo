package com.model.game.character.combat.nvp;

import com.model.game.World;
import com.model.game.character.Animation;
import com.model.game.character.Hit;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.combat_data.CombatAnimation;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.npcs.AbstractBossCombat;
import com.model.game.character.combat.npcs.BossScripts;
import com.model.game.character.combat.pvm.PlayerVsNpcCombat;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Boundary;
import com.model.game.character.player.Player;
import com.model.game.character.player.ProjectilePathFinder;
import com.model.game.location.Position;
import com.model.utility.Utility;

/**
 * Handles all Npc Vs Player combat methods
 * 
 * @author Mobster
 *
 */
public class NpcVsPlayerCombat {

	/**
	 * Handles the npcs combat timers
	 * 
	 * @param npc
	 *            The {@link NPC} to handle combat timers for
	 */
	public static void handleCombatTimer(NPC npc) {
		//npc.forceChat("attacktimer: "+npc.attackTimer+" "+npc.walkingHome+" "+npc.targetId);
		
		boolean isBoss = BossScripts.isBoss(npc.npcId);

		// Delay before we can attack again
		if (npc.attackTimer > 0) {
			npc.attackTimer--;
			//npc.forceChat("atk timer: "+npc.attackTimer+" "+npc.walkingHome+" "+npc.randomWalk);
		}
		
		if(!isBoss) {
			if (npc.attackTimer == 1) {
				executeDamage(npc);
			}
		}

		// If we havent been attacked within last 5 secs reset who last attack us
		if (System.currentTimeMillis() - npc.lastDamageTaken > 5000) {
			npc.underAttackBy = 0;
		}

		// Call code to attack our target if we're alive
		if (!npc.isDead && !npc.walkingHome && npc.targetId > 0) {
			Player player = World.getWorld().getPlayers().get(npc.targetId);

			if (npc.followTarget != player)
				npc.followTarget = player;

			if (player == null) {
				// out of range
				npc.targetId = 0;
				npc.underAttack = false;
				npc.resetFace();
			} else {
				if (npc.attackTimer == 0) {
					NpcVsPlayerCombat.attackPlayer(player, npc);
				}
				// Following called in process()
			}
		}
	}

	/**
	 * Handles an npc attacking a player
	 * 
	 * @param player
	 *            The {@link Player} being attacked
	 * @param npc
	 *            The {@link NPC} attacking the player
	 */
	public static void attackPlayer(Player player, NPC npc) {
		if (npc != null) {
			if (npc.isDead) {
				return;
			}
			/*
			 * Check to not allow attack through walls
			 */
			if (Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS)) {
				if (!Boundary.isIn(player, Boundary.GODWARS_BOSSROOMS)) {
					npc.targetId = 0;
					npc.underAttack = false;
					return;
				}
			}
			if (!validateAttack(player, npc)) {
				System.out.println("Stopping npc validation "+npc.getName());
				return;
			}
			
			npc.faceEntity(player);
			
			if (goodDistance(npc.getX(), npc.getY(), player.getX(), player.getY(),
					NPCCombatData.distanceRequired(npc))) {
				npc.randomWalk = false;
				
				boolean isBoss = BossScripts.isBoss(npc.npcId);
				AbstractBossCombat boss_cb = BossScripts.get(npc.npcId);
				if (isBoss) {
					boss_cb.execute(npc, player);
					// don't do any code below this, boss script handles all.
				} else {
					npc.attackTimer = npc.getDefinition().getAttackSpeed();
					npc.playAnimation(Animation.create(npc.getAttackAnimation()));

					if (npc.projectileId > 0) {
						int nX = npc.getX();
						int nY = npc.getY();
						int pX = player.getX();
						int pY = player.getY();
						int offX = (nX - pX) * -1;
						int offY = (nY - pY) * -1;
						player.getProjectile().createPlayersProjectile(nX, nY, offX, offY, 50,
								NPCCombatData.getProjectileSpeed(npc), npc.projectileId,
								NPCCombatData.getProjectileStartHeight(npc.npcId, npc.projectileId),
								NPCCombatData.getProjectileEndHeight(npc.npcId, npc.projectileId), -player.getId() - 1, 65);
					}
				}
				player.lastAttacker = npc;
				player.lastWasHitTime = System.currentTimeMillis();
				player.singleCombatDelay2.reset();
				npc.oldIndex = player.getIndex();
				player.updateLastCombatAction();
				player.setInCombat(true);
				player.getActionSender().sendRemoveInterfacePacket();
			}
		}
	}

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	/**
	 * Checks if the attack is okay on the player
	 * 
	 * @param player
	 *            The {@link Player} being attacked
	 * @param npc
	 *            The {@link NPC} attacking the player
	 * @return If the npc can attack the player
	 */
	private static boolean validateAttack(Player player, NPC npc) {
		if (npc.isDead || player.isDead()) {
			return false;
		}
		
		if (npc.npcId == 6617) {
			return false;
		}
		if (!player.isVisible()) {
			return false;
		}
		if (npc.npcId != 5535 && npc.npcId != 494) { // small tent and kraken can attack in single
			if (!npc.inMulti() && npc.underAttackBy > 0 && npc.underAttackBy != player.getIndex()) {
				npc.targetId = 0;
				return false;
			}
			if (!npc.inMulti()) {
				if ((npc.lastAttacker != player && Combat.hitRecently(npc, 4000)) || npc != player.lastAttacker && Combat.hitRecently(player, 4000)) {
					npc.targetId = 0;
					return false;
				}
			}
		}
		/*
		 * This doesn't work.
		 */
		if (npc.heightLevel != player.heightLevel) {
			npc.targetId = 0;
			return false;
		}

		player.combatCountdown = 10;
		if (player.inTutorial()) {
			npc.targetId = 0;
			return false;
		}
		if (PlayerVsNpcCombat.canTouch(player, npc, false)) {
			return true;
		}

		boolean ignoreClip = npc.getId() == 494 || npc.getId() == 492 || npc.getId() == 5862 || npc.getId() == 493
				|| npc.getId() == 496 || npc.getId() == 2054 || npc.getId() == 5947 || npc.getId() == 2215 || npc.getId() == 2218 
				|| npc.getId() == 2217 || npc.getId() == 2216;
		if (ignoreClip) {
			return true;
		}

		// Always last
		if (npc.getCombatType() != CombatStyle.MELEE) {
			for (Position pos : npc.getBorder()) {
				if (ProjectilePathFinder.isProjectilePathClear(player.getPosition(), pos)) {
					return true;
				}
			}
		} else {
			for (Position pos : npc.getBorder()) {
				if (ProjectilePathFinder.isInteractionPathClear(player.getPosition(), pos)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Applies damage to the target, for non boss characters.
	 * 
	 * @param npc
	 *            The {@link NPC} attacking the player
	 */
	public static void executeDamage(NPC npc) {
		
		if (npc != null) {
			if (World.getWorld().getPlayers().get(npc.oldIndex) == null) {
				return;
			}

			Player player = World.getWorld().getPlayers().get(npc.oldIndex);

			if (npc.isDead || player == null || player.isDead()) {
				return;
			}
			// Autoretal
			if (player.getCombat().noTarget()) {
				if (player.isAutoRetaliating()) {
					player.getCombat().setTarget(npc);
				}
			}
			// block anim
			if (player.attackDelay <= 3 || player.attackDelay == 0) {
				player.playAnimation(Animation.create(CombatAnimation.getDefendAnimation(player)));
			}

			int damage = Utility.getRandom(npc.getDefinition().getMaxHit());
			// Set up a Hit instance
            Hit hitInfo = player.take_hit(npc, damage, npc.getCombatType(), false);

            // apply damage - call this, change the 'delay' param to whatever you want the delay to be
            // and the method submits the Event
            Combat.hitEvent(npc, player, 1, hitInfo, npc.getCombatType());
		}
	}

}