package com.model.game.character.npc.drops;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.model.game.character.npc.NPCDropAnnouncement;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.Player;
import com.model.game.character.player.PlayerUpdating;
import com.model.game.character.player.Skills;
import com.model.game.character.player.content.Feed;
import com.model.game.character.player.skill.prayer.Prayer.Bone;
import com.model.game.index.ItemIndex;
import com.model.game.item.GameItem;
import com.model.game.item.Item;
import com.model.game.item.ground.GroundItem;
import com.model.game.item.ground.GroundItemHandler;
import com.model.utility.Utility;
import com.model.utility.misc;
import com.model.utility.json.definitions.ItemDefinition;
import com.google.common.reflect.TypeToken;


/**
 * The npc drop system handles dropping items for npcs
 * 
 * @author Optimum
 */
public class NpcDropSystem {
	
	/**
	 * The logger for the class.
	 */
	private static Logger logger = Logger.getLogger(NpcDropSystem.class.getSimpleName());
	
	/**
	 * The directory of the npcdropsystem
	 */
	private static final String DIRECTORY = "./data/json/";
	
	/**
	 * The file to save to
	 */
	private static final String FILE = DIRECTORY + "npcdrops.json";
	
    /**
     * The file to save to
     */
    private static final String RARE_FILE = DIRECTORY + "rare_drop_table.json";
	
    /**
     * Gson object used saving and loading data
     */
    private static final Gson GSON = new Gson();
	
	/**
	 * NpcDropSystem instance object
	 */
	private static NpcDropSystem instance = new NpcDropSystem();
	
	/**
	 * Getter for the NpcDropSystem instance
	 * @return
	 */
	public static NpcDropSystem get()
	{
		return instance;
	}

	public RareDropTable rareDropTable = new RareDropTable(null, null);
	
	
	public void saveDrops()
	{
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(FILE));
			out.write(toJson());
			out.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void loadRareDrops()
	{
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(RARE_FILE));
			String data = "";
			data = in.readLine();
			fromJsonRare(data);
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void loadDrops()
	{
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(FILE));
			String data = "";
			data = in.readLine();
			fromJson(data);
			in.close();
			
			logger.info("Loaded " + npcDropData.size() + " npc drops.");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * loads the npc drop data
	 */
	public void fromJson(String json)
	{
		Type listType = new TypeToken<ArrayList<NpcDropData>>() { }.getType();
		npcDropData = GSON.fromJson(json, listType);
	}
	
	public void fromJsonRare(String json)
	{
		rareDropTable = GSON.fromJson(json, RareDropTable.class);
	}
	
	/**
	 * Private Constructor
	 */
	private NpcDropSystem() {}
	
	/**
	 * Formats the data to json
	 * @return
	 */
	public String toJson()
	{
		return GSON.toJson(npcDropData);
	}
	
	/**
	 * The list of all npc drops
	 */
	private List<NpcDropData> npcDropData = new ArrayList<>();

	public void bankLoot(Player player, int npc, int kills, float increase)
	{
		
		for(NpcDropData drops : npcDropData)
		{
			for(int npcs : drops.getNpcList())
			{
				if(npcs == npc)
				{
					for(int i = 0; i < kills; i++)
					{
						List<GameItem> items = drops.generateDropList(increase);
						
						for(GameItem item : items)
						{
							player.getItems().sendItemToAnyTab(item.getId(), item.getAmount());
						}
					}
					
				}
				
			}
			
		}
	}
	
	/**
	 * drops items for a player from an npc
	 * 
	 * @param player - the player
	 * @param npc - the npc
	 */
	public void drop(Player player, NPC npc, float percentIncrease) {
		NPCDropAnnouncement dropAnnouncement = new NPCDropAnnouncement(npc);
		if(npcDropData == null)
		{
			return;
		}

		if(npc.getId() == 415 && player.getSlayerInterface().getUnlocks().entrySet().toString().contains("LIMEY")) {
			if(Utility.random(25) == 0) {
				player.getActionSender().sendMessage("You feel your lime whip drop chances increasing...");
			}
			if(Utility.random(1000) == 1) {
				Location location = null;
				location = new Location(player.getX(), player.getY(), player.getZ());
				GroundItemHandler.createGroundItem(new GroundItem(new Item(ItemIndex.LIME_WHIP, 1), location.getX(), location.getY(), location.getH(), player));	
				PlayerUpdating.executeGlobalMessage("@red@[News]@blu@" + player.getName() + "@bla@ just got a @red@ LIME WHIP @bla@drop.");		
			}
	}
		for(NpcDropData drops : npcDropData) {
			
			for(int npcs : drops.getNpcList()) {
				if(npcs == npc.getId()) {
					
					List<GameItem> items = drops.generateDropList(percentIncrease);
					
					Location location = null;
					
					for(GameItem item : items) {
						if(npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044 || npc.getId() == 494 || npc.getId() == 492) {
							location = new Location(player.getX(), player.getY(), player.getZ());
						}
						else { 
							location = new Location(npc.getX(), npc.getY(), npc.heightLevel); 
						}
						if(npc.getName().toLowerCase().contains("mithril dragon") && player.getSlayerInterface().getUnlocks().keySet().toString().contains("DULY_NOTED") &&
								item.getId() == 2359){
							item.id = 2360;
						}
						Bone bones = Bone.forId(item.getId());
						if (bones != null) {
							if (player.getSkills().getPrayer().isHoldingBoneCrusher(player)) {
								player.getSkills().addExperience(Skills.PRAYER, bones.getXp());
								continue;
							}
						}
						dropAnnouncement.announce(player, item.getId(), item.getAmount());
						GroundItemHandler.createGroundItem(new GroundItem(new Item(item.getId(), item.getAmount()), location.getX(), location.getY(), location.getH(), player));
						ItemDefinition itemDef = ItemDefinition.forId(item.getId());
						if (itemDef.getGeneralPrice() > 10000000) {
							PlayerUpdating.executeGlobalMessage("@red@[News]@blu@" + player.getName() + "@bla@ just got a @red@" + ItemDefinition.forId(item.getId()).getName() + " @bla@drop.");
							
						}
						Feed.submit("@whi@[@gre@"+Utility.getDate()+"@or1@" + player.getName() + "@whi@ Received@yel@ " + ItemDefinition.forId(item.getId()).getName());

					}
				}
			}
		}
	}

	/**
	 * Gets the {@linkplain npcDropData}
	 * @return - the {@linkplain npcDropData}
	 */
	public List<NpcDropData> getNpcDropData() 
	{
		return npcDropData;
	}

}//Optimums code
