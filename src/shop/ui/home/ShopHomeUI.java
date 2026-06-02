package shop.ui.home;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import currency.CurrencyManager;
import shop.ui.ShopUI;
import shop.ui.ShopUIHolder;
import shop.ui.ShopUIManager;

public class ShopHomeUI extends ShopUI
{
    public ShopHomeUI(ShopUIManager uiManager, CurrencyManager currencyManager)
    {
        super(uiManager, currencyManager);
    }

    @Override
    public Inventory buildInventory(Player player)
    {
        Inventory inv = super.buildInventory(player);

        // Should not produce error
        ((ShopUIHolder) inv.getHolder()).setUI(this);

        ItemStack sellButton = new ItemStack(Material.PAPER);
        ItemMeta meta = sellButton.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§e§l[ Sell Menu ]");
            meta.setLore(List.of(
                    "§7Click here to sell your valuable",
                    "§7items to the server and make profit.",
                    "",
                    "§a▶ Click to open sell shop"));

            meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            sellButton.setItemMeta(meta);
        }

        inv.setItem(2 * 9 + 4, sellButton);

        return inv;
    }

    @Override
    public boolean onClick(Player player, int slot, ItemStack item, ClickType clickType)
    {
        if (super.onClick(player, slot, item, clickType))
            return false;

        if (slot >= getRows() * 9 - 1)
            return false;

        if (clickType == ClickType.LEFT && slot == 2 * 9 + 4) {
            shopUIManager.openUI(player, "sell");

            return false;
        }

        return false;
    }

    @Override
    public String getName()
    {
        return "§8[Shop] Main Menu";
    }
}
