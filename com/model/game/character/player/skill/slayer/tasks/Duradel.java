package com.model.game.character.player.skill.slayer.tasks;

public enum Duradel implements Task {
	
	/*KING_BLACK_DRAGON(239, 1), 
	CHAOS_ELEMENTAL(2054, 1), 
	DAGANNOTH_SUPREME(2265, 1), 
	DAGGANOTH_REX(2267, 1), 
	DAGGANOTH_PRIME(2266, 1), 
	VENENATIS(6610, 1), 
	VETION(6611, 1), 
	SCORPIA(6615, 1), 
	CALLISTO(6609, 1),
	KRIL_TSUTSAROTH(3129, 1), 
	KREE_ARRA(3162, 1), 
	COMMANDER_ZILYANA(2205, 1), 
	GENERAL_GRAARDOR(2215, 1), 
	KRAKEN(494, 87),
	BARRELCHEST(6342, 1),
	ZOMBIES_CHAMPION(3359, 1),
	CORPOREAL_BEAST(319, 1),
	CHAOS_FANATIC(6619, 1);*/
	
	ABERRANT_SPECTRE(7,60,7,0),
	ABYSSAL_DEMON(415, 85,12,0), 
	ANKOU(7257,70,5,0),
	//AVIANSIE
	BLACK_DEMON(1432,1,8,0),
	BLACK_DRAGON(252, 1,9,0), 
	BLOODVELD(484, 50,8,0), 
	BLUE_DRAGON(268, 1,4,0), 
	CAVE_HORROR(3209, 58,4,0), 
	CAVE_KRAKEN(492, 87, 9, 0), 
	DAGANNOTH(140,1, 9, 0),
	DARK_BEAST(4005, 90, 11, 0), 
	DUST_DEVIL(423, 65,5,0),
	//elves
	FIRE_GIANT(2075, 1,7,0), 
	
	GREATER_DEMON(2026, 1,9,0), 
	HELLHOUND(135, 1,10,0), 
	STEEL_DRAGON(274, 1,7,0), 
	IRON_DRAGON(272, 1,5,0), 
	
	//kalphite
	//lizardman
	MITHRIL_DRAGON(2919, 1, 10, 0),
	NECHRYAEL(11, 80, 9, 0), 
	RED_DRAGON(247, 1, 8, 0),
	WYVERN(465, 72, 7, 0),
	SMOKE_DEVIL(498, 93, 9, 0),
	TUROTH(432, 55,3,0),
	//waterfiend
	//suqah
	//troll
	//tzhaar
	KRAKEN(494, 87, 6, 0);
	
	/**
	 * The slayer npc ID
	 */
	private final int id;
	/**
	 * The level requirement
	 */
	private final int slayerRequirement;
	/**
	 * The experience multiplier
	 */
	
	Duradel(int id, int slayerRequirement, int weight, int percentage) {
		this.id = id;
		this.slayerRequirement = slayerRequirement;
		this.weight = weight;
		this.percentage = percentage;
	}

	public int getId() {
		return id;
	}
	
	private int weight;
	
	private int percentage;
	
	public void setPercentage(int percentage){
		this.percentage = percentage;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public static int getTotal() {
		return total;
	}
	
	public static int total;
	
	public int getSlayerReq() {
		return slayerRequirement;
	}
	public int getPercentage(){
		return percentage;
	}
	static {
		for (Duradel t : Duradel.values()) {
			total += t.getWeight();
			}
		}
}
