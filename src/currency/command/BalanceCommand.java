package currency.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import command.CommandUtil;
import command.SubCommand;
import currency.CurrencyFormatter;
import currency.CurrencyManager;

public class BalanceCommand extends SubCommand
{
    private CurrencyManager currencyManager;

    public BalanceCommand(CurrencyManager currencyManager)
    {
        super("balance", List.of("bal", "b"));
        this.currencyManager = currencyManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length == 0 || args[0].equals("get")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cOnly players can execute this command.");

                return true;
            }
            return handleGet(player);

        } else if (args[0].equals("set")) {
            if (!(sender.isOp())) {
                sender.sendMessage("§cYou do not have permission to use this command");

                return true;
            }

            return handleSet(sender, args);
        }

        return false;
    }

    private boolean handleGet(Player player)
    {
        player.sendMessage(String.format("balance: %s",
                CurrencyFormatter.format(currencyManager.getBalance(player.getUniqueId()))));

        return true;
    }

    private boolean handleSet(CommandSender sender, String[] args)
    {
        if (args.length < 3) {
            sender.sendMessage("§cusage: /tr currency balance set <player> <amount>");
            return true;
        }

        String playerName = args[1];
        String amount = args[2];

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("§cPlayer not found or currently offline.");
            return true;
        }

        long amountValue;
        try {
            amountValue = Long.parseLong(amount);
            if (amountValue < 0) {
                sender.sendMessage("§cAmount cannot be negative");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("§cAmount should be number");
            return true;
        }

        currencyManager.setBalance(player.getUniqueId(), amountValue);
        sender.sendMessage(
                String.format("§a%s's balance have been set to %s", player.getName(),
                        CurrencyFormatter.format(amountValue)));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        if (args.length == 1) {
            return CommandUtil.partialMatch(
                    args[0],
                    sender.isOp() ? List.of("get", "set") : List.of("get"));
        }

        if (args.length == 2
                && args[0].equals("set") && sender.isOp()) {

            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .toList();
        }

        return Collections.emptyList();
    }
}
