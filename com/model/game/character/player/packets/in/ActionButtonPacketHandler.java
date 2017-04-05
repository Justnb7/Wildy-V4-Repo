package com.model.game.character.player.packets.in;

import com.model.Server;
import com.model.game.character.Animation;
import com.model.game.character.Graphic;
import com.model.game.character.combat.PrayerHandler;
import com.model.game.character.combat.magic.SpellBook;
import com.model.game.character.combat.weapon.AttackStyle.FightType;
import com.model.game.character.combat.weaponSpecial.SpecialAttackHandler;
import com.model.game.character.player.EmotesManager.EmoteData;
import com.model.game.character.player.Player;
import com.model.game.character.player.content.ItemOnDeath;
import com.model.game.character.player.content.clan.ClanManager;
import com.model.game.character.player.content.multiplayer.MultiplayerSessionStage;
import com.model.game.character.player.content.multiplayer.MultiplayerSessionType;
import com.model.game.character.player.content.multiplayer.duel.DuelSession;
import com.model.game.character.player.content.multiplayer.duel.DuelSessionRules.Rule;
import com.model.game.character.player.content.music.MusicData;
import com.model.game.character.player.content.questtab.QuestTabPage;
import com.model.game.character.player.content.questtab.QuestTabPageHandler;
import com.model.game.character.player.content.questtab.QuestTabPages;
import com.model.game.character.player.content.teleport.TeleportHandler;
import com.model.game.character.player.content.teleport.TeleportHandler.TeleportationTypes;
import com.model.game.character.player.content.teleport.Teleports;
import com.model.game.character.player.minigames.BarrowsFull.CorrectFirstClickButton;
import com.model.game.character.player.packets.PacketType;
import com.model.game.character.player.packets.buttons.ActionButtonEventListener;
import com.model.game.character.player.packets.out.*;
import com.model.game.character.player.skill.fletching.Fletching;
import com.model.game.item.bank.BankItem;
import com.model.game.item.bank.BankTab;
import com.model.utility.Utility;
import com.model.utility.json.definitions.ItemDefinition;

import java.util.Objects;

/**
 * Handles clicking on most buttons in the interface.
 * 
 * @author Patrick van Elderen
 * 
 */
public class ActionButtonPacketHandler implements PacketType {

	@Override
	public void handle(final Player player, int id, int size) {
		int button = Utility.hexToInt(player.getInStream().buffer, 0, size);
		
		DuelSession duelSession = null;
		
		if (player.isDead()) {
			return;
		}
		
		if (player.inDebugMode()) {
			System.out.printf("ActionButtonPacket: button %d - packet %d - packetSize %d%n", button, id, size);
		}
		
		CorrectFirstClickButton.handleButton(player, button);
		
		
		if (button == 165179) {
			player.getGameModeSelection().confirm(player);
			return;
		}
		
		if (button >= 165162 && button <= 165165) {
			player.getGameModeSelection().selectMode(player, button);
			return;
		}
		
		if (player.inTutorial() && button != 9178 && button != 9179 && button != 9180 && button != 9181
				&& button != 14067 && button != 9154) {
			return;
		}

		if (player != null) {
			EmoteData.useBookEmote(player, button);
		}
		
		// First verify this button is something even remotely related to teleports
		if(Teleports.isTeleportButton(player, button)) {
			// Activate a teleport for that button
			Teleports.startTeleport(player, button);
			return;
		}
		
		if (SpecialAttackHandler.handleButtons(player, button)) {
			return;
		}
		
		if (FightType.setStyle(player, button)) {
			return;
		}
		
		if (ClanManager.handleButtons(player, button)) {
			return;
		}
		
		for (int fletchingButton = 0; fletchingButton < Fletching.otherButtons.length; fletchingButton++) {
			if (button == Fletching.otherButtons[fletchingButton][0]) {
				Fletching.handleFletchingClick(player, button);
				return;
			}
		}
		player.getPestControlRewards().click(button);
		/*Obelisks.chooseTeleport(player, button);*/
		PrayerHandler.togglePrayer(player, button);
		player.getLunarSpell().processLunarSpell(button);
		QuestTabPage page = player.getAttribute(QuestTabPageHandler.QUEST_TAB_PAGE, QuestTabPages.HOME_PAGE).getPage();
		page.onButtonClick(player, button);
		// AttackStyle.switchAttackStyle(player, buttonId);
		ActionButtonEventListener.onButtonClick(player, button);
		switch (button) {
		
		case 114220:
			player.getAchievements().drawInterface(0);
			break;
			
		case 191109:
			player.getAchievements().drawInterface(0);
			break;
			
		case 191112:
			player.getAchievements().drawInterface(1);
			break;
			
		case 191115:
			player.getAchievements().drawInterface(2);
			break;
		
		case 222174:
			TeleportHandler.open(player, TeleportationTypes.SKILLING);
			break;
		case 222178:
			TeleportHandler.open(player, TeleportationTypes.PVP);
			break;
		case 222182:
			TeleportHandler.open(player, TeleportationTypes.PVM);
			break;
		case 222186:
			TeleportHandler.open(player, TeleportationTypes.MINIGAME);
			break;
		case 222195:
			TeleportHandler.teleport(player);
			break;
		case 222219:
		case 222223:
		case 222227:
		case 222231:
		case 222235:
		case 222239:
		case 222243:
		case 222247:
		case 222251:
		case 222255:
		case 223003:
		case 223007:
		case 223011:
		case 223015:
		case 223019: 
		case 223023:
		case 223027:
		case 223031:
		case 223035:
		case 223039:
		case 223043:
		case 223047:
			TeleportHandler.select(player, button);
			break;
		
		/**
		 * Exp counter 'reset exp'
		 */
		case 1219:
			player.getSkills().setExpCounter(0);
			player.getActionSender().sendExperienceCounter(0, 0);
			player.getActionSender().sendMessage("You have reset your experience counter to zero.");
			break;

		case 114230:
			if(player.getArea().inWild()) {
				return;
			}
			if (player.onAuto) {
				player.getActionSender().sendMessage("You can't switch spellbooks with Autocast enabled.");
				return;
			}
			switch (player.getSpellBook()) {
			case MODERN:
				player.setSpellBook(SpellBook.ANCIENT);
				player.write(new SendSidebarInterfacePacket(6, 12855));
				player.getActionSender().sendMessage("An ancient wisdom fills your mind.");
				break;
			case ANCIENT:
				player.setSpellBook(SpellBook.LUNAR);
				player.write(new SendSidebarInterfacePacket(6, 29999));
				player.getActionSender().sendMessage("The power of the moon overpowers you.");
				break;
			case LUNAR:
				player.setSpellBook(SpellBook.MODERN);
				player.write(new SendSidebarInterfacePacket(6, 1151));
				player.getActionSender().sendMessage("You feel a drain on your memory.");
				break;
			}
			player.autocastId = -1;
			player.getPA().resetAutoCast();
			player.onAuto = false;
			break;

		case 114226:
			QuestTabPageHandler.write(player, QuestTabPages.HOME_PAGE);
			player.getActionSender().sendMessage("You refresh your information tab.");
			break;

		case 19137:
			player.write(new SendSidebarInterfacePacket(5, 17200));
			break;

		case 104078:
			player.write(new SendSidebarInterfacePacket(3, 3213));
			break;
			

		/** Equipment screen */
		case 108005:
			player.write(new SendInterfacePacket(15106));
			break;

		case 108006:
			ItemOnDeath.activateItemsOnDeath(player);
			break;

		case 108020:
			break;

		case 42210:
		case 90163:
		case 55096:
		case 86019:
		case 140162:
		case 162235:
		case 222170:
			player.getActionSender().sendRemoveInterfacePacket();
			break;
			
		case 55095:
			player.getPA().handleDestroyItem();
			break;

		/*
		 * Bank Searching
		 */
		case 226162:
			if (!player.isBanking())
				return;
			if (System.currentTimeMillis() - player.lastBankDeposit < 500)
				return;
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			player.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < player.playerItems.length; slot++) {
				if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
					player.getItems().addToBank(player.playerItems[slot] - 1, player.playerItemsN[slot], false);
				}
			}
			player.getItems().updateInventory();
			player.getItems().resetBank();
			player.getItems().resetTempItems();
			break;

		case 226170:
			if (!player.isBanking())
				return;
			if (System.currentTimeMillis() - player.lastBankDeposit < 250)
				return;
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			player.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < player.playerEquipment.length; slot++) {
				if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
					if (!player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
							player.playerEquipmentN[slot], false))
						break;
				}
			}
			player.getItems().updateInventory();
			player.getItems().resetBank();
			player.getItems().resetTempItems();
			break;

		case 226186:
		case 226198:
		case 226209:
		case 226220:
		case 226231:
		case 226242:
		case 226253:
		case 227008:
		case 227019:
			if (!player.isBanking()) {
				player.getActionSender().sendRemoveInterfacePacket();
				return;
			}
			int tabId = button == 226186 ? 0
					: button == 226198 ? 1
							: button == 226209 ? 2
									: button == 226220 ? 3
											: button == 226231 ? 4
													: button == 226242 ? 5
															: button == 226253 ? 6
																	: button == 227008 ? 7
																			: button == 227019 ? 8 : -1;
			if (tabId <= -1 || tabId > 8)
				return;
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset(tabId);
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			BankTab tab = player.getBank().getBankTab(tabId);
			if (tab.getTabId() == player.getBank().getCurrentBankTab().getTabId())
				return;
			if (tab.size() <= 0 && tab.getTabId() != 0) {
				player.getActionSender().sendMessage("Drag an item into the new tab slot to create a tab.");
				return;
			}
			player.getBank().setCurrentBankTab(tab);
			player.getPA().openBank();
			break;

		case 226197:
		case 226208:
		case 226219:
		case 226230:
		case 226241:
		case 226252:
		case 227007:
		case 227018:
			if (!player.isBanking()) {
				player.getActionSender().sendRemoveInterfacePacket();
				return;
			}
			tabId = button == 226197 ? 1
					: button == 226208 ? 2
							: button == 226219 ? 3
									: button == 226230 ? 4
											: button == 226241 ? 5
													: button == 226252 ? 6
															: button == 227007 ? 7 : button == 227018 ? 8 : -1;
			tab = player.getBank().getBankTab(tabId);
			if (tab == null || tab.getTabId() == 0 || tab.size() == 0) {
				player.getActionSender().sendMessage("You cannot collapse this tab.");
				return;
			}
			if (tab.size() + player.getBank().getBankTab()[0].size() >= player.BANK_SIZE) {
				player.getActionSender().sendMessage("You cannot collapse this tab. The contents of this tab and your");
				player.getActionSender().sendMessage("main tab are greater than " + player.BANK_SIZE + " unique items.");
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			for (BankItem item : tab.getItems()) {
				player.getBank().getBankTab()[0].add(item);
			}
			tab.getItems().clear();
			if (tab.size() == 0) {
				player.getBank().setCurrentBankTab(player.getBank().getBankTab(0));
			}
			player.getPA().openBank();
			break;

		case 226185:
		case 226196:
		case 226207:
		case 226218:
		case 226229:
		case 226240:
		case 226251:
		case 227006:
		case 227017:
			if (!player.isBanking()) {
				player.getActionSender().sendRemoveInterfacePacket();
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			tabId = button == 226185 ? 0
					: button == 226196 ? 1
							: button == 226207 ? 2
									: button == 226218 ? 3
											: button == 226229 ? 4
													: button == 226240 ? 5
															: button == 226251 ? 6
																	: button == 227006 ? 7
																			: button == 227017 ? 8 : -1;
			tab = player.getBank().getBankTab(tabId);
			long value = 0;
			if (tab == null || tab.size() == 0)
				return;
			for (BankItem item : tab.getItems()) {
				ItemDefinition itemDef = ItemDefinition.forId(item.getId() - 1);
				long tempValue = (long) (item.getId() - 1 == 995 ? 1 : itemDef.getGeneralPrice());
				value += tempValue * item.getAmount();
			}
			player.getActionSender().sendMessage("<col=255>The total networth of tab " + tab.getTabId()
					+ " is </col><col=600000>" + Utility.insertCommas(Long.toString(value)) + " gp</col>.");
			break;

		case 22024:
		case 86008:
			player.getPA().openBank();
			break;

		case 9190:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(1)) {
					break;
				}
			}
			break;
		case 9191:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(2)) {
					break;
				}
			}
			break;

		case 9192:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(3)) {
					break;
				}
			}
			break;

		case 9193:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(4)) {
					break;
				}
			}
			break;
			
		case 9194:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(5)) {
					break;
				}
			}
			break;

		case 9167:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(1)) {
					break;
				}
			}
			break;

		case 9168:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(2)) {
					break;
				}
			}
			break;

		case 9169:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(3)) {
					break;
				}
			}
			break;

		case 9178:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(1)) {
					break;
				}
			}
			break;

		case 9179:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(2)) {
					break;
				}
			}
			break;

		case 9180:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(3)) {
					break;
				}
			}
			break;

		case 9181:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(4)) {
					break;
				}
			}
			break;

		case 9157:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(1)) {
					break;
				}
			}
			break;

		case 9158:
			if (player.dialogue().isActive()) {
				if (player.dialogue().select(2)) {
					break;
				}
			}
			break;

		case 4169: // god spell charge
			player.usingMagic = true;
			if (player.getCombat().checkMagicReqs(48)) {
				if (System.currentTimeMillis() - player.godSpellDelay < 300000L) {
					player.getActionSender().sendMessage("You still feel the charge in your body!");
				} else {
					player.godSpellDelay = System.currentTimeMillis();
					player.getActionSender().sendMessage("You feel charged with a magical power!");
					player.playGraphics(Graphic.create(player.MAGIC_SPELLS[48][3], 0, 0));
					player.playAnimation(Animation.create(player.MAGIC_SPELLS[48][2]));
					player.usingMagic = false;
				}
			}
			break;

		case 9154:
			player.logout();
			break;

		case 105230:
			player.getActionSender().sendRemoveInterfacePacket();
			break;

		case 226154:
			player.takeAsNote = !player.takeAsNote;// rerun that
			break;

		case 39178:
			player.playAnimation(Animation.create(65535));
			player.getActionSender().sendRemoveInterfacePacket();
			break;

		case 59004:
			player.getActionSender().sendRemoveInterfacePacket();
			break;

		case 70212:
			player.write(new SendInterfacePacket(18300));
			break;

		/** Settings */
		case 140188:
			player.getActionSender().sendMessage(":updateSettings:");
			player.write(new SendSidebarInterfacePacket(11, 28400));
			break;
		case 110245:
			player.getActionSender().sendMessage(":saveSettings:");
			player.write(new SendSidebarInterfacePacket(11, 36000));
			player.getActionSender().sendMessage("@red@Your settings have been saved!");
			break;
		case 110248:
			player.getActionSender().sendMessage(":defaultSettings:");
			player.getActionSender().sendMessage("@red@Your settings have been reset!");
			break;
		case 140191:
			player.write(new SendInterfacePacket(28200));
			break;
		case 110046:
			player.getActionSender().sendMessage(":transparentTab:");
			break;
		case 110047:
			player.getActionSender().sendMessage(":transparentChatbox:");
			break;
		case 110048:
			player.getActionSender().sendMessage(":sideStones:");
			break;

		case 4026:
			player.setRunning(!player.isRunning());
			//player.getActionSender().sendConfig(152, player.isRunning() ? 1 : 0));
			break;

		case 3138:
			player.setScreenBrightness((byte) 1);
			break;
		case 3140:
			player.setScreenBrightness((byte) 2);
			break;
		case 3142:
			player.setScreenBrightness((byte) 3);
			break;
		case 3144:
			player.setScreenBrightness((byte) 4);
			break;

		case 140181:
			player.setAcceptAid(!player.getAcceptAid());
			player.getActionSender().sendConfig(200, player.getAcceptAid() ? 1 : 0);
			break;	
			
		case 140185:
			player.setSplitPrivateChat(!player.getSplitPrivateChat());
			player.getActionSender().sendConfig(287, player.getSplitPrivateChat() ? 1 : 0);
			player.getActionSender().sendConfig(205, player.getSplitPrivateChat() ? 1 : 0);
			break;
			
		case 140186:
			player.setEnableSound(!player.isEnableSound());
			player.getActionSender().sendConfig(206, player.isEnableSound() ? 1 : 0);
			player.getActionSender().sendMessage(String.format("You have %s sound effects.", player.isEnableSound() ? "enabled" : "disabled"));
			break;
			
		case 140187:
			player.setEnableMusic(!player.isEnableMusic());
			player.getActionSender().sendConfig(207, player.isEnableMusic() ? 1 : 0);
			if(player.isEnableMusic()) {
				player.getActionSender().sendMessage("You've enabled your music player.");
				MusicData.playMusic(player);
			} else if(!player.isEnableMusic()) {
				player.getActionSender().sendMessage("You've disabled your music player.");
				player.write(new SendSongPacket(-1));
			}
			break;

		case 89061:
			System.out.println("tick: " +player.isAutoRetaliating());
			player.setAutoRetaliating(!player.isAutoRetaliating());
			player.getCombat().reset();
			break;
			
		case 7217:
		case 7212:
		case 24017:
			player.getPA().resetAutoCast();
			player.getWeaponInterface().sendWeapon(player.playerEquipment[player.getEquipment().getWeaponId()], ItemDefinition.forId(player.playerEquipment[player.getEquipment().getWeaponId()]).getName());
			break;
			
		case 1093:
		case 1094:
		case 1097:
			player.onAuto = true;
			if (player.autocastId > 0) {
				player.getPA().resetAutoCast();
			} else {
				if (player.getSpellBook() == SpellBook.ANCIENT) {
					player.write(new SendSidebarInterfacePacket(0, 1689));
				} else if (player.getSpellBook() == SpellBook.MODERN) {
					player.write(new SendSidebarInterfacePacket(0, 12050));
				}
			}
			break;
			
			/** Dueling **/
			case 26065:
			case 26040:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.FORFEIT);
				break;
			case 26066: // no movement
			case 26048:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				if (!duelSession.getRules().contains(Rule.FORFEIT)) {
					duelSession.toggleRule(player, Rule.FORFEIT);
				}
				duelSession.toggleRule(player, Rule.NO_MOVEMENT);
				break;

			case 26069: // no range
			case 26042:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_RANGE);
				break;

			case 26070: // no melee
			case 26043:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_MELEE);
				break;

			case 26071: // no mage
			case 26041:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_MAGE);
				break;

			case 26072: // no drinks
			case 26045:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_DRINKS);
				break;

			case 26073: // no food
			case 26046:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_FOOD);
				break;

			case 26074: // no prayer
			case 26047:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_PRAYER);
				break;

			case 26076: // obsticals
			case 26075:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.OBSTACLES);
				break;

			case 2158: // fun weapons
			case 2157:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				if (duelSession.getRules().contains(Rule.WHIP_AND_DDS)) {
					duelSession.toggleRule(player, Rule.WHIP_AND_DDS);
					return;
				}
				if (!Rule.WHIP_AND_DDS.getReq().get().meets(player)) {
					player.getActionSender().sendString("You must have a whip and dragon dagger to select this.", 6684);
					return;
				}
				if (!Rule.WHIP_AND_DDS.getReq().get().meets(duelSession.getOther(player))) {
					player.getActionSender().sendString("Your opponent does not have a whip and dragon dagger.", 6684);
					return;
				}
				if (duelSession.getStage().getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
					player.getActionSender().sendMessage("You cannot change rules whilst on the second interface.");
					return;
				}
				duelSession.getRules().reset();
				for (Rule rule : Rule.values()) {
					int index = rule.ordinal();
					if (index == 3 || index == 8 || index == 10 || index == 14) {
						continue;
					}
					duelSession.toggleRule(player, rule);
				}
				break;

			case 30136: // sp attack
			case 30137:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_SPECIAL_ATTACK);
				break;

			case 53245: // no helm
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_HELM);
				break;

			case 53246: // no cape
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_CAPE);
				break;

			case 53247: // no ammy
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_AMULET);
				break;

			case 53249: // no weapon
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_WEAPON);
				break;

			case 53250: // no body
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_BODY);
				break;

			case 53251: // no shield
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_SHIELD);
				break;

			case 53252: // no legs
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_LEGS);
				break;

			case 53255: // no gloves
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_GLOVES);
				break;

			case 53254: // no boots
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_BOOTS);
				break;

			case 53253: // no rings
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_RINGS);
				break;

			case 53248: // no arrows
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(player, Rule.NO_ARROWS);
				break;

			case 26018:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (System.currentTimeMillis() - player.getDuel().getLastAccept() < 1000) {
					return;
				}
				if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
					duelSession.accept(player, MultiplayerSessionStage.OFFER_ITEMS);
				}
				break;

			case 25120:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
				if (System.currentTimeMillis() - player.getDuel().getLastAccept() < 1000) {
					return;
				}
				if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
					duelSession.accept(player, MultiplayerSessionStage.CONFIRM_DECISION);
				}
				break;

		}
		if (player.isAutoButton(button)) {
			player.assignAutocast(button);
		}
	}
}
