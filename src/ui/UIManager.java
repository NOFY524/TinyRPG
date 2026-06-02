package ui;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class UIManager<M extends UIManager<M>>
{
    private Map<UUID, Deque<UI>> uiStacks;
    private Map<String, Function<M, UI>> uiRegistry;

    public UIManager()
    {
        this.uiStacks = new HashMap<>();
        this.uiRegistry = new HashMap<>();
    }

    public void registerUI(String uiName, Function<M, UI> function)
    {
        uiRegistry.put(uiName, function);
    }

    public boolean openUI(Player player, String uiName)
    {
        Function<M, UI> function = uiRegistry.get(uiName);
        if (function == null) {
            return false;
        }

        @SuppressWarnings("unchecked")
        UI ui = function.apply((M) this);

        openUI(player, ui);

        return true;
    }

    private void openUI(Player player, UI ui)
    {
        Deque<UI> uiStack = uiStacks.get(player.getUniqueId());
        if (uiStack == null) {
            uiStack = new LinkedList<>();
            uiStacks.put(player.getUniqueId(), uiStack);
        }

        uiStack.push(ui);

        player.openInventory(ui.buildInventory(player));
    }

    public void openPreviousUI(Player player)
    {
        Deque<UI> uiStack = uiStacks.get(player.getUniqueId());

        if (uiStack.size() <= 1)
            return;

        uiStack.pop();

        player.openInventory(uiStack.peek().buildInventory(player));
    }

    public void clearStack(Player player)
    {
        uiStacks.remove(player.getUniqueId());
    }

    @EventHandler
    public abstract void onOpen(InventoryOpenEvent event);

    @EventHandler
    public abstract void onClose(InventoryCloseEvent event);

    @EventHandler
    public abstract void onClick(InventoryClickEvent event);

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        clearStack(event.getPlayer());
    }
}
