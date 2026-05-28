package currency;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CurrencyDepositListener implements Listener
{
    private CurrencyManager currencyManager;

    public CurrencyDepositListener(CurrencyManager currencyManager)
    {
        this.currencyManager = currencyManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!CurrencyItemManager.isCashCheck(item))
            return;

        event.setCancelled(true);

        long amount = CurrencyItemManager.getCheckValue(item);
        if (amount <= 0)
            return;

        item.setAmount(item.getAmount() - 1);
        currencyManager.addBalance(player.getUniqueId(), amount);

        player.sendMessage(
                String.format("§aSuccessfully deposited §6%s §ato your account.", CurrencyFormatter.format(amount)));
    }
}
