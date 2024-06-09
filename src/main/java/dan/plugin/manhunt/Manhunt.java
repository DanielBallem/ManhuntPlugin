package dan.plugin.manhunt;

import dan.plugin.manhunt.interfaces.PlayerAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Manhunt extends JavaPlugin {

    private final ManhuntGame manhuntGame = new ManhuntGame(this);

    @Override
    public void onEnable() {
        getLogger().info("Manhunt plugin is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt plugin is enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("startManhunt")) {
            boolean gameStarted = manhuntGame.startGame();
            if (!gameStarted) {
                sender.sendMessage(Component.text("Teams have not been set up"));
                return false;
            }
            getServer().getPluginManager().registerEvents(manhuntGame, this);

            sender.sendMessage(Component.text("Manhunt game started"));
            return true;
        }

        if (command.getName().equalsIgnoreCase("stopManhunt")) {
            manhuntGame.stopGame();
            HandlerList.unregisterAll((Plugin)this);
            sender.sendMessage(Component.text("Manhunt game has stopped"));
            return true;
        }

        if (command.getName().equalsIgnoreCase("addHunter")) {
            return handlePlayerCommand(
                    sender,
                    args,
                    manhuntGame::addHunter,
                    "added to the hunter team",
                    label
            );
        }

        if (command.getName().equalsIgnoreCase("removeHunter")) {
            return handlePlayerCommand(
                    sender,
                    args,
                    manhuntGame::removeHunter,
                    "removed from the hunter team",
                    label
            );
        }

        if (command.getName().equalsIgnoreCase("setRunner")) {
            return handlePlayerCommand(
                    sender,
                    args,
                    manhuntGame::setRunner,
                    "set as the runner",
                    label
            );
        }
        return false;
    }

    private boolean handlePlayerCommand(CommandSender sender, String[] args, PlayerAction action, String successMessage, String commandLabel) {
        if (args == null || args.length != 1) {
            sender.sendMessage(Component.text("Usage: /" + commandLabel + " <playerName>"));
            return false;
        }

        Player player = getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Component.text("Player not found").color(NamedTextColor.RED));
            return false;
        }

        action.execute(player);
        sender.sendMessage(Component.text(player.getName() + " has been " + successMessage).color(NamedTextColor.GREEN));
        return true;
    }
}
