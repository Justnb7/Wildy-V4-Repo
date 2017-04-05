package com.model.game.item.ground;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.model.Server;
import com.model.game.World;
import com.model.game.character.player.Player;
import com.model.game.character.player.account.Account;
import com.model.game.item.Item;
import com.model.game.item.ground.GroundItem.State;
import com.model.game.location.Position;
import com.model.task.ScheduledTask;
import com.model.task.Stackable;
import com.model.task.Walkable;
import com.model.utility.logging.PlayerLogging;
import com.model.utility.logging.PlayerLogging.LogType;

/**
 * A handler for a collection of {@link GroundItem}s
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class GroundItemHandler {

	private static final List<GroundItem> groundItems = new ArrayList<>();
	
	/**
	 * Registers a new ground item.
	 * 
	 * @param groundItem
	 *            the ground item
	 */
	public static boolean register(GroundItem groundItem) {
		if (find(groundItem.getPosition(), groundItem.getItem().getId()) != null) {
			if (groundItem.getItem().getDefinition().isStackable()) {
				find(groundItem.getPosition(), groundItem.getItem().getId()).setItem(new Item(groundItem.getItem().getId(), find(groundItem.getPosition(), groundItem.getItem().getId()).getItem().getAmount() + groundItem.getItem().getAmount()));
				return false;
			}
		}
		groundItems.add(groundItem);
		return true;
	}
	
	/**
	 * Finds the ground item by a given location.
	 * 
	 * @param position
	 *            the item location
	 * 
	 * @param itemIndex
	 *            the item index
	 */
	public static GroundItem find(Position position, int itemIndex) {
		for (GroundItem groundItem : groundItems) {
			if (groundItem == null) {
				continue;
			}
			if (groundItem.getPosition().equals(position) && groundItem.getItem().getId() == itemIndex) {
				return groundItem;
			}
		}
		return null;
	}

	public static Optional<GroundItem> get(int id, Position position/*int x, int y, int z*/) {
		return groundItems.stream().filter(item -> item.getItem().getId() == id && item.getPosition().getX() == position.getX()
				&& item.getPosition().getY() == position.getY() && item.getPosition().getZ() == position.getZ()).findFirst();
	}

	/**
	 * Creates the ground item
	 */
	public static int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 },
			{ 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 }, { 4722, 4902 }, { 4732, 4932 }, { 4734, 4938 },
			{ 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 },
			{ 4745, 4956 }, { 4747, 4962 }, { 4749, 4968 }, { 4751, 4974 }, { 4753, 4980 }, { 4755, 4986 },
			{ 4757, 4992 }, { 4759, 4998 } };

	public static boolean removeGroundItem(GroundItem groundItem) {
		for (GroundItem other : groundItems) {
			if (groundItem.getItem().getId() == other.getItem().getId()
					&& groundItem.getPosition().getX() == other.getPosition().getX()
					&& groundItem.getPosition().getY() == other.getPosition().getY()
					&& groundItem.getPosition().getZ() == other.getPosition().getZ() && !other.isRemoved()
					&& groundItem.getItem().getAmount() == other.getItem().getAmount()) {
				other.setRemoved(true);
				removeRegionalItem(other);
				return true;
			}
		}
		return false;
	}

	private static void removeRegionalItem(GroundItem groundItem) {
		for (Player player : World.getWorld().getPlayers()) {
			if (player == null || player.distanceToPoint(groundItem.getPosition().getX(), groundItem.getPosition().getY()) > 60) {
				continue;
			}

			player.getActionSender().sendRemoveGroundItem(groundItem);
		}
	}

	private static void addRegionalItem(GroundItem groundItem) {
		for (Player player : World.getWorld().getPlayers()) {
			if (player == null || player.getPosition().getZ() != groundItem.getPosition().getZ() || player.distanceToPoint(groundItem.getPosition().getX(), groundItem.getPosition().getY()) > 60) {
				continue;
			}
			
			if (player.getAccount().getType().alias().equals(Account.IRON_MAN_TYPE.alias()) || player.getAccount().getType().alias().equals(Account.ULTIMATE_IRON_MAN_TYPE.alias()) || player.getAccount().getType().alias().equals(Account.HARDCORE_IRON_MAN_TYPE.alias())) {
				continue;
			}

			// If we are globalizing an item, don't re-add it for the owner
			if (player.usernameHash != groundItem.getOwnerHash()) {

				if (groundItem.getType() == GroundItemType.PRIVATE) {
					continue;
				}

				/**
				 * If the item is a non-tradable item continue
				 * 
				 */

				if (!player.getItems().isTradeable(groundItem.getItem().getId())) {
					continue;
				}

				// get the owner of the item by name and see if they are
				// online.
				if (player.distanceToPoint(groundItem.getPosition().getX(), groundItem.getPosition().getY()) <= 60
						&& player.getPosition().getZ() == player.getPosition().getZ()) {
					player.getActionSender().sendGroundItem(groundItem);
				}
			}
		}
	}
	
	public static void pulse() {
		Iterator<GroundItem> iterator = groundItems.iterator();
		while (iterator.hasNext()) {
			GroundItem item = iterator.next();

			if (item.isRemoved()) {
				iterator.remove();
				continue;
			}
			
			
			if (item.decreaseTimer() < 1) {
				if (item.getState() == State.GLOBAL) {
					item.setRemoved(true);
					iterator.remove();
					removeRegionalItem(item);
					if (item.deathShop()) {
						Player owner = item.getOwner();
						if (owner != null) {
							if (owner.deathShopEnabled) {
								owner.deathShop.getContainer().add(item.getItem());
								owner.getActionSender().sendMessage("The " + item.getItem().getDefinition().getName() + "x" + item.getItem().getAmount() + " is now in your death store!");
							}
						}
					}
				}

				if (item.getState() == State.PRIVATE) {
					item.setState(State.GLOBAL);
					item.setTimer(400);
					addRegionalItem(item);
				}
			}
		}
	}

	public static boolean add(GroundItem groundItem) {
		return groundItems.add(groundItem);
	}

	public static int getItemAmount(GroundItem groundItem) {
		Item item = groundItem.getItem();
		for (GroundItem other : groundItems) {
			if (groundItem.getOwnerHash() == other.getOwnerHash() || other.getState() == (State.GLOBAL)) {
				if (groundItem.getItem().getId() == other.getItem().getId()
						&& groundItem.getPosition().getX() == other.getPosition().getX()
						&& groundItem.getPosition().getY() == other.getPosition().getY()
						&& groundItem.getPosition().getZ() == other.getPosition().getZ() && !other.isRemoved()) {
					if (item.getDefinition().isStackable()) {
						int existingCount = other.getItem().getAmount();
						long newCount = (long) existingCount + item.getAmount();
						if (newCount > Integer.MAX_VALUE) {
							return -1;
						}
						return (int) newCount;
					}
				}
			}
		}
		return 0;
	}

	public static void reloadGroundItems(Player player) {
		for (GroundItem groundItem : groundItems) {
			if (player.getPosition().getZ() != groundItem.getPosition().getZ() || player.distanceToPoint(groundItem.getPosition().getX(), groundItem.getPosition().getY()) > 60) {
				continue;
			}
			
			if (groundItem.getOwnerHash() != player.usernameHash) {
				if (!player.getAccount().getType().unownedDropsVisible() || (groundItem.getType() == GroundItemType.PRIVATE)) {
					continue;
				}
			}
			
			if (groundItem.getOwnerHash() != player.usernameHash) {
				if (groundItem.getType() == GroundItemType.PRIVATE) {
					continue;
				}
			}
			
			if (player.getItems().isTradeable(groundItem.getItem().getId())
					|| groundItem.getOwnerHash() == player.usernameHash) {
				
				if (groundItem.getState() == State.GLOBAL || groundItem.getOwnerHash() == player.usernameHash) {
					//System.out.println(player.getLocation() + " : " + groundItem.getLocation());
					player.getActionSender().sendRemoveGroundItem(groundItem);
					player.getActionSender().sendGroundItem(groundItem);
				}
			}
		}
	}

	public static boolean createGroundItem(GroundItem groundItem) {
		Player player = groundItem.getOwner();
		if (groundItem.getItem().getId() < 0) {
			return false;
		}
		if (player == null) {
			groundItem.setState(State.GLOBAL);
		}

		PlayerLogging.write(LogType.DEATH_LOG, groundItem.getOwner(), "Items added to floor : " + groundItem.getItem().getId() + " Amount : "  + groundItem.getItem().getAmount());
		if (player != null && player.getAccount().getType().alias().equals(Account.IRON_MAN_TYPE.alias()) || player.getAccount().getType().alias().equals(Account.ULTIMATE_IRON_MAN_TYPE.alias()) || player.getAccount().getType().alias().equals(Account.HARDCORE_IRON_MAN_TYPE.alias())) {
			groundItem.setGroundItemType(GroundItemType.PRIVATE);
		}
		
		if (groundItem.getItem().getId() > 4705 && groundItem.getItem().getId() < 4760) {
			for (int[] brokenBarrow : brokenBarrows) {
				if (brokenBarrow[0] == groundItem.getItem().getId()) {
					groundItem.getItem().id = brokenBarrow[1];
					break;
				}
			}
		}
		
		if (groundItem.getItem().getId() >= 2412 && groundItem.getItem().getId() <= 2414) {
			player.getActionSender().sendMessage("The cape vanishes as it touches the ground.");
			return false;
		}

		for (GroundItem other : groundItems) {
			if (groundItem.getItem().getId() == other.getItem().getId()
					&& groundItem.getPosition().getX() == other.getPosition().getX()
					&& groundItem.getPosition().getY() == other.getPosition().getY()
					&& groundItem.getPosition().getZ() == other.getPosition().getZ() && !other.isRemoved()) {
				if (other.getState() == State.GLOBAL || other.getOwnerHash() == player.usernameHash) {
					int existing = getItemAmount(groundItem);

					if (existing == -1) {
						if (player != null) {
							player.getActionSender().sendMessage("There is not enough room for your item on this tile.");
						}
						return false;
					}

					if (existing > 0) {
						other.getItem().setCount(existing);
						other.setTimer(groundItem.getState() == State.GLOBAL ? 200 : 100);
						return true;
					}
				}
			}
		}

		if (add(groundItem)) {
			groundItem.setTimer(groundItem.getState() == State.GLOBAL ? 200 : 100);
			if (player != null) {
				player.getActionSender().sendGroundItem(groundItem);
				//System.out.println("Grounditem height: "+player.getLocation().getH());
			}
			if (groundItem.getState() == State.GLOBAL) {
				addRegionalItem(groundItem);
			}
		}

		return true;
	}

	public static void pickup(Player player, int id, Position position) {
		Optional<GroundItem> optionalGroundItem = get(id, position);
		if (!optionalGroundItem.isPresent()) {
			return;
		}

		GroundItem groundItem = optionalGroundItem.get();

		Server.getTaskScheduler().schedule(new ScheduledTask(player, 1, true, Walkable.WALKABLE, Stackable.STACKABLE) {
			@Override
			public void execute() {
				if (!player.isActive()) {
					stop();
					return;
				}
				if (groundItem.getState() != State.GLOBAL && groundItem.getOwnerHash() != player.usernameHash) {
					stop();
					return;
				}
				if (groundItem.isRemoved()) {
					stop();
					return;
				}

				Item item = groundItem.getItem();

				if (player.getPosition().getX() == groundItem.getPosition().getX()
						&& player.getPosition().getY() == groundItem.getPosition().getY()) {

					if (player.getItems().freeSlots() == 0
							&& !(player.getItems().playerHasItem(item.getId()) && item.getDefinition().isStackable())) {
						player.getActionSender().sendMessage("You do not have enough inventory space to pick that up.");
						stop();
						return;
					}
					removeGroundItem(groundItem);
					player.getItems().addItemtoInventory(item);
					PlayerLogging.write(LogType.DEATH_LOG, groundItem.getOwner(), "Item Dropped: " + groundItem.getItem().getId() + " Items picked up by : " + player.getName() + " Amount:  " + groundItem.getItem().getAmount() );
					stop();
				}
			}
		});
	}

}