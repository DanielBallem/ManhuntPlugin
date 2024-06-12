package dan.plugin.manhunt;

import dan.plugin.manhunt.utils.MessageUtils;
import dan.plugin.manhunt.utils.Team;
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
//    private final List<Player> hunters = new ArrayList<>();
    public final Team hunterTeam = new Team("hunters");
    public final Team runnerTeam = new Team("runner", 1);
//    private Player runner = null;
    private int taskId = -1;
    public final Manhunt plugin;

    public boolean GAME_OPTION_RUNNER_ENFORCEMENT = false;

    public ManhuntGame(Manhunt plugin) {
        this.plugin = plugin;
    }

    public List<String> getHunterNames() {
        if (hunterTeam.size() == 0) return Collections.emptyList();
        return hunterTeam.stream().map(Player::getName).collect(Collectors.toList());
    }

    public String getRunnerName() {
        if (runnerTeam.isEmpty()) return "No runner selected.";
        return runnerTeam.get(0).getName();
    }

    public void addHunter(Player p) {
        if (GAME_OPTION_RUNNER_ENFORCEMENT) {
            if (!runnerTeam.isEmpty() && runnerTeam.contains(p)) {
                runnerTeam.clear();
                MessageUtils.sendWarning("You are no longer the runner.", p);
            }
        }

        // It errors if it's not synchronized, I'm not sure why
        synchronized (hunterTeam) {
            if (!hunterTeam.contains(p)) {
                hunterTeam.add(p);
                MessageUtils.sendConfirmation("You have been added to the hunter team", p);
            }
        }
    }

    public void removeRunner() {
        if (runnerTeam.isEmpty()) return;
        Player p = runnerTeam.get(0);
        MessageUtils.sendWarning("You are no longer the runner", p);
        runnerTeam.remove(p);
    }

    public void removeHunter(Player p) {
        if (hunterTeam.contains(p)) {
            hunterTeam.remove(p);
            MessageUtils.sendWarning("You've been removed from the hunter team", p);
        }
    }

    public void setRunnerTeam(Player p) {
        if (!runnerTeam.isEmpty() && !runnerTeam.get(0).equals(p)) {
            MessageUtils.sendWarning("You are being swapped for another runner.", runnerTeam.get(0));
        }
        if (GAME_OPTION_RUNNER_ENFORCEMENT) {
            if (hunterTeam.contains(p)) {
                removeHunter(p);
            }
        }
        runnerTeam.clear();
        runnerTeam.add(p);
        p.sendMessage(Component.text("You are now the runner").color(NamedTextColor.GREEN));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (hunterTeam.contains(player)) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        hunterTeam.remove(player); //if player is not in hunters this does nothing.
    }

    public boolean startGame(CommandSender sender) {
        if (!canGameStart(sender)) return false;

        //setup players
        for (Player p : hunterTeam) {
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
        if (GAME_OPTION_RUNNER_ENFORCEMENT) {
            for (Player runner : runnerTeam) {
                if (hunterTeam.contains(runner)) {
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

        taskId = new BukkitRunnable() {
            @Override
            public void run() {

                if (!runnerTeam.isEmpty()) {
                    Location loc = runnerTeam.get(0).getLocation();
                    synchronized (hunterTeam) {
                        for (Player hunter : hunterTeam) {
                            hunter.setCompassTarget(loc);
                        }
                    }

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
        return !runnerTeam.isEmpty() && !hunterTeam.isEmpty();
    }

    public void messageAllPlayers(String message, NamedTextColor color) {
        Set<Audience> players = new HashSet<>(hunterTeam);
        if (!runnerTeam.isEmpty()) {
            players.add(runnerTeam.get(0));
        }
        MessageUtils.sendAllPlayersAMessage(message, players, color);
    }
}
