package currency;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class CurrencyItemManager
{
    private static NamespacedKey cashKey;

    public static void init(JavaPlugin javaPlugin)
    {
        cashKey = new NamespacedKey(javaPlugin, "tr_cash_key");
    }

    public static ItemStack createCheck(Long value)
    {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§l[ TR Cash Check ]");
            meta.setLore(List.of(
                    "§7Value: §6" + CurrencyFormatter.format(value),
                    "§7Right-click to deposit this cash to your account."));

            if (value >= 10000) {
                meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setRarity(ItemRarity.EPIC);
            } else if (value >= 5000) {
                meta.setRarity(ItemRarity.RARE);
            } else if (value >= 1000) {
                meta.setRarity(ItemRarity.UNCOMMON);
            } else {
                meta.setRarity(ItemRarity.COMMON);
            }

            meta.getPersistentDataContainer().set(cashKey, PersistentDataType.LONG, value);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static boolean isCashCheck(ItemStack item)
    {
        if (item == null || !item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();

        return meta.getPersistentDataContainer().has(cashKey);
    }

    public static Long getCheckValue(ItemStack item)
    {
        if (item == null || !item.hasItemMeta())
            return 0L;

        ItemMeta meta = item.getItemMeta();
        Long value = meta.getPersistentDataContainer().get(cashKey, PersistentDataType.LONG);

        return value == null ? 0L : value;
    }
}
