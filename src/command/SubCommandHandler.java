package command;

import java.util.Set;
import java.util.List;

import org.bukkit.command.CommandSender;

public interface SubCommandHandler
{
    public void register(SubCommand subCommand);

    public Set<String> getCommands();

    public boolean execute(CommandSender sender, String[] args);

    public List<String> tabComplete(CommandSender sender, String[] args);
}