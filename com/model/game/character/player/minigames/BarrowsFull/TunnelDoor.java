package com.model.game.character.player.minigames.BarrowsFull;

import com.model.game.character.player.Player;
import com.model.game.location.Position;

public class TunnelDoor extends GameObject {

	public TunnelDoor(int doorID, Position position, Rotation rotation) {
		super(doorID, position, 0, rotation);
	}

	public static TunnelDoor create(int doorID, Position position, Rotation rotation) {
		return new TunnelDoor(doorID, position, rotation);
	}

	public void resetDoor(Player client) {
		GameObjectManager.placeLocalObject(client, this);
	}

	public void closeDoor(Player client, int closeID) {
		GameObjectManager.placeLocalObject(client, new GameObject(closeID, getPosition(), 0, getRotation()));
	}
}