package com.model.game.regions.impl;

import com.model.game.character.Entity;
import com.model.game.index.ObjectIndex;
import com.model.game.index.PositionIndex;
import com.model.game.location.Position;
import com.model.game.regions.Areas;

public class GodWars implements Areas{

	@Override
	public void sendFirstClickObject(Entity entity, int object) {
		switch (object) {
		case ObjectIndex.GODWARS_ENTRANCE:
			entity.asPlayer().getPA().movePlayer(PositionIndex.GODWARS_ENTERED);
			entity.getActionSender().sendMessage("You find yourself in an ancient cave...");
			break;
		case ObjectIndex.GODWARS_ROPE_EXIT:
			entity.asPlayer().getPA().movePlayer(PositionIndex.GODWARS_ENTER);
		
		case ObjectIndex.SARADOMIN_ROCK_ENTRANCE_1:
			entity.asPlayer().getPA().movePlayer(PositionIndex.SARADOMIN_ROCK_ENTRANCE);
			break;
		case ObjectIndex.SARADOMIN_ROCK_ENTRANCE_2:
			entity.asPlayer().getPA().movePlayer(PositionIndex.SARADOMIN_ROCK_ENTRANCE_2);
			break;
		case ObjectIndex.SARADOMIN_DOOR:
			if(entity.getX() == 2909) {
				entity.asPlayer().getPA().movePlayer(new Position(2908, 5265, 0));
			} else if(entity.getX() == 2908) {
				entity.asPlayer().getPA().movePlayer(new Position(2909, 5265, 0));
			}
			break;
		case ObjectIndex.BANDOS_DOOR:
			if(entity.getX() == 2862) {
				entity.asPlayer().getPA().movePlayer(new Position(2863, 5354, 2));
			} else if(entity.getX() == 2863) {
				entity.asPlayer().getPA().movePlayer(new Position(2862, 5354, 2));
			}
			break;	
		case ObjectIndex.BANDOS_DOOR_1:
			if(entity.getX() == 2851) {
				entity.asPlayer().getPA().movePlayer(new Position(2850, 5333, 2));
			} else if(entity.getX() == 2850) {
				entity.asPlayer().getPA().movePlayer(new Position(2851, 5333, 2));
			}
			break;
		case ObjectIndex.ARMADYL_DOOR:
			if(entity.getY() == 5294) {
				entity.asPlayer().getPA().movePlayer(new Position(2839, 5295, 2));
			} else if(entity.getY() == 5295 || entity.getY() == 5296) {
				entity.asPlayer().getPA().movePlayer(new Position(2839, 5294, 2));
			}
			break;
		case ObjectIndex.ZAMORAK_RIVER_ENTER:
			if(entity.getY() <= 5332) {
				entity.asPlayer().getPA().movePlayer(PositionIndex.ZAMMY_CROSS_RIVER);
			} else if(entity.getY() >= 5345) {
				entity.asPlayer().getPA().movePlayer(PositionIndex.ZAMMY_LEAVE_RIVER);
			}
			break;
		case 26505:
			if(entity.getY() == 5333) {
				entity.asPlayer().getPA().movePlayer(new Position(2925, 5332, 2));
			} else if(entity.getY() == 5332) {
				entity.asPlayer().getPA().movePlayer(new Position(2925, 5333, 2));
			}
			break;
			
		}
	}

	@Override
	public void sendSecondClickObject(Entity player, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickObject(Entity player, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFirstClickNpc(Entity player, int npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSecondClickNpc(Entity player, int npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickNpc(Entity player, int npc) {
		// TODO Auto-generated method stub
		
	}

}
