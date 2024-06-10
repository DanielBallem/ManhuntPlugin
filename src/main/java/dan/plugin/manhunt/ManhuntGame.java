package dan.plugin.manhunt;

import dan.plugin.manhunt.utils.MessageUtils;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ManhuntGame implements Listener {
    private final List<Player> hunters = new ArrayList<>();
    private Player runner = null;
    private int taskId = -1;
    public final Manhunt plugin;

    public boolean GAME_OPTION_RUNNER_ENFORCEMENT = false;

    public ManhuntGame(Manhunt plugin) {
        this.plugin = plugin;
    }

    public List<String> getHunterNames() {
        if (hunters.size() == 0) return Collections.emptyList();
        return hunters.stream().map(Player::getName).collect(Collectors.toList());
    }

    public String getRunnerName() {
        if (runner == null) return "No runner selected.";
        return runner.getName();
    }

    public void addHunter(Player p) {
        if (GAME_OPTION_RUNNER_ENFORCEMENT) {
            if (runner != null && runner.equals(p)) {
                runner = null;
                MessageUtils.sendWarning("You are no longer the runner.", p);
            }
        }

        // It errors if it's not synchronized, I'm not sure why
        synchronized (hunters) {
            if (!hunters.contains(p)) {
                hunters.add(p);
                MessageUtils.sendConfirmation("You have been added to the hunter team", p);
            }
        }
    }

    public void removeRunner() {
        if (runner == null) return;
        Player p = runner;
        MessageUtils.sendWarning("You are no longer the runner", p);
        runner = null;
    }

    public void removeHunter(Player p) {
        if (hunters.contains(p)) {
            hunters.remove(p);
            MessageUtils.sendWarning("You've been removed from the hunter team", p);
        }
    }

    public void setRunner(Player p) {
        if (runner != null && !runner.equals(p)) {
            MessageUtils.sendWarning("You are being swapped for another runner.", runner);
        }
        if (GAME_OPTION_RUNNER_ENFORCEMENT) {
            if (hunters.contains(p)) {
                removeHunter(p);
            }
        }
        runner = p;
        p.sendMessage(Component.text("You are now the runner").color(NamedTextColor.GREEN));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (hunters.contains(player)) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        hunters.remove(player); //if player is not in hunters this does nothing.
    }

    public boolean startGame(CommandSender sender) {
        if (!canGameStart()) {
            MessageUtils.sendError("Hunter and runner teams are both not setup.", sender);
            return false;
        }
        if (GAME_OPTION_RUNNER_ENFORCEMENT && hunters.contains(runner)) {
            MessageUtils.sendError("Cannot start the game until no hunter is a runner.", sender);
            return false;
        }

        //setup players
        for (Player p : hunters) {
            MessageUtils.sendConfirmation("Start hunting! The game has started", p);
            p.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
        MessageUtils.sendConfirmation("Start running! The game has started", runner);

        startCompassUpdateTask();
        return true;
    }

    public void stopGame() {
        for (Player p : hunters) {
            MessageUtils.sendWarning("The game has stopped", p);
        }
        MessageUtils.sendWarning("The game has stopped", runner);

        stopCompassUpdateTask();
    }

    public void startCompassUpdateTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                if (runner != null) {
                    Location loc = runner.getLocation();
                    for (Player hunter : hunters) {
                        hunter.setCompassTarget(loc);
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

    public boolean canGameStart() {
        return runner != null && hunters.size() != 0;
    }
}
