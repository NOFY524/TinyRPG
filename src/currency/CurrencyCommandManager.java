package currency;

import java.util.List;

import org.bukkit.command.CommandSender;

import command.MultiSubCommand;

import currency.command.BalanceCommand;
import currency.command.WithdrawCommand;

public class CurrencyCommandManager extends MultiSubCommand
{
    public CurrencyCommandManager(CurrencyManager currencyManager)
    {
        super("currency", List.of("cr", "c"));

        register(new BalanceCommand(currencyManager));
        register(new WithdrawCommand(currencyManager));
    }

    @Override
    protected boolean handleDefault(CommandSender sender)
    {
        sender.sendMessage(String.format("§eUsage: /tr %s < balance >", getName()));

        return true;
    }

    @Override
    protected boolean handleNotFound(CommandSender sender, String command)
    {
        sender.sendMessage(String.format("§cCommand not found: %s", command));

        return true;
    }
}