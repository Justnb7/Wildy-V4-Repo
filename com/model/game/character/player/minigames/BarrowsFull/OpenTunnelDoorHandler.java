package com.model.game.character.player.minigames.BarrowsFull;

import java.util.ArrayList;
import java.util.List;

import com.model.Server;
import com.model.game.character.npc.NPC;
import com.model.game.character.npc.NPCHandler;
import com.model.game.character.player.Player;
import com.model.task.ScheduledTask;



public class OpenTunnelDoorHandler {

	private final Player client;

	private ScheduledTask doorTask;

	private boolean moved;

	private int targetX;

	private int targetY;

	public OpenTunnelDoorHandler(Player client) {
		this.client = client;
	}

	public boolean openDoor(int objectID, int objectX, int objectY) {
		TunnelDoor door = TunnelDoors.getDoor(objectX, objectY);
		
		if (door == null || door.getID() != objectID) {
			client.getActionSender().sendMessage("Null1");
			return false;
		}

		if (doorTask != null) {
			client.getActionSender().sendMessage("Null2");
			doorTask.stop();
		}
		
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (client.goodDistance(objectX, objectY, client.absX, client.absY, 1)) {
					BarrowsPuzzleDisplay puzzle = client.getBarrows().getPuzzle();
					client.getActionSender().sendMessage("Null3");
					if (BarrowsConstants.CHEST_DOORS.contains(objectID) && !puzzle.isSolved()) {
						puzzle.displayInterface(client);
						stop();
					} else {
						walkThroughDoor(this, door, objectX, objectY);
						client.getActionSender().sendMessage("Null4");
						this.stop();
					}
				}
				
			}
			
			@Override
			public void onStop() {
				moved = false;
			}
		});

		return true;
	}

	private void walkThroughDoor(ScheduledTask task, TunnelDoor door, int objectX, int objectY) {
		if (!moved) {
			targetX = objectX;
			targetY = objectY;

			if (client.absX == objectX) {
				if (door.getRotation() == Rotation.EAST) {
					targetX = objectX - 1;
				} else if (door.getRotation() == Rotation.WEST) {
					targetX = objectX + 1;
				}
			}
			if (client.absY == objectY) {
				if (door.getRotation() == Rotation.NORTH) {
					targetY = objectY + 1;
				} else if (door.getRotation() == Rotation.SOUTH) {
					targetY = objectY - 1;
				}
			}

			int deltaX = targetX - client.absX;
			int deltaY = targetY - client.absY;
			client.getPA().walkTo(deltaX, deltaY);
			//client.getPA().appendWalkingQueue(targetX, targetY);
			moved = true;
		}

		if (moved) {
			if (client.absX == targetX && client.absY == targetY) {
				//spawnRandomMonster(targetX, targetY);
				task.stop();
			}
		}
	}

	private void spawnRandomMonster(int x, int y) {
		BarrowsNpcController npcConroller = client.getBarrows().getNpcController();

		List<Integer> npcs = new ArrayList<>();
		npcs.addAll(BarrowsConstants.DOOR_NPCS);
		npcs.addAll(BarrowsConstants.DOOR_NPCS);
		npcs.addAll(BarrowsConstants.DOOR_NPCS);
		npcs.addAll(generateBarrowsNpcs());

		// Pick a random npc index.
		
		int randomNpcID = npcs.get((int) (Math.random() * npcs.size()));
		Brother brother = Brother.lookup(randomNpcID);
		NPC npc = null;
		
		if (brother != null) {
			npcConroller.registerSpawn(brother);
			NPCHandler.spawnNpc(brother.getID(), x, y, client.heightLevel, 0, 1, 1, 1, 1);
		} else {
			 NPCHandler
					.spawnNpc(randomNpcID, x, y, client.heightLevel, 0, 1, 1, 1, 1);
		}
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			
			@Override
			public void execute() {
				if (client.distanceToPoint(npc.getX(), npc.getY()) > 4) {
					//NPCHandler.relocate(npc, -1, -1, 0);
				//	NPCHandler.npcs[npc.npcId] = null;
					if (brother != null) {
						npcConroller.unregisterSpawn(brother);
					}
					stop();
				}
			}
				
			
			
			@Override
			public void onStop() {
				moved = false;
			}
		});

		
	}

	private List<Integer> generateBarrowsNpcs() {
		BarrowsNpcController npcConroller = client.getBarrows().getNpcController();

		// If a brother isn't killed or isn't spawned, add to the list.
		List<Integer> npcs = new ArrayList<>();
		for (Brother brother : Brother.values()) {
			if (!npcConroller.checkIfKilled(brother) && !npcConroller.checkIfSpawned(brother)) {
				npcs.add(brother.getID());
			}
		}
		return npcs;
	}
}