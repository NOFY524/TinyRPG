package currency;

import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import command.CommandUtil;
import command.SubCommand;
import command.SubCommandHandler;

import currency.command.BalanceCommand;

public class CurrencyCommandManager implements SubCommandHandler
{
    private Map<String, SubCommand> subCommands;

    public CurrencyCommandManager(CurrencyManager currencyManager)
    {
        this.subCommands = new HashMap<>();

        register(new BalanceCommand(currencyManager));
    }

    @Override
    public void register(SubCommand subCommand)
    {
        subCommands.put(subCommand.getName(), subCommand);
        for (String alias : subCommand.getAliases())
            subCommands.put(alias, subCommand);
    }

    @Override
    public Set<String> getCommands()
    {
        return subCommands.keySet();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        if (args.length == 1) {
            return CommandUtil.partialMatch(args[0], subCommands.keySet());
        }

        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            return Collections.emptyList();
        }

        return subCommand.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length == 0) {
            return false;
        }

        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            return false;
        }

        return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }
}