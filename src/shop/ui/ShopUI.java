package shop.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import currency.CurrencyFormatter;
import currency.CurrencyManager;
import ui.UI;

public abstract class ShopUI implements UI
{
    protected ShopUIManager shopUIManager;
    protected CurrencyManager currencyManager;
    private int rows = 5;

    public ShopUI(ShopUIManager shopUIManager, CurrencyManager currencyManager)
    {
        this.shopUIManager = shopUIManager;
        this.currencyManager = currencyManager;
    }

    private ItemStack getBackgroundItem()
    {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§7");

            meta.setLore(Collections.emptyList());

            item.setItemMeta(meta);
        }

        return item;
    }

    private ItemStack createPlayerHead(Player player)
    {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if (head.getItemMeta() instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(player);

            skullMeta.setDisplayName("§b§l" + player.getName());

            List<String> lore = new ArrayList<>();
            lore.add("§7--------------------");

            long balance = currencyManager.getBalance(player.getUniqueId());

            lore.add("§bMoney: §6" + CurrencyFormatter.formatCompact(balance));
            lore.add("§7--------------------");

            skullMeta.setLore(lore);

            head.setItemMeta(skullMeta);
        }

        return head;
    }

    private ItemStack createPrevButton()
    {
        ItemStack prevButton = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = prevButton.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§c§l◀ Previous Page");

            meta.setLore(Collections.emptyList());

            prevButton.setItemMeta(meta);
        }

        return prevButton;
    }

    @Override
    public Inventory buildInventory(Player player)
    {
        Inventory inv = Bukkit.createInventory(new ShopUIHolder(this), (rows + 1) * 9, getName());

        ItemStack background = getBackgroundItem();
        for (int i = 0; i < 9; i++) {
            inv.setItem(rows * 9 + i, background);
        }

        inv.setItem(rows * 9, createPrevButton());
        inv.setItem(rows * 9 + 8, createPlayerHead(player));

        return inv;
    }

    @Override
    public boolean onClick(Player player, int slot, ItemStack item, ClickType type)
    {
        if (type == ClickType.LEFT && (rows * 9 <= slot && slot <= rows * 9 + 8)) {
            if (slot == rows * 9)
                shopUIManager.openPreviousUI(player);

            return true;
        }

        return false;
    }

    @Override
    public int getRows()
    {
        return rows;
    }
}
