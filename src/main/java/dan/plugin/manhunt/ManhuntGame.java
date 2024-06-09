package dan.plugin.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ManhuntGame implements Listener {
    private final List<Player> hunters = new ArrayList<>();
    private Player runner = null;
    private int taskId = -1;

    private final Manhunt plugin;

    public ManhuntGame(Manhunt plugin) {
        this.plugin = plugin;
    }

    public void addHunter(Player p) {
        if (!hunters.contains(p)) {
            hunters.add(p);
            p.sendMessage(Component.text("You have been added to the hunter team").color(NamedTextColor.GREEN));
        }
    }

    public void removeHunter(Player p) {
        if (hunters.contains(p)) {
            hunters.remove(p);
            p.sendMessage(Component.text("You've been removed from the hunter team").color(NamedTextColor.YELLOW));
        }
    }

    public void setRunner(Player p) {
        if (runner != null) {
            runner.sendMessage(Component.text("You are no longer the runner").color(NamedTextColor.YELLOW));
        }
        //Add this back in when I want to enforce this again (I'm testing by myself)
        //if (hunters.contains(p)){
        //removeHunter(p);
        //}
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

    public boolean startGame() {
        if (!canGameStart()) return false;

        for (Player p : hunters) {
            p.sendMessage(Component.text("Start hunting! The game has started").color(NamedTextColor.GREEN));
            p.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
        runner.sendMessage(Component.text("Start running! The game has started").color(NamedTextColor.GREEN));

        startCompassUpdateTask();
        return true;
    }

    public void stopGame() {
        for (Player p : hunters) {
            p.sendMessage(Component.text("The game has stopped").color(NamedTextColor.YELLOW));
        }
        runner.sendMessage(Component.text("The game has stopped").color(NamedTextColor.YELLOW));

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
