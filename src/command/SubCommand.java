package command;

import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class SubCommand
{
    private String name;

    private List<String> aliases;

    public SubCommand(String name, List<String> aliases)
    {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getAliases()
    {
        return aliases;
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public abstract List<String> tabComplete(CommandSender sender, String[] args);
}
