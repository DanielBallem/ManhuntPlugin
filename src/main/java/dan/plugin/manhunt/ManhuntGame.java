package dan.plugin.manhunt;

import dan.plugin.manhunt.utils.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class ManhuntGame implements Listener {

    public final OptionManager optionManager;
    public final ManhuntTeamManager teamManager;
    private int taskId = -1;
    public final Manhunt plugin;

    public ManhuntGame(Manhunt plugin, OptionManager optionManager) {
        this.plugin = plugin;
        this.optionManager = optionManager;
        teamManager = new ManhuntTeamManager(optionManager);
    }



    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (teamManager.hunterTeam.contains(player)) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        teamManager.removeHunter(player);
    }

    public boolean startGame(CommandSender sender) {
        if (!canGameStart(sender)) return false;

        //setup players
        for (Player p : teamManager.hunterTeam) {
            p.getInventory().addItem(new ItemStack(Material.COMPASS));
        }

        messageAllPlayers("The game has started!", NamedTextColor.GREEN);
        startCompassUpdateTask();
        return true;
    }

    private boolean canGameStart(CommandSender sender) {
        if (!allTeamsAreSetup()) {
            MessageUtils.sendError("Hunter and runner teams are both not setup.", sender);
            return false;
        }
        if (optionManager.getBooleanOption(OptionConstants.PREVENT_TEAM_OVERLAP)) {
            for (Player runner : teamManager.runnerTeam) {
                if (teamManager.hunterTeam.contains(runner)) {
                    MessageUtils.sendError("Cannot start the game until no hunter is a runner.", sender);
                    return false;
                }
            }
        }
        return true;
    }

    public void stopGame() {
        messageAllPlayers("The game has stopped", NamedTextColor.RED);
        stopCompassUpdateTask();
    }

    public void startCompassUpdateTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        Team runner = teamManager.runnerTeam;
        Team hunters = teamManager.hunterTeam;

        taskId = new BukkitRunnable() {
            @Override
            public void run() {

                if (!runner.isEmpty()) {
                    Location loc = runner.get(0).getLocation();
                    synchronized (hunters) {
                        for (Player hunter : hunters) {
                            if (hunter != null)
                                hunter.setCompassTarget(loc);
                        }
                    }
                } else {
                    stopGame(); //no runner
                    messageAllPlayers("There is no longer a runner", NamedTextColor.RED);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L).getTaskId(); // Update every second (20 ticks)
    }

    public void stopCompassUpdateTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public boolean allTeamsAreSetup() {
        return !teamManager.runnerTeam.isEmpty() && !teamManager.hunterTeam.isEmpty();
    }

    public void messageAllPlayers(String message, NamedTextColor color) {
        Set<Audience> players = new HashSet<>(teamManager.hunterTeam);
        if (!teamManager.runnerTeam.isEmpty()) {
            players.add(teamManager.runnerTeam.get(0));
        }
        MessageUtils.sendAllPlayersAMessage(message, players, color);
    }

    public List<String> getHunterNames() {
        return teamManager.getHunterNames();
    }

    public String getRunnerName() {
        return teamManager.getRunnerName();
    }

    public void addHunter(Player p) {
        teamManager.addHunter(p);
    }

    public void removeRunner() {
        teamManager.removeRunner();
    }

    public void removeHunter(Player p) {
        teamManager.removeHunter(p);
    }

    public void setRunnerTeam(Player p) {
        teamManager.setRunnerTeam(p);
    }
}
