package currency.command;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import command.SubCommand;
import currency.CurrencyFormatter;
import currency.CurrencyItemManager;
import currency.CurrencyManager;

public class WithdrawCommand extends SubCommand
{
    CurrencyManager currencyManager;

    public WithdrawCommand(CurrencyManager currencyManager)
    {
        super("withdraw", List.of("wd"));

        this.currencyManager = currencyManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can execute this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§eUsage: /tr currency withdraw <amount> [count]");

            return true;
        }

        long amount;
        try {
            amount = Long.parseLong(args[0]);
            if (amount <= 0) {
                player.sendMessage("§cAmount should be greater than 0.");

                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§cAmount should be number.");

            return true;
        }

        int count = 1;
        if (args.length >= 2) {
            try {
                count = Integer.parseInt(args[1]);
                if (count <= 0) {
                    player.sendMessage("§cCount must be greater than 0.");

                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage("§cPlease enter a valid count.");

                return true;
            }
        }

        long totalAmount;
        try {
            totalAmount = Math.multiplyExact(amount, count);
        } catch (ArithmeticException e) {
            player.sendMessage("§cThe total amount is too massive to process.");

            return true;
        }

        if (currencyManager.getBalance(player.getUniqueId()) < totalAmount) {
            player.sendMessage(
                    "§cYou do not have enough money. (Total required: " + CurrencyFormatter.format(totalAmount) + ")");

            return true;
        }

        if (!hasEnoughInventorySpace(player, amount, count)) {
            player.sendMessage("§cWithdraw failed. Your inventory does not have enough space!");

            return true;
        }

        if (!currencyManager.removeBalance(player.getUniqueId(), totalAmount)) {
            player.sendMessage("§cAn error occurred while processing transaction.");

            return true;
        }

        int remainingCount = count;
        while (remainingCount > 0) {
            int currentStackSize = Math.min(64, remainingCount);

            ItemStack checkItem = CurrencyItemManager.createCheck(amount);
            checkItem.setAmount(currentStackSize);

            player.getInventory().addItem(checkItem);

            remainingCount -= currentStackSize;
        }

        player.sendMessage(String.format("§aSuccessfully withdrew §e%d §aplates of §6%s §achecks.", count,
                CurrencyFormatter.format(amount)));
        player.sendMessage(String.format("§7(Total deducted: §c-%s§7)", CurrencyFormatter.format(totalAmount)));

        return true;
    }

    private boolean hasEnoughInventorySpace(Player player, long amount, int count)
    {
        ItemStack[] contents = player.getInventory().getStorageContents();

        Inventory mockInventory = Bukkit.createInventory(null, 36);
        mockInventory.setContents(contents);

        int remainingCount = count;
        while (remainingCount > 0) {
            int currentStackSize = Math.min(64, remainingCount);
            ItemStack checkItem = CurrencyItemManager.createCheck(amount);
            checkItem.setAmount(currentStackSize);

            java.util.Map<Integer, ItemStack> overflow = mockInventory.addItem(checkItem);

            if (!overflow.isEmpty())
                return false;

            remainingCount -= currentStackSize;
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        if (args.length == 1)
            return List.of("<amount>");
        if (args.length == 2)
            return List.of("[count]");
        return Collections.emptyList();
    }
}
