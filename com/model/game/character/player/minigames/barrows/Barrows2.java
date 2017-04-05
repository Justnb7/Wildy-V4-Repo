package com.model.game.character.player.minigames.barrows;
/*package com.model.game.character.player.minigames.barrows;

import com.model.game.World;
import com.model.game.character.npc.NPC;
import com.model.game.character.npc.NPCHandler;
import com.model.game.character.player.Player;
import com.model.game.character.player.packets.out.SendInterfacePacket;
import com.model.game.item.Item;
import com.model.game.location.Location;
import com.model.game.location.Position;
import com.model.game.object.GlobalObject;
import com.model.utility.Utility;
import com.model.utility.json.definitions.ItemDefinition;

*//**
 * Handles the Barrows minigame and it's objects, npcs, etc.
 * 
 * @editor Gabbe
 *//*
public class Barrows {
	
	static Position DHAROKS_HILL = new Position(3574, 3297);
	static Position VERACS_HILL = new Position(3557, 3297);
	static Position AHRIMS_HILL = new Position(3565, 3288);
	static Position TORAGS_HILL = new Position(3554, 3282);
	static Position GUTHANS_HILL = new Position(3577, 3282);
	static Position KARILS_HILL = new Position(3566, 3275);
	
	
	public static void handleLogin(Player player) {
		updateInterface(player);
	}
	public static final int[][] BROTHER_AREA = { { 3563, 3285, 3567, 3291 }, { 3563, 3273, 3568, 3278 }, { 3573, 3295, 3578, 3300 }, { 3554, 3294, 3560, 3300 }, { 3551, 3280, 3556, 3285 }, { 3575, 3280, 3580, 3284 }, };
	public static final int[][] PLAYER_ENTRE = { { 3557, 9703 }, { 3546, 9684 }, { 3556, 9718 }, { 3578, 9706 }, { 3568, 9683 }, { 3534, 9704 }, };

	public static boolean digToBrother(final Player c) {
		//c.startAnimation(830);
		for (int i = 0; i < BROTHER_AREA.length; i++) {
			if (c.absX >= BROTHER_AREA[i][0] && c.absX <= BROTHER_AREA[i][2] && c.absY >= BROTHER_AREA[i][1] && c.absY <= BROTHER_AREA[i][3]) {

				c.getPA().movePlayer(c.teleportToX = PLAYER_ENTRE[i][0], c.teleportToY = PLAYER_ENTRE[i][1], 3);
			//	c.sendMessage("If your Barrows Npc TEXT doesn't change to red re click on the coffin.");
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBarrowsNpc(int npc){
		switch (npc){
	case 1677://ahrims
	case 1676: // torag
	case 1675://karil
	case 1674://guthan
	case 1673://dharok
	case 1672://verac
		return true;
		
		}
		return false;
	}
	
	*//**
	 * Handles all objects in the Barrows minigame: Coffins, doors, etc.
	 * 
	 * @param player
	 *            The player calling this method
	 * @param object
	 *            The object the player is requesting
	 *//*
	public static boolean handleObject(final Player player, GlobalObject object) {
		switch (object.getObjectId()) {
		
		case 20668:
			player.getPA().movePlayer(DHAROKS_HILL);
			return true;
		case 20672:
			player.getPA().movePlayer(VERACS_HILL);
			return true;
		case 20667:
			player.getPA().movePlayer(AHRIMS_HILL);
			return true;
		case 20671:
			player.getPA().movePlayer(TORAGS_HILL);
			return true;
		case 20669:
			player.getPA().movePlayer(GUTHANS_HILL);
			return true;
		case 20670:
			player.getPA().movePlayer(KARILS_HILL);
			return true;
			
		case 20720: // Dharoks 2026
			searchCoffin(player, object.getObjectId(), 4, 1673, object.getPosition() != null
					? new Position(3557, 9715, player.getPosition().getZ()) : new Position(3552, 9693));
			return true;
		case 20772: // verac 2030
			searchCoffin(player, object.getObjectId(), 0, 1672, object.getPosition() != null
					? new Position(3575, 9704, player.getPosition().getZ()) : new Position(3552, 9693));
			return true;
		case 20770: //ahrim
			searchCoffin(player, object.getObjectId(), 5, 1677, object.getPosition() != null
					? new Position(3557, 9699, player.getPosition().getZ()) : new Position(3552, 9693));
			return true;
		case 20721: //torag
			searchCoffin(player, object.getObjectId(), 1, 1676, object.getPosition() != null
					? new Position(3568, 9683, player.getPosition().getZ()) : new Position(3552, 9693));
			return true;
		case 20771: //karil
			searchCoffin(player, object.getObjectId(), 2, 1675, object.getPosition() != null
					? new Position(3549, 9681, player.getPosition().getZ()) : new Position(3552, 9693));
			return true;
		case 20722:  //guthan
			searchCoffin(player, object.getObjectId(), 3, 1674, object.getPosition() != null
					? new Position(3537, 9703, player.getPosition().getZ()) : new Position(3552, 9693));
			return true;
		case 20710:
			if (object.getPosition().getX() == 3535 && object.getPosition().getY() == 9684) {
				player.getPA().movePlayer(new Position(3535, 9689));
				return true;
			} else if (object.getPosition().getX() == 3534 && object.getPosition().getY() == 9688) {
				player.getPA().movePlayer(new Position(3534, 9683));
				return true;
			}
			break;
		case 20691:
			if (object.getPosition().getX() == 3535 && object.getPosition().getY() == 9688) {
				player.getPA().movePlayer(new Position(3535, 9683));
				return true;
			} else if (object.getPosition().getX() == 3534 && object.getPosition().getY() == 9684) {
				player.getPA().movePlayer(new Position(3534, 9689));
				return true;
			}
			break;
		case 20702:
			if (object.getPosition().getX() == 3535 && object.getPosition().getY() == 9701) {
				player.getPA().movePlayer(new Position(3535, 9706));
				return true;
			} else if (object.getPosition().getX() == 3534 && object.getPosition().getY() == 9705) {
				player.getPA().movePlayer(new Position(3534, 9700));
				return true;
			}
			break;
		case 20683:
			if (object.getPosition().getX() == 3534 && object.getPosition().getY() == 9701) {
				player.getPA().movePlayer(new Position(3534, 9706));
				return true;
			} else if (object.getPosition().getX() == 3535 && object.getPosition().getY() == 9705) {
				player.getPA().movePlayer(new Position(3535, 9700));
				return true;
			}
			break;
		case 20684:
			if (object.getPosition().getX() == 3541 && object.getPosition().getY() == 9712) {
				player.getPA().movePlayer(new Position(3546, 9712));
				return true;
			} else if (object.getPosition().getX() == 3545 && object.getPosition().getY() == 9711) {
				player.getPA().movePlayer(new Position(3540, 9711));
				return true;
			}
			break;
		case 20703:
			if (object.getPosition().getX() == 3541 && object.getPosition().getY() == 9711) {
				player.getPA().movePlayer(new Position(3546, 9711));
				return true;
			} else if (object.getPosition().getX() == 3545 && object.getPosition().getY() == 9712) {
				player.getPA().movePlayer(new Position(3540, 9712));
				return true;
			}
			break;
		case 20705:
			if (object.getPosition().getX() == 3558 && object.getPosition().getY() == 9711) {
				player.getPA().movePlayer(new Position(3563, 9711));
				return true;
			} else if (object.getPosition().getX() == 3562 && object.getPosition().getY() == 9712) {
				player.getPA().movePlayer(new Position(3557, 9712));
				return true;
			}
			break;
		case 20686:
			if (object.getPosition().getX() == 3558 && object.getPosition().getY() == 9712) {
				player.getPA().movePlayer(new Position(3563, 9712));
				return true;
			} else if (object.getPosition().getX() == 3562 && object.getPosition().getY() == 9711) {
				player.getPA().movePlayer(new Position(3557, 9711));
				return true;
			}
			break;
		case 20706:
			if (object.getPosition().getX() == 3568 && object.getPosition().getY() == 9705) {
				player.getPA().movePlayer(new Position(3568, 9700));
				return true;
			} else if (object.getPosition().getX() == 3569 && object.getPosition().getY() == 9701) {
				player.getPA().movePlayer(new Position(3569, 9706));
				return true;
			}
			break;
		case 20687:
			if (object.getPosition().getX() == 3569 && object.getPosition().getY() == 9705) {
				player.getPA().movePlayer(new Position(3569, 9700));
				return true;
			} else if (object.getPosition().getX() == 3568 && object.getPosition().getY() == 9701) {
				player.getPA().movePlayer(new Position(3568, 9706));
				return true;
			}
			break;
		case 20712:
			if (object.getPosition().getX() == 3568 && object.getPosition().getY() == 9688) {
				player.getPA().movePlayer(new Position(3568, 9683));
				return true;
			} else if (object.getPosition().getX() == 3569 && object.getPosition().getY() == 9684) {
				player.getPA().movePlayer(new Position(3569, 9689));
				return true;
			}
			break;
		case 20693:
			if (object.getPosition().getX() == 3569 && object.getPosition().getY() == 9688) {
				player.getPA().movePlayer(new Position(3569, 9683));
				return true;
			} else if (object.getPosition().getX() == 3568 && object.getPosition().getY() == 9684) {
				player.getPA().movePlayer(new Position(3568, 9689));
				return true;
			}
			break;
		case 20714:
			if (object.getPosition().getX() == 3562 && object.getPosition().getY() == 9678) {
				player.getPA().movePlayer(new Position(3557, 9678));
				return true;
			} else if (object.getPosition().getX() == 3558 && object.getPosition().getY() == 9677) {
				player.getPA().movePlayer(new Position(3563, 9677));
				return true;
			}
			break;
		case 20695:
			if (object.getPosition().getX() == 3562 && object.getPosition().getY() == 9677) {
				player.getPA().movePlayer(new Position(3557, 9677));
				return true;
			} else if (object.getPosition().getX() == 3558 && object.getPosition().getY() == 9678) {
				player.getPA().movePlayer(new Position(3563, 9678));
				return true;
			}
			break;
		case 20713:
			if (object.getPosition().getX() == 3545 && object.getPosition().getY() == 9678) {
				player.getPA().movePlayer(new Position(3540, 9678));
				return true;
			} else if (object.getPosition().getX() == 3541 && object.getPosition().getY() == 9677) {
				player.getPA().movePlayer(new Position(3546, 9677));
				return true;
			}
			break;
		case 20694:
			if (object.getPosition().getX() == 3545 && object.getPosition().getY() == 9677) {
				player.getPA().movePlayer(new Position(3540, 9677));
				return true;
			} else if (object.getPosition().getX() == 3541 && object.getPosition().getY() == 9678) {
				player.getPA().movePlayer(new Position(3546, 9678));
				return true;
			}
			break;
		case 10284:
			if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() < 5)
				return true;
			if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player
					.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] == 0) {
				
				
				
				handleObject(player, new GlobalObject(COFFIN_AND_BROTHERS[player.getMinigameAttributes()
						.getBarrowsMinigameAttributes().getRandomCoffin()][0], null));
				
				
				
				player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player
						.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] = 1;
				return true;
			} else if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player
					.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] == 1) {
				player.getActionSender().sendMessage("You cannot loot this whilst in combat!");
				return true;
			} else if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player
					.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] == 2
					&& player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() >= 6) {
				if (player.getInventory().freeSlot() < 2) {
					player.getActionSender()
							.sendMessage("You need at least 2 free inventory slots to loot this chest.");
					return true;
				}
				resetBarrows(player);
				player.getInventory().add(new Item(randomRunes(), 25 + Utility.getRandom(100)));
				player.getInventory().add(new Item(randomRunes(), 25 + Utility.getRandom(200)));
				player.getInventory().add(new Item(randomRunes(), 25 + Utility.getRandom(300)));
				player.getInventory().add(new Item(995, Utility.inclusiveRandom(1500, 25000)));
				player.barrowsChestsLooted++;
				if (Utility.getRandom(100) >= 92) {
					int b = randomBarrows();
					int dragonKite = Utility.inclusiveRandom(1, 132);
					player.getInventory().add(new Item(b, 1));
					player.getActionSender().sendMessage("<icon=1><col=FF8C38><shad=0> " + player.getName() + " has just received " + ItemDefinition.forId(b).getName() + " from Barrows!");
					player.barrowsChestRewards++;
//					if(dragonKite == 1) {
//						player.getInventory().add(11613, 1);
//						World.sendMessage("<icon=1><col=FF8C38><shad=0> " + player.getUsername() + " has been rewarded a "
//								+ ItemDefinition.forId(11613).getName() + " from Barrows!");
//						player.barrowsChestRewards++;
//					}
				}
				player.getActionSender().sendShakeScreen(3, 2, 3, 2);
				player.getActionSender().sendMessage("The cave begins to collapse!");
			//	TaskManager.submit(new CeilingCollapseTask(player));
				player.getActionSender().sendMessage("You have looted this chest "+player.getBarrowsChestsLooted()+" times and recieved "+player.getBarrowsChestRewards()+" Barrows pieces.");
			}
			break;
		case 6744:
		case 6725:
			if (player.getPosition().getX() == 3563)
				showRiddle(player);
			break;
		case 6746:
		case 6727:
			if (player.getPosition().getY() == 9683)
				showRiddle(player);
			break;
		case 6743:
		case 6724:
			if (player.getPosition().getX() == 3540)
				showRiddle(player);
			break;
		case 6739:
		case 6720:
			if (player.getPosition().getY() == 9706)
				player.getPA().movePlayer(new Position(3551, 9694));
			break;
		}
		return false;
	}

	public static void showRiddle(Player player) {
		player.getActionSender().sendString("1.", 4553);
		player.getActionSender().sendString("2.", 4554);
		player.getActionSender().sendString("3.", 4555);
		player.getActionSender().sendString("4.", 4556);
		player.getActionSender().sendString("Which item comes next?", 4549);
		int riddle = Utility.getRandom(riddles.length - 1);
		player.getActionSender().moveComponent(4545, riddles[riddle][0], 200);
		player.getActionSender().moveComponent(4546, riddles[riddle][1], 200);
		player.getActionSender().moveComponent(4547, riddles[riddle][2], 200);
		player.getActionSender().moveComponent(4548, riddles[riddle][3], 200);
		player.getActionSender().moveComponent(4550, riddles[riddle][4], 200);
		player.getActionSender().moveComponent(4551, riddles[riddle][5], 200);
		player.getActionSender().moveComponent(4552, riddles[riddle][6], 200);
		player.getMinigameAttributes().getBarrowsMinigameAttributes().setRiddleAnswer(riddles[riddle][7]);
		player.write(new SendInterfacePacket(4543));
	}

	public static void handlePuzzle(Player player, int puzzleButton) {
		if (puzzleButton == player.getMinigameAttributes().getBarrowsMinigameAttributes().getRiddleAnswer()) {
			player.getPA().movePlayer(new Position(3551, 9694));
			player.getActionSender().sendMessage("You got the correct answer.");
			player.getActionSender().sendMessage("A magical force guides you to a chest located in the center room.");
		} else
			player.getActionSender().sendMessage("You got the wrong answer.");
		player.getMinigameAttributes().getBarrowsMinigameAttributes().setRiddleAnswer(-1);
	}

	public static int[][] riddles = { { 2349, 2351, 2353, 2355, 2359, 2363, 2361, 0 } };

	*//**
	 * Handles coffin searching
	 * 
	 * @param player
	 *            Player searching a coffin
	 * @param obj
	 *            The object (coffin) being searched
	 * @param coffinId
	 *            The coffin's array index
	 * @param npcId
	 *            The NPC Id of the NPC to spawn after searching
	 *//*
	public static void searchCoffin(final Player player, final int obj, final int coffinId, int npcId,
			Position spawnPos) {
		player.getActionSender().clearScreen();
		
		if (player.getPosition().getZ() == 3) {
			if (selectCoffin(player, obj))
				return;
		}
		System.out.println(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[coffinId][1]);
		if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[coffinId][1] == 0) {
			NPC npc = NPCHandler.spawnNpc(player, npcId, player.getX() + 1, player.getY(), player.getZ(), 0, true, false, false);
				player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[coffinId][1] = 1;
		} else {
			player.getActionSender().sendMessage("You have already searched this sarcophagus.");
		}
	}

	public static void resetBarrows(Player player) {
		player.getMinigameAttributes().getBarrowsMinigameAttributes().setKillcount(0);
		for (int i = 0; i < player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData().length; i++)
			player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][1] = 0;
		updateInterface(player);
		player.getMinigameAttributes().getBarrowsMinigameAttributes().setRandomCoffin(getRandomCoffin());
	}

	public static final Object[][] data = { { "Verac The Defiled", 37203 }, { "Torag The Corrupted", 37205 },
			{ "Karil The Tainted", 37207 }, { "Guthan The Infested", 37206 }, { "Dharok The Wretched", 37202 },
			{ "Ahrim The Blighted", 37204 } };

	*//**
	 * Deregisters an NPC located in the Barrows minigame
	 * 
	 * @param player
	 *            The player that's the reason for deregister
	 * @param killed
	 *            Did player kill the NPC?
	 *//*
	public static void killBarrowsNpc(Player player, NPC n, boolean killed) {
		if (player == null || n == null)
			return;
		if (n.getId() == 58) {
			player.getMinigameAttributes().getBarrowsMinigameAttributes()
					.setKillcount(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() + 1);
			updateInterface(player);
			return;
		}
		int arrayIndex = getBarrowsIndex(player, n.getId());
		if (arrayIndex < 0)
			return;
		if (killed && player.getRegionInstance() != null) {
			player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[arrayIndex][1] = 2;
			player.getActionSender().sendMessage("Killed brother:: "+player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[arrayIndex][0]);
			
			player.getMinigameAttributes().getBarrowsMinigameAttributes()
					.setKillcount(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() + 1);
			player.getActionSender().sendMessage("KillCount is now: "+player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount());
		//	player.getRegionInstance().getNpcsList().remove(player);
		} else if (arrayIndex >= 0)
			player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[arrayIndex][1] = 0;
		updateInterface(player);
	}

	*//**
	 * Selects the coffin and shows the interface if coffin id matches random
	 * coffin
	 **//*
	public static boolean selectCoffin(Player player, int coffinId) {
		if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin() == 0)
			player.getMinigameAttributes().getBarrowsMinigameAttributes().setRandomCoffin(getRandomCoffin());
		if (COFFIN_AND_BROTHERS[player.getMinigameAttributes().getBarrowsMinigameAttributes()
				.getRandomCoffin()][0] == coffinId) {
		//	player.getDialog().start(new BarrowsTunnel(player));
			return true;
		}
		return false;
	}

	public static int getBarrowsIndex(Player player, int id) {
		int index = -1;
		for (int i = 0; i < player.getMinigameAttributes().getBarrowsMinigameAttributes()
				.getBarrowsData().length; i++) {
			if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][0] == id) {
				index = i;
			}
		}
		return index;
	}

	public static void updateInterface(Player player) {
		for (int i = 0; i < data.length; i++) {
			boolean killed = player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][1] == 2;
			String s = killed ? "@gre@" : "@red@";
			player.getActionSender().sendString("" + s + "" + (String) data[i][0], (int) data[i][1]);
		}
		player.getActionSender().sendString("Killcount: " + player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount(),
				37208);
	}

	public static void fixBarrows(Player player) {
		player.getActionSender().clearScreen();
		int totalCost = 0;
		int money = player.getInventory().getAmount(995);
		boolean breakLoop = false;
		for (Item items : player.getInventory().getItems()) {
			if (items == null)
				continue;
			for (int i = 0; i < brokenBarrows.length; i++) {
				if (player.getInventory().getSlot(items.getId()) > 0) {
					if (items.getId() == brokenBarrows[i][1]) {
						if (totalCost + 45000 > money) {
							breakLoop = true;
							player.getActionSender().sendMessage("You need at least 45000 coins to fix this item.");
							break;
						} else {
							totalCost += 45000;
							player.getInventory().setItem(player.getInventory().getSlot(items.getId()),
									new Item(brokenBarrows[i][0], 1));
							player.getInventory().refresh();
						}
					}
				}
			}
			if (breakLoop)
				break;
		}
		if (totalCost > 0)
			player.getInventory().delete(new Item(995), totalCost);
	}

	public static int runes[] = { 4740, 558, 560, 565 };

	public static int barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734,
			4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759};

	public static final int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 },
			{ 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 },
			{ 4730, 4926 }, { 4745, 4956 }, { 4747, 4926 }, { 4749, 4968 }, { 4751, 4994 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public static final int[][] COFFIN_AND_BROTHERS = { { 6823, 2030 }, { 6772, 2029 }, { 6822, 2028 }, { 6773, 2027 },
			{ 6771, 2026 }, { 6821, 2025 } };

	public static boolean isBarrowsNPC(int id) {
		for (int i = 0; i < COFFIN_AND_BROTHERS.length; i++) {
			if (COFFIN_AND_BROTHERS[i][1] == id)
				return true;
		}
		return false;
	}

	public static final Position[] UNDERGROUND_SPAWNS = { new Position(3569, 9677), new Position(3535, 9677),
			new Position(3534, 9711), new Position(3569, 9712) };

	public static int getRandomCoffin() {
		return Utility.getRandom(COFFIN_AND_BROTHERS.length - 1);
	}

	public static int randomRunes() {
		return runes[(int) (Math.random() * runes.length)];
	}

	public static int randomBarrows() {
		return barrows[(int) (Math.random() * barrows.length)];
	}

}
*/