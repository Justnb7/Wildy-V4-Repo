package com.model.game.character.player.serialize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Iterables;
import com.model.game.World;
import com.model.game.character.combat.magic.SpellBook;
import com.model.game.character.npc.BossDeathTracker.BossName;
import com.model.game.character.npc.SlayerDeathTracker.SlayerNpcName;
import com.model.game.character.player.Player;
import com.model.game.character.player.Rights;
import com.model.game.character.player.Skills;
import com.model.game.character.player.content.bounty_hunter.BountyHunterConstants;
import com.model.game.character.player.content.cluescrolls.ClueDifficulty;
import com.model.game.character.player.content.cluescrolls.ClueScroll;
import com.model.game.character.player.content.cluescrolls.ClueScrollContainer;
import com.model.game.character.player.dialogue.impl.slayer.interfaceController.SlayerInterface;
import com.model.game.character.player.minigames.BarrowsFull.Barrows;
import com.model.game.character.player.minigames.BarrowsFull.Brother;
import com.model.game.item.Item;
import com.model.game.item.bank.BankItem;
import com.model.utility.Utility;
import com.model.utility.logging.PlayerLogging;
import com.model.utility.logging.PlayerLogging.LogType;



/**
 * Handles all player serialization
 *
 * @author Sanity
 * @author Mobster
 * @author Patrick van Elderen
 */
public class PlayerSerialization {

    /**
     */
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static int loadGame(Player p, String playerName, String playerPass) {
        return loadGame(p, playerName, playerPass, false);
    }
    
    /**
	 * The log directory which we can use to log data
	 */
	public static final File CHARACTER_DIRECTORY = new File("./Data/characters/");
	
	/**
	 * Gets the directory for the characters
	 * 
	 * @return The directory for the characters
	 */
	public static String getCharacterDirectory() {
		return CHARACTER_DIRECTORY.getPath();
	}
	
    /**
     * Loading
     *
     * @throws IOException
     */
	public static int loadGame(Player p, String playerName, String playerPass, boolean withoutPass) {
		playerName = playerName.toLowerCase();

		Path path = Paths.get(getCharacterDirectory(), playerName.charAt(0) + File.separator + playerName + ".txt");
		
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			int mode = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {

					String key = line.substring(0, spot).trim();
					String value = line.substring(spot + 1).trim();
					String[] values = value.split("\t");

					
					switch (mode) {
					
					//Account
					case 1:
						if (key.equals("character-password")) {
                            p.passHash = value;
                            if (!withoutPass) {
                                if (!Utility.md5Hash(playerPass).equals(value)) {
                                    return 3;
                                }
                            }
						} else if (key.equals("character-rights")) {
							Rights right = Rights.get(Integer.parseInt(value));
							p.setRights(right);
						} else if (key.equals("character-identity")) {
							if (!p.getAttribute("identity", "").equalsIgnoreCase(value)) {
								PlayerLogging.write(LogType.CHANGE_IDENTITY, p, "Identity Changed: previous = " + value + ", new = " + p.getAttribute("identity", ""));
							}
							p.setIdentity(p.getAttribute("character-identity", ""));
						} else if (key.equals("character-mac-address")) {
							if (!p.getAttribute("mac-address", "").equalsIgnoreCase(value)) {
								PlayerLogging.write(LogType.CHANGE_MAC_ADDRESS, p, "Mac Address Changed: previous = " + value + ", new = " + p.getAttribute("mac-address", ""));
							}
							p.setMacAddress(p.getAttribute("character-mac-address", ""));
						} else if (key.equals("character-account-tutorial")) {
							p.setTutorial(Boolean.parseBoolean(value));
						} else if (key.equals("character-received-starter")) {
							p.setReceivedStarter(Boolean.parseBoolean(value));
						} else if (key.equals("character-account-type")) {
							p.setGameMode(value);
						} else if (key.equals("character-posx")) {
							p.teleportToX = (Integer.parseInt(value) <= 0 ? 3091 : Integer.parseInt(value));
						} else if (key.equals("character-posy")) {
							p.teleportToY = (Integer.parseInt(value) <= 0 ? 3502 : Integer.parseInt(value));
						} else if (key.equals("character-height")) {
							p.teleHeight = Integer.parseInt(value);
    					} else if (key.equals("character-special-amount")) {
                            p.setSpecialAmount(Integer.parseInt(value));
    					}
						break;
						
					//Character	
					case 2:
						if (key.equals("magic-book")) {
							// read right book
							for( SpellBook b : SpellBook.values()) {
								if (value.equalsIgnoreCase(b.name())) {
									p.setSpellBook(b);
									break;
								}
							}
						} else if (key.equals("exp-counter")) {
							p.getSkills().setExpCounter(Integer.parseInt(value));
						} else if (key.equals("recoil")) {
							p.setRecoil(Integer.parseInt(value));
						} else if (key.equals("ringOfSuffering")) {
							p.setROSuffering(Integer.parseInt(value));
						} else if (key.equals("votePoints")) {
							p.setVotePoints(Integer.parseInt(value));
                        } else if (key.equals("totalVotes")) {
                        	p.setTotalVotes(Integer.parseInt(value));
                        } else if (key.equals("pkPoints")) {
                        	p.setPkPoints(Integer.parseInt(value));
                        } else if (key.equals("slayerPoints")) {
                        	p.setSlayerPoints(Integer.parseInt(value));
                        } else if (key.equals("achievement-points")) {
							p.getAchievements().setPoints(Integer.parseInt(value));
                        } else if (key.equals("amount-donated")) {
                        	p.setAmountDonated(Integer.parseInt(value));
                        } else if (key.equals("total-amount-donated")) {
                        	p.setTotalAmountDonated(Integer.parseInt(value));
                        } else if (key.equals("teleblock-length")) {
                            p.teleblock.reset();
                            p.teleblockLength = Integer.parseInt(value);
                        } else if (key.equals("muted")) {
    						p.isMuted = Boolean.parseBoolean(value);
                        } else if (key.equals("skull-timer")) {
                            p.skullTimer = Integer.parseInt(value);
						} else if (key.equals("infection-type")) {
                            p.infection = Integer.parseInt(value);
						} else if (key.equals("can-teleport-to-slayer-tasks")) {
							p.setCanTeleportToTask(Boolean.parseBoolean(value));
						} else if (key.equals("slayer-task")) {
                            p.setSlayerTask(Integer.parseInt(value));
                        } else if (key.equals("task-amount")) {
                        	p.setSlayerTaskAmount(Integer.parseInt(value));
                        } else if (key.equals("task-difficulity")) {
                        	p.setSlayerTaskDifficulty(Integer.parseInt(value));
                        } else if (key.equals("first-slayer-task-completed")) {
                             p.setFirstSlayerTask(Boolean.parseBoolean(value));
                        } else if (key.equals("first-boss-slayer-task-completed")) {
                            p.setFirstBossSlayerTask(Boolean.parseBoolean(value));
                        } else if (key.equals("slayer-streak")) {
                            p.setSlayerStreak(Integer.parseInt(value));
                        } else if (key.equals("slayer-streakrecord")) {
                            p.setSlayerStreakRecord(Integer.parseInt(value));     
                        } else if (key.equals("lastClanChat")) {
                            p.setTempKey(value);
                        } else if (key.equals("clanChatPunishment")) {
                            p.isClanMuted = Boolean.parseBoolean(value);
						} else if(key.equals("gear-points")) {
							p.setGearPoints(Integer.parseInt(value));
						} else if (key.equals("unlock-preserve")) {
                            p.setPreserveUnlocked(Boolean.parseBoolean(value));
						} else if (key.equals("unlock-rigour")) {
                            p.setRigourUnlocked(Boolean.parseBoolean(value));
						} else if (key.equals("unlock-augury")) {
                            p.setAuguryUnlocked(Boolean.parseBoolean(value));
						}
						try {
						if(key.equals("barrows-broskilled")) {
							int bros= Integer.parseInt(value);
							p.getBarrows().getNpcController().setKilledBrothers(bros);
						} else if(key.equals("barrows-target")) {
							int brotherID = Integer.parseInt(value);
							p.getBarrows().getNpcController().setTargetBrother(Brother.lookup(brotherID));
						} else if(key.equals("barrows-looted")) {
							boolean looted = Boolean.parseBoolean(value);
							p.getBarrows().getChest().setLooted(looted);
						} else if(key.equals("barrows-kc")) {
							int kc = Integer.parseInt(value);
							p.getBarrows().getNpcController().setKillCount(kc);
						}
				
                    	
						/*} else if(key.equals("unlocked")) {
                    		System.out.print("Adding player unlocks: "+value);
                    		p.getSlayerInterface().getUnlocks().put(Integer.parseInt(key), value);
                    	}*/
						
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					
					// Equipment
					case 3:
						if (key.equals("character-equip")) {
							p.playerEquipment[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.playerEquipmentN[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
						}
						break;

					// Look
					case 4:
						if (key.equals("character-look")) {
							p.playerAppearance[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
						}
						break;

					// Skills
					case 5:
						if (key.equals("character-skill")) {
							p.getSkills().getAllDynamicLevels()[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.getSkills().getAllXP()[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
						}
						break;

					// Inventory Items
					case 6:
						if (key.equals("character-item")) {
							p.playerItems[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.playerItemsN[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
						}
						break;

					// Bank
					case 7:
						if (key.equals("character-bank")) {
							p.bankItems[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.bankItemsN[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
							p.getBank().getBankTab()[0].add(new BankItem(Integer.parseInt(values[1]), Integer.parseInt(values[2])));
						} else if (key.equals("bank-tab")) {
							int tabId = Integer.parseInt(values[0]);
							int itemId = Integer.parseInt(values[1]);
							int itemAmount = Integer.parseInt(values[2]);
							p.getBank().getBankTab()[tabId].add(new BankItem(itemId, itemAmount));
						}
						break;
						
					// Character Friends
					case 8:
						if (key.equals("friends")) {
                            p.getFAI().getFriendsList().add(Long.parseLong(values[0]));
                        }
						break;
					
					// Character Ignores
					case 9:
						if (key.equals("ignores")) {
                            for (int i = 0; i < values.length; i++) {
                                p.getFAI().getIgnoreList().add(Long.parseLong(values[i]));
                            }
                        }
						break;
						
					// Character Killstreaks
					case 10:
						if (line.startsWith("currentKillStreak")) {
							p.setCurrentKillStreak(Integer.parseInt(value));
						} else if (line.startsWith("highestKillStreak")) {
							p.setHighestKillStreak(Integer.parseInt(value));
						} else if (line.startsWith("wildernessKillStreak")) {
							p.setWildernessKillStreak(Integer.parseInt(value));
						}
						break;
					
					// Character Wilderness
					case 11:
						if (key.startsWith("last-killed")) {
							p.lastKilledList.add(value);
						} else if (line.startsWith("killCount")) {
							p.setKillCount(Integer.parseInt(value));
						} else if (line.startsWith("deathCount")) {
							p.setDeathCount(Integer.parseInt(value));
						}
						break;
						
					// Bounty Hunter
					case 12:
						if (key.equals("rogue-current")) {
                            p.setAttribute(BountyHunterConstants.ROGUE_CURRENT, Integer.parseInt(value));
                        } else if (key.equals("rogue-record")) {
                            p.setAttribute(BountyHunterConstants.ROGUE_RECORD, Integer.parseInt(value));
                        } else if (key.equals("hunter-current")) {
                            p.setAttribute(BountyHunterConstants.HUNTER_CURRENT, Integer.parseInt(value));
                        } else if (key.equals("hunter-record")) {
                            p.setAttribute(BountyHunterConstants.HUNTER_RECORD, Integer.parseInt(value));
                        } else if (key.equals("bounty-points")) {
                            p.setBountyPoints(Integer.parseInt(value));
                        }
						break;
					
					// Boss Tracker
					case 13:
						BossName name = BossName.get(key);
						if (name != null) {
							p.getBossDeathTracker().getTracker().put(name, Integer.parseInt(value));
						}
                        break;
                        
                    // Slayer Tracker 
					case 14:
						SlayerNpcName names = SlayerNpcName.get(key);
                        if (names != null) {
                            p.getSlayerDeathTracker().getTracker().put(names, Integer.parseInt(value));
                        }
						break;
						
					// Pets
					case 15:
						if (key.equals("pet-spawned")) {
    						p.setPetSpawned(Boolean.parseBoolean(value));
                    	} else if(key.equals("pet")) {
                    		p.setPet(Integer.parseInt(value));
                    	}
						break;
						
						// Achievements Tier 1
					case 16:
                        if (values.length < 2)
                            continue;
                        p.getAchievements().read(key, 0, Integer.parseInt(values[0]), Boolean.parseBoolean(values[1]));
                        break;
                        
                    // Achievements Tier 2
                    case 17:
                        if (values.length < 2)
                            continue;
                        p.getAchievements().read(key, 1, Integer.parseInt(values[0]), Boolean.parseBoolean(values[1]));
                        break;
                        
                    // Achievements Tier 3
                    case 18:
                        if (values.length < 2)
                            continue;
                        p.getAchievements().read(key, 2, Integer.parseInt(values[0]), Boolean.parseBoolean(values[1]));
                        break;
                     
					// Clues crolls
					case 19:
						if (key.equals("clue-container")) {
							try {
								if (value == null || !value.equals("null")) {
									List<ClueScroll> list = new ArrayList<>();
									for (int i = 0; i < values.length; i++)
										list.add(ClueScroll.valueOf(values[i]));
									p.clueContainer = new ClueScrollContainer(p, Iterables.toArray(list, ClueScroll.class));
								}
							} catch (IllegalArgumentException e) {
								p.clueContainer = null;
							}
						} else if (key.equals("clue-reward")) {
							if (!value.equals("null"))
								p.bossDifficulty = ClueDifficulty.valueOf(value);
						} else if (key.equals("easy-clue")) {
							p.easyClue = Integer.parseInt(value);
						} else if (key.equals("medium-clue")) {
							p.mediumClue = Integer.parseInt(value);
						} else if (key.equals("hard-clue")) {
							p.hardClue = Integer.parseInt(value);
						} else if (key.equals("elite-clue")) {
							p.eliteClue = Integer.parseInt(value);
						}
						break;
                    
                    // Titles
                    case 20:
                    	
                    	break;
                    	
                    // Settings
                    case 21:
                    	if (key.equals("isRunning")) {
                    		p.setRunning(Boolean.parseBoolean(value));
                        } else if (key.equals("autoRetaliate")) {
                            p.setAutoRetaliating(Boolean.parseBoolean(value));
                        } else if (key.equals("attackStyle")) {
                        	p.setAttackStyle(Integer.parseInt(value));
                        } else if (key.equals("attackStyleConfig")) {
                        	p.setAttackStyleConfig(Integer.parseInt(value));
                        } else if (key.equals("brightness")) {
                            p.setScreenBrightness((byte) Integer.parseInt(value));
                        } else if (key.equals("splitprivatechat")) {
                            p.setSplitPrivateChat(Boolean.parseBoolean(value));
                        } else if (key.equals("music-player")) {
                            p.setEnableMusic(Boolean.parseBoolean(value));
                        } else if (key.equals("sounds-player")) {
                            p.setEnableSound(Boolean.parseBoolean(value));
                        } else if (key.equals("music_volume")) {
                            p.setAttribute("music_volume", Integer.parseInt(value));
                        } else if (key.equals("sound_volume")) {
                            p.setAttribute("sound_volume", Integer.parseInt(value));
                        }
                    	break;
                    	
                    case 22:
                    	if(key.equals("item-set")) {
                    		p.getRunePouchContainer().add(new Item(Integer.parseInt(values[1]), Integer.parseInt(values[2])));
                    	}
                    	break;
                    	
                    case 23:
                    	if (key.equals("death-shop-chat")) {
                            p.deathShopChat = Boolean.parseBoolean(value);
                        } else if (key.equals("death-shop-items")) {
                            p.deathShop.getContainer().container()[Integer.parseInt(values[0])] = new Item(Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                        }
                    	break;
                    case 27:
                    	if(key.equals("blocked-tasks")) {
                    		p.getSlayerInterface().getBlockedTasks().ensureCapacity(6);
                    		System.out.println(values[0]);
                    		p.getSlayerInterface().getBlockedTasks().add(Integer.parseInt(values[0]));
                    	}
                   
                    	break;
                    	
                    case 28:
                    	if(key.equals("slayer-unlocked")) {
                    		System.out.println("Adding player unlocks: "+values[0]+ " VALUE 1 "+values[1]);
                    		p.getSlayerInterface().getUnlocks().put(Integer.parseInt(values[0]), values[1]);
                    		}
                    	
                    	break;
                    	
                    case 29:
                    	if(key.equals("slayer-extensions")) {
                    		System.out.println("Adding player unlocks: "+values[0]+ " VALUE 1 "+values[1]);
                    		p.getSlayerInterface().getExtensions().put(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                    		}
                    	break;
                    	/*writer.write("[Slayer]");
            			writer.newLine();
            			for (Entry<Integer, String> entrys : p.getSlayerInterface().unlocks.entrySet()) {
            				if (entrys != null) {
            					if (entrys.getKey() > 0) {
            						writer.write(entrys.getKey().toString() + " = " + entrys.getValue());
            						writer.newLine();
            					}
            				}
            			}*/
					}
				} else {
					if (line.equals("[CHARACTER-ACCOUNT]")) {
						mode = 1;
					} else if (line.equals("[CHARACTER]")) {
						mode = 2;
					} else if (line.equals("[CHARACTER-EQUIPMENT]")) {
						mode = 3;
					} else if (line.equals("[CHARACTER-LOOK]")) {
						mode = 4;
					} else if (line.equals("[CHARACTER-SKILLS]")) {
						mode = 5;
					} else if (line.equals("[CHARACTER-ITEMS]")) {
						mode = 6;
					} else if (line.equals("[CHARACTER-BANK]")) {
						mode = 7;
					} else if (line.equals("[CHARACTER-FRIENDS]")) {
                        mode = 8;
                    } else if (line.equals("[CHARACTER-IGNORES]")) {
                        mode = 9;
                    } else if (line.equals("[CHARACTER-KILLSTREAKS]")) {
                        mode = 10;
                    } else if (line.equals("[CHARACTER-KILLS]")) {
                        mode = 11;
                    } else if (line.equals("[BOUNTY-HUNTER]")) {
                        mode = 12;
                    } else if (line.equals("[BOSS-TRACKER]")) {
                        mode = 13;
                    } else if (line.equals("[SLAYER-TRACKER]")) {
                        mode = 14;
                    } else if (line.equals("[CHARACTER-PETS]")) {
                        mode = 15;
                    } else if (line.equals("[ACHIEVEMENTS-TIER-1]")) {
                        mode = 16;
                    } else if (line.equals("[ACHIEVEMENTS-TIER-2]")) {
                        mode = 17;
                    } else if (line.equals("[ACHIEVEMENTS-TIER-3]")) {
                        mode = 18;
                    } else if (line.equals("[CLUE-SCROLLS]")) {
    					mode = 19;
                    } else if (line.equals("[CHARACTER-TITLES]")) {
    					mode = 20;
                    } else if (line.equals("[CHARACTER-SETTINGS]")) {
    					mode = 21;
                    } else if (line.equals("[CHARACTER-RUNE-POUCH]")) {
                    	mode = 22;
                    } else if (line.equals("[TASKS-DIFFICULTY-EASY]")) {
                        mode = 23;
                    } else if (line.equals("[TASKS-DIFFICULTY-MEDIUM]")) {
                        mode = 24;
                    } else if (line.equals("[TASKS-DIFFICULTY-HARD]")) {
                        mode = 25;
                    } else if (line.equals("[TASKS-DIFFICULTY-ELITE]")) {
                        mode = 26;
                    } else if (line.equals("[SLAYER-BLOCKED]")) {
                        mode = 27;
                    }  else if (line.equals("[SLAYER-UNLOCKS]")) {
                        mode = 28;
                    } else if (line.equals("[SLAYER-EXTENSIONS]")) {
                        mode = 29;
                    }
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 13;
	}

    /**
     * Saves the account for a player
     *
     * @param player
     *            The {@link Player to save the account for
     */
    public static void saveGame(final Player player) {
        if (!player.saveFile || !player.saveCharacter) {
            return;
        }
        if (player.getName() == null || World.getWorld().getPlayers().get(player.getIndex()) == null) {
            return;
        }
        writeData(player);
    }
    
    public static String quick = "";

    /**
     * Writes data to the character file
     *
     * @param p
     *            The {@link Player} to write the data for
     */
    public static void writeData(Player p) {
        String username = p.getName().toLowerCase();
        final int time = p.teleblock.isStopped() ? 0 : (int) (p.teleblockLength - p.teleblock.elapsedTime());
        final int tbTime = time > 300000 || time < 0 ? 0 : time;
       
        Path path = Paths.get(getCharacterDirectory(), username.charAt(0) + File.separator + username + ".txt");
		
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
        	
        	/* Character Account */
        	
            writer.write("[CHARACTER-ACCOUNT]");
            writer.newLine();
            writer.write("character-password = ");
            // hashing only half implemented, should have removed old totally for security
            String passToWrite = p.getPassword() == null || p.getPassword().length() == 0 ? p.passHash : Utility.md5Hash(p.getPassword());
            writer.write(passToWrite);
            writer.newLine();
            writer.write("character-rights = " + p.getRights().getValue());
            writer.newLine();
            writer.write("character-identity = " + p.getIdentity());
            writer.newLine();
            writer.write("character-mac-address = " + p.getMacAddress());
            writer.newLine();
            writer.write("character-account-tutorial = ");
            writer.write(Boolean.toString(p.inTutorial()));
            writer.newLine();
            writer.write("character-received-starter = ");
            writer.write(Boolean.toString(p.receivedStarter()));
            writer.newLine();
            writer.write("character-account-type = " + p.getGameMode());
            writer.newLine();
            writer.write("character-posx = ");
            writer.write(Integer.toString(p.getX()));
            writer.newLine();
            writer.write("character-posy = ");
            writer.write(Integer.toString(p.getY()));
            writer.newLine();
            writer.write("character-height = ");
            writer.write(Integer.toString(p.heightLevel));
            writer.newLine();
	        writer.write("character-special-amount = ");
	        writer.write(Integer.toString(p.getSpecialAmount()));
	        writer.newLine();
			writer.newLine();
			
			/* Character */
			
			writer.write("[CHARACTER]");
			writer.newLine();
            writer.write("magic-book = "+p.getSpellBook().toString());
            writer.newLine();
            writer.write("exp-counter = " + p.getSkills().getExpCounter());
            writer.newLine();
            writer.write("recoil = " + p.getRecoil());
            writer.newLine();
            writer.write("ringOfSuffering = " + p.getROSuffering());
            writer.newLine();
            writer.write("votePoints = " + p.getVotePoints());
            writer.newLine();
            writer.write("totalVotes = " + p.getTotalVotes());
            writer.newLine();
            writer.write("pkPoints = " + p.getPkPoints());
            writer.newLine();
            writer.write("slayerPoints = " + p.getSlayerPoints());
            writer.newLine();
            writer.write("achievement-points = " + p.getAchievements().getPoints());
            writer.newLine();
            writer.write("amount-donated = " + p.getAmountDonated());
            writer.newLine();
            writer.write("total-amount-donated = " + p.getTotalAmountDonated());
            writer.newLine();
            writer.write("teleblock-length = " + tbTime);
            writer.newLine();
            writer.write("muted = " + p.isMuted);
            writer.newLine();
            writer.write("skull-timer = " + p.skullTimer);
            writer.newLine();
            writer.write("infection-type = " + p.infection);
            writer.newLine();
            writer.newLine();  writer.newLine();  writer.newLine();
			writer.write("slayer-task = " + p.getSlayerTask());
            writer.newLine();
            writer.write("can-teleport-to-slayer-tasks = " + p.canTeleportToSlayerTask());
            writer.newLine();
            writer.write("task-amount = " + p.getSlayerTaskAmount());
            writer.newLine();
            writer.write("task-difficulity = " + p.getSlayerTaskDifficulty());
            writer.newLine();
            writer.write("first-slayer-task-completed = " + p.getFirstSlayerTask());
            writer.newLine();
            writer.write("first-boss-slayer-task-completed = " + p.getFirstBossSlayerTask());
            writer.newLine();
            writer.write("slayer-streak = " + p.getSlayerStreak());
            writer.newLine();
            writer.write("slayer-streakrecord = " + p.getSlayerStreakRecord());
            writer.newLine();  writer.newLine();  writer.newLine();
            writer.write("lastClanChat = ");
            writer.write(p.getClanMembership() == null ? "" : p.getClanMembership().getClanOwner());
            writer.newLine();
			writer.write("clanChatPunishment = " + p.getClanPunishment());
            writer.newLine();
            writer.write("gear-points = " + p.getGearPoints());
			writer.newLine();
			writer.write("unlock-preserve = " + p.isPreserveUnlocked());
			writer.newLine();
			writer.write("unlock-rigour = " + p.isRigourUnlocked());
			writer.newLine();
			writer.write("unlock-augury = " + p.isAuguryUnlocked());
			writer.newLine();
			// Barrows
			Barrows barrows = p.getBarrows();
			writer.write("barrows-broskilled = " + barrows.getNpcController().getKilledBrothers());
			writer.newLine();
			writer.write("barrows-target = " + barrows.getNpcController().getTargetBrother().getID());
			writer.newLine();
			writer.write("barrows-looted = " + barrows.getChest().hasLooted());
			writer.newLine();
			writer.write("barrows-kc = " + barrows.getNpcController().getKillCount());
			writer.newLine();
            
			
			writer.write("[SLAYER-BLOCKED]");
			writer.newLine();
			for(int i = 0; i < p.getSlayerInterface().getBlockedTasks().size(); i++){
				writer.write("blocked-tasks = ");
				p.getSlayerInterface().getBlockedTasks().ensureCapacity(6);
				if(p.getSlayerInterface().getBlockedTasks() != null)
                writer.write(p.getSlayerInterface().getBlockedTasks().get(i) + "\t");
                writer.newLine();
				
			}
			writer.newLine();
			writer.write("[SLAYER-UNLOCKS]");
			writer.newLine();
			for (Entry<Integer, String> entrys : p.getSlayerInterface().getUnlocks().entrySet()) {
				if (entrys != null) {
					if (entrys.getKey() > 0) {
						//  writer.write("bank-tab = " + i + "\t" + item.getId() + "\t" + item.getAmount());
						writer.write("slayer-unlocked = "+entrys.getKey().toString());
						writer.write("	");
						writer.write(entrys.getValue().toString());
						writer.newLine();
					}
				}
			}
			writer.newLine();
			writer.write("[SLAYER-EXTENSIONS]");
			writer.newLine();
			for (Entry<Integer, Integer> entrys : p.getSlayerInterface().getExtensions().entrySet()) {
				if (entrys != null) {
					if (entrys.getKey() > 0) {
						//  writer.write("bank-tab = " + i + "\t" + item.getId() + "\t" + item.getAmount());
						writer.write("slayer-extensions = "+entrys.getKey().toString());
						writer.write("	");
						writer.write(entrys.getValue().toString());
						writer.newLine();
					}
				}
			}
			writer.newLine();
            /* EQUIPMENT */
            writer.write("[CHARACTER-EQUIPMENT]");
            writer.newLine();
            for (int i = 0; i < p.playerEquipment.length; i++) {
                writer.write("character-equip = ", 0, 18);
                writer.write(Integer.toString(i), 0, Integer.toString(i).length());
                writer.write("	", 0, 1);
                writer.write(Integer.toString(p.playerEquipment[i]), 0, Integer.toString(p.playerEquipment[i]).length());
                writer.write("	", 0, 1);
                writer.write(Integer.toString(p.playerEquipmentN[i]), 0, Integer.toString(p.playerEquipmentN[i]).length());
                writer.write("	", 0, 1);
                writer.newLine();
            }
            writer.newLine();
            writer.newLine();

            /* LOOK */
            writer.write("[CHARACTER-LOOK]");
            writer.newLine();
            for (int i = 0; i < p.playerAppearance.length; i++) {
                writer.write("character-look = ", 0, 17);
                writer.write(Integer.toString(i), 0, Integer.toString(i).length());
                writer.write("	", 0, 1);
                writer.write(Integer.toString(p.playerAppearance[i]), 0, Integer.toString(p.playerAppearance[i]).length());
                writer.newLine();
            }
            writer.newLine();
            writer.newLine();

            /* SKILLS */
            writer.write("[CHARACTER-SKILLS]");
            writer.newLine();
            for (int i = 0; i < Skills.SKILL_COUNT; i++) {
                writer.write("character-skill = ", 0, 18);
                writer.write(Integer.toString(i), 0, Integer.toString(i).length());
                writer.write("	", 0, 1);
                writer.write(Integer.toString(p.getSkills().getLevel(i)), 0, Integer.toString(p.getSkills().getLevel(i)).length());
                writer.write("	", 0, 1);
                writer.write(Integer.toString((int) p.getSkills().getExperience(i)), 0, Integer.toString((int) p.getSkills().getExperience(i)).length());
                writer.newLine();
            }
            writer.newLine();
            writer.newLine();

            /* ITEMS */
            writer.write("[CHARACTER-ITEMS]");
            writer.newLine();
            for (int i = 0; i < p.playerItems.length; i++) {
                if (p.playerItems[i] > 0) {
                    writer.write("character-item = ", 0, 17);
                    writer.write(Integer.toString(i), 0, Integer.toString(i).length());
                    writer.write("	", 0, 1);
                    writer.write(Integer.toString(p.playerItems[i]), 0, Integer.toString(p.playerItems[i]).length());
                    writer.write("	", 0, 1);
                    writer.write(Integer.toString(p.playerItemsN[i]), 0, Integer.toString(p.playerItemsN[i]).length());
                    writer.newLine();
                }
            }
            writer.newLine();
            writer.newLine();

            /* BANK */
            writer.write("[CHARACTER-BANK]");
            writer.newLine();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < p.BANK_SIZE; j++) {
                    if (j > p.getBank().getBankTab()[i].size() - 1)
                        break;
                    BankItem item = p.getBank().getBankTab()[i].getItem(j);
                    if (item == null)
                        continue;
                    writer.write("bank-tab = " + i + "\t" + item.getId() + "\t" + item.getAmount());
                    writer.newLine();
                }
            }

            writer.newLine();
            writer.newLine();
            
            /* FRIENDS */
            writer.write("[CHARACTER-FRIENDS]");
            writer.newLine();
            for (Long l : p.getFAI().getFriendsList()) {
                writer.write("friends = ");
                writer.write(l + "\t");
                writer.newLine();
            }
            writer.newLine();
            writer.newLine();
            
            /* IGNORES */
            writer.write("[CHARACTER-IGNORES]");
            writer.newLine();
            for (Long l : p.getFAI().getIgnoreList()) {
                writer.write("ignores = ");
                writer.write(l + "\t");
                writer.newLine();
            }
			writer.newLine();
			writer.newLine();
			
			/* Killstreaks */
            writer.write("[CHARACTER-KILLSTREAKS]");
            writer.newLine();
            writer.write("currentKillStreak = " + p.getCurrentKillStreak());
            writer.newLine();
            writer.write("highestKillStreak = " + p.getHighestKillStreak());
            writer.newLine();
            writer.write("wildernessKillStreak = " + p.getWildernessKillStreak());
            writer.newLine();
            writer.newLine();
            
            /* Kills */
            writer.write("[CHARACTER-KILLS]");
            writer.newLine();
            for (int i = 0; i < p.lastKilledList.size(); i++) {
                if (p.lastKilledList.get(i) != null && !p.lastKilledList.get(i).equalsIgnoreCase("null")) {
                    writer.write("last-killed = " + p.lastKilledList.get(i));
                    writer.newLine();
                }
            }
            writer.newLine();
            writer.write("killCount = " + p.getKillCount());
            writer.newLine();
            writer.write("deathCount = " + p.getDeathCount());
            writer.newLine();
            writer.newLine();
            
            /* Bounty Hunter */
            writer.write("[BOUNTY-HUNTER]");
            writer.newLine();
            writer.write("rogue-current = " + p.getAttribute(BountyHunterConstants.ROGUE_CURRENT, 0));
            writer.newLine();
            writer.write("rogue-record = " + p.getAttribute(BountyHunterConstants.ROGUE_RECORD, 0));
            writer.newLine();
            writer.write("hunter-current = " + p.getAttribute(BountyHunterConstants.HUNTER_CURRENT, 0));
            writer.newLine();
            writer.write("hunter-record = " + p.getAttribute(BountyHunterConstants.HUNTER_RECORD, 0));
            writer.newLine();
            writer.write("bounty-points = " + p.getBountyPoints());
			writer.newLine();
			writer.newLine();
			
			/* Boss Tracker */
			writer.write("[BOSS-TRACKER]");
			writer.newLine();
			for (Entry<BossName, Integer> entry : p.getBossDeathTracker().getTracker().entrySet()) {
				if (entry != null) {
					if (entry.getValue() > 0) {
						writer.write(entry.getKey().toString() + " = " + entry.getValue());
						writer.newLine();
					}
				}
			}
			writer.newLine();
			writer.newLine();

			/* Slayer Tracker */
			writer.write("[SLAYER-TRACKER]");
			writer.newLine();
			for (Entry<SlayerNpcName, Integer> entrys : p.getSlayerDeathTracker().getTracker().entrySet()) {
				if (entrys != null) {
					if (entrys.getValue() > 0) {
						writer.write(entrys.getKey().toString() + " = " + entrys.getValue());
						writer.newLine();
					}
				}
			}
			writer.newLine();
			writer.newLine();

			/* Pets */
			writer.write("[CHARACTER-PETS]");
			writer.newLine();
			writer.write("pet-spawned = "+p.isPetSpawned());
			writer.newLine();
			writer.write("pet = "+p.getPet());
			writer.newLine();
			writer.newLine();

			/* Achievements */
			writer.write("[ACHIEVEMENTS-TIER-1]");
            writer.newLine();
            p.getAchievements().print(writer, 0);
            writer.newLine();
            writer.newLine();
            writer.write("[ACHIEVEMENTS-TIER-2]");
            writer.newLine();
            p.getAchievements().print(writer, 1);
            writer.newLine();
            writer.newLine();
            writer.write("[ACHIEVEMENTS-TIER-3]");
            writer.newLine();
            p.getAchievements().print(writer, 2);

			writer.newLine();
			writer.newLine();
			
			/* Clue scrolls */
			writer.write("[CLUE-SCROLLS]");
			writer.newLine();
			writer.write("clue-container = ");
			if (p.clueContainer == null || p.clueContainer.stages.peek() == null) {
				writer.write("null");
			} else {
				for (ClueScroll c : p.clueContainer.stages)
					writer.write(c.name() + "\t");
			}
			writer.newLine();
			writer.write("clue-reward = ");
			writer.write(p.bossDifficulty == null ? "null" : p.bossDifficulty.name());
			writer.newLine();
			writer.write("easy-clue = ");
			writer.write(Integer.toString(p.easyClue));
			writer.newLine();
			writer.write("medium-clue = ");
			writer.write(Integer.toString(p.mediumClue));
			writer.newLine();
			writer.write("hard-clue = ");
			writer.write(Integer.toString(p.hardClue));
			writer.newLine();
			writer.write("elite-clue = ");
			writer.write(Integer.toString(p.eliteClue));
			writer.newLine();
			writer.newLine();

			/* Titles */
			writer.write("[CHARACTER-TITLES]");
			writer.newLine();
			writer.write("character-title = " + p.getCurrentTitle());
			writer.newLine();
			writer.write("character-title-color = " + p.getCurrentTitleColor());
			writer.newLine();
			writer.newLine();

			/* Settings */
			writer.write("[CHARACTER-SETTINGS]");
			writer.newLine();
			writer.write("isRunning = " + p.isRunning());
			writer.newLine();
			writer.write("autoRetaliate = ");
			writer.write(Boolean.toString(p.isAutoRetaliating()));
			writer.newLine();
			writer.write("attackStyle = " + p.getAttackStyle());
			writer.newLine();
			writer.write("attackStyleConfig = " + p.getAttackStyleConfig());
			writer.newLine();
			writer.write("brightness = " + p.getScreenBrightness());
			writer.newLine();
			writer.write("splitprivatechat = " + p.getSplitPrivateChat());
			writer.newLine();
			writer.write("music-player = " + p.isEnableMusic());
			writer.newLine();
			writer.write("sounds-player = " + p.isEnableSound());
			writer.newLine();
			writer.write("music_volume = " + p.getAttribute("music_volume", 3));
            writer.newLine();
            writer.write("sound_volume = " + p.getAttribute("sound_volume", 3));
            writer.newLine();
            writer.write("music-unlocked = ");
            writer.newLine();
			writer.newLine();

			/* RUNE-POUCH */
			writer.write("[CHARACTER-RUNE-POUCH]");
			writer.newLine();
			for (int i = 0; i < p.getRunePouchContainer().size(); i++) {
				writer.write("item-set = ");
				writer.write(Integer.toString(i));// slot
				writer.write("	");
				writer.write(Integer.toString(p.getRunePouchContainer().getId(i)));// id
				writer.write("	");
				writer.write(Integer.toString(p.getRunePouchContainer().get(i).getAmount()));
				writer.newLine();
			}
			writer.newLine();
			writer.newLine();
			
			/* Death Shop */
			writer.write("[CHARACTER-DEATH-SHOP]");
			writer.newLine();
			writer.write("death-shop-chat = ");
            writer.write(Boolean.toString(p.deathShopChat));
            writer.newLine();
            for (int i = 0; i < p.deathShop.getContainer().container().length; i++) {
                if (p.deathShop.getContainer().container()[i] == null)
                    continue;
                writer.write("death-shop-items = ");
                writer.write(Integer.toString(i));
                writer.write("\t");
                writer.write(Integer.toString(p.deathShop.getContainer().container()[i].id));
                writer.write("\t");
                writer.write(Integer.toString(p.deathShop.getContainer().container()[i].amount));
                writer.write("\t");
            }
			writer.newLine();
            writer.newLine();
	            
			
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static boolean passwordMatches(String name, String password) throws IOException {
        if (!playerExists(name)) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("./data/characters/" + name + ".txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                int spot = line.indexOf("=");
                if (spot > -1) {
                    String key = line.substring(0, spot).trim();
                    String value = line.substring(spot + 1).trim();
                    if (key != null && value != null) {
                        if (key.equals("character-password")) {
                            reader.close();
                            return password.equalsIgnoreCase(value) || Utility.basicEncrypt(password).equals(value) || Utility.md5Hash(password)
                                .equals(value);
                        }
                    }
                }
            }
            reader.close();
        }
        return false;
    }

    public static boolean playerExists(String name) {
        File file = null;
        file = new File("./data/characters/" + name + ".txt");
        return file != null && file.exists();
    }
    private static <T> void write(BufferedWriter characterfile, String name, T value) throws IOException {
		String delimeter = name + " = ";
		characterfile.write(delimeter);
		characterfile.write(value.toString());
		characterfile.newLine();
	}
	public static ExecutorService getExecutor() {
		return executor;
	}
	
	// TODO check if linkedlist is thread safe
	public static LinkedList<String> BUSY_PROFILES = new LinkedList<String>();
	
	// Runs File IO on a seperate thread so the gameserver is unaffected by lag/filesystem load speed.
	public static void change_offline_password(Player wants_feedback, String forname, String newpass) {
		if (BUSY_PROFILES.contains(forname)) {
			wants_feedback.getActionSender().sendMessage(forname+"'s profile is in use, maybe they just logged in/out 1s ago");
		} else {
			BUSY_PROFILES.add(forname);
			PlayerSerialization.executor.submit(new Runnable() {

				@Override
				public void run() {
					wants_feedback.getActionSender().sendMessage("Loading profile of "+forname+"...");
					// load, replace pw, save -> since this is on a diff thread
					// we can be lasy a load the full profile instead of just the pw
					Player target = new Player(forname);
					PlayerSerialization.loadGame(target, forname, "", true);
					wants_feedback.getActionSender().sendMessage("Loaded profile of "+forname+", now changing pw and saving...");
					target.setPassword("");
					target.passHash = Utility.md5Hash(newpass);
					writeData(target); // Write new data. SaveProfile(player) requires them to be online
					BUSY_PROFILES.remove(forname);
					wants_feedback.getActionSender().sendMessage("Profile of "+forname+" password updated to "+newpass);
				}
				
			});
		}
	}
}