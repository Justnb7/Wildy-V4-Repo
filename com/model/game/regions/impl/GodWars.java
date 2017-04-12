package com.model.game.regions.impl;

import com.model.Server;
import com.model.game.Constants;
import com.model.game.character.Entity;
import com.model.game.index.ObjectIndex;
import com.model.game.index.PositionIndex;
import com.model.game.location.Position;
import com.model.game.regions.Areas;
import com.model.task.ScheduledTask;
import com.model.task.Stackable;
import com.model.task.Walkable;

public class GodWars implements Areas {

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
				if(entity.asPlayer().saradominKillCount >= Constants.GODWARS_KILLCOUNT) {
				entity.asPlayer().getPA().movePlayer(new Position(2908, 5265, 0));
				resetKC(entity, 4);
				} else 
				entity.asPlayer().getActionSender().sendMessage("You need "+Constants.GODWARS_KILLCOUNT+" killcount to enter Commander Zilyana's Chamber.");
				
			} else if(entity.getX() == 2908) {
				entity.asPlayer().getPA().movePlayer(new Position(2909, 5265, 0));
			}
			break;
		case ObjectIndex.BANDOS_DOOR:
			if(entity.getX() == 2862) {
				if(entity.asPlayer().bandosKillCount >= Constants.GODWARS_KILLCOUNT) {
				entity.asPlayer().getPA().movePlayer(new Position(2863, 5354, 2));
				resetKC(entity, 3);
				} else 
				entity.asPlayer().getActionSender().sendMessage("You need "+Constants.GODWARS_KILLCOUNT+" killcount to enter General Graador's Chamber.");
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
				if(entity.asPlayer().armadylKillCount >= Constants.GODWARS_KILLCOUNT) {
				entity.asPlayer().getPA().movePlayer(new Position(2839, 5295, 2));
				resetKC(entity, 2);
				} else 
				entity.asPlayer().getActionSender().sendMessage("You need "+Constants.GODWARS_KILLCOUNT+" killcount to enter Kree'Arra's Chamber.");
				
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
				if(entity.asPlayer().zamorakKillCount >= Constants.GODWARS_KILLCOUNT && entity.asPlayer().getRights().isAdministrator()) {
				entity.asPlayer().getPA().movePlayer(new Position(2925, 5332, 2));
				resetKC(entity, 1);
				} else 
				entity.asPlayer().getActionSender().sendMessage("You need "+Constants.GODWARS_KILLCOUNT+" killcount to enter K'ril Tsutsaroth's Chamber.");
			} else if(entity.getY() == 5332) {
				entity.asPlayer().getPA().movePlayer(new Position(2925, 5333, 2));
			}
			break;
			
		}
	}
	/**
	 * Resets Killcount after 5 seconds, incase of 
	 * missclick by entity.
	 * @param player
	 * @param type
	 */
	public void resetKC(Entity player, int type){
		Server.getTaskScheduler().schedule(new ScheduledTask(5) {
			@Override
			public void execute() {
				if(type == 1){//zammy
					player.asPlayer().zamorakKillCount = 0;
					player.getActionSender().sendString("" +  player.asPlayer().zamorakKillCount, 16219);	
				} else if(type == 2){//Arma
					player.asPlayer().zamorakKillCount = 0;
					 player.getActionSender().sendString("" +  player.asPlayer().armadylKillCount, 16216);
				}  else if(type == 3){//Bandos
					player.asPlayer().bandosKillCount = 0;
					  player.getActionSender().sendString("" +  player.asPlayer().bandosKillCount, 16217);
				} else if(type == 4){//Sara
					player.asPlayer().saradominKillCount = 0;
					player.getActionSender().sendString("" +  player.asPlayer().saradominKillCount, 16218);	
				}
				this.stop();
			}
	});
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
