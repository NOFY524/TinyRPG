package command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

public abstract class MultiSubCommand extends SubCommand
{
    private Map<String, SubCommand> subCommands;

    public MultiSubCommand(String name)
    {
        this(name, Collections.emptyList());
    }

    public MultiSubCommand(String name, List<String> aliases)
    {
        super(name, aliases);
        this.subCommands = new HashMap<>();
    }

    public void register(SubCommand subCommand)
    {
        subCommands.put(subCommand.getName(), subCommand);
        for (String alias : subCommand.getAliases())
            subCommands.put(alias, subCommand);
    }

    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length == 0)
            return handleDefault(sender);

        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null)
            return handleNotFound(sender, args[0]);

        return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        if (args.length == 1)
            return CommandUtil.partialMatch(args[0], subCommands.keySet());

        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null)
            return Collections.emptyList();

        return subCommand.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    protected abstract boolean handleDefault(CommandSender sender);

    protected abstract boolean handleNotFound(CommandSender sender, String command);
}