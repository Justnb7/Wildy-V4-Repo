package com.model.task.impl;

import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.CRYPT_RAT_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.SKELETON_ID_4;

import com.model.Server;
import com.model.game.World;
import com.model.game.character.Animation;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.nvp.NPCCombatData;
import com.model.game.character.npc.GroupRespawn;
import com.model.game.character.npc.NPC;
import com.model.game.character.npc.NPCHandler;
import com.model.game.character.player.Player;
import com.model.game.character.player.content.music.sounds.MobAttackSounds;
import com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants;
import com.model.game.character.player.minigames.fight_caves.FightCaves;
import com.model.game.character.player.skill.slayer.SlayerTaskManagement;
import com.model.task.ScheduledTask;

/**
 * Handles respawning an {@link NPC} which has just died
 * 
 * @author Mobster
 * @author lare96 <http://github.com/lare96>
 * @author Patrick van Elderen
 */
public class NPCDeathTask extends ScheduledTask {

    /**
     * The time to tick while waiting to return the npc
     */
    private int counter;

    /**
     * Constructs a new npc death task which will reset the npc after its died
     * 
     * @param npc
     *            The {@link NPC} which has died
     */
    public NPCDeathTask(NPC npc) {
        super(1, true);
        attach(npc);
        npc.isDead = true;
    }

    @Override
    public void execute() {
        NPC npc = (NPC) getAttachment();

        if (npc == null || World.getWorld().getNpcs().get(npc.getIndex()) == null) {
            stop();
            return;
        }
        if (counter == 0) {
            initialDeath(npc);
            
        } else if (counter == 5) {
            npc.removeFromTile();
            setNpcToInvisible(npc);
            Player killer = World.getWorld().getPlayers().get(npc.spawnedBy);
            if (killer != null && killer.getKraken() != null && killer.getKraken().npcs != null && killer.getKraken().npcs[0] != null) {
            	if (npc == killer.getKraken().npcs[0]) {
            		for (NPC n : killer.getKraken().npcs) {
            			if (n.npcId == 5535) {
            				// kill off tents
            				n.isDead = true;
            				Server.getTaskScheduler().schedule(new NPCDeathTask(n));
            			}
            		}
            		// set next wave AFTER LOGIC ABOVE
    				killer.getKraken().spawnNextWave(killer);
            	}
			}
            if (killer != null || isUnspawnableNpc(npc) || !npc.shouldRespawn) {
				handleUnspawnableNpc(npc);
				removeMobFromWorld(killer, npc);
				stop();
				return;
			}
        } else if (counter == 6) {
        	if (GroupRespawn.check_groups(npc)) {
        		stop();
        		return;
        	}
        	if (npc.getDefinition().getRespawnTime() == -1) {// this npc does not respawn
        		stop();
        	}
        } else if (counter == (6 + npc.getDefinition().getRespawnTime())) { //60s later
            respawn(npc);
            GroupRespawn.on_boss_spawned(npc);
            stop();
            return;
        }
        counter++;
    }

    /**
     * Handles the unspawnable npcs upon death
     * 
     * @param npc
     *            The {@link NPC} that has died
     * @param killer
     *            The {@link Player} that has killed the npc
     */
    private void handleUnspawnableNpc(NPC npc) {

    }

    /**
     * Checks if the npc is unspawnable
     * 
     * @param npc
     *            The {@link NPC} to check if its unspawnable
     * @return If the npc is unspawnable
     */
    private boolean isUnspawnableNpc(NPC npc) {
        return NPCCombatData.getUnspawnableNpcs().contains(npc.getId());
    }

    /**
     * Removes custom spawned npc & minigame npc so they do not respawn
     * 
     * @param player
     *            The {@link Player} who has killed this npc
     * @param mob
     *            The {@link NPC} who died
     */
    private void removeMobFromWorld(Player player, NPC npc) {
        if (player != null && !player.isDead()) {}
        World.getWorld().unregister(npc);
    }

    /**
     * We set the npc to invisible and restore its values
     * 
     * @param npc
     */
    public static void setNpcToInvisible(NPC npc) {
        npc.removeFromTile();
        NPCHandler.dropItems(npc);
        npc.setVisible(false);
        npc.setAbsX(npc.makeX);
        npc.setAbsY(npc.makeY);
        npc.currentHealth = npc.maximumHealth;
		
        if (!npc.noDeathEmote) {
            npc.playAnimation(Animation.create(808));
        }
        
        npc.animUpdateRequired = true; // might not be needed but leave incase it breaks somet
        npc.resetDamageReceived();

        if (npc.npcId == 492 && npc.isDead) {
        	npc.requestTransform(493);
			npc.transformUpdateRequired = true;
        }
    }

    /**
     * The npc has just died, so we perform the death emote and reset facing
     * 
     * @param npc
     *            The {@link NPC} which as just died
     */
    private void initialDeath(NPC npc) {
        npc.updateRequired = true;
        npc.resetFace();

        String killerName = npc.getKiller();

        Player killer = World.getWorld().getPlayerByName(killerName);

        if (killer != null) {
            npc.killedBy = killer.getIndex();
        } else {
            npc.killedBy = -1;
        }
        if (npc.npcId == 6618 && npc.isDead) {
        	npc.forceChat("Ow!");
        }
        if (npc.npcId == 6615 && npc.isDead) {
        	npc.spawnedScorpiaMinions = false;
        }
        if(killer.getBarrows().isBarrowsNpc(npc.getId()) || npc.getId() >= BarrowsConstants.BLOODWORM_ID && npc.getId() <= SKELETON_ID_4){
        	//Barrows.killBarrowsNpc(killer, npc, true);
        	killer.getBarrows().getNpcController().registerKill(npc.getId());
        	killer.getActionSender().sendString("" + killer.getBarrows().getNpcController().getKillCount(), 16137);
        	
        }
       
		
        if (npc.npcId == 6611 && npc.transformId != 6612) {
			npc.requestTransform(6612);
			npc.transformUpdateRequired = true;
			npc.currentHealth = 255;
			npc.isDead = false;
			npc.spawnedVetionMinions = false;
			npc.forceChat("Do it again!!");
			stop();
			return;
		} else {
			if (npc.npcId == 6612) {
				npc.npcId = 6611;
				npc.spawnedVetionMinions = false;
				npc.forceChat("Got'em");
			}
		}
		if (npc.killedBy >= 0) {
			Player player = World.getWorld().getPlayers().get(npc.killedBy);
			if (player != null) {
				System.out.println("1: "+NPC.getName(player.getSlayerTask()));
				System.out.println("2 defin: "+npc.getDefinition().getName());
				if (npc.npcId == player.getSlayerTask() || NPC.getName(npc.npcId).toLowerCase().equalsIgnoreCase(NPC.getName(player.getSlayerTask()).toLowerCase())) {
					System.out.println("Were decreasin");
					SlayerTaskManagement.decreaseTask(player, npc);
				}
				Combat.resetCombat(player);
			}
		}

		if (npc.npcId == 6613 || npc.npcId == 6614) {
        	for (NPC i : World.getWorld().getNpcs()) {
        		if(i == null)
        			continue;
        		if(i.npcId == 6611 || i.npcId == 6612 && i.dogs > 0) {
        			i.dogs--;
        		}
        	}
        }
		
        if (npc.npcId != 6611) // vetion or somet
			npc.playAnimation(Animation.create(npc.getDeathAnimation())); // dead emote
        if (killer != null) {
        	FightCaves.sendDeath(killer, npc);
		}
        if (npc.transformId == 6612) {
			npc.requestTransform(6611);
			npc.transformUpdateRequired = true;
		}
       
        if (!npc.noDeathEmote) {
            npc.playAnimation(Animation.create(npc.getDeathAnimation())); // dead
        }
        
        if (killer != null) {
            MobAttackSounds.sendDeathSound(killer, npc.getId());
        }
        
        reset(npc);
    }
    
    public static void reset(NPC npc) {
    	npc.infected = false;
        npc.appearanceUpdateRequired = true;
        npc.freeze(0);
        npc.targetId = 0;
        npc.underAttack = false;
        npc.resetFace();
    }

    /**
     * The mob is respawned and set to visible to be updated again
     * 
     * @param mob2
     */
    public static void respawn(NPC npc) {
        npc.killedBy = -1;
        npc.setVisible(true);
        npc.isDead = false;
        npc.setOnTile(npc.absX, npc.absY, npc.heightLevel);
    }

}
