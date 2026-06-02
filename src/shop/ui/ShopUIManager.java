package shop.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import currency.CurrencyManager;
import shop.config.ShopConfigManager;
import shop.ui.home.ShopHomeUI;
import shop.ui.sell.ShopSellUI;
import ui.UIManager;

public class ShopUIManager extends UIManager<ShopUIManager> implements Listener
{

    public ShopUIManager(CurrencyManager currencyManager, ShopConfigManager shopConfigManager)
    {
        registerUI("home", uiManager -> new ShopHomeUI(uiManager, currencyManager));
        registerUI("sell", uiManager -> new ShopSellUI(uiManager, currencyManager, shopConfigManager));
    }

    @Override
    @EventHandler
    public void onOpen(InventoryOpenEvent event)
    {
    }

    @Override
    @EventHandler
    public void onClose(InventoryCloseEvent event)
    {
    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        Inventory topInventory = event.getView().getTopInventory();
        if (!(topInventory.getHolder() instanceof ShopUIHolder uiHolder))
            return;

        event.setCancelled(true);

        uiHolder.getUI().onClick(player, event.getRawSlot(), event.getCurrentItem(),
                event.getClick());
    }
}