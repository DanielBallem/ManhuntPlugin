package dan.plugin.manhunt;

import dan.plugin.manhunt.commands.*;
import dan.plugin.manhunt.interfaces.SubCommand;
import dan.plugin.manhunt.utils.CommandConstants;
import dan.plugin.manhunt.utils.OptionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class ManhuntCommandManager {

    private final ManhuntGame manhuntGame;

    private final Map<String, SubCommand> commands = new HashMap<>();


    public ManhuntCommandManager(ManhuntGame game, OptionManager optionManager) {
        this.manhuntGame = game;
        registerCommands(optionManager);
    }

    private void registerCommands(OptionManager optionManager) {
        commands.put(CommandConstants.START_MANHUNT, new StartManhuntCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.STOP_MANHUNT, new StopManhuntCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.MANHUNT_INFO, new ManhuntInfoCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.PREVENT_TEAM_OVERLAP_OPTION, new RunnerEnforcementCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.ADD_HUNTER, new AddHunterCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.REMOVE_HUNTER, new RemoveHunterCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.SET_RUNNER, new SetRunnerCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.REMOVE_RUNNER, new RemoveRunnerCommand(manhuntGame, optionManager));
        commands.put(CommandConstants.TEST, new TestManhuntCommand(manhuntGame, optionManager));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SubCommand subCommand = commands.get(command.getName().toLowerCase());
        if (subCommand != null) {
            return subCommand.execute(sender, command, label, args);
        }
        return false;
    }
}
