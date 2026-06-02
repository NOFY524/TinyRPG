package shop;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import command.CommandUtil;
import command.SubCommand;
import shop.ui.ShopUIManager;

public class ShopCommandHandler extends SubCommand
{
    private ShopUIManager shopUIManager;

    public ShopCommandHandler(ShopUIManager shopUIManager)
    {
        super("shop");
        this.shopUIManager = shopUIManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can execute this command.");

            return true;
        }

        shopUIManager.clearStack(player);
        shopUIManager.openUI(player, "home");

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        if (args.length >= 1)
            return CommandUtil.partialMatch(args[0], List.of("shop"));

        return Collections.emptyList();
    }
}
