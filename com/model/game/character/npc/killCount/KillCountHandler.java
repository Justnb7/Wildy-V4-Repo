package com.model.game.character.npc.killCount;

import java.util.HashMap;

import com.model.game.character.npc.BossDeathTracker.BossName;

public class KillCountHandler {
	
	private HashMap<KillCountType, Integer> container = new HashMap<KillCountType, Integer>();

	public HashMap<KillCountType, Integer> getTracker() {
		return container;
	}
	

}
