package com.model.game.character.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.model.Server;
import com.model.game.Constants;
import com.model.game.World;
import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Hit;
import com.model.game.character.HitType;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.CombatAssistant;
import com.model.game.character.combat.CombatDamage;
import com.model.game.character.combat.PrayerHandler;
import com.model.game.character.combat.PrayerHandler.Prayers;
import com.model.game.character.combat.magic.LunarSpells;
import com.model.game.character.combat.magic.SpellBook;
import com.model.game.character.combat.range.Projectile;
import com.model.game.character.combat.weapon.AttackStyle;
import com.model.game.character.combat.weapon.WeaponInterface;
import com.model.game.character.npc.BossDeathTracker;
import com.model.game.character.npc.NPCAggression;
import com.model.game.character.npc.NPC;
import com.model.game.character.npc.SlayerDeathTracker;
import com.model.game.character.npc.pet.Pet;
import com.model.game.character.player.account.Account;
import com.model.game.character.player.account.ironman.GameModeSelection;
import com.model.game.character.player.content.FriendAndIgnoreList;
import com.model.game.character.player.content.achievements.AchievementHandler;
import com.model.game.character.player.content.clan.ClanMember;
import com.model.game.character.player.content.cluescrolls.ClueDifficulty;
import com.model.game.character.player.content.cluescrolls.ClueScrollContainer;
import com.model.game.character.player.content.consumable.Consumable;
import com.model.game.character.player.content.consumable.food.FoodConsumable;
import com.model.game.character.player.content.consumable.potion.PotionData;
import com.model.game.character.player.content.consumable.potion.Potions;
import com.model.game.character.player.content.multiplayer.MultiplayerSessionStage;
import com.model.game.character.player.content.multiplayer.MultiplayerSessionType;
import com.model.game.character.player.content.multiplayer.duel.Duel;
import com.model.game.character.player.content.multiplayer.duel.DuelSession;
import com.model.game.character.player.content.questtab.QuestTabPageHandler;
import com.model.game.character.player.content.questtab.QuestTabPages;
import com.model.game.character.player.content.teleport.TeleportHandler.TeleportationTypes;
import com.model.game.character.player.content.trade.TradeState;
import com.model.game.character.player.controller.Controller;
import com.model.game.character.player.controller.ControllerManager;
import com.model.game.character.player.dialogue.DialogueManager;
import com.model.game.character.player.instances.InstancedAreaManager;
import com.model.game.character.player.instances.impl.KrakenInstance;
import com.model.game.character.player.minigames.MinigameAttributes;
import com.model.game.character.player.minigames.BarrowsFull.Barrows;
import com.model.game.character.player.minigames.BarrowsFull.HideMiniMap;
import com.model.game.character.player.minigames.fight_caves.FightCaves;
import com.model.game.character.player.minigames.pest_control.PestControl;
import com.model.game.character.player.minigames.pest_control.PestControlRewards;
import com.model.game.character.player.minigames.warriors_guild.WarriorsGuild;
import com.model.game.character.player.packets.PacketEncoder;
import com.model.game.character.player.packets.out.SendSidebarInterfacePacket;
import com.model.game.character.player.packets.out.SendSkillPacket;
import com.model.game.character.player.packets.out.SendSoundPacket;
import com.model.game.character.player.skill.SkillCyclesTask;
import com.model.game.character.player.skill.SkillTask;
import com.model.game.character.player.skill.herblore.Herblore;
import com.model.game.character.player.skill.mining.Mining;
import com.model.game.character.player.skill.thieving.Thieving;
import com.model.game.character.walking.MovementHandler;
import com.model.game.item.Item;
import com.model.game.item.ItemAssistant;
import com.model.game.item.UseItem;
import com.model.game.item.bank.Bank;
import com.model.game.item.container.impl.Inventory;
import com.model.game.item.container.impl.LootingBagContainer;
import com.model.game.item.container.impl.RunePouchContainer;
import com.model.game.item.container.impl.TradeContainer;
import com.model.game.item.equipment.Equipment;
import com.model.game.item.ground.GroundItemHandler;
import com.model.game.location.Area;
import com.model.game.location.Position;
import com.model.game.shop.Currency;
import com.model.game.shop.Shop;
import com.model.net.network.Packet;
import com.model.net.network.rsa.GameBuffer;
import com.model.net.network.rsa.ISAACRandomGen;
import com.model.net.network.session.GameSession;
import com.model.task.ScheduledTask;
import com.model.task.impl.DistancedActionTask;
import com.model.utility.MutableNumber;
import com.model.utility.Stopwatch;
import com.model.utility.Utility;

import io.netty.buffer.Unpooled;

public class Player extends Entity {
	
	private final MutableNumber poisonImmunity = new MutableNumber();
	
	/**
     * Gets the poison immunity counter value.
     *
     * @return the poison immunity counter.
     */
    public MutableNumber getPoisonImmunity() {
        return poisonImmunity;
    }
    
    
	/**
	 * Godwars variables
	 * 
	 */

	public int bandosKillCount;
	public int zamorakKillCount;
	public int saradominKillCount;
	public int armadylKillCount;
	
	
	//Fletching variables
	public boolean isFletching = false, needsFletchDelay = false;
	public long lastFletch = 0;
	public int fletchDelay = -1, fletchAmount = -1, arrowShaft = 52, fletchItem = -1, fletchIndex = -1;
	public String fletchThis = "";
	public int[] fletchSprites = { -1, -1, -1, -1, -1 };
	
    private SkillCyclesTask skillCyclesTask = new SkillCyclesTask(this);
	
	public SkillCyclesTask getSkillCyclesTask() {
		return skillCyclesTask;
	}
	private final Barrows barrows = new Barrows(this);

	public Barrows getBarrows() {
		return barrows;
	}
    private Mining mining = new Mining(this);
	
	public Mining getMining() {
		return mining;
	}
	
	private Thieving thieving = new Thieving(this);
	
	public Thieving getThieving() {
		return thieving;
	}
	
	/**
	 * The account type
	 */
    private Account account;
	
    /**
     * Gets the account type, exampe Ironman Account
     * @return the accounts
     */
	public Account getAccount() {
		if (account == null)
			account = new Account(this);
		return account;
	}
	
	/**
     * Gets the container that holds the inventory items.
     *
     * @return the container for the inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }
    
    /**
     * The container that holds the inventory items.
     */
    private final Inventory inventory = new Inventory(this);
	
	private Duel duelSession = new Duel(this);
	
	public Duel getDuel() {
		return duelSession;
	}
	
	/**
	 * The players game mode, can either be PKER or TRAINED.
	 */
    private String gameMode = "PKER";
	
    /**
     * Gets the players game mode.
     * 
     * @return
     */
	public String getGameMode() {
		return gameMode;
	}
	
	/**
	 * Sets the players game mode.
	 * 
	 * @param gameMode
	 */
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * The player is still in the tutorial
	 */
    private boolean tutorial = true;
	
    /**
	 * Gets if the player is in the tutorial
	 * 
	 * @return
	 */
	public boolean inTutorial() {
		return tutorial;
	}
	
	/**
	 * Sets if the player is in the tutorial
	 * 
	 * @param tutorial
	 */
	public void setTutorial(boolean tutorial) { 
		this.tutorial = tutorial;
	}
	
	/**
	 * The player has received a starter pack
	 */
	private boolean receivedStarter;
	
	/**
	 * Gets if the player has received a starter kit
	 * 
	 * @return
	 */
	public boolean receivedStarter() {
		return receivedStarter;
	}

	/**
	 * Sets if the player has received a starter kit
	 * 
	 * @param received
	 */
	public void setReceivedStarter(boolean received) {
		this.receivedStarter = received;
	}
	
	/**
	 * Are we using special attack
	 */
	private boolean usingSpecial;
	
	public boolean isUsingSpecial() {
		return usingSpecial;
	}
	
	public void setUsingSpecial(boolean usingSpecial) {
		this.usingSpecial = usingSpecial;
	}
	
	/**
	 * Represents the amount donated
	 */
	private int amountDonated;
	
	public int getAmountDonated() {
		return amountDonated;
	}
	
	public void setAmountDonated(int amountDonated) {
		this.amountDonated = amountDonated;
	}
	
	/**
	 * Represents the total amount donated
	 */
	private int totalAmountDonated;
	
	public int getTotalAmountDonated() {
		return totalAmountDonated;
	}
	
	public void setTotalAmountDonated(int totalAmountDonated) {
		this.totalAmountDonated = totalAmountDonated;
	}
	
	/**
	 * Represents the players total death amount, (inside the wilderness)
	 */
	private int deathCount;
	
	public int getDeathCount() {
		return deathCount;
	}
	
	public void setDeathCount(int deathCount) {
		this.deathCount = deathCount;
	}
	
	/**
	 * Represents the players total kill amount, (inside the wilderness)
	 */
	private int killCount;
	
	public int getKillCount() {
		return killCount;
	}
	
	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}
	
	/**
	 * Represents the players current killstreak, (inside the wilderness)
	 */
	private int currentKillStreak;
	
	public int getCurrentKillStreak() {
		return currentKillStreak;
	}
	
	public void setCurrentKillStreak(int currentKillStreak) {
		this.currentKillStreak = currentKillStreak;
	}
	
	/**
	 * Represents the players highest killstreak, (inside the wilderness)
	 */
	private int highestKillStreak;
	
	public int getHighestKillStreak() {
		return highestKillStreak;
	}
	
	public void setHighestKillStreak(int highestKillStreak) {
		this.highestKillStreak = highestKillStreak;
	}
	
	/**
	 * Represents the players current wilderness killstreak
	 */
	private int wildernessKillStreak;
	
	public int getWildernessKillStreak() {
		return wildernessKillStreak;
	}
	
	public void setWildernessKillStreak(int wildernessKillStreak) {
		this.wildernessKillStreak = wildernessKillStreak;
	}
	
	/**
	 * Teleport to slayer task abilitie
	 */
	private boolean canTeleToTask;
	
	public boolean canTeleportToSlayerTask() {
		return canTeleToTask;
	}
	
	public void setCanTeleportToTask(boolean able_to_tele) {
		this.canTeleToTask = able_to_tele;
	}
	
	/**
	 * Representing the amount of completed slayer tasks
	 */
	private int slayerTasksCompleted;
	
	public int getSlayerTasksCompleted() {
		return slayerTasksCompleted;
	}
	
	public void setSlayerTasksCompleted(int slayerTasksCompleted) {
		this.slayerTasksCompleted = slayerTasksCompleted;
	}
	
	/**
	 * A reward for doing slayer tasks
	 */
	private int slayerPoints;
	
	public int getSlayerPoints() {
		return slayerPoints;
	}
	
	public void setSlayerPoints(int slayerPoints) {
		this.slayerPoints = slayerPoints;
	}
	
	/**
	 * A reward for killing players
	 */
	private int pkPoints;
	
	public int getPkPoints() {
		return pkPoints;
	}
	
	public void setPkPoints(int pkPoints) {
		this.pkPoints = pkPoints;
	}
	
	/**
	 * A reward for playing pest control
	 */
	private int pestPoints;
	
	public int getPestControlPoints() {
		return pestPoints;
	}
	
	public void setPestControlPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}
	
	/**
	 * Vote reward currency
	 */
	private int votePoints;
	
	public int getVotePoints() {
		return votePoints;
	}
	
	public void setVotePoints(int votePoints) {
		this.votePoints = votePoints;
	}
	
	/**
	 * Total times voted
	 */
	private int totalVotes;
	
	public int getTotalVotes() {
		return totalVotes;
	}
	
	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}
	
	/**
	 * Gear points are given every 5 minutes.
	 */
	private int gearPoints;
	
	public int getGearPoints() {
		return gearPoints;
	}
	
	public void setGearPoints(int gearPoints) {
		this.gearPoints = gearPoints;
	}
	
	/**
	 * The players current prayer points
	 */
	private double prayerPoint = 1.0;

	/**
	 * The players current active prayers
	 */
	private boolean[] activePrayer = new boolean[29];

	/**
	 * The players current prayer icon
	 */
	private int prayerIcon = -1;

	/**
	 * The prayer drain rate
	 */
	private double prayerDrainRate;
	
	/**
	 * Gets the active prayer
	 * 
	 * @param prayer
	 * @return
	 */
	public boolean isActivePrayer(Prayers prayer) {
		int index = prayer.getPrayerIndex(prayer);
		return activePrayer[index];
	}

	/**
	 * Returns the players active prayers
	 * 
	 * @return
	 */
	public boolean[] getPrayers() {
		return activePrayer;
	}

	/**
	 * Sets the active prayer
	 * 
	 * @param prayer
	 * @param active
	 * @return
	 */
	public Player setActivePrayer(Prayers prayer, boolean active) {
		int index = prayer.getPrayerIndex(prayer);
		this.activePrayer[index] = active;
		return this;
	}

	/**
	 * Gets the player icon
	 * 
	 * @return
	 */
	public int getPrayerIcon() {
		return prayerIcon;
	}

	/**
	 * Sets the prayer icon
	 * 
	 * @param icon
	 * @return
	 */
	public Player setPrayerIcon(int icon) {
		this.prayerIcon = icon;
		return this;
	}

	/**
	 * Gets the player drain rate
	 * 
	 * @return
	 */
	public double getPrayerDrainRate() {
		return prayerDrainRate;
	}

	/**
	 * Adds to the player drain rate
	 * 
	 * @param rate
	 * @return
	 */
	public Player addPrayerDrainRate(double rate) {
		this.prayerDrainRate += rate;
		return this;
	}

	/**
	 * Gets the players prayer points
	 * 
	 * @return
	 */
	public double getPrayerPoint() {
		return prayerPoint;
	}

	/**
	 * Sets the players prayer points
	 * 
	 * @param prayerPoint
	 */
	public void setPrayerPoint(double prayerPoint) {
		this.prayerPoint = prayerPoint;
	}
	
	/**
	 * Gets the attack style config
	 * 
	 * @return
	 */
	public int getAttackStyleConfig() {
		return attackStyleConfig;
	}

	/**
	 * Sets the attack style config
	 * 
	 * @param config
	 */
	public void setAttackStyleConfig(int config) {
		this.attackStyleConfig = config;
	}
	
	/**
	 * The attack style config
	 */
	private int attackStyleConfig;
	
	/**
	 * The attack style id.
	 */
	public int attackStyle;
	
	public int getAttackStyle() {
		return attackStyle;
	}

	public void setAttackStyle(int attackStyle) {
		this.attackStyle = attackStyle;
	}
	public void setLastDragonfireShieldAttack(long lastAttack) {
		this.lastDragonfireShieldAttack = lastAttack;
	}

	public long getLastDragonfireShieldAttack() {
		return lastDragonfireShieldAttack;
	}

	public boolean isDragonfireShieldActive() {
		return dragonfireShieldActive;
	}

	public void setDragonfireShieldActive(boolean dragonfireShieldActive) {
		this.dragonfireShieldActive = dragonfireShieldActive;
	}
	/**
	 * The playeers total special amount
	 */
	private int specialAmount = 100;
	
	/**
	 * Returns the players amount of special
	 * 
	 * @return
	 */
	public int getSpecialAmount() {
		return specialAmount;
	}

	/**
	 * Sets the players amount of special
	 * 
	 * @param amount
	 */
	public void setSpecialAmount(int amount) {
		this.specialAmount = amount;
	}
	
	/**
	 * Sets the players spell id
	 * 
	 * @param id
	 *            The id of the spell
	 */
	public void setSpellId(int id) {
		this.spellId = id;
	}

	/**
	 * Returns the players spell id
	 * 
	 * @return
	 */
	public int getSpellId() {
		return spellId;
	}
	
	/**
	 * The players spell Id
	 */
	public int spellId = -1;

	/**
	 * The players autocast Id
	 */
	public int autocastId = -1;
	
	/**
	 * The player is auto casting
	 */
	public boolean autoCast = false;
	public boolean onAuto = false;

	/**
	 * Constructs a new {@link LootingBag}.
	 */
	private final LootingBagContainer lootingbagContainer = new LootingBagContainer(this);
	
	/**
	 * @see {@link #lootingbagContainer}.
	 * <b>no point in documentating a getter</b>
	 */
	public LootingBagContainer getLootingBagContainer() {
		return lootingbagContainer;
	}
	
	/**
	 * Constructs a new {@link RunePouchContainer}.
	 */
	public final RunePouchContainer runePouchContainer = new RunePouchContainer(this);
	
	/**
	 * @see {@link #runePouchContainer}.
	 * <b>no point in documentating a getter</b>
	 */
	public RunePouchContainer getRunePouchContainer() {
		return runePouchContainer;
	}
	
	/**
	 * The player's spell book.
	 */
	public SpellBook spell = SpellBook.MODERN;
	
	public SpellBook getSpellBook() {
		return spell;
	}

	public void setSpellBook(SpellBook spell) {
		this.spell = spell;
	}
	
	private String yellColor = "ff0000";
	
	public String getYellColor() {
		return yellColor;
	}

	public void setYellColor(String yellColor) {
		this.yellColor = yellColor;
	}
	
	public boolean inDebugMode() {
		return debugMode;
	}
	
	public void setDebugMode(boolean on) {
		this.debugMode = on;
	}
	
	/**
	 * The player is in debug mode
	 */
	private boolean debugMode;

	/**
	 * The shop that you currently have open.
	 */
	private String openShop = "";
	
	/**
	 * The players death store
	 */
	public Shop deathShop = new Shop("Death Store", new Item[0], false, false, Currency.COINS);
	
	/**
	 * Does the player have the death store enabled
	 */
	public boolean deathShopEnabled = true;
	
	/**
	 * Using the death shop chat
	 */
	public boolean deathShopChat;

	
	/**
	 * Reload ground items.
	 * @param player
	 */
	public void reloadItems(Player player) {
		Server.getTaskScheduler().schedule(new ScheduledTask(4) {
			@Override
			public void execute() {
				GroundItemHandler.reloadGroundItems(player);
				this.stop();
			}
		});
	}

	/**
	 * Writes an encoded packet to the client
	 *
	 * @param encoder
	 *            the {@link PacketEncoder} to write to the client
	 */
	public void write(PacketEncoder encoder) {
		if (getOutStream() != null) {
			encoder.encode(this);
		}
	}

	private long xlogDelay;
	
	public int getId() {
		return getIndex();
	}
	
	/**
	 * The player's skill levels.
	 */
	public Skills skills = new Skills(this);
	
	public Skills getSkills() {
		return skills;
	}
	
	@Override
	public boolean isNPC() {
		return false;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}
	
	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}
	
	@Override
	public Position getCentreLocation() {
		return getPosition();
	}
	
	@Override
	public int getProjectileLockonIndex() {
		return -getIndex() - 1;
	}

	public long lastBankDeposit;

	public GameBuffer inStream = null, outStream = null;
	private GameSession session;

	public GameSession getSession() {
		return session;
	}

	public void setBountyPoints(int points) {
		this.bountyPoints = points;
	}

	public int getBountyPoints() {
		return bountyPoints;
	}
	
	private int recoil = 40;
	
	public int getRecoil() {
		return recoil;
	}

	public void setRecoil(int recoil) {
		this.recoil = recoil;
	}
	
    private int suffering = 0;
	
	public int getROSuffering() {
		return suffering;
	}

	public void setROSuffering(int suffering) {
		this.suffering = suffering;
	}

	public long teleblockLength;

	public boolean WithinDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public int[][] MAGIC_SPELLS = { { 1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0, 993 }, // wind
																												// strike
			{ 1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0, 211 }, // water
																					// strike
			{ 1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0, 0 }, // earth
																					// strike
			{ 1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0, 0 }, // fire
																						// strike
			{ 1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0, 0 }, // wind
																					// bolt
			{ 1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0, 0 }, // water
																						// bolt
			{ 1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0, 0 }, // earth
																						// bolt
			{ 1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0, 0 }, // fire
																						// bolt
			{ 1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0, 0 }, // wind
																						// blast
			{ 1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0, 0 }, // water
																						// blast
			{ 1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0, 0 }, // earth
																						// blast
			{ 1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0, 0 }, // fire
																						// blast
			{ 1183, 62, 727, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0, 0 }, // wind
																						// wave
			{ 1185, 65, 727, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0, 0 }, // water
																						// wave
			{ 1188, 70, 727, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0, 0 }, // earth
																						// wave
			{ 1189, 75, 727, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0, 0 }, // fire
																						// wave

			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0, 0 }, // stun

			{ 1572, 20, 710, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0, 0 }, // bind
			{ 1582, 50, 710, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0, 0 }, // snare
			{ 1592, 79, 710, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0, 0 }, // entangle

			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0, 0 }, // crumble
																						// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0, 0 }, // iban
																					// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0, 0 }, // magic
																						// dart

			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0, 0 }, // sara
																					// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0, 0 }, // cause
																					// of
																					// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0, 0 }, // flames
																					// of
																					// zammy

			{ 12445, 85, 1819, 0, 0, 1299, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1, 0 }, // smoke
																							// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1, 0 }, // shadow
																							// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0, 0 }, // blood
																						// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0, 0 }, // ice
																						// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2, 0 }, // smoke
																						// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2, 0 }, // shadow
																						// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0, 0 }, // blood
																						// burst
			{ 12265, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0, 0 }, // ice
																						// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2, 0 }, // smoke
																							// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2, 0 }, // shadow
																							// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0, 0 }, // blood
																						// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0, 0 }, // ice
																						// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4, 0 }, // smoke
																						// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3, 0 }, // shadow
																						// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0, 0 }, // blood
																						// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0, 0 }, // ice
																						// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0, 0 }, // low
																				// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0, 0 }, // high
																				// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0, 0 }, // telegrab
			{ -1, 75, 1167, 1251, 1252, 1253, 29, 35, 0, 0, 0, 0, 0, 0, 0, 0 }, // trident
																				// of
																				// the
																				// seas
			{ -1, 75, 1167, 665, 1040, 1042, 32, 35, 0, 0, 0, 0, 0, 0, 0, 0 }, // trident
																				// of
																				// the
																				// swamp
			{1337, 80, 2078, 145, 146, 147, 40, 1, 0, 0, 0, 0, 0, 0, 0, 0} //polypore 54
			// example {magicId, level req, animation, startGFX, projectile Id, endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2 amount, rune 3, rune 3 amount, rune 4, rune 4 amount}
			

			// blast
			// water
			// blast

	};
	
	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button) {
				return true;
			}
		}
		return false;
	}

	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
			51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0, 7039, 1,
			7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051,
			13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14, 47024, 15 };

	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Player c = World.getWorld().getPlayers().get(this.getIndex());
				autoCast = true;
				autocastId = autocastIds[j + 1];
				c.getActionSender().sendConfig(108, 1);
				c.write(new SendSidebarInterfacePacket(0, 328));
				break;
			}
		}
	}
	
	public int[] playerEquipment() {
		return playerEquipment;
	}
	
	private Equipment equipment = new Equipment();
	
	/**
	 * Gets the player's equipment.
	 * 
	 * @return The player's equipment.
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	public Set<Player> localPlayers = new LinkedHashSet<>(255);
	public Set<NPC> localNpcs = new LinkedHashSet<>(255);

	 /**
     * Gets the hash collection of the local players.
     *
     * @return the local players.
     */
    public Set<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * Gets the hash collection of the local npcs.
     *
     * @return the local npcs.
     */
    public Set<NPC> getLocalNpcs() {
        return localNpcs;
    }

    
	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel) {
			return false;
		}
		int deltaX = otherPlr.getX() - getX(), deltaY = otherPlr.getY() - getY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel) {
			return false;
		}
		if (!npc.isVisible()) {
			return false;
		}
		int deltaX = npc.getX() - getX(), deltaY = npc.getY() - getY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean destinationReached() {
		Position player = new Position(absX, absY, heightLevel);
		Position object = new Position(objectX, objectY, heightLevel);
		if (player.equals(object) || player.withinDistance(object, objectDistance)) {
			return true;
		} else {
			return false;
		}
	}

	private void hasDied() {
		Server.getTaskScheduler().schedule(new ScheduledTask(1, true) {

			@Override
			public void execute() {
				if (!isRegistered() || !isDead()) {
					stop();
					return;
				}
				switch (countdown) {
				case 0:
					setDead(true);
					getMovementHandler().resetWalkingQueue();
					break;
				case 1:
					playAnimation(Animation.create(0x900));
					poisonDamage = -1;
					infection = 0;
					infected = false;
					break;
				case 4:
					getPlayerDeath().characterDeath();
					break;
				case 5:
					getPlayerDeath().giveLife();
					setDead(false);
					stop();
					break;
				}
				countdown++;
			}

			@Override
			public void onStop() {
				setDead(false);
				countdown = 0;
			}
		}.attach(this));
	}

	private GameBuffer updateBlock = null;
	
	public void setForceMovement(int xx1, int yy1, int xx2, int yy2, int speedd1, int speedd2, int directionn) {
        this.x1 = xx1;
        this.y1 = yy1;
        this.x2 = xx2;
        this.y2 = yy2;
        this.speed1 = speedd1;
        this.speed2 = speedd2;
        this.direction = directionn;
        this.forceMovementUpdateRequired = true;
        this.updateRequired = true;
    }

	public void appendMask400Update(GameBuffer str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	public void clearUpdateFlags() {
		forceMovementUpdateRequired = false;
		updateRequired = false;
		appearanceUpdateRequired = false;
		chatTextUpdateRequired = false;
		setHitUpdateRequired(false);
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		this.gfxUpdateRequired = false;
		this.animUpdateRequired = false;
		setTeleporting(false);
		faceTileX = -1;
		faceTileY = -1;
		this.faceUpdateRequired = false;
		entityFaceIndex = 65535;
		setUpdateBlock(null);
		super.clear();
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}
	

	public void stopMovement() {
		getMovementHandler().reset();
	}

	public MovementHandler getMovementHandler() {
		return movementHandler;
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getZ() {
		return heightLevel;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setInStreamDecryption(ISAACRandomGen inStreamDecryption) {
	}

	public void setOutStreamDecryption(ISAACRandomGen outStreamDecryption) {
	}

	public void putInCombat(int attacker) {
		logoutDelay.reset();
		singleCombatDelay.reset();
		updateLastCombatAction();
		setInCombat(true);
	}

	private int lastRegionHeight;

	public int setLastRegionHeight(int height) {
		return this.lastRegionHeight = height;
	}

	public int getLastRegionHeight() {
		return this.lastRegionHeight;
	}

	public String getIdentity() {
		return identity;
	}

	public String setIdentity(String identity) {
		return this.identity = identity;
	}

	public void mapData(boolean s) {
		outStream.writeFrame(99);
		outStream.writeByte(s ? 2 : 0);
	}

	public Player(String username) {
		super(EntityType.PLAYER);
		this.username = username;
		usernameHash = Utility.playerNameToInt64(username);
		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
			playerItemsN[i] = 0;
		}
		for (int i = 0; i < BANK_SIZE; i++) {
			bankItems[i] = 0;
			bankItemsN[i] = 0;
		}
		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 0; // head
		playerAppearance[2] = 18;// Torso
		playerAppearance[3] = 26; // arms
		playerAppearance[4] = 33; // hands
		playerAppearance[5] = 36; // legs
		playerAppearance[6] = 42; // feet
		playerAppearance[7] = 10; // beard
		playerAppearance[8] = 0; // hair colour
		playerAppearance[9] = 0; // torso colour
		playerAppearance[10] = 0; // legs colour
		playerAppearance[11] = 0; // feet colour
		playerAppearance[12] = 0; // skin colour
		dialogue = new DialogueManager(this);
		playerEquipment[getEquipment().getHelmetId()] = -1;
		playerEquipment[getEquipment().getCapeId()] = -1;
		playerEquipment[getEquipment().getAmuletId()] = -1;
		playerEquipment[getEquipment().getChestId()] = -1;
		playerEquipment[getEquipment().getShieldId()] = -1;
		playerEquipment[getEquipment().getLegsId()] = -1;
		playerEquipment[getEquipment().getGlovesId()] = -1;
		playerEquipment[getEquipment().getBootsId()] = -1;
		playerEquipment[getEquipment().getRingId()] = -1;
		playerEquipment[getEquipment().getQuiverId()] = -1;
		playerEquipment[getEquipment().getWeaponId()] = -1;
		heightLevel = 0;
		teleportToX = 3087;
		teleportToY = 3499;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		getMovementHandler().resetWalkingQueue();
		outStream = new GameBuffer(new byte[Constants.BUFFER_SIZE]);
		outStream.offset = 0;
		inStream = new GameBuffer(new byte[Constants.BUFFER_SIZE]);
		inStream.offset = 0;
	}
	
	@Override
	public Hit decrementHP(Hit hit) {
		int damage = hit.getDamage(); 
		if (this.getSkills().getLevel(3) - damage <= 0) {
			damage = this.getSkills().getLevel(3);
			//System.out.println("["+this.getName()+"] dmg was over current hp ("+getSkills().getLevel(3)+"), adjusted to "+damage);
		}
		//System.out.println("you're defo using the right method btw "+damage+" vs "+this.getSkills().getLevel(3));
		if (!this.hasAttribute("infhp"))
			this.getSkills().setLevel(3, this.getSkills().getLevel(3) - damage);

		/*
		 * Check if our player has died, if so start the death task
		 * 
		 */
		if (this.getSkills().getLevel(3) <= 0 && !isDead()) {
			setDead(true);
			hasDied();
		}
		return new Hit(damage, hit.getType());
	}

	public void flushOutStream() {
		if (outStream == null || getSession() == null || !getSession().getChannel().isOpen() || (outStream.offset == 0)) {
			return;
		}
		byte[] temp = new byte[outStream.offset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		getSession().getChannel().writeAndFlush(new Packet(-1, Unpooled.wrappedBuffer(temp)));
		outStream.offset = 0;
	}
	
	public void initialize() {
		//set flags, 0 is flagged as bot i believe
		outStream.writeFrame(249);
		outStream.putByteA(0);
		//Sent the index to the client
		outStream.writeWordBigEndianA(getIndex());
		flushOutStream();

		//Update our combat before login
		combatLevel = getSkills().getCombatLevel();
		//Update our total level before login
		totalLevel = getSkills().getTotalLevel();
		//Update our skills before login
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			this.write(new SendSkillPacket(i));
		}
		//Reset prayers before login
		PrayerHandler.resetAllPrayers(this);
		getActionSender().sendConfig(709, PrayerHandler.canActivate(this, Prayers.PRESERVE, false) ? 1 : 0);
		getActionSender().sendConfig(711, PrayerHandler.canActivate(this, Prayers.RIGOUR, false) ? 1 : 0);
		getActionSender().sendConfig(713, PrayerHandler.canActivate(this, Prayers.AUGURY, false) ? 1 : 0);
		//Set our sidebars before login
		getActionSender().sendSidebarInterfaces();
		//Update inventory before login
		getItems().resetItems(3214);
		//Update equipment before login
		getEquipment().updateEquipment(this);
		//Update friends and ignores
		getFAI().handleLogin();
		//Update right click menu
		getActionSender().sendInteractionOption("Follow", 4, true);
		getActionSender().sendInteractionOption("Trade With", 5, true);
		//Update location
		correctPlayerCoordinatesOnLogin();
		//Refresh the player settings
		refreshSettings();
		//Set last known height
		this.setLastRegionHeight(this.getZ());
		//Set session active
		setActive(true);
		Utility.println("[REGISTERED]: " + this + "");
		//activate login delay
		setAttribute("login_delay", System.currentTimeMillis());
		
		//We can goahead and finish of the players login
		submitAfterLogin();
	}

	public void refreshSettings() {
		AttackStyle.adjustAttackStyleOnLogin(this);
		this.setScreenBrightness((byte) 4);
		getActionSender().sendString("100%", 149);
		getActionSender().sendConfig(166, getScreenBrightness());
		getActionSender().sendConfig(207, isEnableMusic() ? 1 : 0);
		getActionSender().sendConfig(206, isEnableSound() ? 1 : 0);
		getActionSender().sendConfig(287, getSplitPrivateChat() ? 1 : 0);
		getActionSender().sendConfig(205, getSplitPrivateChat() ? 1 : 0);
		getActionSender().sendConfig(200, getAcceptAid() ? 1 : 0);
		getActionSender().sendConfig(172, isAutoRetaliating() ? 1 : 0);
		getActionSender().sendConfig(152, isRunning() ? 1 : 0);
	}

	private void submitAfterLogin() {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {

			@Override
			public void execute() {
				Player player = (Player) getAttachment();
				if (player == null || !player.isActive()) {
					stop();
					return;
				}
				
						if (absX >= 3521 && absX <= 3582 && absY >= 9664 && absY <= 9728) {
							barrows.getMaze().randomizeMaze();
							HideMiniMap.toggle(player);
						}
						player.getActionSender().sendMessage("Welcome to the Wildy Reborn Alpha. You can use ::item, ::getid && ::master");
						player.getActionSender().sendMessage("Previous content updates: ::barrows");
						player.getActionSender().sendMessage("Please remember this is still beta, newest update ::gws");
				//player.getActionSender().sendMessage("Welcome back to " + Constants.SERVER_NAME + ".");
				
				if (!receivedStarter() && inTutorial()) {
					player.dialogue().start("STARTER");
					PlayerUpdating.executeGlobalMessage("<col=255>" + Utility.capitalize(getName()) + "</col> Has joined Wildy Reborn for the first time.");
				}
				
				if (isMuted()) {
					player.getActionSender().sendMessage("You are currently muted. Other players will not see your chat messages.");
				}
				
				QuestTabPageHandler.write(player, QuestTabPages.HOME_PAGE);

				if (player.isPetSpawned()) {
		            Pet pet = new Pet(player, player.getPet());
		            player.setPet(player.getPet());
		            World.getWorld().register(pet);
		        }

				if (player.getName().equalsIgnoreCase("Mopar")) {
					player.setDebugMode(true);
				}
				if (tempKey == null || tempKey.equals("") || tempKey.isEmpty()) {
					player.getActionSender().sendMessage("<col=ff0033>We noticed you aren't in a clanchat, so we added you to the community clanchat!");
					tempKey = "Mopar";
				}
				if (tempKey != null) {
					com.model.game.character.player.content.clan.ClanManager.joinClan(player, player.tempKey);
				}
				this.stop();
			}
		}.attach(this));

	}
	
	private boolean isMuted() {
		return this.isMuted;
	}

	private void correctPlayerCoordinatesOnLogin() {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {

			@Override
			public void execute() {
				Player player = (Player) getAttachment();
				if (player == null || !player.isActive()) {
					stop();
					return;
				}
				final Boundary pc = PestControl.GAME_BOUNDARY;
				final Boundary fc = Boundary.FIGHT_CAVE;
				int x = teleportToX;
				int y = teleportToY;
				if (x > pc.getMinimumX() && x < pc.getMaximumX() && y > pc.getMinimumY() && y < pc.getMaximumY()) {
					player.getPA().movePlayer(2657, 2639, 0);
				} else if (x > fc.getMinimumX() && x < fc.getMaximumX() && y > fc.getMinimumY() && y < fc.getMaximumY()) {
					player.getActionSender().sendMessage("Wave " + (player.waveId + 1) + " will start in approximately 5-10 seconds. ");
					player.getFightCave().startWave();
				}
				ControllerManager.setControllerOnWalk(player);
				controller.onControllerInit(player);
				stop();
			}

		}.attach(this));
	}

	public void logout() {
		//Are we allowed to logout
		if (!controller.canLogOut(this)) {
			return;
		}
		
		//Reset poison and venom
		this.infection = 0;
		this.infected = false;
		this.poisonDamage = 0;
		
		//Dueling check
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(this, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
			if (duelSession.getStage().getStage() >= MultiplayerSessionStage.FURTHER_INTERACTION) {
				getActionSender().sendMessage("You are not permitted to logout during a duel. If you forcefully logout you will");
				getActionSender().sendMessage("lose all of your staked items, if any, to your opponent.");
			}
		}
		
		//If we're no longer in combat we can goahead and logout
		if (logoutDelay.elapsed(10000) && getLastCombatAction().elapsed(600)) {
			outStream.writeFrame(109);
			flushOutStream();
			properLogout = true;
			World.getWorld().unregister(this);
		} else {
			getActionSender().sendMessage("You must wait 10 seconds before logging out.");
		}
	}

	@Override
	public void process() {
		try {
			refresh_inventory();
			PrayerHandler.handlePrayerDraining(this);
			if (clickObjectType > 0 && destinationReached())
				handleObjectAction();

			update_attack_style(); // also updates follow distance. Must be done before following & combat
			process_following();
			combatProcessing();

			controller.tick(this);
			
			NPCAggression.process(this);
			
			if (hasAttribute("antiFire")) {
				if (System.currentTimeMillis() - (long)getAttribute("antiFire", 0L) < 360000) {
					if (System.currentTimeMillis() - (long)getAttribute("antiFire", 0L) > 15000 && System.currentTimeMillis() - (long)getAttribute("antiFire", 0L) < 14000) {
						getActionSender().sendMessage("Your anti fire potion is about to wear off!");
					}
				} else if ((long)getAttribute("antiFire", 0L) > 0L && System.currentTimeMillis() - (long)getAttribute("antiFire", 0L) > 360000) {
					getActionSender().sendMessage("Your resistance to dragon breath has worn off!");
					removeAttribute("antiFire");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void update_attack_style() {

		// Every game tick, update our combat style for worn items. This means we'll keep pathing towards any non-null target properly.
		if (getCombat().target != null) {
			Combat.setCombatStyle(this);
			faceEntity(getCombat().target);
			setFollowing(getCombat().target);
		}
	}

	public GameBuffer getInStream() {
		return inStream;
	}

	public GameBuffer getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}
	
	public void process_following() {
		if (followTarget != null) {
			if (followTarget.isNPC())
				getPA().followNpc(followTarget);
			else
				getPA().followPlayer(!asPlayer().getCombat().noTarget(), followTarget);
		}
	}

	public void combatProcessing() {
		try {
			if (singleCombatDelay.elapsed(6000)) {
				setInCombat(false);
			}
			if (singleCombatDelay2.elapsed(6000)) {
				resetDamageReceived();
			}
			if (skullTimer > 0) {
				skullTimer--;
				if (skullTimer == 1) {
					isSkulled = false;
					attackedPlayers.clear();
					skullIcon = -1;
					skullTimer = -1;
					getPA().requestUpdates();
				}
			}

			super.frozen_process();

			if (attackDelay > 0) {
				attackDelay--;
			}
			if (attackDelay == 0) {
				// Now attack a target if we have one
				Combat.playerVsEntity(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateWalkEntities() {
		ControllerManager.setControllerOnWalk(this);
		ControllerManager.updateControllerOnWalk(this);
		
		if (hasMultiSign && !getArea().inMulti()) {
			hasMultiSign = false;
			this.getActionSender().sendMultiway(-1);
		} else if (!hasMultiSign && getArea().inMulti()) {
			hasMultiSign = true;
			this.getActionSender().sendMultiway(1);
		}
	}

	public void handleObjectAction() {
		face(this, new Position(objectX, objectY));
		if (clickObjectType == 1)
			getActions().firstClickObject(objectId, objectX, objectY);
		else if (clickObjectType == 2)
			getActions().secondClickObject(objectId, objectX, objectY);
		else if (clickObjectType == 3)
			getActions().thirdClickObject(objectId, objectX, objectY);
		else if (clickObjectType == 4)
			UseItem.ItemonObject(this, objectId, objectX, objectY, itemUsedOn);
	}

	public int getChunckX() {
		return (absX >> 6);
	}

	public int getChunckY() {
		return (absY >> 6);
	}

	public int getRegionId() {
		return ((getChunckX() << 8) + getChunckY());
	}

	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private TradeContainer tradeContainer = new TradeContainer(this);
	
	private TradeState tradeState = TradeState.NONE;

	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

	public GameBuffer getUpdateBlock() {
		return updateBlock;
	}

	public void setUpdateBlock(GameBuffer updateBlock) {
		this.updateBlock = updateBlock;
	}

	public void setSession(GameSession session) {
		this.session = session;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * The players active status
	 */
	private boolean isActive;

	/**
	 * Gets the shop that you currently have open.
	 *
	 * @return the shop you have open.
	 */
	public String getOpenShop() {
		return openShop;
	}

	/**
	 * Sets the value for {@link Player#openShop}.
	 *
	 * @param openShop
	 *            the new value to set.
	 */
	public void setOpenShop(String openShop) {
		if (openShop == null && this.openShop != null) {
			Shop s = Shop.SHOPS.get(this.openShop);
			if (s == null) {
				this.openShop = openShop;
				return;
			}
			s.getPlayers().remove(this);
		}
		this.openShop = openShop;
	}

	/**
	 * Checks if the player can unregister from the server, this will prevent
	 * xlogging
	 *
	 * @return If the player is capable of being unregistered from the server
	 */
	public boolean canUnregister() {
		if (System.currentTimeMillis() - lastWasHitTime > 4000) { // out of cb
			xlogDelay = 0;
		}
		boolean inCombat = (System.currentTimeMillis() - xlogDelay < 20000);
		return !inCombat && !isDead();
	}

	public void setXLogDelay(long delay) {
		this.xlogDelay = delay;
	}

	public String getName() {
		return Utility.formatPlayerName(username);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Player [username=" + getName() + ", index: " + getIndex() + "]";
	}

	public TradeState getTradeState() {
		return tradeState;
	}

	public void setTradeState(TradeState state) {
		this.tradeState = state;
	}

	public TradeContainer getTradeContainer() {
		return tradeContainer;
	}

	public RequestManager getRequestManager() {
		return requestManager;
	}

	/**
	 * A list of running tasks for this player
	 */
	private List<ScheduledTask> runningTasks = new LinkedList<>();
	
	/**
	 * Our players current controller
	 */
	private Controller controller = ControllerManager.DEFAULT_CONTROLLER;

	public FriendAndIgnoreList getFAI() {
		return friendAndIgnores;
	}

	/**
	 * Sets the players {@link DistancedActionTask} to activate when reached
	 *
	 * @param task
	 *            The {@link DistancedActionTask} to perform when the
	 *            destination is reached
	 */
	public void setDistancedTask(ScheduledTask task) {
		stopDistancedTask();
		this.distancedTask = task;
		if (task != null) {
			Server.getTaskScheduler().submit(task);
		}
	}

	/**
	 * Safely stops the current {@link distancedTask} from running
	 */
	public void stopDistancedTask() {
		if (distancedTask != null && distancedTask.isRunning()) {
			distancedTask.stop();
		}
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public void onControllerFinish() {
		controller = ControllerManager.DEFAULT_CONTROLLER;
	}

	/**
	 * Sets the players controller without initializing it
	 *
	 * @param controller
	 * @return
	 */
	public boolean setControllerNoInit(Controller controller) {
		this.controller = controller;
		return true;
	}

	/**
	 * Sets our controller and initializes it
	 *
	 * @param controller
	 * @return
	 */
	public boolean setController(Controller controller) {
		this.controller = controller;
		controller.onControllerInit(this);
		return true;
	}

	/**
	 * Gets our players controller
	 *
	 * @return
	 */
	public Controller getController() {
		if (controller == null) {
			setController(ControllerManager.DEFAULT_CONTROLLER);
		}

		return controller;
	}

	public void setSkillTask(SkillTask task) {
		stopSkillTask();
		this.skillTask = task;
		if (task != null) {
			Server.getTaskScheduler().schedule(task);
		}
	}

	public void stopSkillTask() {
		if (skillTask != null) {
			if (skillTask.isRunning())
				skillTask.stop();
		}
		skillTask = null;
	}

	public List<ScheduledTask> getTasks() {
		return runningTasks;
	}

	private int string_receiver;
	private String tempKey;
	private ClanMember member;

	public int getStringReceiver() {
		return string_receiver;
	}

	public String getTempKey() {
		return tempKey;
	}

	/**
	 * Get the players mute status
	 */

	public boolean getClanPunishment() {
		return isClanMuted;
	}

	public void setClanPunishment(boolean isMuted) {
		this.isClanMuted = isMuted;
	}

	public ClanMember getClanMembership() {
		return member;
	}

	public void setTempKey(String s) {
		this.tempKey = s;
	}

	public void setStringReceiver(int i) {
		this.string_receiver = i;
	}

	public void setClanMembership(ClanMember member) {
		this.member = member;
	}
	
	private int lastSlayerTask;

	public int getLastSlayerTask() {
		return lastSlayerTask;
	}

	public void setLastSlayerTask(int lastSlayerTask) {
		this.lastSlayerTask = lastSlayerTask;
	}
	
	private int slayerTask;
	
	public int getSlayerTask() {
		return slayerTask;
	}
	
	public void setSlayerTask(int task) {
		this.slayerTask = task;
	}
	
	private int taskAmount;
	
	public int getSlayerTaskAmount() {
		return taskAmount;
	}
	
	public void setSlayerTaskAmount(int left) {
		this.taskAmount = left;
	}
	
    private int taskDifficulty;
	
	public int getSlayerTaskDifficulty() {
		return taskDifficulty;
	}
	
	public void setSlayerTaskDifficulty(int difficulty) {
		this.taskDifficulty = difficulty;
	}
	
	private boolean firstSlayerTask;
	
	public boolean getFirstSlayerTask() {
		return firstSlayerTask;
	}
	
	public void setFirstSlayerTask(boolean firstTime) {
		this.firstSlayerTask = firstTime;
	}
	
    private boolean firstBossSlayerTask;
	
	public boolean getFirstBossSlayerTask() {
		return firstBossSlayerTask;
	}
	
	public void setFirstBossSlayerTask(boolean firstTime) {
		this.firstBossSlayerTask = firstTime;
	}

	public int localX() {
		return absX;
	}

	public int localY() {
		return absY;
	}
	
	/**
	 * Determines if the player is susceptible to venom by comparing
	 * the duration of their immunity to the time of the last cure.
	 * @return	true of they can be infected by venom.
	 */
	public boolean isSusceptibleToVenom() {
		return System.currentTimeMillis() - lastVenomCure > venomImmunity && !getItems().isWearingItem(12931) && !getItems().isWearingItem(13197) && !getItems().isWearingItem(13199);
	}
	
	/**
	 * The time in milliseconds that the player healed themselves of venom
	 * @return	the last time the player cured themself of poison
	 */
	public long getLastVenomCure() {
		return lastVenomCure;
	}

	/**
	 * Sets the time in milliseconds that the player cured themself of poison
	 * @param lastPoisonCure	the last time the player cured themselves
	 */
	public void setLastVenomCure(long lastVenomCure) {
		this.lastVenomCure = lastVenomCure;
	}
	
	/**
	 * The duration of time in milliseconds the player is immune to venom for
	 * @return	the duration of time the player is immune to poison for
	 */
	public long getVenomImmunity() {
		return venomImmunity;
	}

	/**
	 * Modifies the current duration of venom immunity
	 * @param duration	the new duration
	 */
	public void setVenomImmunity(long duration) {
		this.venomImmunity = duration;
	}
	
	private Herblore herblore = new Herblore(this);

	public Herblore getHerblore() {
		return herblore;
	}
	
	public Rights rights = Rights.PLAYER;
	
	/**
	 * Retrieves the rights for this player.
	 * @return	the rights
	 */
	public Rights getRights() {
		return rights;
	}

	/**
	 * Updates the rights for this player by comparing the players
	 * current rights to that of the available rights and assigning
	 * the first rank found. 
	 */
	public void setRights(Rights rights) {
		this.rights = rights;
	}

	public int packetSize = 0, packetType = -1;
	
	private long lastContainerSearch;
	
	public long getLastContainerSearch() {
		return lastContainerSearch;
	}

	public void setLastContainerSearch(long lastContainerSearch) {
		this.lastContainerSearch = lastContainerSearch;
	}
	
	public long lastCast;
	public int projectileStage;
	public boolean usingObelisk = false;

	public int getMaximumHealth() {
		int base = this.getSkills().getLevelForExperience(Skills.HITPOINTS);
		return base;
	}
	
	/**
	 * Combat refferences
	 */
	public void addDamageReceived(String player, int damage) {
		if (damage <= 0) {
			return;
		}
		CombatDamage combatDamage = new CombatDamage(damage);
		if (damageReceived.containsKey(player)) {
			damageReceived.get(player).add(new CombatDamage(damage));
		} else {
			damageReceived.put(player, new ArrayList<CombatDamage>(Arrays.asList(combatDamage)));
		}
	}

	public void updateLastCombatAction() {
		lastCombatAction.reset();
	}

	public Stopwatch getLastCombatAction() {
		return lastCombatAction;
	}

	public void resetDamageReceived() {
		damageReceived.clear();
	}
	
	public void appendPoisonDamage() {
		if (poisonDamage <= 0) {
			return;
		}
		Player player = this;
		if (poisonDamageHistory.size() >= 4) {
			poisonDamageHistory.clear();
			poisonDamage--;
		}
		if (poisonDamage <= 0) {
			player.getActionSender().sendMessage("The poison has subsided.");
			player.getPA().requestUpdates();
			return;
		}
		poisonDamageHistory.add(poisonDamage);
		
		damage(new Hit(poisonDamage, HitType.POISON));
		player.getPA().requestUpdates();
	}

	/**
	 * End of combat refferences
	 */
	
	/**
	 * The current title the player has
	 */
	private String currentTitle = "";
	
	/**
	 * The color of the current title
	 */
	private String currentTitleColor = "";
	
	/**
	 * The current title 
	 * @return	custom title
	 */
	public String getCurrentTitle() {
		if (currentTitle == null) {
			return "";
		}
		return currentTitle;
	}
	
	/**
	 * Modifies the current title to that of the one we pass in the parameter
	 * @param currentTitle	the title
	 */
	public void setCurrentTitle(String currentTitle) {
		this.currentTitle = currentTitle;
	}
	
	/**
	 * The string of characters that makeup the color of the title
	 * @return	the title color
	 */
	public String getCurrentTitleColor() {
		return currentTitleColor;
	}
	
	/**
	 * Modifies the color of the displayable color
	 * @param color	the color of the title
	 */
	public void setCurrentTitleColor(String color) {
		this.currentTitleColor = color;
	}
	
	public DialogueManager dialogue() {
		return dialogue;
	}

	private String killer;

	public String getKiller() {
		return killer;
	}

	public void setKiller(String killer) {
		this.killer = killer;
	}

	public Bank getBank() {
		if (bank == null)
			bank = new Bank(this);
		return bank;
	}

	public int getSessionExperience() {
		return sessionExperience;
	}
	
	public LunarSpells getLunarSpell() {
		return lunar;
	}

	/**
	 * End of constructors
	 */
	
	/**
	 * Instances
	 */
	private DialogueManager dialogue;
	private MovementHandler movementHandler = new MovementHandler(this);
	private HashMap<String, ArrayList<CombatDamage>> damageReceived = new HashMap<>();
	private FriendAndIgnoreList friendAndIgnores = new FriendAndIgnoreList(this);
	private RequestManager requestManager = new RequestManager(this);
	private ScheduledTask distancedTask;
	private SkillTask skillTask;
	private Bank bank;
	private int sessionExperience;
	private LunarSpells lunar = new LunarSpells(this);
	
	/**
	 * End of instances
	 */

	/**
	 * 
	 * @param macAddress
	 */
	private String macAddress = "";
	
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getMacAddress() {
		return this.macAddress;
	}
	
	public boolean isMaxed() {
		int skill = 0;
		for (int i = 0; i < 21; i++) {
			if (this.getSkills().getLevelForExperience(i) == 99) {
				skill++;
			}
		}
		return (skill == 21);
	}
	
	private boolean yellOff;

	public int combatLevel = 3;
	
	public boolean isYellOff() {
		return yellOff;
	}

	public void setYellOff(boolean yellOff) {
		this.yellOff = yellOff;
	}

	private transient long boneDelay;
	
	public long getBoneDelay() {
		return boneDelay;
	}
	
	public void addBoneDelay(long time) {
		boneDelay = time + Utility.currentTimeMillis();
	}
	
	/**
	 * A prayer handler
	 */
	private PrayerHandler prayerHandler = new PrayerHandler();
	
	/**
	 * Gets the prayer handler
	 * 
	 * @return
	 */
	public PrayerHandler getPrayerHandler() {
		return prayerHandler;
	}

	private boolean running = true;
	
	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	private byte screenBrightness = 3;
	
	public byte getScreenBrightness() {
		return screenBrightness;
	}
	
	public void setScreenBrightness(byte screenBrightness) {
		this.screenBrightness = screenBrightness;
	}
	
	private boolean splitPrivateChat;
	
	public void setSplitPrivateChat(boolean splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
	}
	
	public boolean getSplitPrivateChat() {
		return splitPrivateChat;
	}
	
	/**
	 * Auto-retaliation setting.
	 */
	private boolean isAutoRetaliating;
	
	/**
	 * Set the entity's autoretaliation setting.
	 * 
	 * @param b
	 *            <code>true/false</code> Whether or not this entity will
	 *            autoretaliate when attacked.
	 */
	public void setAutoRetaliating(boolean b) {
		this.isAutoRetaliating = b;
	}

	/**
	 * Get this entity's autoretaliation setting.
	 * 
	 * @return <code>true</code> if autoretaliation is on, <code>false</code> if
	 *         not.
	 */
	public boolean isAutoRetaliating() {
		return isAutoRetaliating;
	}
	
	private boolean enableSound = true;
	
	public boolean isEnableSound() {
		return enableSound;
	}

	public void setEnableSound(boolean enableSound) {
		this.enableSound = enableSound;
	}
	
	private boolean enableMusic = false;

	public boolean isEnableMusic() {
		return enableMusic;
	}

	public void setEnableMusic(boolean enableMusic) {
		this.enableMusic = enableMusic;
	}
	
	private boolean acceptAid;

	public void setAcceptAid(boolean acceptAid) {
		this.acceptAid = acceptAid;
	}

	public boolean getAcceptAid() {
		return acceptAid;
	}
	
    private final MinigameAttributes minigameAttributes = new MinigameAttributes();
    
	 public MinigameAttributes getMinigameAttributes() {
	        return minigameAttributes;
	    }
	private final PlayerDeath player_death = new PlayerDeath(this);
	
	public PlayerDeath getPlayerDeath() {
		return player_death;
	}
	
    private final WeaponInterface weaponInterface = new WeaponInterface(this);
	
	public WeaponInterface getWeaponInterface() {
		return weaponInterface;
	}
	
	private Projectile projectile = new Projectile(this);
	
	public Projectile getProjectile() {
		return projectile;
	}

	/**
	 * Handle the instanced floor reset
	 */
	public void instanceFloorReset() {
		if (kraken != null) {
			if (!Boundary.isIn(this, Boundary.KRAKEN)) {
				System.out.println("Reset for player " + this.getName());
				if (kraken.getInstance() != null)
					InstancedAreaManager.getSingleton().disposeOf(kraken.getInstance());
			}
		}
	}
	
	private KrakenInstance kraken;
	
	public KrakenInstance getKraken() {
		if (kraken == null)
			kraken = new KrakenInstance();
		return kraken;
	}

	public void refresh_inventory() {
		// Makes switching faster .. send our inventory straight after packets (switching items)
		// is processed rather than maybe a couple milliseconds later after other processing like
		// combat, following, npc agro etc etc. those couple MS make a big diff w/ lots of plrs. 
		this.getItems().resetItems(3214);
	}
	
	/**
	 * Gets the players current consumable type
	 * 
	 * @return
	 */
	public Consumable getConsumable() {
		return consumable;
	}
	
	/**
	 * The players current consumable
	 */
	private Consumable consumable;

	/**
	 * Sends a new consumable to be consumed
	 * 
	 * @param type
	 *            The type of consumable
	 * @param id
	 *            The id of the consumable item
	 * @param slot
	 *            The slot of the consumable item
	 */
	public void sendConsumable(String type, int id, int slot) {
		if (this.consumable != null) {
			if (System.currentTimeMillis() - this.consumable.getCurrentDelay(type) < this.consumable.getDelay(type)) {
				return;
			}
		}
		Consumable consumable = new Potions(this, PotionData.forId(id), slot);
		this.consumable = consumable;
		if (consumable != null) {
			consumable.consume();
		}
	}
	
	public void checkDonatorRank() {
		if(this.getTotalAmountDonated() > 9) {
			this.setRights(Rights.DONATOR);
		} else if(this.getTotalAmountDonated() > 29) {
			this.setRights(Rights.SUPER_DONATOR);
		} else if(this.getTotalAmountDonated() > 99) {
			this.setRights(Rights.ELITE_DONATOR);
		} else if(this.getTotalAmountDonated() > 199) {
			this.setRights(Rights.EXTREME_DONATOR);
		}  else {
			if(!this.getRights().isStaff())
			this.setRights(Rights.PLAYER);
		}
	}
	
	public void rspsdata(Player player, String username) {
		try {
			username = username.replaceAll(" ", "_");
			String secret = "92dfa194391a59dc65b88b704599dbd6";
			String email = "patrick.vanelderen@live.nl";
			URL url = new URL("http://rsps-pay.com/includes/listener.php?username=" + username + "&secret=" + secret + "&email=" + email);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String results = reader.readLine();
			if (results.toLowerCase().contains("!error:")) {

			} else {
				String[] ary = results.split(",");
				for (int i = 0; i < ary.length; i++) {
					switch (ary[i]) {
					case "0":
						player.getActionSender().sendMessage("We couldn't find your purchase in our system.");
						break;
					case "20355":
						player.getItems().addOrSendToBank(13652, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 25);
						break;
					case "20356":
						player.getItems().addOrSendToBank(11802, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 15);
						break;
					case "20357":
						player.getItems().addOrSendToBank(19481, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 20);
						break;
					case "20360":
						player.getItems().addOrSendToBank(12006, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 3);
						break;
					case "20364":
						player.getItems().addOrSendToBank(12926, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 10);
						break;
					case "20366":
						Item[] trident_set = {new Item(12899), new Item (11907)};
						for(Item item : trident_set) {
							player.getItems().addOrSendToBank(item.getId(), 1);
							player.setTotalAmountDonated(player.getTotalAmountDonated() + 10);
							break;
						}
						break;
					case "20367":
						player.getItems().addOrSendToBank(12904, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 10);
						break;
					case "20368":
						player.getItems().addOrSendToBank(11791, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 5);
						break;
					case "20371":
						player.getItems().addOrSendToBank(13576, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 20);
						break;
					case "20374":
						player.getItems().addOrSendToBank(21999, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 5);
						break;
					case "20376":
						player.getItems().addOrSendToBank(22000, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 2);
						break;
					case "20380":
						player.getItems().addOrSendToBank(22003, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 5);
						break;
					case "20382":
						player.getItems().addOrSendToBank(22004, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 5);
						break;
					case "20383":
						player.getItems().addOrSendToBank(22005, 1);
						player.setTotalAmountDonated(player.getTotalAmountDonated() + 5);
						break;
					case "20384":
						Item[] partyhat_set = {new Item(12399), new Item (11862), new Item (11863), new Item (1038), new Item (1040), new Item (1042), new Item (1044), new Item (1046), new Item (1048) };
						for(Item item : partyhat_set) {
							player.getItems().addOrSendToBank(item.getId(), 1);
							player.setTotalAmountDonated(player.getTotalAmountDonated() + 100);
							break;
						}
						break;
					case "20385":
						Item[] halloween_mask_set = {new Item(1053), new Item (1055), new Item (1057), new Item (11847)};
						for(Item item : halloween_mask_set) {
							player.getItems().addOrSendToBank(item.getId(), 1);
							player.setTotalAmountDonated(player.getTotalAmountDonated() + 50);
							break;
						}
						break;
					}
					checkDonatorRank();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}							

	private long lastAltarPrayer = -3000;
	
	public long getLastAltarPrayer() {
		return System.currentTimeMillis() - lastAltarPrayer;
	}

	public void setLastAltarPrayer(long lastAltarPrayer) {
		this.lastAltarPrayer = lastAltarPrayer;
	}
	
	/**
	 * The action sender.
	 */
	private final ActionSender actionSender = new ActionSender(this);
	
	/**
	 * Gets the action sender.
	 * 
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
		return actionSender;
	}
	
	private boolean playerTransformed;
	
	public boolean isPlayerTransformed() {
		return playerTransformed;
	}
	
	public void setPlayerTransformed(boolean transform) {
		this.playerTransformed = transform;
	}
	
	/**
	 * The imitated npc.
	 */
	private int pnpc = -1;

	/**
	 * @return the pnpc
	 */
	public int getPnpc() {
		return pnpc;
	}

	/**
	 * @param pnpc the pnpc to set
	 */
	public void setPnpc(int pnpc) {
		this.pnpc = pnpc;
	}
	
	private BossDeathTracker bossTracker;

	public BossDeathTracker getBossDeathTracker() {
		if (bossTracker == null)
			bossTracker = new BossDeathTracker(this);
		return bossTracker;
	}
	

	private SlayerDeathTracker slayerTracker;
	
	public SlayerDeathTracker getSlayerDeathTracker() {
		if (slayerTracker == null)
			slayerTracker = new SlayerDeathTracker(this);
		return slayerTracker;
	}
	
	/**
     * Sends a global sound
     *
     * @param player
     * @param soundId
     * @param type
     * @param delay
     */
    public void sendGlobalSound(Player player, int soundId, int type, int delay) {
        for (Player target : World.getWorld().getPlayers()) {
            if (target != null) {
                if (player.goodDistance(target.getX(), target.getY(), player.getX(), player.getY(), 8)) {
                    target.write(new SendSoundPacket(soundId, type, delay));
                }
            }
        }
    }
    
    private int teleportButton;

	public int getTeleportButton() {
		return teleportButton;
	}

	public void setTeleportButton(int teleportButton) {
		this.teleportButton = teleportButton;
	}

	private TeleportationTypes teleportationType;
	
	public TeleportationTypes getTeleportationType() {
		return teleportationType;
	}

	public void setTeleportationType(TeleportationTypes teleportationType) {
		this.teleportationType = teleportationType;
	}
	
	/**
	 * The vengeance flag.
	 */
	private boolean vengeance = false;

	/**
	 * The can vengeance flag.
	 */
	private boolean canVengeance = true;
	
	/**
	 * @return the vengeance
	 */
	public boolean hasVengeance() {
		return vengeance;
	}

	/**
	 * @param vengeance the vengeance to set
	 */
	public void setVengeance(boolean vengeance) {
		this.vengeance = vengeance;
	}
	
	/**
	 * @return the canVengeance
	 */
	public boolean canVengeance() {
		return canVengeance;
	}

	/**
	 * @param canVengeance the canVengeance to set
	 */
	public void setCanVengeance(boolean canVengeance) {
		this.canVengeance = canVengeance;
	}

	/**
	 * @param canVengeance the canVengeance to set
	 */
	public void setCanVengeance(int ticks) {
		Server.getTaskScheduler().schedule(new ScheduledTask(ticks) {
			@Override
			public void execute() {
				canVengeance = true;
				this.stop();
			}			
		});
	}
	
	/**
	 * The teleporting flag.
	 */
	private boolean teleport = false;

	/**
	 * @return we're teleporting
	 */
	public boolean isTeleporting() {
		return teleport;
	}

	/**
	 * @param set teleport true or false
	 */
	public void setTeleporting(boolean teleport) {
		this.teleport = teleport;
	}
	
	/**
	 * @param set shopping true or false
	 */
	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}
	
	/**
	 * The shopping flag.
	 */
	private boolean shopping = false;

	/**
	 * @return we're shopping
	 */
	public boolean isShopping() {
		return shopping;
	}
	
    private boolean trading;
	
	public void setTrading(boolean trading) {
		this.trading = trading;
	}

	public boolean isTrading() {
		return this.trading;
	}
	
    private boolean banking;
	
	public void setBanking(boolean banking) {
		this.banking = banking;
	}

	public boolean isBanking() {
		return this.banking;
	}
	
	/**
	 * We can't perform actions while the other person is busy.
	 */
	public boolean isBusy() {
		if(isTeleporting() || isShopping() || isTrading() || isBanking()) {
			return true;
		}
		return false;
	}
	
	private Area area = new Area(this);

	public Area getArea() {
		return area;
	}
	
	private int destroyItem = -1;

	public int getDestroyItem() {
		return destroyItem;
	}

	public void setDestroyItem(int destroyItem) {
		this.destroyItem = destroyItem;
	}
	
	private FoodConsumable food = new FoodConsumable(this);

	public FoodConsumable getFood() {
		return food;
	}
	
	/**
	 * Player updating
	 */
	private GameBuffer playerProps = new GameBuffer(new byte[300]);
	
	public GameBuffer getPlayerProps() {
		return playerProps;
	}
	
	// clue scroll
	public ClueScrollContainer clueContainer;
	public ClueDifficulty bossDifficulty;
	public int randomClueReward = 0;
	public int easyClue = 0;
	public int mediumClue = 0;
	public int hardClue = 0;
	public int eliteClue = 0;
	
	/**
	 * Temp solution to clue-scroll bug
	 * - Allows player to reset his clue status 
	 * - Allowing them to continue to gather clues
	 * @param player
	 */
	public void resetClueStatus(Player player) {
		if (player.getItems().playerOwnsAnyItems(ClueDifficulty.getClueIds())) {
			player.getActionSender().sendMessage("It seems you have a clue scroll, please complete it.");
			return;
		}
		if(player.bossDifficulty != null) {
			player.bossDifficulty = null;
		}
		if(player.clueContainer != null) {
			player.clueContainer = null;
		}
	}
	
    /**
     * Integers
     */
	public int playerAppearance[] = new int[13];
	public int openInterface = -1;
	public int countdown;
	public int combatCountdown = 10;
	public int distance;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;
	public int mapRegionX, mapRegionY;
	public int currentX, currentY;
	public int teleportToX = -1, teleportToY = -1;
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];
	public int BANK_SIZE = 350;
	public int bankItems[] = new int[BANK_SIZE];
	public int bankItemsN[] = new int[BANK_SIZE];
	public int standTurnAnimation = 0x328;
	public int turnAnimation = 0x337;
	public int walkAnimation = 0x333;
	public int turn180Animation = 0x334;
	public int turn90ClockWiseAnimation = 0x335;
	public int turn90CounterClockWiseAnimation = 0x336;
	public int runAnimation = 0x338;
	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	private int x1 = -1;
	private int y1 = -1;
	private int x2 = -1;
	private int y2 = -1;
	private int speed1 = -1;
	private int speed2 = -1;
	private int direction = -1;

	public int lastClickedItem;
	public int[] playerBonus = new int[12];
	public int[] itemKeptId = new int[4];
	public int WillKeepAmt1, WillKeepAmt2, WillKeepAmt3, WillKeepAmt4, WillKeepItem1, WillKeepItem2, WillKeepItem3,
			WillKeepItem4, WillKeepItem1Slot, WillKeepItem2Slot, WillKeepItem3Slot, WillKeepItem4Slot, EquipStatus;
	
	public int totalLevel,
			lastChatId = 1, privateChat, specBarId, skullTimer,
			followDistance,
			xInterfaceId, xRemoveId, xRemoveSlot, frozenBy,
			wildLevel, teleTimer, killerId,
			attackDelay, npcClickIndex, oldSpellId,
			clickNpcType, clickObjectType, objectId, itemUsedOn, objectX, objectY, tradeStatus, tradeWith,
			walkTutorial = 15, skullIcon = -1, bountyPoints;
	public int objectDistance, teleHeight;
	
	/**
	 * Booleans
	 */
	public boolean usingBow, usingMagic, castingMagic, magicFailed;
	public boolean isDead = false;
	public boolean chatTextUpdateRequired = false;
	private boolean dragonfireShieldActive;
	public boolean forceMovementUpdateRequired = false;
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
	public boolean usingCross;
	public boolean isMuted, isClanMuted,
	isSkulled, hasMultiSign, saveCharacter,
	properLogout, playerIsFiremaking,
	acceptedTrade, saveFile, takeAsNote, didTeleport, mapRegionDidChange;
	
	public boolean isMapRegionChanging(){
		return mapRegionDidChange;
	}
	
	/**
	 * Strings
	 */
	public String connectedFrom = "";
	private String username = null;
	public String passHash;
	private String password = "";
	public String lastClanChat = "";
	private String identity;
	public String loyaltyTitle = "";
	
    /**
     * Longs
     */
	public long usernameHash;
	private long lastVenomCure;
	private long venomImmunity;
	public long godSpellDelay;
	private long lastDragonfireShieldAttack;

	/**
	 * Bytes
	 */
	private byte chatText[] = new byte[4096];
	private byte chatTextSize = 0;
	public byte venomDamage;
	public byte poisonDamage;
	private List<Byte> poisonDamageHistory = new ArrayList<>(4);
	
	/**
	 * Timers
	 */
	private Stopwatch lastCombatAction = new Stopwatch();
	public Stopwatch switchDelay = new Stopwatch();
	public Stopwatch aggressionTolerance = new Stopwatch();
	public Stopwatch singleCombatDelay = new Stopwatch();
	public Stopwatch singleCombatDelay2 = new Stopwatch();
	public Stopwatch ditchDelay = new Stopwatch();
	public Stopwatch foodDelay = new Stopwatch();
	public Stopwatch teleblock = new Stopwatch();
	public Stopwatch lastSpear = new Stopwatch();
	public Stopwatch lastProtItem = new Stopwatch();
	public Stopwatch logoutDelay = new Stopwatch();
	public Stopwatch cannotUsePrayer = new Stopwatch();
	public Stopwatch lastVeng = new Stopwatch();
	
	
	public ArrayList<String> lastKilledList = new ArrayList<String>();
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();

	private AchievementHandler achievementHandler;

	public AchievementHandler getAchievements() {
		if (achievementHandler == null)
			achievementHandler = new AchievementHandler(this);
		return achievementHandler;
	}

	public void debug(String string) {
		if (this.rights == Rights.ADMINISTRATOR) {
			this.getActionSender().sendMessage(string);
		}
	}
	
	/**
	 * The last fire made.
	 */
	private long lastFire;

	public long getLastFire() {
		return lastFire;
	}

	public void setLastFire(long lastFire) {
		this.lastFire = lastFire;
	}
	
	private int pet;

	public void setPet(int pet) {
		this.pet = pet;
	}

	public int getPet() {
		return pet;
	}
	
	private boolean petSpawned;
	
	public boolean isPetSpawned() {
		return petSpawned;
	}

	public void setPetSpawned(boolean petSpawned) {
		this.petSpawned = petSpawned;
	}

	@Override
	public int clientIndex() {
		return 32768 + this.getIndex();
	}
	
	private GameModeSelection select_game_mode = new GameModeSelection();

	public GameModeSelection getGameModeSelection() {
		return select_game_mode;
	}
	
	//Minigame variables
	public int pestControlDamage;
	public boolean isAnimatedArmourSpawned;
	public int waveId;
	public boolean secondOption;
	
	private FightCaves fightcave = null;
	
	public FightCaves getFightCave() {
		if (fightcave == null)
			fightcave = new FightCaves(this);
		return fightcave;
	}
	
    private boolean completedFightCaves;
	
	public boolean hasCompletedFightCaves() {
		return completedFightCaves;
	}

	public void setCompletedFightCaves() {
		if(!completedFightCaves) {
			completedFightCaves = true;
		}
	}
	
	/**
	 * The single instance of the {@link PestControlRewards} class for this player
	 * @return	the reward class
	 */
	private PestControlRewards pestControlRewards = new PestControlRewards(this);
	
	public PestControlRewards getPestControlRewards() {
		return pestControlRewards;
	}
	
	private WarriorsGuild warriorsGuild = new WarriorsGuild(this);

	public WarriorsGuild getWarriorsGuild() {
		return warriorsGuild;
	}
	
	/**
	 * The progress bar.
	 */
	private int progressBar;

	public int getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(int progressBar) {
		this.progressBar = progressBar;
	}
	
	private boolean preserveUnlocked;

	public boolean isPreserveUnlocked() {
		return preserveUnlocked;
	}

	public void setPreserveUnlocked(boolean preserveUnlocked) {
		this.preserveUnlocked = preserveUnlocked;
	}
	
	private boolean rigourUnlocked;

	public boolean isRigourUnlocked() {
		return rigourUnlocked;
	}

	public void setRigourUnlocked(boolean rigourUnlocked) {
		this.rigourUnlocked = rigourUnlocked;
	}
	
	 public int getBarrowsChestsLooted() {
	        return barrowsChestsLooted;
	    }

	    public int getBarrowsChestRewards() {
	        return barrowsChestRewards;
	    }
	
	private boolean auguryUnlocked;
	public int barrowsChestsLooted;
	public int barrowsChestRewards;

	public boolean isAuguryUnlocked() {
		return auguryUnlocked;
	}

	public void setAuguryUnlocked(boolean auguryUnlocked) {
		this.auguryUnlocked = auguryUnlocked;
	}
}
