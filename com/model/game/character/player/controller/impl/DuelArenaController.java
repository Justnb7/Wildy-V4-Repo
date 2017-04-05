package com.model.game.character.player.controller.impl;

import com.model.game.Constants;
import com.model.game.character.player.Boundary;
import com.model.game.character.player.Player;
import com.model.game.character.player.controller.Controller;
import com.model.game.character.player.minigames.BarrowsFull.Barrows;
import com.model.game.character.player.minigames.BarrowsFull.Brother;
import com.model.game.character.player.packets.out.SendWalkableInterfacePacket;
import com.model.game.location.Position;

public class DuelArenaController extends Controller {

	private final Position RESPAWN = new Position(Constants.DUELING_RESPAWN_X, Constants.DUELING_RESPAWN_Y, 0);

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
		if(Boundary.isIn(player, Boundary.BARROWS_HILLS)) 
			return true;
		return false;
	}

	@Override
	public boolean canAttackPlayer(Player attacker, Player victim) {
		return true;
	}

	@Override
	public boolean allowMultiSpells(Player player) {
		return true;
	}

	@Override
	public boolean allowPvPCombat(Player player) {
		if(Boundary.isIn(player, Boundary.BARROWS_HILLS)) 
			return false;
		return true;
	}

	@Override
	public void onDeath(Player player) {
	}

	@Override
	public Position getRespawnLocation(Player player) {
		if(Boundary.isIn(player, Boundary.BARROWS_HILLS)) 
			return Constants.RESPAWN_PLAYER_LOCATION;
		return RESPAWN;
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
		if ((player.getArea().inDuelArena() || Boundary.isIn(player, Boundary.DUEL_ARENAS))) {
			if(Boundary.isIn(player, Boundary.BARROWS_HILLS)) {
				player.write(new SendWalkableInterfacePacket(16128));
				player.getActionSender().disableMap(0);
				player.getBarrows().setBarrowsInterface(player);
				} else
				player.write(new SendWalkableInterfacePacket(201));
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.getActionSender().sendInteractionOption("Attack", 3, true);
			} else if (player.getArea().inDuelArena()) {
				player.getActionSender().sendInteractionOption("Challenge", 3, true);
			} else {
				player.getActionSender().sendInteractionOption("null", 3, true);
			}
		} else {
			player.getActionSender().sendInteractionOption("null", 3, true);
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
		if ((player.getArea().inDuelArena() || Boundary.isIn(player, Boundary.DUEL_ARENAS))) {
			if(Boundary.isIn(player, Boundary.BARROWS_HILLS)) {
			player.write(new SendWalkableInterfacePacket(16128));
			player.getActionSender().disableMap(0);
			player.getBarrows().setBarrowsInterface(player);
			} else
			player.write(new SendWalkableInterfacePacket(201));
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.getActionSender().sendInteractionOption("Attack", 3, true);
			} else if (player.getArea().inDuelArena()) {
				player.getActionSender().sendInteractionOption("Challenge", 3, true);
			} else {
				player.getActionSender().sendInteractionOption("null", 3, true);
			}
		} else {
			player.getActionSender().sendInteractionOption("null", 3, true);
		}
	}

	@Override
	public String toString() {
		return "Duel Arena Controller";
	}

	@Override
	public void onControllerLeave(Player player) {
		player.setAttribute("left_wild_delay", System.currentTimeMillis());
	}

}