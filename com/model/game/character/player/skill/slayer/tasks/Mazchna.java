package com.model.game.character.player.skill.slayer.tasks;

public enum Mazchna implements Task {
	
	 /*
	  * Banshees(414, 15),
	 BAT(2827, 1),
	 BIRDS(2692, 1),
	 BEAR(3423, 1),
	 CAVE_BUGS(481, 7),
	 CAVE_CRAWLERS(406, 10),
	 CAVE_SLIMES(480, 17),
	 COCKATRICE(419, 25),
	 CRAWLING_HANDS(448, 5),
	 GHOSTS(85, 1),
	 HILL_GIANT(2098, 1),
	 ICE_WARRIOR(2841, 1),
	 KALPHITES(955, 1),
	 PYREFIEND(435, 30),
	 ROCK_SLUG(421, 20);
	*/
	
	
	BANSHEE(414, 1, 8, 0),
	BAT(2834, 1, 7, 0),
	BEAR(3423, 1, 6, 0),
	
	CAVE_BUGS(481, 7, 8 , 0),
	CAVE_CRAWLERS(406, 10, 8, 0),
	CAVE_SLIMES(480, 17, 8, 0),
	COCKATRICE(419, 25, 8, 0), 
	CRAWLING_HANDS(448, 5, 8, 0),
	
	//earth warrior

	
	GHOSTS(85, 1, 7, 0),
	HILL_GIANT(2098, 1, 7, 0),
	PYREFIEND(435, 30, 8, 0),
	ICE_WARRIOR(2841, 1, 7, 0),
	SKELETON(70, 1, 6, 0);
	//rockslug
	//scorpion
	//skeleton
	
	
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
	
	Mazchna(int id, int slayerRequirement, int weight, int percentage) {
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
		for (Mazchna t : Mazchna.values()) {
			total += t.getWeight();
			}
		}
}
