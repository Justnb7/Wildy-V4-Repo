package com.model.game.character.player;

import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.combat.combat_data.CombatAnimation;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.npc.NPC;
import com.model.game.character.player.content.BossTracker;
import com.model.game.character.player.content.trade.Trading;
import com.model.game.character.player.packets.out.*;
import com.model.game.character.walking.PathFinder;
import com.model.game.item.Item;
import com.model.game.location.Position;
import com.model.utility.Utility;
import com.model.utility.cache.map.Region;
import com.model.utility.json.definitions.ItemDefinition;

import java.text.DecimalFormat;

import org.omicron.jagex.runescape.CollisionMap;

public class PlayerAssistant {

    private final Player player;
    

    public PlayerAssistant(Player Client) {
        this.player = Client;
    }

    public void playerWalk(int x, int y) {
        PathFinder.getPathFinder().findRoute(player, x, y, true, 1, 1);
    }

    public void movePlayer1(int x, int y) {
        player.getMovementHandler().reset();
        player.teleportToX = x;
        player.teleportToY = y;
        requestUpdates();
    }
        
    public void resetTb() {
        player.teleblockLength = 0;
        player.teleblock.stop();
    }

    public void movePlayer(int x, int y, int h) {
        if (player == null)
            return;
        if (player.isBusy()) {
            return;
        }
		if (!player.lastSpear.elapsed(4000)) {
			player.getActionSender().sendMessage("You're trying to move too fast.");
			return;
		}
        player.getMovementHandler().reset();
        player.teleportToX = x;
        player.teleportToY = y;
        player.teleHeight = h;
        player.setTeleportTarget(Position.create(x, y, h));
        requestUpdates();
        player.getSkillCyclesTask().stop();
        //System.out.println("to "+Arrays.toString(new int[] {x,y,h}));
    }

    public void movePlayer(Position p) {
        movePlayer(p.getX(), p.getY(), p.getZ());
    }
    
    public void showAccountSleection(Player player, int state) {
    	player.write(new SendInterfacePacket(45200));
		int counter2 = 0;
		for (int i = 0; i < arrayItemsLegend.length; i++) {
			player.getActionSender().sendUpdateItem(45245 + counter2, -1, 0, 1);
			counter2++;
		}
		switch (state) {
		case 0: // Hero
			player.getActionSender().sendChangeSprite(45228, (byte) 2);//Ironman
			player.getActionSender().sendChangeSprite(45230, (byte) 0);//Hardcore ironman
			player.getActionSender().sendChangeSprite(45232, (byte) 0);//Ultimate ironman
			player.getActionSender().sendChangeSprite(45234, (byte) 0);//None option
			
			player.getActionSender().sendString("Selection Info: <col=ff9900>Hero </col> ",
					45209);
			player.getActionSender().sendString("<col=ff9900>Selection Perks: </col> ",
					45222);
			player.getActionSender().sendString("As a Hero you get 500 exp", 45211);
			player.getActionSender().sendString("This is our 'easy' mode", 45212);
			player.getActionSender().sendString("If you are looking not to grind", 45213);
			player.getActionSender().sendString("for hours this is your", 45214);
			player.getActionSender().sendString("optimal mode of choice.", 45216);
			player.getActionSender().sendString("You do have certain limitations", 45217);
			player.getActionSender().sendString("in-game, that you will  ", 45218);
			player.getActionSender().sendString("soon find out about.", 45219);
			player.getActionSender().sendString("", 45220);

			// Perks
			player.getActionSender().sendString("Faster Exp", 45224);
			player.getActionSender().sendString("Normal Game mode", 45225);
			player.getActionSender().sendString("CANNOT Prestige", 45226);
			player.getActionSender().sendString("<col=ff9900>10x ACTIVITY GOLD", 45227);

			int counter = 0;
			for (int i = 0; i < arrayItems.length; i++) {
				
				player.getActionSender().sendUpdateItem(45245 + counter, arrayItems[i][0], 0,
						arrayItemsLegend[i][1]);
				counter++;
			}
			break;

		case 1: // legend
			player.getActionSender().sendChangeSprite(45228, (byte) 0);//Ironman
			player.getActionSender().sendChangeSprite(45230, (byte) 2);//Hardcore ironman
			player.getActionSender().sendChangeSprite(45232, (byte) 0);//Ultimate ironman
			player.getActionSender().sendChangeSprite(45234, (byte) 0);//None option
			player.getActionSender().sendString(
					"Selection Info: <col=ff9900>Legend </col> ", 45209);
			player.getActionSender().sendString("<col=ff9900>Selection Perks: </col> ",
					45222);
			player.getActionSender().sendString("Our most popular game ", 45211);
			player.getActionSender().sendString("mode with 50xp rates", 45212);
			player.getActionSender().sendString("As a Legend you", 45213);
			player.getActionSender().sendString("will have access to all server", 45214);
			player.getActionSender().sendString(" features, plus some", 45216);
			player.getActionSender().sendString("legend only content.", 45217);
			player.getActionSender().sendString("optimal mode of choice.", 45218);
			player.getActionSender().sendString("Want an exciting and rewarding", 45219);
			player.getActionSender().sendString("experience? This is your mode", 45220);

			// Perks
			player.getActionSender().sendString("Boosted Drop Rate %", 45224);
			player.getActionSender().sendString("Legends Only Whip", 45225);
			player.getActionSender().sendString("<col=ff9900>20x Activity Gold", 45226);
			player.getActionSender().sendString("Prestiging ", 45227);

			int counter1 = 0;
			for (int i = 0; i < arrayItemsLegend.length; i++) {
				player.getActionSender().sendUpdateItem(45245 + counter1,
						arrayItemsLegend[i][0], 0, arrayItemsLegend[i][1]);
				counter1++;
			}
			break;
		case 2: // Hardcore
			player.getActionSender().sendChangeSprite(45228, (byte) 0);//Ironman
			player.getActionSender().sendChangeSprite(45230, (byte) 0);//Hardcore ironman
			player.getActionSender().sendChangeSprite(45232, (byte) 2);//Ultimate ironman
			player.getActionSender().sendChangeSprite(45234, (byte) 0);//None option
			player.getActionSender().sendString(
					"Selection Info: <col=ff9900>Hardcore </col> ", 45209);
			player.getActionSender().sendString("<col=ff9900>Selection Perks: </col> ",
					45222);
			player.getActionSender().sendString("Our most difficult game mode", 45211);
			player.getActionSender().sendString("As a Hardcore you", 45212);
			player.getActionSender().sendString("will have access", 45213);
			player.getActionSender().sendString("to all server features. If you", 45214);
			player.getActionSender().sendString("are looking for a great", 45216);
			player.getActionSender().sendString("challenge and to live", 45217);
			player.getActionSender().sendString("and breathe the grind", 45218);
			player.getActionSender().sendString("This is the mode for you", 45219);
			player.getActionSender().sendString("", 45220);

			// Perks
			player.getActionSender().sendString("Boosted Drop Rate 8%", 45224);
			player.getActionSender().sendString("Hardcore Sword", 45225);
			player.getActionSender().sendString("<col=ff9900>30x Activity Gold", 45226);
			player.getActionSender().sendString("Base Bonus +8 Prayer, +5 Str", 45227);
			int counter11 = 0;
			for (int i = 0; i < arrayItemsHardcore.length; i++) {
				player.getActionSender().sendUpdateItem(45245 + counter11,
						arrayItemsHardcore[i][0], 0, arrayItemsLegend[i][1]);
				counter11++;
			}
			break;
		case 3: // Master
			player.getActionSender().sendChangeSprite(45228, (byte) 0);//Ironman
			player.getActionSender().sendChangeSprite(45230, (byte) 0);//Hardcore ironman
			player.getActionSender().sendChangeSprite(45232, (byte) 0);//Ultimate ironman
			player.getActionSender().sendChangeSprite(45234, (byte) 2);//None option
			player.getActionSender().sendString(
					"Selection Info: <col=ff9900>Master </col> ", 45209);
			player.getActionSender().sendString("<col=ff9900>Selection Perks: </col> ",
					45222);

			player.getActionSender().sendString("Are you a pker? This is your", 45211);
			player.getActionSender().sendString("mode. As a Master you can start", 45212);
			player.getActionSender().sendString("off by setting your Combat stats", 45213);
			player.getActionSender().sendString("to what you desire. This ", 45214);
			player.getActionSender().sendString(" eliminates the 'grind' ", 45216);
			player.getActionSender().sendString("and allows you to start ", 45217);
			player.getActionSender().sendString("pking instantly. There are limitations  ",
					45218);
			player.getActionSender().sendString("to keep our gameplay fair. That", 45219);
			player.getActionSender().sendString("are stated below.", 45220);

			// Perks
			player.getActionSender().sendString("Boosted PkP rate", 45224);
			player.getActionSender().sendString("Extremely low drop rate %", 45225);
			player.getActionSender().sendString("Cannot attack NPC's outside of wild", 45226);
			player.getActionSender().sendString("etc", 45227);
			int counter111 = 0;
			for (int i = 0; i < arrayItems.length; i++) {
				player.getActionSender().sendUpdateItem(45245 + counter111, arrayItems[i][0],
						0, arrayItemsLegend[i][1]);
				counter111++;
			}
			break;

		case 4: // Iron man
			player.getActionSender().sendString(
					"Selection Info: <col=ff9900>Iron Man </col> ", 45209);
			player.getActionSender().sendString("<col=ff9900>Selection Perks: </col> ",
					45222);

			player.getActionSender().sendString("Iron Man is an obvious", 45211);
			player.getActionSender().sendString("mode. By selecting Iron", 45212);
			player.getActionSender().sendString(
					"man, <col=ff9900>You will need to talk to the ", 45213);
			player.getActionSender().sendString(" <col=ff9900>Iron man instructor to ",
					45214);
			player.getActionSender().sendString(" <col=ff9900>complete your  ", 45216);
			player.getActionSender().sendString(" <col=ff9900>IRON MAN ACCOUNT", 45217);
			player.getActionSender().sendString(" ", 45218);
			player.getActionSender().sendString("<col=ff9900>Find him west of here.", 45219);
			player.getActionSender().sendString("", 45220);

			// Perks
			player.getActionSender().sendString("<col=ff9900>20x Activity Gold", 45224);
			player.getActionSender().sendString("", 45225);
			player.getActionSender().sendString("", 45226);
			player.getActionSender().sendString("", 45227);
			int counter1111 = 0;
			for (int i = 0; i < arrayItems.length; i++) {
				player.getActionSender().sendUpdateItem(45245 + counter1111, arrayItems[i][0],
						0, arrayItemsLegend[i][1]);
				counter1111++;
			}
			break;

		}

		// player.getPA().sendFrame34a(45245,14484, 0, 1);

	}
    public int arrayItems[][] = { {995, 500_000}, {1115,1}, {1153, 10000}, {1067, 1}, {1704, 1}, {3105 , 1 }, {1129, 1}, {1191,1}, {1323,1},
			{6107,1}, {6108,1}, {6109,1}, {7458,1 }};

	public int arrayItemsHardcore[][] = { {995, 500_000}, {13110,1}, {13319,1}, {1115,1}, {1153, 1}, {1067, 1}, {1704, 1}, {3105 , 1 }, {1129, 1}, {1191,1}, {1323,1},
			{6107,1}, {6108,1}, {6109,1}, {7458,1 }};

	public int arrayItemsLegend[][] = { {995, 500_000}, {3281,1}, {1052,1}, {1115,1}, {1115,1}, {1153, 1}, {1067, 1}, {1704, 1}, {3105 , 1 }, {1129, 1}, {1191,1}, {1323,1},
			{6107,1}, {6108,1}, {6109,1}, {7458,1 }};

    public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
        Region.addWorldObject(objectId, objectX, objectY, player.heightLevel);
        if (player.distanceToPoint(objectX, objectY) > 60) {
            return;
        }
        if (objectId == 1596) {
            CollisionMap.setFlag(0, objectX, objectY, 0);
            CollisionMap.setFlag(0, objectX + 1, objectY, 0);
            CollisionMap.setFlag(0, objectX, objectY + 1, 0);
            CollisionMap.setFlag(0, objectX - 1, objectY, 0);
            CollisionMap.setFlag(0, objectX, objectY - 1, 0);
        }
        if (player.getOutStream() != null) {
            player.getOutStream().writeFrame(85);
            player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
            player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
            player.getOutStream().writeFrame(101);
            player.getOutStream().writeByteC((objectType << 2) + (face & 3));
            player.getOutStream().writeByte(0);
            if (objectId != -1) { // removing
                player.getOutStream().writeFrame(151);
                player.getOutStream().writeByteS(0);
                player.getOutStream().writeWordBigEndian(objectId);
                player.getOutStream().writeByteS((objectType << 2) + (face & 3));
            }
        }
    }
    public void resetAutoCast() {
        player.autocastId = 0;
        player.onAuto = false;
        player.autoCast = false;
        player.getActionSender().sendConfig(108, 0);
    }

    /**
     * Following
     */
    public void followPlayer(boolean forCombat, Entity following) {
        int cbDist = player.followDistance;
        if (following == null || following.isDead()) {
            player.setFollowing(null);
            return;
        }
        if (player.frozen()) {
            return;
        }

        if (player.isDead() || player.getSkills().getLevel(Skills.HITPOINTS) <= 0) {
            player.setFollowing(null);
            return;
        }

        int otherX = following.getX();
        int otherY = following.getY();

        if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
            player.setFollowing(null);
            return;
        }

        boolean sameSpot = (player.absX == otherX && player.absY == otherY);
        if (sameSpot) {
            if (Region.getClipping(player.getX() - 1, player.getY(), player.heightLevel, -1, 0)) {
                walkTo(-1, 0);
            } else if (Region.getClipping(player.getX() + 1, player.getY(), player.heightLevel, 1, 0)) {
                walkTo(1, 0);
            } else if (Region.getClipping(player.getX(), player.getY() - 1, player.heightLevel, 0, -1)) {
                walkTo(0, -1);
            } else if (Region.getClipping(player.getX(), player.getY() + 1, player.heightLevel, 0, 1)) {
                walkTo(0, 1);
            }
            return;
        }

        player.faceEntity(following);

        /**
         * Out of combat following, possibly a bug or 2?
         */
        if (!forCombat) {
            int fx = following.lastTile.getX();
            int fy = following.lastTile.getY();

            int delay = (player.getMovementHandler().isMoving() || ((Player)following).getMovementHandler().isMoving()) ? 1
                : (player.walkTutorial + 1 >= Integer.MAX_VALUE ? player.walkTutorial = 0 : player.walkTutorial++);
            int remainder = delay % 2;
            if (remainder == 1) {
                int x = fx - player.getX();
                int y = fy - player.getY();
                playerWalk(player.getX() + x, player.getY() + y);
                return;
            }
        } else {

            boolean goodCombatDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), cbDist);
            /*
             * Check for other range weapons which require a distance of 4
             */
            if (goodCombatDistance) {
                player.getMovementHandler().stopMovement();
                return;
            }
            /*
             * Check our regular combat styles for distance
             */
            if (player.getCombatType() == CombatStyle.MELEE && player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1)) {
                if (otherX != player.getX() && otherY != player.getY()) {
                    stopDiagonal(player, otherX, otherY);
                    return;
                } else {
                    player.getMovementHandler().stopMovement();
                    return;
                }
            }

            Position[] locs = { new Position(otherX + 1, otherY, player.getZ()), new Position(otherX - 1, otherY, player.getZ()), new Position(otherX, otherY + 1, player.getZ()),
                    new Position(otherX, otherY - 1, player.getZ()), };

            Position followLoc = null;

            for (Position i : locs) {
                if (followLoc == null || player.getPosition().getDistance(i) < player.getPosition().getDistance(followLoc)) {
                    followLoc = i;
                }
            }
            if (followLoc != null) {
                playerWalk(followLoc.getX(), followLoc.getY());
                player.getMovementHandler().followPath = true;
            }
        }
    }

    public static void stopDiagonal(Player player, int otherX, int otherY) {
    	if (player.frozen()) {
            return;
        }
        player.getMovementHandler().reset();
        int xMove = otherX - player.getX();
        int yMove = 0;

        if (xMove == 0) {
            yMove = otherY - player.getY();
        }

        player.getMovementHandler().addToPath(new Position(player.getX() + xMove, player.getY() + yMove, 0));
    }

    public void followNpc(Entity targ) {

        if (targ == null || targ.isDead()) {
            player.setFollowing(null);
            return;
        }
        if (player.frozen()) {
            return;
        }
        if (player.isDead() || player.getSkills().getLevel(Skills.HITPOINTS) <= 0) {
            player.setFollowing(null);
            return;
        }

        int otherX = targ.getX();
        int otherY = targ.getY();

        boolean goodCombatDist = player.goodDistance(otherX, otherY, player.getX(), player.getY(), player.followDistance);

        if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
            player.setFollowing(null);
            return;
        }

		if (goodCombatDist) {
			return;
		}

        NPC npc = (NPC) targ;

        boolean inside = false;
        for (Position tile : npc.getTiles()) {
            if (player.absX == tile.getX() && player.absY == tile.getY()) {
                inside = true;
                break;
            }
        }

        if (!inside) {
            for (Position npcloc : npc.getTiles()) {
                double distance = npcloc.distance(player.getPosition());
                if (distance <= player.followDistance) {
                    player.stopMovement();
                    return;
                }
            }
        }

        if (inside) {
            int r = Utility.getRandom(3);
            switch (r) {
            case 0:
                walkTo(0, -1);
                break;
            case 1:
                walkTo(0, 1);
                break;
            case 2:
                walkTo(1, 0);
                break;
            case 3:
                walkTo(-1, 0);
                break;
            }
        } else {
        	Position[] locs = { new Position(otherX + 1, otherY, player.getZ()), new Position(otherX - 1, otherY, player.getZ()), new Position(otherX, otherY + 1, player.getZ()),
                    new Position(otherX, otherY - 1, player.getZ()), };

            Position followLoc = null;

            for (Position i : locs) {
                if (followLoc == null || player.getPosition().getDistance(i) < player.getPosition().getDistance(followLoc)) {
                    followLoc = i;
                }
            }

            if (followLoc != null) {
                playerWalk(followLoc.getX(), followLoc.getY());
                player.getMovementHandler().followPath = true;
            }
        }
    }

    public void walkTo(int i, int j) {
        player.getMovementHandler().reset();
        player.getMovementHandler().addToPath(new Position(player.getX() + i, player.getY() + j, player.getZ()));
        player.getMovementHandler().finish();
    }

    /**
     * reseting animation
     */
    public void resetAnimation() {
    	CombatAnimation.itemAnimations(player);
    	player.playAnimation(Animation.create(player.standTurnAnimation));
        requestUpdates();
    }

    public void requestUpdates() {
        player.updateRequired = true;
        player.appearanceUpdateRequired = true;
    }

    public void useOperate(int itemId) {
    	
        switch (itemId) {
			
        case 2572:
        	BossTracker.open(player);
            break;
            
        }
    }

    public void openBank() {
    	if (!player.getAccount().getType().canBank()) {
			player.getActionSender().sendMessage("You're restricted to bank because of your account type.");
			return;
		}
        if (player.takeAsNote)
        	player.getActionSender().sendConfig(115, 1);
        else
        	player.getActionSender().sendConfig(115, 0);
        
        if (Trading.isTrading(player)) {
            Trading.decline(player);
        }
        
        if (player.getArea().inWild() && !(player.getRights().isBetween(2, 3))) {
			player.getActionSender().sendMessage("You can't bank in the wilderness!");
			return;
		}
		
        player.stopSkillTask();
        if (player.getBank().getBankSearch().isSearching()) {
            player.getBank().getBankSearch().reset();
        }
        
        player.write(new SendSoundPacket(1457, 0, 0));
        player.getActionSender().sendString("Search", 58113);
        
        if (player.getOutStream() != null && player != null) {
        	player.setBanking(true);
            player.getItems().resetItems(5064);
            player.getItems().resetBank();
            player.getItems().resetTempItems();
            player.getOutStream().writeFrame(248);
            player.getOutStream().writeWordA(5292);
            player.getOutStream().writeShort(5063);
            player.getActionSender().sendString(player.getName() + "'s Bank", 58064);
        }
    }

    public void sendFriendServerStatus(final int i) { // friends and ignore list status
        if (this.player.getOutStream() != null && this.player != null) {
            this.player.getOutStream().writeFrame(221);
            this.player.getOutStream().writeByte(i);
        }
    }
	
	DecimalFormat format = new DecimalFormat("##.##");

	public static double getRatio(int kills, int deaths) {
		double ratio = kills / Math.max(1D, deaths);
		return ratio;
	}

	public double getRatio(Player player) {
		return getRatio(player.getKillCount(), player.getDeathCount());
	}

	public String displayRatio(Player player) {
		return format.format(getRatio(player));
	}
	
	public void destroyItem(Item item) {
		player.getActionSender().sendUpdateItem(14171, item.getId(), 0, 1);
		player.getActionSender().sendString("Are you sure you want to drop this item?", 14174);
		player.getActionSender().sendString("Yes.", 14175);
		player.getActionSender().sendString("No.", 14176);
		player.getActionSender().sendString("", 14177);
		player.getActionSender().sendString("This item is valuable, you will not", 14182);
		player.getActionSender().sendString("get it back once lost.", 14183);
		player.getActionSender().sendString(ItemDefinition.forId(item.getId()).getName(), 14184);
		player.write(new SendChatBoxInterfacePacket(14170));
	}

	public void handleDestroyItem() {
		if (player.getDestroyItem() != -1) {
			if (player.getItems().playerHasItem(player.getDestroyItem())) {
				player.getItems().deleteItem(player.getDestroyItem());
				player.setDestroyItem(-1);
				player.getActionSender().sendRemoveInterfacePacket();
			}
		}
	}

	public void displayReward(Item... items) {
		player.outStream.createFrameVarSizeWord(53);
		player.outStream.writeWord(6963);
		player.outStream.writeWord(items.length);

		for (Item item : items) {
			if (item.amount > 254) {
				player.outStream.writeByte(255);
				player.outStream.writeDWord_v2(item.amount);
			} else {
				player.outStream.writeByte(item.amount);
			}
			if (item.id > 0) {
				player.outStream.writeWordBigEndianA(item.id + 1);
			} else {
				player.outStream.writeWordBigEndianA(0);
			}
		}
		player.outStream.endFrameVarSizeWord();
		player.flushOutStream();
		player.write(new SendInterfacePacket(6960));
	}

	public void sendItems(Item... items) {
		player.outStream.createFrameVarSizeWord(53);
		player.outStream.writeWord(6963);
		player.outStream.writeWord(items.length);

		for (Item item : items) {
			if (item.amount > 254) {
				player.outStream.writeByte(255);
				player.outStream.writeDWord_v2(item.amount);
			} else {
				player.outStream.writeByte(item.amount);
			}
			if (item.id > 0) {
				player.outStream.writeWordBigEndianA(item.id + 1);
			} else {
				player.outStream.writeWordBigEndianA(0);
			}
		}
		player.outStream.endFrameVarSizeWord();
		player.flushOutStream();
		player.write(new SendInterfacePacket(6960));
	}

	public void restoreHealth() {
    	player.faceEntity(player);
		player.stopMovement();
		player.setSpecialAmount(100);
		player.getWeaponInterface().restoreWeaponAttributes();
		player.lastVeng.reset();
		player.setVengeance(false);
		player.setUsingSpecial(false);
		player.attackDelay = 10;
		player.infection = 0;
		player.appearanceUpdateRequired = true;
		player.skullIcon = -1;
		requestUpdates();
		requestUpdates();
		resetAnimation();
		resetTb();
        player.setFollowing(null);
		player.getActionSender().sendRemoveInterfacePacket();
    }

	public void removeobject(int objectX, int objectY) {
		// TODO Auto-generated method stub
		
	}

	


	

	
}