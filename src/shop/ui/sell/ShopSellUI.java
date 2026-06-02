package shop.ui.sell;

import currency.CurrencyFormatter;
import currency.CurrencyManager;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import shop.config.ShopConfigManager;
import shop.ui.ShopUI;
import shop.ui.ShopUIHolder;
import shop.ui.ShopUIManager;

public class ShopSellUI extends ShopUI
{
    private ShopConfigManager configManager;
    private ItemStack selectedItem;

    public ShopSellUI(ShopUIManager shopUIManager, CurrencyManager currencyManager, ShopConfigManager configManager)
    {
        super(shopUIManager, currencyManager);
        this.configManager = configManager;
        this.selectedItem = null;
    }

    @Override
    public String getName()
    {
        return "§8[Shop] Sell Menu";
    }

    @Override
    public Inventory buildInventory(Player player)
    {
        // 1. Build background rows from parent class ShopUI
        Inventory inv = super.buildInventory(player);
        ((ShopUIHolder) inv.getHolder()).setUI(this);

        // 2. Clear out center rows to make an open visual window for clarity if
        // desired,
        // or keep standard layout. Let's keep it clean.

        if (selectedItem != null && selectedItem.getType() != Material.AIR) {
            inv.setItem(2 * 9 + 4, selectedItem);
        } else {
            ItemStack placeholder = new ItemStack(Material.BARRIER);
            ItemMeta meta = placeholder.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§c[ No Item Selected ]");
                meta.setLore(List.of(
                        "§7Left-Click any target item within",
                        "§7your inventory to check market values."));
                placeholder.setItemMeta(meta);
            }

            inv.setItem(2 * 9 + 4, placeholder);
        }
        return inv;
    }

    @Override
    public boolean onClick(Player player, int rawSlot, ItemStack item, ClickType clickType)
    {
        // Let parent class handle standard back button interactions
        if (super.onClick(player, rawSlot, item, clickType)) {
            return false;
        }

        // Identify if the player clicked their own inventory zone (Below the shop GUI)
        // Upper interface is 5 rows * 9 slots = 45 total slots
        int topGuiSize = (getRows() + 1) * 9;

        if (rawSlot >= topGuiSize) {
            if (item == null || item.getType() == Material.AIR) {
                return false;
            }

            // Fetch pricing data dynamically handled from the YAML config layer
            long unitPrice = configManager.getPrice(item.getType());

            // 1. Left-Click: Simple price appraisal check
            if (clickType == ClickType.LEFT) {
                ItemStack displayCopy = new ItemStack(item.getType());

                ItemMeta meta = displayCopy.getItemMeta();
                if (meta != null) {
                    // Injects completely custom English price lore onto the display copy
                    meta.setDisplayName("§e§lAppraised: §f" + displayCopy.getType().name());
                    meta.setLore(List.of(
                            "§7--------------------",
                            "§7Price: §6" + CurrencyFormatter.formatCompact(unitPrice),
                            "§7--------------------",
                            "§7Right click to sell one, shift right click to sell entire stack"));
                    displayCopy.setItemMeta(meta);
                }
                selectedItem = displayCopy;
                player.openInventory(buildInventory(player));

                return false;
            }

            if (clickType == ClickType.RIGHT) {
                long totalEarnings = unitPrice;
                String itemName = item.getType().name();

                // Deduct materials safely from player inventory context
                item.setAmount(item.getAmount() - 1);

                // Add funds to player profile via CurrencyManager
                // Assuming getCurrencyManager() or direct field access is wired up
                // (If currencyManager field in ShopUI is private, make it protected or add
                // getter)
                currencyManager.addBalance(player.getUniqueId(), totalEarnings);
                player.sendMessage("§aSuccessfully sold " + itemName + " for §6"
                        + CurrencyFormatter.formatCompact(totalEarnings));

                // Play a generic clear transactional sound effect if wanted, then refresh
                // layout view
                player.openInventory(buildInventory(player));

                return false;
            }

            // 2. Right-Click: Execute Instant Trade Settlement
            if (clickType == ClickType.SHIFT_RIGHT) {
                int soldAmount = item.getAmount();
                long totalEarnings = unitPrice * soldAmount;
                String itemName = item.getType().name();

                // Deduct materials safely from player inventory context
                item.setAmount(0);

                // Add funds to player profile via CurrencyManager
                // Assuming getCurrencyManager() or direct field access is wired up
                // (If currencyManager field in ShopUI is private, make it protected or add
                // getter)
                currencyManager.addBalance(player.getUniqueId(), totalEarnings);
                player.sendMessage("§aSuccessfully sold " + soldAmount + "x " + itemName + " for §6"
                        + CurrencyFormatter.formatCompact(totalEarnings));

                // Play a generic clear transactional sound effect if wanted, then refresh
                // layout view
                player.openInventory(buildInventory(player));
                return false;
            }
        }

        return false;
    }
}