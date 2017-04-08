package com.model.game.regions;

import com.model.game.character.Entity;

public interface Areas {
	/**
	 * This abstract void represents the first click option of a object in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param player
	 * @param object
	 */
	public void sendFirstClickObject(Entity player, int object);

	/**
	 * This abstract void represents the second click option of a object in
	 * which the player interacts with, an Integer is placed inside the
	 * parameter so that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param player
	 * @param object
	 */
	public void sendSecondClickObject(Entity player, int object);

	/**
	 * This abstract void represents the third click option of a object in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param player
	 * @param object
	 */
	public void sendThirdClickObject(Entity player, int object);

	/**
	 * This abstract void represents the first click option of a npc in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param player
	 * @param npc
	 */
	public void sendFirstClickNpc(Entity player, int npc);

	/**
	 * This abstract void represents the second click option of a npc in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param player
	 * @param npc
	 */
	public void sendSecondClickNpc(Entity player, int npc);

	/**
	 * This abstract void represents the third click option of a npc in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param player
	 * @param npc
	 */
	public void sendThirdClickNpc(Entity player, int npc);
	
	
}
