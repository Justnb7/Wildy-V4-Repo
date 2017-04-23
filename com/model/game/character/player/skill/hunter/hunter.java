package com.model.game.character.player.skill.hunter;

import java.util.HashMap;

import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.npc.NPC;
import com.model.game.character.npc.NPCHandler;
import com.model.game.character.player.Player;
import com.model.task.events.CycleEvent;
import com.model.task.events.CycleEventContainer;
import com.model.task.events.CycleEventHandler;
import com.model.utility.misc;
/**
 * @author milan
 */

public class hunter {


		public enum impData {
			BABY_IMPLING(1645, 1, 25, 11238),
			YOUNG_IMPLING(1646, 22, 65, 11240),
			GOURMET_IMPLING(1647, 28, 113, 11242),
			EARTH_IMPLING(1648, 36, 177, 11244),
			ESSENCE_IMPLING(1649, 42, 255, 11246),
			ECLECTIC_IMPLING(1650, 50, 289, 11248),
			NATURE_IMPLING(1651, 58, 353, 11250),
			MAGPIE_IMPLING(1652, 65, 409, 11252),
			NINJA_IMPLING(1653, 74, 481, 11254),
			DRAGON_IMPLING(1654, 83, 553, 11256);

			public static HashMap<Integer, impData> implings = new HashMap<>();

			static {
				for(impData t : impData.values()) {
					implings.put(t.impType, t);
				}
			}

			private int impType;
			private int levelRequired;
			private int experience;
			private int itemId;

			impData(final int impType, final int levelRequired, final int experience, final int itemId) {
				this.impType = impType;
				this.levelRequired = levelRequired;
				this.experience = experience;
				this.itemId = itemId;
			}
			
			private int getImpId() {
				return impType;
			}
			
			private int getLevelRequired() {
				return levelRequired;
			}
			
			private int getXp() {
				return experience;
			}
			
			private int getJar() {
				return itemId;
			}
			
			private String getName() {
				return misc.optimizeText(toString().toLowerCase().replaceAll("_", " "));
			}
			
			public static impData forId(int id) {
				return implings.get(id);
			}
			/**
			 * 
			 * @param player
			 * @param npc
			 * Utilizing Entity Player and NPC we COULD call Player player but
			 * we have Entity system in the server so why not use it?
			 */
			
			//Check  firstClickNpc(NPC npc) { in actionhandler i left a note
		public static void Catch(Entity player, Entity npc) {
			impData t = impData.implings.get(npc.asNpc().getId());

			if (player.asPlayer().catchingImp) {
				return;
			}

			if (player.asPlayer().playerEquipment[player.asPlayer().playerWeapon] != 10010 && player.asPlayer().playerEquipment[player.asPlayer().playerWeapon] != 11259) {
				//Updated the sendmessage method
				player.asPlayer().getActionSender().sendMessage("You need a butterfly net");
				return;
			}

			if (player.asPlayer().playerLevel[22] < t.getLevelRequired()) {
				player.asPlayer().getActionSender().sendMessage("You need a hunter of " + t.getLevelRequired() + "To catch this imp");
				return;
			}

			if (!player.asPlayer().getItems().playerHasItem(11260)) {
				player.asPlayer().getActionSender().sendMessage("You need a impling jar");
				return;
			}

			player.asPlayer().catchingImp = true;
			if (misc.random(10) >= ((player.asPlayer().playerLevel[22] - 10) / 10) + 1) {
				//how the server handles animations
				player.asPlayer().playAnimation(Animation.create(6605));
				//player.asPlayer().playAnimation(6605);
				player.asPlayer().getActionSender().sendMessage("You fail catching the " + t.getName() + "!");
			} else {
				player.asPlayer().playAnimation(Animation.create(6605));
				player.asPlayer().getItems().addItem(t.getJar(), 1);
				player.asPlayer().getItems().deleteItem(11260, player.asPlayer().getItems().getItemSlot(11260), 1);
				player.asPlayer().getSkills().addExperience(t.getXp() * 50, 22);
				player.asPlayer().getActionSender().sendMessage("You catch the Imp");
				
			}
			player.asPlayer().catchingImp = false;
		}
			
			public static void IMPLING_RESPAWN(Entity player, Entity npc, int x, int y, int z) {
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					int i = 0;
					public void execute(CycleEventContainer container) {
						if(i++ == 1) {
							//NPCHandler.newNPC(id, x, y, z, 1);
							container.stop();
						}
					}

					public void stop() {
					}
				}, 2);
			}
			/*
			 * This method I can fix when i have time, i am exhausted
			 * but basically we need to remove the npc from the world. I 
			 * forgot how to do it i need to look into wildy v2 to see. 
			 */
			public static void IMPLING_DEATH(Entity player, Entity npc, int impling) {
				impData t = impData.implings.get(npc.asNpc().getId());
				NPC n = NPCHandler.npcs[impling];
				npc.asNpc();
				if(n != null && n.npcType == t.getImpId()) {
						int x = n.absX;
						int y = n.absY;
						int z = n.heightLevel;
						n.absX = 0;
						n.absY = 0;
						n.isDead = true;
						NPCHandler.npcs[impling] = null;
						IMPLING_RESPAWN(player, npc, x, y, z);
				}
			}

			public static void Catch(Player player, NPC npc, int impling) {
				// TODO Auto-generated method stub
				
			}
		}
	}
