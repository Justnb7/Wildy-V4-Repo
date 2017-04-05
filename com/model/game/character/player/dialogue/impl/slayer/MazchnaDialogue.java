package com.model.game.character.player.dialogue.impl.slayer;

import com.model.game.character.npc.NPC;
import com.model.game.character.player.dialogue.Dialogue;
import com.model.game.character.player.dialogue.Expression;
import com.model.game.character.player.dialogue.Type;
import com.model.game.character.player.skill.slayer.Slayer;
import com.model.game.character.player.skill.slayer.SlayerMasters;
import com.model.game.character.player.skill.slayer.SlayerTaskManagement;

/**
 * The dialogue enacted by Mazchna the easy slayer master (Easy Tasks)
 * 
 * @author Patrick van Elderen
 *
 */
public class MazchnaDialogue extends Dialogue {
	
	private static final int NPC_ID = 402;
	
	@Override
	protected void start(Object... parameters) {
		if (player.getFirstSlayerTask()) {
			send(Type.NPC, NPC_ID, Expression.DEFAULT, "Hello brave warrior. What would you like?");
			setPhase(0);
		} else {
			player.getActionSender().sendMessage("You cannot talk to Mazchna as you are yet to start the 'Slayer' skill.");
			player.getActionSender().sendMessage("Talk to @blu@Turael@bla@ in Edgeville to do so.");
		}
		
	}
	
	@Override
	protected void next() {
		System.out.println("next : phase " + getPhase());
		if (getPhase() == 0) {
			send(Type.CHOICE, DEFAULT_OPTION_TITLE, "I would like an assignment.", "Do you have anything for trade?", "Uhm... nothing...");
			setPhase(1);
		} else {
			if (getPhase() == 2) {
				if (Slayer.hasTask(player)) {
					send(Type.NPC, NPC_ID, Expression.DEFAULT, "You already have an assignment,", "you can reset your task by talking to Nieve.");
					setPhase(3);
				} else if (!Slayer.hasTask(player) && Slayer.suitableMaster(player) == SlayerMasters.MAZCHNA) {
					SlayerTaskManagement.easyTask(player);
					send(Type.NPC, NPC_ID, Expression.DEFAULT, "Okay, your task is to kill " + player.getSlayerTaskAmount(), " @blu@" + NPC.getName(player.getSlayerTask()) + "s@bla@. Good luck " + player.getName() + ".");
					player.getActionSender().sendMessage("Remember you can use an Enchanted gem to remind you of your task.");
					setPhase(3);
				} else if (Slayer.suitableMaster(player) == SlayerMasters.TURAEL && !Slayer.hasTask(player)) {
					send(Type.NPC, NPC_ID, Expression.DEFAULT, "You are not strong enough to handle my assignments.", "Come back when you are a bit more experienced.");
					player.getActionSender().sendMessage("You need a combat level of 20 to get an assignment from Mazchna.");
					setPhase(3);
				} else if (Slayer.suitableMaster(player) == SlayerMasters.VANNAKA && !Slayer.hasTask(player)) {
					send(Type.NPC, NPC_ID, Expression.DEFAULT, "Someone of your strength should go and see Vannaka.", "Would you like to get an assignment from him?");
					setPhase(4);
				} else if (Slayer.suitableMaster(player) == SlayerMasters.CHAELDAR && !Slayer.hasTask(player)) {
					send(Type.NPC, NPC_ID, Expression.DEFAULT, "Someone of your strength should go and see Chaeldar.", "Would you like to get an assignment from her?");
					setPhase(4);
				} else if (Slayer.suitableMaster(player) == SlayerMasters.NIEVE && !Slayer.hasTask(player)) {
					send(Type.NPC, NPC_ID, Expression.DEFAULT, "Someone of your strength should go and see Nieve.", "Would you like to get an assignment from her?");
					setPhase(4);
				} else if (Slayer.suitableMaster(player) == SlayerMasters.DURADEL) {
					send(Type.NPC, NPC_ID, Expression.DEFAULT, "Someone of your strength should go and see Duradel.", "Would you like to get an assignemnt from him?");
					setPhase(4);
				}
			} else {
				if (getPhase() == 3) {
					stop();
				} else {
					if (getPhase() == 4) {
						send(Type.CHOICE, DEFAULT_OPTION_TITLE, "Yes, I'd like more of a challenge.", "No thanks, I'd like an assignment from you.");
						setPhase(5);
					} else {
						if (getPhase() == 6) {
							if (Slayer.suitableMaster(player) == SlayerMasters.VANNAKA) {
								send(Type.NPC, NPC_ID, Expression.DEFAULT, "Okay. Vannaka can be found in the midst of Edgeville", "dungeon. You can get there using the 'Teleport' option", "on an Enchanted gem.");
								setPhase(3);
							} else if (Slayer.suitableMaster(player) == SlayerMasters.CHAELDAR) {
								send(Type.NPC, NPC_ID, Expression.DEFAULT, "Okay. Chaeldar can be found in Fairy Land.", "You can get there using the 'Teleport' option", "on an Enchanted gem.");
								setPhase(3);
							} else if (Slayer.suitableMaster(player) == SlayerMasters.NIEVE) {
								send(Type.NPC, NPC_ID, Expression.DEFAULT, "Okay. Nieve can be found in Draynor Village.", "You can get there using the 'Teleport' option", "on an Enchanted gem.");
								setPhase(3);
							} else if (Slayer.suitableMaster(player) == SlayerMasters.DURADEL) {
								send(Type.NPC, NPC_ID, Expression.DEFAULT, "Okay. Duradel can be found in Burthorpe.", "You can get there by using the 'Teleport' option", "on your Enchanted gem.");
								setPhase(3);
							}
						} else {
							if (getPhase() == 7) {
								send(Type.NPC, NPC_ID, Expression.DEFAULT, "Okay fine. Your task is to kill " + player.getSlayerTaskAmount(), " @blu@" + NPC.getName(player.getSlayerTask()) + "s@bla@. Good luck " + player.getName());
								setPhase(3);
							} else {
								if (getPhase() == 8) {
									stop();
									player.getActionSender().sendMessage("The @blu@Rewards@bla@ store can be accessed by speaking to Nieve.");
								} else {
									if (getPhase() == 9) {
										SlayerTaskManagement.easyTask(player);
										send(Type.NPC, NPC_ID, Expression.DEFAULT, "Okay, fine. Your task", "is to kill " + player.getSlayerTaskAmount() + " @blu@" + NPC.getName(player.getSlayerTask()) + "s@bla@. Good luck " + player.getName() + ".");
										setPhase(3);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void select(int index) {
		System.out.println("Phase" + getPhase() + " index : " + index);
		if (getPhase() == 1) {
			if (index == 1) {
				send(Type.PLAYER, Expression.DEFAULT, "I would like an assignment.");
				setPhase(2);
			} else if (index == 2) {
				send(Type.PLAYER, Expression.DEFAULT, "Do you have anything for trade?");
				if (!player.getFirstSlayerTask()) {
					setPhase(3);
					player.getActionSender().sendMessage("You do not have access to the Slayer store as you have not started the 'Slayer' skill.");
					player.getActionSender().sendMessage("Talk to @blu@Turael@bla@ who is located in Edgeville to do.");
				} else {
					setPhase(8);
				}
			} else if (index == 3) {
				send(Type.PLAYER, Expression.DEFAULT, "Uhh... nothing...");
				setPhase(3);
			}
		} else {
			if (getPhase() == 5) {
				if (index == 1) {
					send(Type.PLAYER, Expression.DEFAULT, "Yes, I'd like more of a challenge.");
					setPhase(6);
				} else if (index == 2) {
					send(Type.PLAYER, Expression.DEFAULT, "No thanks, I'd like an assignment from you.");
					setPhase(9);
				}
			}
		}
	}
}