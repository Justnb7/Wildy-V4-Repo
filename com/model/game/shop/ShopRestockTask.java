package com.model.game.shop;

import java.util.Objects;

import com.model.game.item.Item;
import com.model.task.ScheduledTask;

/**
 * The task that will restock items in shop containers when needed.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ShopRestockTask extends ScheduledTask {

    /**
     * The container that will be restocked.
     */
    private final Shop container;

    /**
     * Creates a new {@link ShopRestockTask}.
     *
     * @param container
     *         the container that will be restocked.
     */
    public ShopRestockTask(Shop container) {
        super(20, false);
        super.attach(container);
        this.container = container;
    }

    @Override
    public void execute() {
        if (container.restockCompleted() || !container.isRestock()) {
            this.stop();
            return;
        }
        container.getContainer().stream().filter(Objects::nonNull).forEach(this::restock);
    }

    /**
     * Attempts to restock {@code item} for the container.
     *
     * @param item
     *         the item to attempt to restock.
     */
    private void restock(Item item) {
        if (container.getItemCache().containsKey(item.getId()) && item.getAmount()< container.getItemCache().get(item.getId())) {
            item.incrementAmount();
            int size = container.getContainer().size();
            container.getPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getActionSender().sendItemsOnInterface(3900, container.getContainer().container(), size));
        }
    }
}