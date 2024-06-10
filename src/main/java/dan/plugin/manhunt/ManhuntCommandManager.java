package dan.plugin.manhunt;

import dan.plugin.manhunt.commands.*;
import dan.plugin.manhunt.interfaces.SubCommand;
import dan.plugin.manhunt.utils.CommandConstants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class ManhuntCommandManager {

    private final ManhuntGame manhuntGame;

    private final Map<String, SubCommand> commands = new HashMap<>();


    public ManhuntCommandManager(ManhuntGame game) {
        this.manhuntGame = game;
        registerCommands();
    }

    private void registerCommands() {
        commands.put(CommandConstants.START_MANHUNT, new StartManhuntCommand(manhuntGame));
        commands.put(CommandConstants.STOP_MANHUNT, new StopManhuntCommand(manhuntGame));
        commands.put(CommandConstants.MANHUNT_INFO, new ManhuntInfoCommand(manhuntGame));
        commands.put(CommandConstants.RUNNER_ENFORCEMENT, new RunnerEnforcementCommand(manhuntGame));
        commands.put(CommandConstants.ADD_HUNTER, new AddHunterCommand(manhuntGame));
        commands.put(CommandConstants.REMOVE_HUNTER, new RemoveHunterCommand(manhuntGame));
        commands.put(CommandConstants.SET_RUNNER, new SetRunnerCommand(manhuntGame));
        commands.put(CommandConstants.REMOVE_RUNNER, new RemoveRunnerCommand(manhuntGame));
        commands.put(CommandConstants.TEST, new TestManhuntCommand(manhuntGame));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SubCommand subCommand = commands.get(command.getName().toLowerCase());
        if (subCommand != null) {
            return subCommand.execute(sender, command, label, args);
        }
        return false;
    }
}
