package tinyrpg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import currency.CurrencyCommandManager;
import currency.CurrencyDepositListener;
import currency.CurrencyItemManager;
import currency.CurrencyManager;
import currency.CurrencyUIManager;
import currency.command.WithdrawCommand;

public class TinyRPG extends JavaPlugin
{
    private CurrencyManager currencyManager;
    private CurrencyUIManager currencyUIManager;
    private CurrencyDepositListener currencyDepositListener;

    private TinyRPGCommandExecutor tinyRPGCommandExecutor;

    @Override
    public void onEnable()
    {
        getLogger().info("Hello, world! from TinyRPG");

        CurrencyItemManager.init(this);

        // Initializing currency and scheduling data store
        this.currencyManager = new CurrencyManager(this);
        Bukkit.getScheduler().runTaskTimer(this, currencyManager::storeData, 20L * 60L * 5L, 20L * 60L * 5L);

        this.currencyUIManager = new CurrencyUIManager(currencyManager);
        Bukkit.getPluginManager().registerEvents(currencyUIManager, this);

        this.currencyDepositListener = new CurrencyDepositListener(currencyManager);
        Bukkit.getPluginManager().registerEvents(currencyDepositListener, this);

        // CommandHandler registration
        tinyRPGCommandExecutor = new TinyRPGCommandExecutor();

        tinyRPGCommandExecutor.register(new CurrencyCommandManager(currencyManager));
        tinyRPGCommandExecutor.register(new WithdrawCommand(currencyManager));

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