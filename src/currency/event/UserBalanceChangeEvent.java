package currency.event;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserBalanceChangeEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private final UUID playerUUID;
    private final long oldBalance, newBalance;

    public UserBalanceChangeEvent(UUID playerUUID, long oldBalance, long newBalance)
    {
        this.playerUUID = playerUUID;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
    }

    public UUID getPlayerUUID()
    {
        return playerUUID;
    }

    public long getOldBalance()
    {
        return oldBalance;
    }

    public long getNewBalance()
    {
        return newBalance;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}