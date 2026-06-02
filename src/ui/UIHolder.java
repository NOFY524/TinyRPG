package ui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class UIHolder implements InventoryHolder
{
    private UI ui;

    public UIHolder(UI ui)
    {
        this.ui = ui;
    }

    public UI getUI()
    {
        return ui;
    }

    public void setUI(UI ui)
    {
        this.ui = ui;
    }

    @Override
    public Inventory getInventory()
    {
        return null;
    }
}
