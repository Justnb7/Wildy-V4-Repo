package com.model.game.character.player.minigames.warriors_guild;

import com.model.game.character.npc.NPCHandler;
import com.model.game.character.player.Player;
import com.model.game.item.Item;
import com.model.game.item.ground.GroundItem;
import com.model.game.item.ground.GroundItemHandler;
import com.model.task.events.CycleEvent;
import com.model.task.events.CycleEventContainer;
import com.model.task.events.CycleEventHandler;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 20, 2013
 * 
 * @author Patrick van Elderen
 * @date June 11, 2016
 */
public class AnimatedArmour {

	private static final int SPAWN_X = 2851;
	private static final int SPAWN_Y = 3536;

	public enum Armour {
		
		BRONZE(2450, 1155, 1117, 1075, 5),
		IRON(2451, 1153, 1115, 1067, 10),
		STEEL(2452, 1157, 1119, 1069, 15),
		MITHRIL(2454, 1159, 1121, 1071, 50),
		ADAMANT(2455, 1161, 1123, 1073, 60),
		RUNE(2456, 1163, 1127, 1079, 80);
		
		int npcId, helm, platebody, platelegs, tokens;
		
		Armour(int npcId, int helm, int platebody, int platelegs, int tokens) {
			this.npcId = npcId;
			this.helm = helm;
			this.platebody = platebody;
			this.platelegs = platelegs;
			this.tokens = tokens;
		}
		
		public int getNpcId() {
			return npcId;
		}
		
		public int getHelmId() {
			return helm;
		}
		
		public int getPlatebodyId() {
			return platebody;
		}
		
		public int getPlatelegsId() {
			return platelegs;
		}
		
		public int getAmountOfTokens() {
			return tokens;
		}
	}
	
	private static Armour getArmourForItemId(int itemId) {
		for(Armour a : Armour.values())
			if(a.getHelmId() == itemId || a.getPlatebodyId() == itemId || a.getPlatelegsId() == itemId)
				return a;
		return null;
	}
	
	private static Armour getArmourForNpcId(int npcId) {
		for(Armour a : Armour.values())
			if(a.getNpcId() == npcId)
				return a;
		return null;
	}
	
	public static boolean isAnimatedArmourNpc(int npcId) {
		for (Armour armour : Armour.values()) {
			if (armour.npcId == npcId) {
				return true;
			}
		}
		return false;
	}
	
	public static void itemOnAnimator(final Player player, int itemId) {
		if(player.absX != 2851 || player.absY != 3537) {
			player.getActionSender().sendMessage("You need to move closer.");
			return;
		}
		if(player.isAnimatedArmourSpawned) {
			player.getActionSender().sendMessage("An Animated Armour npc is already spawned.");
			return;
		}
		final Armour armour = getArmourForItemId(itemId);
		if(!player.getItems().playerHasItem(armour.getPlatebodyId(), 1) || !player.getItems().playerHasItem(armour.getPlatelegsId(), 1) || !player.getItems().playerHasItem(armour.getHelmId(), 1)) {
			player.getActionSender().sendMessage("You need the helm, platebody, and platelegs to spawn the animated armour.");
			return;
		}
		player.isAnimatedArmourSpawned = true;
		player.getItems().deleteItem(armour.getPlatebodyId(), 1);
		player.getItems().deleteItem(armour.getPlatelegsId(), 1);
		player.getItems().deleteItem(armour.getHelmId(), 1);
		player.getPA().walkTo(0, +5);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			
			@Override
			public void execute(CycleEventContainer event) {
				NPCHandler.spawnNpc(player, armour.getNpcId(), SPAWN_X, SPAWN_Y, 0, 1, true, true, false);
				player.getActionSender().sendMessage("An animated armour has spawned...");
				event.stop();
			}
			
			@Override
			public void stop() {
				
			}
		}, 6);
	}

	public static void dropTokens(Player player, int npcType, int x, int y) {
		Armour npc = getArmourForNpcId(npcType);
		if(npc != null) {
			GroundItemHandler.createGroundItem(new GroundItem(new Item(8851, npc.getAmountOfTokens()), x, y, player.getHeight(), player));
			player.isAnimatedArmourSpawned = false;
		}
	}

}