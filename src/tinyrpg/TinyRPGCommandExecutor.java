package tinyrpg;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import command.MultiSubCommand;

public class TinyRPGCommandExecutor extends MultiSubCommand implements CommandExecutor, TabCompleter
{
    public TinyRPGCommandExecutor()
    {
        super("tr");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean result = execute(sender, args);

        if (!result)
            sender.sendMessage("§cInvalid usage.");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        return tabComplete(sender, args);
    }

    @Override
    public boolean handleDefault(CommandSender sender)
    {
        sender.sendMessage("§eUsage: /" + getName() + " < currency | auction | shop >");

        return true;
    }

    @Override
    public boolean handleNotFound(CommandSender sender, String command)
    {
        sender.sendMessage("§cUnknown subcommand: " + command);
        handleDefault(sender);

        return true;
    }
}
