package com.model.game.character.player.dialogue.impl.slayer.interfaceController;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import com.model.game.character.player.Player;
import com.model.game.character.player.packets.out.SendInterfacePacket;
import com.model.game.character.player.packets.out.SendMessagePacket;


/**
 * 
 * @author Harambe_
 * Class represents and handles the Slayer Interface
 *
 */
public class InterfaceController {
	
	enum Action {
		INTERFACE, CLOSE, CONFIRM;
	}
	
	public enum ButtonData {
		
		UNLOCK_INTERFACE(new int[] {91005, 90161, 91105, 89222}, 23400, Action.INTERFACE),
		SLAYER_XTEND_INTERFACE(new int[] {91006, 90162, 91106, 89223}, 23300, Action.INTERFACE),
		SLAYER_BUY_INTERFACE(new int[] {91007, 90163, 91107, 89224}, 23000, Action.INTERFACE),
		SLAYER_TASK_INTERFACE(new int[] {91008, 90164, 91108, 89225}, 23200, Action.INTERFACE),
		CLOSE(new int[]{89218}, 0, Action.CLOSE);
		//SLAYER_CONFIRM_INTERFACE(23100),
		private int[] button;
		private int interfaceId;
		Action action;
		private ButtonData(int[] button, int interfaceId, Action action){
			this.button = button;
			this.interfaceId = interfaceId;
			this.action = action;
		}
		
		public int[] getButton() {
			return button;
		}
		
		public int getInterface(){
			return interfaceId;
		}
		
		public Action getAction(){
			return action;
		}
		
		public static HashMap<Integer, ButtonData> buttonMap = new HashMap<Integer, ButtonData>();

		static {
			for (final ButtonData buttonData : ButtonData.values()) {
				for(final int button : buttonData.getButton()) {
					ButtonData.buttonMap.put(button, buttonData);
				}
			}

	}
	
}
	
	
	/**
	 * Opens the interface (not used yet)
	 * @param player
	 */
	public void open(Player player){
		player.write(new SendInterfacePacket(23200));
	}
	
	/**
	 * 
	 * @param player
	 * @param buttonId
	 * @return Handles the selection of the interface
	 */
	public static boolean selection(Player player, int buttonId) {
		ButtonData button = ButtonData.buttonMap.get(buttonId);
		if(button == null) {
			player.write(new SendMessagePacket("Null"));
			return false;
		}
		switch(button.getAction()) {
		case INTERFACE:
			player.write(new SendMessagePacket("Button: "+buttonId+" trying to open interface "+button.getInterface()));
			player.write(new SendInterfacePacket(button.getInterface()));
			return true;
		case CLOSE:
			player.getActionSender().closeAllWindows();
			return true;
		default:
			break;		
		}
		return false;
	}
}


