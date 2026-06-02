package currency;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import currency.event.UserBalanceChangeEvent;

public class CurrencyUIManager implements Listener
{
    private CurrencyManager currencyManager;
    private Map<UUID, Scoreboard> playerBoards;

    public CurrencyUIManager(CurrencyManager currencyManager)
    {
        this.currencyManager = currencyManager;
        this.playerBoards = new HashMap<>();
    }

    @EventHandler
    public void onUserBalanceChange(UserBalanceChangeEvent event)
    {
        Player player = Bukkit.getPlayer(event.getPlayerUUID());

        if (player != null && player.isOnline())
            updateBalance(event.getPlayerUUID(), event.getNewBalance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        assignScoreboard(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        playerBoards.remove(event.getPlayer().getUniqueId());
    }

    private void assignScoreboard(UUID playerUUID)
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null)
            return;

        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("stats", Criteria.DUMMY, "§e§l[ Tiny RPG ]");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§7--------------------").setScore(3);

        // Prevent flickering
        String entryKey = "§f";
        Team balanceTeam = board.registerNewTeam("balance_team");
        balanceTeam.addEntry(entryKey);

        // Initial text
        balanceTeam.setPrefix("§bMoney: §d0");
        objective.getScore(entryKey).setScore(2);

        // Additional information
        objective.getScore("§7 ").setScore(1);
        objective.getScore("§enofy.o-r.kr").setScore(0);

        // Scoreboard apply, register
        Bukkit.getPlayer(playerUUID).setScoreboard(board);
        playerBoards.put(playerUUID, board);

        updateBalance(playerUUID, currencyManager.getBalance(playerUUID));
    }

    private void updateBalance(UUID playerUUID, Long newBalance)
    {
        Scoreboard board = playerBoards.get(playerUUID);
        if (board == null)
            return;

        Team balanceTeam = board.getTeam("balance_team");
        if (balanceTeam == null)
            return;

        balanceTeam.setPrefix("§bMoney: §6" + CurrencyFormatter.formatCompact(newBalance));
    }
}