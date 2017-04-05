package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.player.Player;
import com.model.game.character.player.packets.out.SendWalkableInterfacePacket;

public class HideMiniMap {

	public static boolean toggle(Player client) {
		// 3520 9664
		
		// 3521 9728
		if ((client.absX >= 3521 && client.absX <= 3582) && (client.absY >= 9664 && client.absY <= 9728)) {
			client.getActionSender().disableMap(2);
			System.out.println("Here");
			//client.write(new SendWalkableInterfacePacket(BarrowsConstants.KILLCOUNT_WIDGET_ID));
			//client.getActionSender().sendString("Killcount: " + client.getBarrows().getNpcController().getKillCount(),
				//	BarrowsConstants.KILLCOUNT_TEXT_WIDGET_ID);
			return true;
		} else {
			client.getActionSender().disableMap(0);
			return false;
		}
	}
}