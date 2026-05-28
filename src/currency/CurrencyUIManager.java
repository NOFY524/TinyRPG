package currency;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CurrencyUIManager
{
    private CurrencyManager currencyManager;

    public CurrencyUIManager(CurrencyManager currencyManager)
    {
        this.currencyManager = currencyManager;
    }

    public void update()
    {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == null)
                continue;

            long balance = currencyManager.getBalance(player.getUniqueId());
            String formatted = CurrencyFormatter.format_compact(balance);

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(formatted));
        }
    }
}