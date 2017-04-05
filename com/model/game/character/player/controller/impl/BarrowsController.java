package com.model.game.character.player.controller.impl;

import com.model.game.Constants;
import com.model.game.character.player.Boundary;
import com.model.game.character.player.Player;
import com.model.game.character.player.controller.Controller;
import com.model.game.character.player.minigames.BarrowsFull.Barrows;
import com.model.game.character.player.minigames.BarrowsFull.Brother;
import com.model.game.character.player.minigames.BarrowsFull.HideMiniMap;
import com.model.game.character.player.packets.out.SendWalkableInterfacePacket;
import com.model.game.location.Position;

public class BarrowsController extends Controller {


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
		return true;
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
		return true;
	}

	@Override
	public void onDeath(Player player) {
	}

	@Override
	public Position getRespawnLocation(Player player) {
		return Constants.RESPAWN_PLAYER_LOCATION;
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
		if ((Boundary.isIn(player, Boundary.BARROWS_HILLS) || Boundary.isIn(player, Boundary.BARROWS_CAVERNS))) {
			player.write(new SendWalkableInterfacePacket(16128));
			if(Boundary.isIn(player, Boundary.BARROWS_CAVERNS))
				player.getActionSender().disableMap(2);
				player.getBarrows().setBarrowsInterface(player);
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
		if ((Boundary.isIn(player, Boundary.BARROWS_HILLS) || Boundary.isIn(player, Boundary.BARROWS_CAVERNS) || player.absX >= 3521 && player.absX <= 3582 && player.absY >= 9664 && player.absY <= 9728)) {
			player.write(new SendWalkableInterfacePacket(16128));
			if(Boundary.isIn(player, Boundary.BARROWS_CAVERNS))
				player.getActionSender().disableMap(2);
				player.getBarrows().setBarrowsInterface(player);
		}
	}

	@Override
	public String toString() {
		return "Barrows Controller";
	}

	@Override
	public void onControllerLeave(Player player) {
		player.setAttribute("left_wild_delay", System.currentTimeMillis());
	}

}