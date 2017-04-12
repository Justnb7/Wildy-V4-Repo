package com.model.game.character.player.controller.impl;

import com.model.game.character.player.Boundary;
import com.model.game.character.player.Player;
import com.model.game.character.player.controller.Controller;
import com.model.game.character.player.packets.out.SendWalkableInterfacePacket;
import com.model.game.location.Position;

public class GodwarsController extends Controller {
	
	private final Position GODWARS_RESPAWN = new Position(3221, 3218, 0);

	@Override
	public void tick(Player player) {
		
	}

	@Override
	public boolean canTalk(Player player) {
		return true;
	}

	@Override
	public boolean canMove(Player player) {
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public boolean canClick(Player player) {
		return true;
	}

	@Override
	public boolean isSafe() {
		return false;
	}

	@Override
	public boolean canAttackNPC(Player player) {
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player attacker, Player victim) {
		return false;
	}

	@Override
	public boolean allowMultiSpells(Player player) {
		return true;
	}

	@Override
	public boolean allowPvPCombat(Player player) {
		return false;
	}

	@Override
	public void onDeath(Player player) {

	}
	@Override
	public Position getRespawnLocation(Player player) {
		return GODWARS_RESPAWN;
	}

	@Override
	public boolean canLogOut(Player player) {
		return true;
	}

	@Override
	public void onDisconnect(Player player) {
	}

	@Override
	public void onControllerInit(Player player) {
		if (( Boundary.isIn(player, Boundary.GWS_MAINROOM) || Boundary.isIn(player, Boundary.GODWARS_BOSSROOMS))) {
		player.write(new SendWalkableInterfacePacket(16210));	
		}
	}
	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	@Override
	public void onTeleport(Player player) {

	}

	@Override
	public boolean canTrade(Player player) {
		return true;
	}

	@Override
	public boolean canEquip(Player player, int id, int slot) {
		return true;
	}

	@Override
	public boolean canUsePrayer(Player player) {
		return true;
	}

	@Override
	public boolean canEat(Player player) {
		return true;
	}

	@Override
	public boolean canDrink(Player player) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player player) {
		return true;
	}

	@Override
	public boolean transitionOnWalk(Player player) {
		return true;
	}

	@Override
	public void onWalk(Player player) {
		player.write(new SendWalkableInterfacePacket(16210));
	}

	@Override
	public String toString() {
		return "Godwars Controller";
	}

	@Override
	public void onControllerLeave(Player player) {
		// TODO Auto-generated method stub
		
	}

}
