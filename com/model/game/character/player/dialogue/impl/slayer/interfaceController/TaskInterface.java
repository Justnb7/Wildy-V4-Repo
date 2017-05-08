package com.model.game.character.player.dialogue.impl.slayer.interfaceController;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import com.model.game.character.player.dialogue.impl.slayer.interfaceController.SlayerConstants;
import com.model.game.character.player.dialogue.impl.slayer.interfaceController.SlayerInterface.ButtonData;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.game.character.player.packets.out.SendInterfacePacket;
import com.model.game.character.player.packets.out.SendMessagePacket;

/**
 * 
 * @author Harambe_ Class represents and handles the Slayer Interface
 *
 */
public class TaskInterface {
	@SuppressWarnings(value = {""})
	public void write(Player player) {
		try { 
		
		String currentTask = player.getSlayerTask() > 0
				? "" + NPC.getName(player.getSlayerTask()) + " X " + player.getSlayerTaskAmount() : "Nothing";
		player.getActionSender().sendString("" + currentTask, 23208); 

		for (int i = 0; i < 6; i++) {
				player.getActionSender().sendString("Empty", 23220 + i); 
				player.getActionSender().sendString("<col=-8434673>Unblock Task</col>", 23232 + i); 	
				player.getActionSender()
				.sendString(" " + NPC.getName(player.getSlayerInterface().getBlockedTasks().get(i)), 23220 + i);
		player.getActionSender().sendString("<col=ffa500>Unblock Task </col>", 23232 + i); 
			}
		

		} catch (Exception name) {
			
		}
		}

}
