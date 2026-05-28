package tinyrpg;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import command.CommandUtil;
import command.SubCommandHandler;

public class TinyRPGCommandExecutor implements CommandExecutor, TabCompleter
{
    private Map<String, SubCommandHandler> subCommandHandlers;

    public TinyRPGCommandExecutor()
    {
        this.subCommandHandlers = new HashMap<>();
    }

    public void addSubCommandHandler(String command, SubCommandHandler subCommandHandler)
    {
        subCommandHandlers.put(command, subCommandHandler);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 1) {
            sender.sendMessage(
                    "Usage: /" + label +
                            " < currency | auction | shop >");

            return true;
        }

        SubCommandHandler subCommandHandler = subCommandHandlers.get(args[0]);

        if (subCommandHandler == null) {
            sender.sendMessage(
                    "Unknown subcommand: " + args[0]);

            sender.sendMessage(
                    "Usage: /" + label +
                            " < currency | auction | shop >");

            return true;
        }

        boolean result = subCommandHandler.execute(sender, Arrays.copyOfRange(args, 1, args.length));

        if (!result) {
            sender.sendMessage(
                    "Invalid usage.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length == 1) {
            return CommandUtil.partialMatch(
                    args[0],
                    subCommandHandlers.keySet());
        }

        SubCommandHandler handler = subCommandHandlers.get(args[0]);

        if (handler == null)
            return Collections.emptyList();

        return handler.tabComplete(sender,
                Arrays.copyOfRange(args, 1, args.length));
    }
}
