package ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface UI
{
    Inventory buildInventory(Player player);

    boolean onClick(Player player, int slot, ItemStack item, ClickType clickType);

    int getRows();

    default void onOpen(Player player)
    {
    }

    default void onClose(Player player)
    {
    }

    String getName();
}
