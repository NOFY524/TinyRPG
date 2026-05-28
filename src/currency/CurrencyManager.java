package currency;

import java.util.Map;
import java.util.UUID;

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

    public long getBalance(UUID uuid)
    {
        if (!currencyData.containsKey(uuid))
            currencyData.put(uuid, 0L);

        return currencyData.get(uuid);
    }

    public void setBalance(UUID uuid, Long amount)
    {
        currencyData.put(uuid, amount);
    }

    public void addBalance(UUID uuid, Long amount)
    {
        if (amount <= 0)
            return;

        long current_balance = getBalance(uuid);

        currencyData.put(uuid, current_balance + amount);
    }

    public boolean removeBalance(UUID uuid, Long amount)
    {
        if (amount <= 0)
            return amount == 0;

        long current_balance = getBalance(uuid);

        if (amount > current_balance)
            return false;

        currencyData.put(uuid, current_balance - amount);

        return true;
    }

    public void storeData()
    {
        currencyStorage.storeData(currencyData);
    }
}
