package currency;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import currency.event.UserBalanceChangeEvent;
import tinyrpg.TinyRPG;

public class CurrencyManager
{
    private Map<UUID, Long> currencyData;
    private CurrencyStorage currencyStorage;

    public CurrencyManager(TinyRPG env)
    {
        this.currencyStorage = new CurrencyStorage(env);
        this.currencyData = currencyStorage.loadData();
    }

    public long getBalance(UUID playerUUID)
    {
        if (!currencyData.containsKey(playerUUID))
            currencyData.put(playerUUID, 0L);

        return currencyData.get(playerUUID);
    }

    public void setBalance(UUID playerUUID, Long amount)
    {
        long oldBalance = getBalance(playerUUID);

        setBalance(playerUUID, oldBalance, amount);
    }

    private void setBalance(UUID playerUUID, Long oldBalance, Long newBalance)
    {
        if (oldBalance.equals(newBalance))
            return;

        currencyData.put(playerUUID, newBalance);

        Bukkit.getPluginManager().callEvent(new UserBalanceChangeEvent(playerUUID, oldBalance, newBalance));
    }

    public void addBalance(UUID playerUUID, Long amount)
    {
        if (amount <= 0)
            return;

        long current_balance = getBalance(playerUUID);

        setBalance(playerUUID, current_balance, current_balance + amount);
    }

    public boolean removeBalance(UUID playerUUID, Long amount)
    {
        if (amount <= 0)
            return amount == 0;

        long current_balance = getBalance(playerUUID);

        if (amount > current_balance)
            return false;

        setBalance(playerUUID, current_balance, current_balance - amount);

        return true;
    }

    public void storeData()
    {
        currencyStorage.storeData(currencyData);
    }
}
