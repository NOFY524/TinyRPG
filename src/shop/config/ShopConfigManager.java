package shop.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tinyrpg.TinyRPG;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class ShopConfigManager
{
    private final TinyRPG env;
    private final File configFile;
    private FileConfiguration config;
    private final Map<Material, Long> priceMap = new EnumMap<>(Material.class);

    public ShopConfigManager(TinyRPG env)
    {
        this.env = env;

        File dataFolder = new File(env.getDataFolder(), "shop");
        if (!dataFolder.exists())
            dataFolder.mkdirs();

        this.configFile = new File(dataFolder, "shop_prices.yml");

        loadConfig();
    }

    public void loadConfig()
    {
        if (!configFile.exists()) {
            env.getDataFolder().mkdirs();
            try {
                configFile.createNewFile();
                // Initialize with default values if file doesn't exist
                config = YamlConfiguration.loadConfiguration(configFile);
                config.set("prices.DIAMOND", 200);
                config.set("prices.GOLD_INGOT", 100);
                config.set("prices.IRON_INGOT", 30);
                config.save(configFile);
            } catch (IOException e) {
                env.getLogger().severe("Could not create shop_prices.yml!");
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        priceMap.clear();

        if (config.getConfigurationSection("prices") != null) {
            for (String key : config.getConfigurationSection("prices").getKeys(false)) {
                try {
                    Material material = Material.valueOf(key.toUpperCase());
                    long price = config.getLong("prices." + key);
                    priceMap.put(material, price);
                } catch (IllegalArgumentException e) {
                    env.getLogger().warning("Invalid Material name in shop_prices.yml: " + key);
                }
            }
        }
    }

    public long getPrice(Material material)
    {
        return priceMap.getOrDefault(material, 0L);
    }
}