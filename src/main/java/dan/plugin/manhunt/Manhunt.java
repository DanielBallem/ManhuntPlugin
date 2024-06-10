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

import java.util.List;

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
        String commandName = command.getName().toLowerCase();
        switch (commandName) {
            case "startmanhunt":
                return handleStartManhunt(sender);
            case "stopmanhunt":
                return handleStopManhunt(sender);
            case "manhuntinfo":
                return handleManhuntInfo(sender);
            case "runnerenforcement":
                return handleRunnerEnforcement(sender, args);
            case "addhunter":
                return handlePlayerCommand(
                        sender,
                        args,
                        manhuntGame::addHunter,
                        "added to the hunter team",
                        label
                );
            case "removehunter":
                return handlePlayerCommand(
                        sender,
                        args,
                        manhuntGame::removeHunter,
                        "removed from the hunter team",
                        label
                );
            case "setrunner":
                return handlePlayerCommand(
                        sender,
                        args,
                        manhuntGame::setRunner,
                        "set as the runner",
                        label
                );
            case "removerunner":
                return handleRemoveRunner(sender, args);
            case "test":
                return handTestScenario(sender, args, label);
        }
        return false;
    }

    private boolean handleRemoveRunner(CommandSender sender, String[] args) {
        if (args.length != 0) {
            MessageUtils.sendError("Usage: /yourcommand", sender);
            return false;
        }
        manhuntGame.removeRunner();
        return true;
    }

    private boolean handleRunnerEnforcement(CommandSender sender, String[] args) {
        if (args.length != 1) {
            MessageUtils.sendError("Usage: /yourcommand <true|false>", sender);
            return false;
        }

        if ("true".equalsIgnoreCase(args[0]) || "false".equalsIgnoreCase(args[0])) {
            manhuntGame.GAME_OPTION_RUNNER_ENFORCEMENT = "true".equalsIgnoreCase(args[0]);
            MessageUtils.sendConfirmation("Runner enforcement is set to " + manhuntGame.GAME_OPTION_RUNNER_ENFORCEMENT, sender);
            return true;
        } else {
            MessageUtils.sendError("Invalid argument. Usage: /yourcommand <true|false>", sender);
            return false;
        }
    }

    private boolean handTestScenario(CommandSender sender, String[] args, String label) {
        handlePlayerCommand(
                sender,
                new String[]{sender.getName()}, //player name as a test arg.
                manhuntGame::addHunter,
                "added to the hunter team",
                label
        );
        handlePlayerCommand(
                sender,
                new String[]{sender.getName()}, //player name as a test arg.
                manhuntGame::setRunner,
                "set as the runner",
                label
        );

        return handleManhuntInfo(sender);
    }

    private boolean handleManhuntInfo(CommandSender sender) {
        List<String> playerList = manhuntGame.getHunterNames();
        MessageUtils.sendMessage("Hunters: ", sender, NamedTextColor.RED);
        MessageUtils.sendMessage("----------", sender, NamedTextColor.RED);
        playerList.forEach(name -> MessageUtils.sendMessage("- " + name, sender, NamedTextColor.RED));

        String runnerName = manhuntGame.getRunnerName();
        MessageUtils.sendMessage("Runner: " + runnerName, sender, NamedTextColor.GREEN);
        return true;
    }

    private boolean handleStopManhunt(CommandSender sender) {
        manhuntGame.stopGame();
        HandlerList.unregisterAll(this);
        sender.sendMessage(Component.text("Manhunt game has stopped"));
        return true;
    }

    private boolean handleStartManhunt(CommandSender sender) {
        boolean gameStarted = manhuntGame.startGame(sender);
        if (!gameStarted) {
            sender.sendMessage(Component.text("Failed to start manhunt"));
            return false;
        }
        getServer().getPluginManager().registerEvents(manhuntGame, this);

        sender.sendMessage(Component.text("Manhunt game started"));
        return true;
    }

    private boolean handlePlayerCommand(CommandSender sender, String[] args, PlayerAction action, String successMessage, String commandLabel) {
        if (args == null || args.length != 1) {
            sender.sendMessage(Component.text("Usage: /" + commandLabel + " <playerName>"));
            return false;
        }

        Player player = getServer().getPlayer(args[0]);
        if (player == null) {
            MessageUtils.sendError("Player not found", sender);
            return false;
        }

        action.execute(player);
        MessageUtils.sendConfirmation(player.getName() + " has been " + successMessage, sender);
        return true;
    }
}
