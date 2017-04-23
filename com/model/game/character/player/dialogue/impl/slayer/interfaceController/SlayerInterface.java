package com.model.game.character.player.dialogue.impl.slayer.interfaceController;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import com.model.game.character.player.Player;
import com.model.game.character.player.dialogue.impl.slayer.interfaceController.ExtendInterface.ExtendButtons;
import com.model.game.character.player.dialogue.impl.slayer.interfaceController.UnlockInterface.UnlockButtons;
import com.model.game.character.player.packets.out.SendInterfacePacket;
import com.model.game.character.player.packets.out.SendMessagePacket;
import com.model.utility.misc;



/**
 * 
 * @author Harambe_
 * Class represents and handles the Slayer Interface
 *
 */
public class SlayerInterface {
	

	enum Action {
		EXTEND_BUTTON, UNLOCK_BUTTON, INTERFACE, CLOSE, CONFIRM, BACK, EXTEND_INERFACE, PURCHASE;
	}
	
	public enum ButtonData {
		/**
		 * Basic core actions
		 */
		UNLOCK_INTERFACE(new int[] {91005, 90161, 91105, 89222}, 23400, Action.INTERFACE),
		SLAYER_EXTEND_INTERFACE(new int[] {91006, 90162, 91106, 89223}, 23300, Action.EXTEND_INERFACE),
		SLAYER_BUY_INTERFACE(new int[] {91007, 90163, 91107, 89224}, 23000, Action.INTERFACE),
		SLAYER_TASK_INTERFACE(new int[] {91008, 90164, 91108, 89225}, 23200, Action.INTERFACE),
		BACK(new int[]{90062}, getPreviousInterface(), Action.BACK),
		CLOSE(new int[]{89218}, 0, Action.CLOSE),
		PURCHSAE(new int[]{90063}, 0, Action.PURCHASE),
		
		
		/**
		 * Unlock Buttons
		 */
		GARGOYLE_SMASHER(new int[]{91110}, 0, Action.UNLOCK_BUTTON),
		SHROOM_SPRAYER(new int[]{91111}, 0, Action.UNLOCK_BUTTON),
		BROADER_FLETCHING(new int[]{91112}, 0, Action.UNLOCK_BUTTON),
		MALEVOLENT_MASQUERADE(new int[]{91113}, 0, Action.UNLOCK_BUTTON),
		RING_BLING(new int[]{91114}, 0, Action.UNLOCK_BUTTON),
		SEEING_RED(new int[]{91115}, 0, Action.UNLOCK_BUTTON),
		MITH_ME(new int[]{91116}, 0, Action.UNLOCK_BUTTON),
		BIRDIE(new int[]{91117}, 0, Action.UNLOCK_BUTTON),
		HOT_STUFF(new int[]{91118}, 0, Action.UNLOCK_BUTTON),
		REPTILE_GOT_RIPPED(new int[]{91119}, 0, Action.UNLOCK_BUTTON),
		LIKE_A_BOSS(new int[]{91120}, 0, Action.UNLOCK_BUTTON),
		KING_BLACK_BONNET(new int[]{91121}, 0, Action.UNLOCK_BUTTON),
		KALPHITE_KAT(new int[]{91122}, 0, Action.UNLOCK_BUTTON),
		UNHOLY_HELMET(new int[]{91123}, 0, Action.UNLOCK_BUTTON),
		BIGGER_AND_BADDER(new int[]{91124}, 0, Action.UNLOCK_BUTTON),
		DULY_NOTED(new int[]{91125}, 0, Action.UNLOCK_BUTTON),
		//SLAYER_CONFIRM_INTERFACE(23100),
		
		
		/**
		 * Extend Actions
		 */
		NEED_MORE_DARKNESS(new int[]{91010},0, Action.EXTEND_BUTTON),
		ANKOU_VERY_MUCH(new int[]{91011},0, Action.EXTEND_BUTTON),
		SUQ_A_NOTHER_ONE(new int[]{91012},0, Action.EXTEND_BUTTON),
		FIRE_AND_DARKNESS(new int[]{91013},0,  Action.EXTEND_BUTTON),
		PEDAL_TO_THE_METALS(new int[]{91014},0, Action.EXTEND_BUTTON),
		I_REALLY_MITH_YOU(new int[]{91015},0, Action.EXTEND_BUTTON),
		SPIRITUAL_FERVOUR(new int[]{91016},0, Action.EXTEND_BUTTON),
		BIRDS_OF_A_FEATHER(new int[]{91017},0, Action.EXTEND_BUTTON),	
		GREATER_CHALLENGE(new int[]{91018},0, Action.EXTEND_BUTTON),	
		ITS_DARK_IN_HERE(new int[]{91019},0, Action.EXTEND_BUTTON),	
		BLEED_ME_DRY(new int[]{91020},0, Action.EXTEND_BUTTON),	
		SMELL_YA_LATER(new int[]{91021},0, Action.EXTEND_BUTTON),	
		HORROIFIC(new int[]{91022},0, Action.EXTEND_BUTTON),	
		DUST_YOU_SHALL_RETURN(new int[]{91023},0, Action.EXTEND_BUTTON),	
		WYVER_ANOTHER_ONE(new int[]{91024},0, Action.EXTEND_BUTTON),	
		GET_SMASHED(new int[]{91025},0, Action.EXTEND_BUTTON),	
		NECHS_PLEASE(new int[]{91026},0, Action.EXTEND_BUTTON),	
		AUGMENT_MY_ABBIES(new int[]{91027},0, Action.EXTEND_BUTTON),	
		KRACK_ON(new int[]{91028}, 0, Action.EXTEND_BUTTON);	
		
		
		
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
	public static HashMap<Integer, String> unlocks = new HashMap<Integer, String>();

	/**
	 * Stores previous interface being viewed
	 */
	public static int prevInterfaceId = 23400;
	/**
	 * Gets the previous interface
	 */
	public static int getPreviousInterface() {
		return prevInterfaceId;
	}
	/**
	 * Sets the previous interface
	 */
	public static void setPreviousInterface(int interfaceId) {
		prevInterfaceId = interfaceId;
	}
	
	/**
	 * Opens the interface (not used yet)
	 * @param player
	 */
	public void open(Player player){
		UnlockInterface unlock = new UnlockInterface();
		unlock.write(player);
		player.write(new SendInterfacePacket(23400));
		
	}
	/**
	 * 
	 * @param player
	 * @param buttonId
	 * @return
	 * Handles unlocking of slayer additions
	 */
	public static boolean unlock(Player player, int buttonId){
		UnlockButtons button = UnlockButtons.unlockButtons.get(buttonId);
		if(button == null) {
			player.write(new SendMessagePacket("Null unlock button"));
			return false;
		}
		player.getActionSender().sendString(""+misc.optimizeText(button.getName()),23106); // make new classes for each tab
		player.getActionSender().sendString(""+button.getDescription(),23107);
		player.getActionSender().sendString("<col=ff0000>Are you sure you want to pay?</col>",23110);
		player.write(new SendInterfacePacket(23100));	
		return true;
	}
	/**
	 * 
	 * @param player
	 * @param buttonId
	 * @return
	 * Handles unlocking of slayer additions
	 */
	public static boolean extend(Player player, int buttonId){
		ExtendButtons button = ExtendButtons.extendButtons.get(buttonId);
		if(button == null) {
			player.write(new SendMessagePacket("Null extend button"));
			return false;
		}
		player.getActionSender().sendString(""+misc.optimizeText(button.getName()),23106); // make new classes for each tab
		player.getActionSender().sendString(""+button.getDescription(),23107);
		player.getActionSender().sendString("<col=ff0000>Are you sure you want to pay?</col>",23110);
		player.write(new SendInterfacePacket(23100));
		return true;
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
			setPreviousInterface(button.getInterface());
			return true;
		case PURCHASE:
			unlocks.put(player.getSlayerSelection(), player.getSlayerSelectionName());
			player.getActionSender().sendMessage("You successfully purchased X");
			player.write(new SendInterfacePacket(getPreviousInterface()));
			return true;
		case EXTEND_INERFACE:
			ExtendInterface unlock = new ExtendInterface();
			unlock.write(player);
			player.write(new SendInterfacePacket(button.getInterface()));
			return true;
		case UNLOCK_BUTTON:
			unlock(player, buttonId);
			player.setSlayerSelection(buttonId, button.name());
			player.write(new SendMessagePacket("Button: "+buttonId+" trying to open interface "+button.getInterface()));
			player.getActionSender().sendConfig(580+(button.ordinal() - 6), 0);
			return true;
		case EXTEND_BUTTON:
			extend(player, buttonId);
			player.setSlayerSelection(buttonId, button.name());
			player.write(new SendMessagePacket("Button: "+buttonId+" trying to open interface "+button.getInterface()));
			player.getActionSender().sendConfig(560+(button.ordinal() - 22), 0);
			return true;
		case CLOSE:
			player.getActionSender().closeAllWindows();
			return true;
		case BACK:
			player.write(new SendInterfacePacket(getPreviousInterface()));
			return true;
		default:
			break;		
		}
		return false;
	}
	
	
	
}




