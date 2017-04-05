package com.model.game.character.player.minigames.BarrowsFull;

import java.util.HashSet;
import java.util.Set;

import com.model.game.character.player.Player;


public final class GameObjectManager {
	/**
	 * The amount of objects to spawn per server tick.
	 */
	private static final int SPAWNS_PER_TICK = 32;

	/**
	 * Packet ID for removing objects.
	 */
	private static final int REMOVE_OBJECT_PACKET = 101;

	/**
	 * Packet ID for adding objects.
	 */
	private static final int ADD_OBJECT_PACKET = 151;

	/**
	 * Packet ID for the local position.
	 */
	private static final int LOCAL_POSITION_PACKET = 85;

	/**
	 * Packet ID for the object animation.
	 */
	private static final int OBJECT_ANIMATION_PACKET = 160;

	/**
	 * Position offset. Doesn't matter since we send packet 85.
	 * First 4 bits is the y offset the next four are the x offset.
	 * Ex: (xOff << 4) | yOff
	 * 
	 * The max offset can only be 16 tiles away so it's not that useful.
	 * Maybe for doors or building a rectangle of objects (use packet 60).
	 * */
	private static final int POSITION_OFFSET = 0;


	/**
	 * Place an object in the world for only the user to see.
	 */
	public static void placeLocalObject(Player client, GameObject object) {
		sendLocalPosition(client, object);
		sendAddObject(client, object);
	}

	/**
	 * Play an animation for the user to see.
	 */
	public static void playAnimation(Player client, int animationID, GameObject object) {
		sendLocalPosition(client, object);
		sendObjectAnimation(client, animationID, object);
	}

	/**
	 * Remove an object in the world a user.
	 */
	public static void removeLocalObject(Player client, GameObject object) {
		sendLocalPosition(client, object);
		sendRemoveObject(client, object);
	}


	private static void sendObjectAnimation(Player client, int animationID, GameObject object) {
		int objectDetails = (object.getType() << 2) + (object.getRotation().offset() & 3);

		client.getOutStream().writeFrame(OBJECT_ANIMATION_PACKET);
		client.getOutStream().writeByteS(POSITION_OFFSET);
		client.getOutStream().writeByteS(objectDetails);
		client.getOutStream().writeWordA(animationID);
	}

	/**
	 * Send an objects local position to the client.
	 */
	private static void sendLocalPosition(Player client, GameObject object) {
		int objectX = object.getPosition().getX();
		int objectY = object.getPosition().getY();
		int realMapX = client.getMapRegionX() << 3;
		int realMapY = client.getMapRegionY() << 3;
		int localX = objectX - realMapX;
		int localY = objectY - realMapY;

		client.getOutStream().writeFrame(LOCAL_POSITION_PACKET);
		client.getOutStream().writeByteC(localY);
		client.getOutStream().writeByteC(localX);
	}

	/**
	 * Remove an object from the map. Objects ID must be -1.
	 * 
	 * Don't use this, this is private use. Instead use removeObject.
	 */
	private static void sendRemoveObject(Player client, GameObject object) {
		int objectID = object.getID();

		if (objectID == -1) {
			int objectDetails = (object.getType() << 2) + (object.getRotation().offset() & 3);
			client.getOutStream().writeFrame(REMOVE_OBJECT_PACKET);
			client.getOutStream().writeByteC(objectDetails);
			client.getOutStream().writeByte(POSITION_OFFSET);
			client.flushOutStream();
		}
	}

	/**
	 * Add and object to the map.
	 * 
	 * Don't use this, this is private use. Instead use placeObject.
	 */
	private static void sendAddObject(Player client, GameObject object) {
		int objectID = object.getID();
		int objectDetails = (object.getType() << 2) + (object.getRotation().offset() & 3);

		client.getOutStream().writeFrame(ADD_OBJECT_PACKET);
		client.getOutStream().writeByte(POSITION_OFFSET);
		client.getOutStream().writeWordBigEndian(objectID); // I renamed this this because BigEndian is wrong.
		client.getOutStream().writeByteS(objectDetails);
		client.flushOutStream();
	}
}