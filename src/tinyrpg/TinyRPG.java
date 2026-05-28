package tinyrpg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import currency.CurrencyCommandManager;
import currency.CurrencyManager;
import currency.CurrencyUIManager;

public class TinyRPG extends JavaPlugin
{
    private CurrencyManager currencyManager;
    private CurrencyUIManager currencyUIManager;

    private TinyRPGCommandExecutor tinyRPGCommandExecutor;

    @Override
    public void onEnable()
    {
        getLogger().info("Hello, world! from TinyRPG");

        // Initializing currency and scheduling data store
        this.currencyManager = new CurrencyManager(this);
        Bukkit.getScheduler().runTaskTimer(this, currencyManager::storeData, 20L * 60L * 5L, 20L * 60L * 5L);

        this.currencyUIManager = new CurrencyUIManager(currencyManager);
        Bukkit.getScheduler().runTaskTimer(this, currencyUIManager::update, 20L, 20L);

        // CommandHandler registration
        tinyRPGCommandExecutor = new TinyRPGCommandExecutor();

        CurrencyCommandManager currencyCommandManager = new CurrencyCommandManager(currencyManager);

        tinyRPGCommandExecutor.addSubCommandHandler("currency", currencyCommandManager);
        tinyRPGCommandExecutor.addSubCommandHandler("cr", currencyCommandManager);

        getCommand("tr").setExecutor(tinyRPGCommandExecutor);
        getCommand("tr").setTabCompleter(tinyRPGCommandExecutor);
    }

    @Override
    public void onDisable()
    {
        currencyManager.storeData();
    }

    public void log(String message)
    {
        getLogger().info(message);
    }

    public void panic(String message)
    {
        getLogger().severe(message);
        getLogger().severe("Disabling this plugin");

        getServer().getPluginManager().disablePlugin(this);
    }
}